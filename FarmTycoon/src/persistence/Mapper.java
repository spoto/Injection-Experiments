package persistence;

import java.sql.SQLException;
import java.util.Map;

/**
 * The interface between the database and
 * {@link persistence.MappeList} items
 * 
 */
public interface Mapper {
	/**
	 * Turns the data passed in to the {@link domain.Savable} object this Mapper
	 * is made for. The complete row of the database is passed ass a
	 * <"columnname",value> Map.
	 * 
	 * @param data
	 *            a Map containing the database data of the object to load.
	 * @return the Savable object generated from the database data.
	 * @throws SQLException
	 */
	public abstract domain.Savable load(DBmap data)
			throws SQLException;

	/**
	 * Converts a {@link domain.Savable} object into database ready data. All
	 * data needed should be passed in a {@code <"columnname",value>} Map.
	 * 
	 * @param obj
	 *            the object to convert
	 * @return a <"columnname", value> map containing all data needed to
	 *         reconstruct the object.
	 * @throws SQLException
	 */
	public abstract DBmap save(domain.Savable obj)
			throws SQLException;

	/**
	 * Method which returns which database fields are needed for the given type.
	 * Returns a {@code <"columnname","sqltype">} Map containing an entry for
	 * every database field needed, except for the id, which is always implicitly
	 * created
	 *
	 * @return a <"columnname","sqltype"> Map containing the fields.
	 */
	public abstract Map<String, String> getFields();
}
