package domain;

import java.sql.SQLException;
import java.util.Set;

/**
 * An abstract class used to abstract the save and load logic from elements that can be saved to the database.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public abstract class Savable {
	protected int id = -1;

	/**
	 * @return the id of this object, if the id field is not available, a new id is generated.
	 */
	public int getId() {
		if (id == -1)
			id = persistence.MapperList.valueOf(
					this.getClass().getSimpleName().toUpperCase()).getNextID();
		return id;
	}

	/**
	 * Save this object.
	 * @throws SQLException
	 */
	public void save() throws SQLException {
		persistence.MapperList.valueOf(
				this.getClass().getSimpleName().toUpperCase()).save(this);
	}

	/**
	 * Load an object.
	 * @param type the type of the object to load.
	 * @param id the id of the object to load.
	 * @return the loaded object.
	 * @throws SQLException
	 */
	public static Savable load(Class<? extends Savable> type, int id)
			throws SQLException {
		return persistence.MapperList.valueOf(
				type.getSimpleName().toUpperCase()).loadById(id);
	}

	/**
	 * Load all objects of a given type.
	 * @param type the type of the objects to load.
	 * @return a set of objects tat are loaded.
	 * @throws SQLException
	 */
	public static Set<Savable> loadAll(Class<? extends Savable> type)
			throws SQLException {
		return persistence.MapperList.valueOf(
				type.getSimpleName().toUpperCase()).loadAll();
	}
}
