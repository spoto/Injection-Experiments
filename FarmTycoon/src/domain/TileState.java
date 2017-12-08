package domain;

import exceptions.InvalidStateException;
import exceptions.InventoryFullException;
import api.TileInfo;

/**
 * This interface defines the glue between the tile and the different possible states.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 */
public interface TileState {
	/**
	 * Get the actions available on the this tile.
	 * @return the actions available on this tile.
	 * @throws InvalidStateException if the tile has an invalid state.
	 */
	public api.TileAction[] getActions() throws InvalidStateException;
	/**
	 * Execute an action on this tile.
	 * The action passed to this method should be previously received by a call to getActions().
	 * @param action the action to execute.
	 * @return whether or not the action succeeded.
	 * @throws InventoryFullException if there are not enough slots left in the inventory.
	 */
	public TileState executeAction(api.TileAction action, domain.Tile tile, long timestamp) throws InventoryFullException;
	/**
	 * @return the time at which this tile will expire. 
	 */
	public long getExpiryTime();
	/**
	 * @return information on this tile.
	 */
	public TileInfo getInfo();
}
