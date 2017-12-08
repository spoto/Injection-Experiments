package domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import exceptions.InvalidStateException;
import exceptions.InventoryFullException;

import api.Coordinate;
import api.TileAction;
import api.TileInfo;

/**
 * 
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class Tile extends Savable {
	private final static SortedMap<Long,Tile> expiryMap = Collections.synchronizedSortedMap(new TreeMap<Long,Tile>());

	private long expiryTime;
	private final Coordinate coord;
	private TileState state;

	/**
	 * create a new empty tile.
	 * @param coord the coordinate for this tile.
	 */
	public Tile(Coordinate coord) {
		this(coord, new domain.tiles.None(), 0);
	}

	/**
	 * create a new tile from data loaded from the database.
	 * @param coord the coordinate for this tile.
	 * @param state the state of this tile.
	 * @param expiryTime the loaded expirytime.
	 */
	public Tile(Coordinate coord, TileState state, long expiryTime) {
		this.coord = coord;
		this.state = state;
		this.expiryTime = expiryTime;
		if (expiryTime > 0) {
			synchronized (expiryMap) {
				while(expiryMap.get(expiryTime) != null) //almost imposible
					expiryTime++;
				expiryMap.put(expiryTime, this);
			}
		}
	}

	/**
	 * @param state the state to set
	 */
	public void setState(TileState state) {
		this.state = state;
	}

	/**
	 * @return this tiles coordinate.
	 */
	public Coordinate getCoordinate() {
		return coord;
	}

	public int getId() {
		return coord.hashCode();
	}

	/**
	 * Get the actions available on the this tile.
	 * @return the actions available on this tile.
	 * @throws InvalidStateException if the tile has an invalid state.
	 * @see domain.TileState#getActions()
	 */
	public TileAction[] getActions() throws InvalidStateException {
		return state.getActions();
	}

	/**
	 * Gets the expireyTime for this instance.
	 * @return The expireyTime.
	 */
	public long getExpiryTime() {
		return this.expiryTime;
	}

	/**
	 * @return the state
	 */
	public TileState getState() {
		return state;
	}

	/**
	 * Execute an action on this tile.
	 * The action passed to this method should be previously received by a call to getActions().
	 * @param action the action to execute.
	 * @return whether or not the action succeeded.
	 */
	public boolean executeAction(TileAction action) {
		return executeAction(action, Game.getGame().getClock().getTime());
	}
	/**
	 * Execute an action on this tile.
	 * The action passed to this method should be previously received by a call to getActions().
	 * @param action the action to execute.
	 * @param timestamp the time at which this should happen/have happened
	 * @return whether or not the action succeeded.
	 */
	public boolean executeAction(TileAction action, long timestamp) {
		try {
			if(Game.getGame().getCash() < action.getCost()) {
				MsgQue.get().put("MSG_NOCASH", timestamp);
				return false;
			}
			TileState tmp = state.executeAction(action, this, timestamp);
			if(tmp == null){
				return false;
			}
			Controller.getInstance().getGame().adjustCash(-action.getCost());
			this.state = tmp;
			this.expiryTime = state.getExpiryTime();
			if (expiryTime > 0) {
				synchronized (expiryMap) {
					while(expiryMap.get(expiryTime) != null){ //almost impossible
						expiryTime++;
					}
					expiryMap.put(expiryTime, this);
				}
			}
			Game.getGame().updateInv();
			return true;
		}catch(InventoryFullException e){
			MsgQue.get().put("INVENTORYFULL", timestamp);
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Update all tiles.
	 * This will let the tiles expire if their expirytime has passed.
	 */
	public static void update() {
		Map<Long,Tile> expiredMap;
		expiredMap = new HashMap<Long,Tile>(expiryMap.headMap(
				domain.Game.getGame().getClock().getTime()));
		for (Map.Entry<Long, Tile> entry : expiredMap.entrySet()) {
			expiryMap.remove(entry.getKey());
			if(entry.getValue().expiryTime == entry.getKey())
				entry.getValue().executeAction(TileAction.Defaults.EXPIRE, entry.getKey());
		}
	}
	
	/**
	 * @return info on this tile.
	 */
	public TileInfo getInfo() {
		return state.getInfo();
	}
}