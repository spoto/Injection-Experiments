package persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import api.StringJoiner;

/**
 * Map extending the HashMap with some Database specific functionality.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class DBmap extends HashMap<String, Object> {
	private final Mapper mapper;

	/**
	 * Construct a new Map form a java ResultSet.
	 * @param mapper the Mapper for the object to create this map for. 
	 * @param rs the java ResultSet to initialize the map from
	 * @throws SQLException
	 */
	public DBmap(Mapper mapper, java.sql.ResultSet rs) throws SQLException {
		this(mapper);
		java.sql.ResultSetMetaData meta = rs.getMetaData();
		int count = meta.getColumnCount();
		for (int i = 1; i <= count; i++)
			put(meta.getColumnName(i), rs.getObject(i));
	}

	/**
	 * Construct a new empty Map.
	 * @param mapper the Mapper for the object to create this map for. 
	 */
	public DBmap(Mapper mapper) {
		super();
		this.mapper = mapper;
	}

	/**
	 * Construct a new Map and initialize it.
	 * @param mapper the Mapper for the object to create this map for.
	 * @param keys an array containing the keys to initialize this map with.
	 * @param vals an array containing the values to initialize this map with, should be the same size as keys. 
	 */
	public DBmap(Mapper mapper, String[] keys, Object[] vals) {
		this(mapper);
		for (int i = 0; i < keys.length; i++)
			put(keys[i], vals[i]);
	}

	/**
	 * Get an integer representation of the specified field.
	 * @param key the name of the field.
	 * @return the integer representation of the value.
	 */
	public int getInt(String key) {
		return (Integer) get(key);
	}

	/**
	 * Get an double representation of the specified field.
	 * @param key the name of the field.
	 * @return the double representation of the value.
	 */
	public double getDouble(String key) {
		return (Double) get(key);
	}

	/**
	 * Get a String representation of the specified field.
	 * @param key the name of the field.
	 * @return the String representation of the value.
	 */
	public String getStr(String key) {
		return (String) get(key);
	}

	/**
	 * Get a long representation of the specified field.
	 * @param key the name of the field.
	 * @return the long representation of the value.
	 */
	public long getLong(String key) {
		if (get(key) instanceof Long)
			return (Long) get(key);
		return (Integer) get(key);
	}

	/**
	 * @return the update sql statement to write this map to a database. 
	 */
	public String getUpdateSql() {
		if(get("id")==null)
			return null;
		return getUpdateSql(getInt("id"));
	}
	/**
	 * @param id the id to include in the update statement.
	 * @return the update sql statement to write this map to a database. 
	 */
	public String getUpdateSql(int id) {
		StringJoiner pairs = new StringJoiner(",");
		for (Map.Entry<String, Object> e : entrySet())
			pairs.add(e.getKey() + " = " + format(e.getValue()));
		return String.format("UPDATE %s SET %s WHERE id = %d", mapper
				.getClass().getSimpleName(), pairs.toString(), id);
	}

	/**
	 * @return the insert sql statement to write this map to a database. 
	 */
	public String getInsertSql() {
		if(get("id")==null)
			return null;
		return getInsertSql(getInt("id"));
	}
	/**
	 * @param id the id to include in the insert statement.
	 * @return the insert sql statement to write this map to a database. 
	 */
	public String getInsertSql(int id) {
		StringJoiner names = new StringJoiner(",", "id");
		StringJoiner values = new StringJoiner(",", id);
		for (Map.Entry<String, Object> e : entrySet()) {
			names.add(e.getKey());
			values.add(format(e.getValue()));
		}
		return String.format("INSERT INTO %s (%s) VALUES(%s);", mapper
				.getClass().getSimpleName(), names.toString(), values
				.toString());
	}

	private String format(Object obj) {
		if (obj instanceof String)
			return "'" + (String) obj + "'";
		return obj.toString();
	}
}
