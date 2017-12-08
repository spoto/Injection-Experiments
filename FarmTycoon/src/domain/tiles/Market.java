package domain.tiles;

import api.TileAction;
import api.TileInfo;
import domain.TileState;

/**
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 * 
 */
public class Market implements TileState {

	/**
	 * Initializes a new market at the given coordinates
	 * 
	 * @param x
	 *            x-coordinate for market
	 * @param y
	 *            y-coordinate for market
	 */
	public Market() {
	}

	@Override
	public TileAction[] getActions() {
		return new TileAction[]{new TileAction(){
			public String name() { return "ENTER"; }
			public int getCost() { return 0; }
			public int getTime() { return 0; }}};
	}

	@Override
	public TileState executeAction(TileAction action, domain.Tile tile, long timestamp) {
		return null;
	}

	@Override
	public long getExpiryTime() {
		return 0;
	}

	@Override
	public TileInfo getInfo() {
		return new TileInfo(getClass().getSimpleName(), null, null);
	}
}
