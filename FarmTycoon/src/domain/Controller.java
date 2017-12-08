package domain;

import java.sql.SQLException;

import exceptions.*;

/**
 * Domain Controlling class for non game-specefic-methods.
 * 
 * This class implements delegation methods needed to start or load a game,
 * together with other methods which do no apply to a specific game.
 *
 * @author Rigès De Witte
 * @author Simon Peeters
 * @author Barny Pieters
 * @author Laurens Van Damme
 * 
 */

public class Controller {
	private static Controller instance;
	private Game game;

	/**
	 * Gets an instance of the controller. If no instance exists, create a new one.
	 *
	 * @return an instance of the domain controller.
	 */
	public static Controller getInstance() {
		if (instance == null)
			instance = new Controller();
		return instance;
	}

	/**
	 * Checks wether an existing savegame exists in the database.
	 *
	 * @return True if an existing game is found, false otherwise
	 */
	public boolean saveExists() {
		try {
			return persistence.PersistenceController.getInstance().saveExists();
		} catch (DBConnectException e) {
			e.printStackTrace();
		} catch (SystemDBException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Loads a prviously saved game from the database
	 */
	public void loadGame() {
		game = new Game(true);
	}

	/**
	 * Create a new game from scratch
	 */
	public void newGame() {
		game = new Game(false);
		try {
			game.save();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SystemDBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the current game
	 */
	public Game getGame() {
		return this.game;
	}
}
