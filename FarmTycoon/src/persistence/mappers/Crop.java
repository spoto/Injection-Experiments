package persistence.mappers;

import java.util.HashMap;
import java.util.Map;

import persistence.DBmap;

public class Crop implements persistence.Mapper {
	public domain.tiles.Crop load(DBmap map) {
		return new domain.tiles.Crop(map.getStr("type"), map.getLong("planted"),map.getStr("state"));
	}

	public DBmap save(domain.Savable obj) {
		domain.tiles.Crop crop = (domain.tiles.Crop) obj;
		return new DBmap(this, new String[] { "type", "planted", "state" },
				new Object[] { crop.getType(), crop.getPlanted(), crop.getState() });
	}

	public Map<String, String> getFields() {
		return new HashMap<String, String>() {
			{
				put("type", "TEXT");
				put("planted", "BIGINT");
				put("state", "TEXT");
			}
		};
	}
}
