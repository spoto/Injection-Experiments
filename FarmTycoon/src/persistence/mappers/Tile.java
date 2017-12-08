package persistence.mappers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import persistence.DBmap;

import api.Coordinate;

import domain.Savable;
import domain.TileState;

public class Tile implements persistence.Mapper {
	@SuppressWarnings("unchecked")
	public domain.Tile load(DBmap map) {
		domain.TileState state;
		try {
			Class<? extends domain.TileState> stateClass = (Class<? extends TileState>) Class
					.forName("domain.tiles." + map.getStr("state"));
			if (map.getInt("stateid") == -1) {
				state = stateClass.newInstance();
			} else {
				state = (TileState) domain.Savable.load(
						(Class<? extends Savable>) stateClass,
						map.getInt("stateid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			state = null;
		}
		return new domain.Tile(new Coordinate(map.getInt("x"),
				map.getInt("y")), state, map.getLong("expiryTime"));
	}

	public DBmap save(domain.Savable obj) throws SQLException {
		domain.Tile tile = (domain.Tile) obj;
		DBmap ret = new DBmap(this);
		ret.put("x", tile.getCoordinate().getX());
		ret.put("y", tile.getCoordinate().getY());
		ret.put("expiryTime", (Long) tile.getExpiryTime());
		ret.put("state", tile.getState().getClass().getSimpleName());
		if (tile.getState() instanceof domain.Savable) {
			domain.Savable state = (domain.Savable) tile.getState();
			ret.put("stateid", state.getId());
			state.save();
		} else {
			ret.put("stateid", -1);
		}
		return ret;
	}

	public Map<String, String> getFields() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("x", "INT");
		fields.put("y", "INT");
		fields.put("expiryTime", "BIGINT");
		fields.put("state", "TEXT");
		fields.put("stateid", "INT");
		return fields;
	}
}
