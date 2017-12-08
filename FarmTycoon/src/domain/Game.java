package domain;

import java.sql.SQLException;

import exceptions.InvalidStateException;
import exceptions.NoSuchTileException;
import exceptions.SystemDBException;

import api.Coordinate;
import api.TileAction;
import api.TileInfo;

/**
 * The controller for the game.
 * This class takes over from the domain controller once a game has been loaded. 
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class Game {
	private Farm farm;
	private Clock clock;
	private static Game current;
	private Inventory inv;

	/**
	 * Construct a new game, if necessary loading all information from the database.
	 * @param load whether or not to load a saved game.
	 */
	public Game(boolean load) {
		current=this;
		if (load) {
			try {
				clock = (Clock) Clock.load(Clock.class, 0);
				farm = (Farm) Farm.load(Farm.class, 0);
				inv = new Inventory(farm.countBarn());
				inv.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			clock = new Clock();
			farm = new Farm();
			inv = new Inventory(0);
		}
	}

	/**
	 * @return the farm.
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @return the clock.
	 */
	public Clock getClock() {
		return clock;
	}

	/**
	 * @return the amount of cash
	 */
	public int getCash() {
		return farm.getCash();
	}

	/**
	 * adjust the cash up or down with the given value.
	 * @param adj the adjustment to be made, >0 adds to the cash <0 reduces the cash.
	 * @return the new amount of cash.
	 */
	int adjustCash(int adj) {
		return farm.adjustCash(adj);
	}

	public void save() throws SQLException, SystemDBException {
		farm.save();
		clock.save();
		inv.save();
		persistence.PersistenceController.getInstance().sync();
	}

	/**
	 * get information on a specific tile.
	 * @param coord the coordinates of the tile for which to get the information.
	 * @return the information.
	 * @throws NoSuchTileException if there is no tile at this coordinate. 
	 */
	public TileInfo getTileInfo(Coordinate coord) throws NoSuchTileException {
		return farm.getTile(coord).getInfo();
	}

	/**
	 * Get the actions available on the tile specified.
	 * @param coord the coordinates of the tile for which to get the actions.
	 * @return the actions available on this tile.
	 * @throws InvalidStateException if the tile has an invalid state.
	 */
	public api.TileAction[] getTileActions(Coordinate coord) throws InvalidStateException {
		return farm.getTile(coord).getActions();
	}
	
	/**
	 * Execute an action on the specified tile.
	 * The action passed to this method should be previously received by a call to getTileActions() for the same tile.
	 * @param coord the coordinates of the tile to execute the action on.
	 * @param action the action to execute.
	 * @return whether or not the action succeeded.
	 * @throws NoSuchTileException if there is no tile at this coordinate. 
	 */
	public boolean executeAction(Coordinate coord, TileAction action) throws NoSuchTileException {
		return farm.getTile(coord).executeAction(action);
	}

	/**
	 * skip one game-day
	 */
	public void skipDay() {
		clock.skipDay();
	}

	/**
	 * increase the clocks multiplier.
	 */
	public void speedUp() {
		clock.setMultiplier(clock.getMultiplier()*2);
	}

	/**
	 * decrease the clocks multiplier.
	 */
	public void slowDown() {
		clock.setMultiplier(clock.getMultiplier()/2);
	}

	/**
	 * @return the current game.
	 */
	public static Game getGame() {
		return current;
	}

	/**
	 * @return the inventory for this game.
	 */
	public Inventory getInv() {
		return inv;
	}
	/**
	 * update the inventory.
	 */
	public void updateInv() {
		inv.setBarnCount(farm.countBarn());
	}

	/**
	 * Sell a given product on the market.
	 * @param product the product to sell.
	 */
	public void sell(Product product) {
		inv.remove(product, 1);
		adjustCash(product.getPrice());
	}

	/**
	 * @return the infection object
	 */
	public Infection getInfection() {
		return farm.getInfecion();
	}

	/**
	 * update this game.
	 */
	public void update() {
		farm.update();
	}

}
