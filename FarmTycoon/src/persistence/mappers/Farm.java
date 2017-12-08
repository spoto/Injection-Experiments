package persistence.mappers;

import java.util.HashMap;
import java.util.Map;

import persistence.DBmap;

public class Farm implements persistence.Mapper {
	public domain.Farm load(DBmap map) {
		return new domain.Farm(map.getInt("cash"), true, map.getLong("nextinfection"), map.getLong("nextstorm"));
	}

	public DBmap save(domain.Savable obj) {
		DBmap ret = new DBmap(this);
		domain.Farm farm = (domain.Farm) obj;
		ret.put("cash", farm.getCash());
		ret.put("nextinfection", farm.getInfecion().getNext());
		ret.put("nextstorm", farm.getStorm().getNext());
		return ret;
	}

	public Map<String, String> getFields() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put("cash", "INT");
		fields.put("nextinfection", "BIGINT");
		fields.put("nextstorm", "BIGINT");
		return fields;
	}
}
