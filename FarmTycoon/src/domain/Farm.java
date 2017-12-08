package domain;

import java.sql.SQLException;
import java.util.Random;

import java.util.Map;
import java.util.HashMap;

import api.Coordinate;

import domain.tiles.Market;
import exceptions.NoSuchTileException;

/**
 * The Class which has control over the whole farm. 
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class Farm extends Savable {
	/**
	 * the initial size of the farm expressed as a coordinate. 
	 */
	public static final Coordinate size = new Coordinate(4,4);
	/**
	 * the amount of cash the farm starts with by default.
	 */
	private static final int STARTCASH = 1000;

	/**
	 * the amount of cash left.
	 */
	private int cash;
	/**
	 * a map containing all tiles mapped to their coordinate.
	 */
	private Map<Coordinate,Tile> tileMap = new HashMap<Coordinate,Tile>();
	/**
	 * the market of this game
	 */
	private Market market = null;
	/**
	 * the infection object.
	 */
	private Infection infection;
	/**
	 * the storm object
	 */
	private Storm storm;

	/**
	 * Construct a default empty farm 
	 */
	public Farm() {
		this(STARTCASH, false,0,0);
	}

	/**
	 * Reconstruct a farm from a previous state.
	 * @param cash the amount of cash to begin with.
	 * @param loadTiles whether to load the tiles from the database or create new empty ones.
	 * @param infection the forecasted time for the next infection.
	 * @param storm the forecasted time for the next storm.
	 */
	public Farm(int cash, boolean loadTiles, long infection, long storm) {
		for (Coordinate i : Coordinate.getCoordSet(new Coordinate(0,0),size)) {
			if (loadTiles)
				try {
					tileMap.put(i, (Tile) Tile.load(
						Tile.class, (i.hashCode())));
					TileState state = tileMap.get(i).getState();
					if (state instanceof Market)
						this.market = (Market) state;
				} catch (Exception e) {
					e.printStackTrace();
					tileMap.put(i, new Tile(i));
				}
			else
				tileMap.put(i, new Tile(i));
		}
		this.infection = new Infection(infection);
		this.storm = new Storm(storm);
		this.cash = cash;
		if (this.market == null) {
			Coordinate[] mcoord = {
				new Coordinate(0,0),
				new Coordinate(0,size.getY()-1),
				new Coordinate(size.getX()-1,0),
				new Coordinate(size.getX()-1,size.getY()-1)};
			int i = new Random().nextInt(4);

			this.tileMap.get(mcoord[i]).setState(new Market());
			this.market = (Market) this.tileMap.get(mcoord[i]).getState();
			if (this.market == null) {
				throw new java.lang.NullPointerException(
						"failed to create market");
			}
		}
		if (this.market == null) {
			throw new java.lang.NullPointerException("no market");
		}

	}

	/**
	 * @return the width of the farm
	 */
	public int getWidth() {
		return size.getX();
	}

	/**
	 * @return the height of the farm
	 */
	public int getHeight() {
		return size.getY();
	}

	/**
	 * @return the amount of cash available
	 */
	public int getCash() {
		return cash;
	}
	
	/**
	 * adjust the cash up or down with the given value.
	 * @param adj the adjustment to be made, >0 adds to the cash <0 reduces the cash.
	 * @return the new amount of cash.
	 */
	int adjustCash(int adj) {
		this.cash += adj;
		return this.cash;
	}

	/**
	 * get the tile at a given coordinate.
	 * @param coord the Coordinate of the tile to get.
	 * @return the Tile at this coordinate.
	 * 
	 * @throws NoSuchTileException if there is no tile at this coordinate. 
	 */
	public Tile getTile(Coordinate coord) throws NoSuchTileException {
		if(tileMap.get(coord)==null)
			throw new NoSuchTileException();
		return tileMap.get(coord);
	}

	/**
	 * @return the market
	 */
	public Market getMarket() {
		return market;
	}

	public int getId() {
		return 0;
	}

	public void save() throws SQLException {
		super.save();
		for (Tile tile : tileMap.values())
			tile.save();
	}

	/**
	 * @return the map containing all tiles.
	 */
	public Map<Coordinate, Tile> getTiles() {
		return tileMap;
	}
	
	public static void objPrinter(String[] options) {
	    Storm[] objStorm = new Storm[options.length];
	    for (int pos = 0; pos < options.length; pos++){
	    	objStorm[pos] = new Storm(Long.parseLong(options[1])); // Fill obj correctly for print
	    }
	    System.out.println(objStorm);
	}
	
	/**
	 * update the farm.
	 */
	public void update() {
		Tile.update();
		storm.update();
		infection.update();
	}

	/**
	 * @return the infection object.
	 */
	public Infection getInfecion() {
		return infection;
	}
	
	/**
	 * @return the storm object.
	 */
	public Storm getStorm() {
		return storm;
	}

	/**
	 * count the barns available.
	 * @return the number of barns on the farm.
	 */
	public int countBarn() {
		int count=0;
		for(Tile tile:tileMap.values())
			if(tile.getState() instanceof domain.tiles.Barn)
				count++;
		return count;
	}
}
