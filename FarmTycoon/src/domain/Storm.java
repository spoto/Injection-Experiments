package domain;

import java.util.ArrayList;
import java.util.Random;

import domain.tiles.Crop;
import domain.tiles.Plowed;
import domain.tiles.Plowing;

import api.Coordinate;
import api.TileAction;
/**
 * this class contains most of the logic to infect tiles.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public class Storm implements TileAction {
	Random rand;
	private long next;
	
	/**
	 * Create a new storm object.
	 * @param time the time at which the next storm will take place, if 0, the time will be randomly chosen.
	 */
	Storm(long time) {
		rand = new Random();
		if(time==0)
			forecastnext();
		else
			next = time;
	}
	/**
	 * generate a random time for the next storm.
	 */
	public void forecastnext() {
		int days = 0;
		while (true) {
			days++;
			if (rand.nextInt(20) == 0)
				break;
		}
		next = Game.getGame().getClock().getTime() + Clock.MSECONDSADAY * days
				+ (long) (rand.nextDouble() * Clock.MSECONDSADAY);
	}
	/**
	 * Execute the storm
	 * @param time the time at which this should happen/have happened
	 */
	public void doStorm(long time) {
		ArrayList<Coordinate> tiles = new ArrayList<Coordinate>();
		for(Tile tile : Game.getGame().getFarm().getTiles().values())
			if (tile.getState() instanceof Crop || tile.getState() instanceof Plowed || tile.getState() instanceof Plowing)
				tiles.add(tile.getCoordinate());
		int count = 0;
		if (tiles.size() > 0)
			while (count <= tiles.size() * 3 / 10) {
				Coordinate coord = tiles.get(rand.nextInt(tiles.size()));
				Game.getGame().executeAction(coord, this);
				count++;
			}
		tiles = new ArrayList<Coordinate>();
		for (Tile tile : Game.getGame().getFarm().getTiles().values())
			if (tile.getState() instanceof domain.tiles.Factory)
				tiles.add(tile.getCoordinate());
		count = 0;
		if (tiles.size() > 0)
			while (count <= tiles.size() / 2) {
				Coordinate coord = tiles.get(rand.nextInt(tiles.size()));
				Game.getGame().executeAction(coord, this);
				count++;
			}
	}
	/**
	 * Update this object.
	 * This executes the storm if the forecasted time has passed.
	 * A new forecasted time is created afterwards.
	 */
	public void update() {
		if (next < Game.getGame().getClock().getTime()){
			doStorm(next);
			MsgQue.get().put("MSG_STORM", next);
			forecastnext();
		}
	}
	/**
	 * @return the time at which the next storm will happen.
	 */
	public long getNext(){
		return next;
	}

	public String name() {return "STORM";}
	public int getCost() {return 0;}
	public int getTime() {return 0;}
}
