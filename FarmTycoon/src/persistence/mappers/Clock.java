package persistence.mappers;

import java.util.HashMap;
import java.util.Map;

import persistence.DBmap;

import domain.Savable;

public class Clock implements persistence.Mapper {
	public domain.Clock load(DBmap map) {
		return new domain.Clock(map.getDouble("Multi"), map.getLong("Offset"));
	}

	public DBmap save(Savable obj) {
		DBmap ret = new DBmap(this);
		domain.Clock clock = (domain.Clock) obj;
		ret.put("Offset", clock.getOffset());
		ret.put("Multi", clock.getMultiplier());
		return ret;
	}

	public Map<String, String> getFields() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("Offset", "BIGINT");
		fields.put("Multi", "DOUBLE");
		return fields;
	}
}