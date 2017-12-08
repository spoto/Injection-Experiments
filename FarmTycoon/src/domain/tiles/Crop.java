package domain.tiles;

import api.TileAction;
import api.TileInfo;

import domain.Clock;
import domain.Game;
import domain.MsgQue;
import domain.Savable;
import domain.TileState;
import exceptions.InventoryFullException;

public class Crop extends Savable implements TileState {

	private enum State {
		GROWING, READY, ROTTEN, INFECTED;
	}
	private enum Actions implements TileAction {
		CLEAR(0, 20),
		DESINFECT(0, 20),
		HARVEST(0, 0);

		private int time, cost;

		public int getCost() {return cost;}
		public int getTime() {return time;}

		Actions(int time, int cost) {
			this.time = time;
			this.cost = cost;
		}
	}

	private Crops crop;
	private State state;
	private long planted;
	private long infected;
	private int infectioncount;

	public Crop(String type, long planted, String state) {
		this(Crops.valueOf(type), planted, State.valueOf(state));
	}

	public Crop(Crops crop) {
		this(crop,  Game.getGame().getClock().getTime(), State.GROWING);
	}

	public Crop(Crops crop, long date, State state) {
		this.crop = crop;
		this.planted = date;
		this.state = state;
	}

	public String getType() {
		return crop.name();
	}

	public long getPlanted() {
		return planted;
	}

	@Override
	public TileAction[] getActions() {
		switch(state) {
		case READY:		return new TileAction[]{Actions.CLEAR, Actions.HARVEST};
		case GROWING:
		case ROTTEN:	return new TileAction[]{Actions.CLEAR};
		case INFECTED:	return new TileAction[]{Actions.CLEAR, Actions.DESINFECT};
		default:	return null;
		}
	}

	@Override
	public TileState executeAction(TileAction action, domain.Tile tile, long timestamp) throws InventoryFullException {
		if(action instanceof domain.Infection && (state == State.GROWING || state == State.READY)) {
			state=State.INFECTED;
			infected=timestamp;
			infectioncount=1;
			return this;
		}
		if(action == TileAction.Defaults.EXPIRE) {
			switch(state) {
			case GROWING:	state=State.READY;  break;
			case READY:		state=State.ROTTEN; 
							MsgQue.get().put("MSG_CROP_ROTTEN", timestamp);
							break;
			case INFECTED:	planted += 2*Clock.MSECONDSADAY;
							if(planted > timestamp)
								state=State.ROTTEN;
							infectioncount++;
							Game.getGame().getInfection().spreadFrom(tile.getCoordinate(),timestamp);
							break;
			default:	return null;
			}
			return this;
		}
		if(action == Actions.CLEAR || action instanceof domain.Storm) {
			return new None();
		}
		if(action == Actions.DESINFECT) {
			state=State.GROWING;
			return this;
		}
		if(this.state == State.READY && (Actions) action == Actions.HARVEST) {
			domain.Game.getGame().getInv().add(crop.getProduct());
			return new Plowed();
		}
		return null;
	}

	@Override
	public long getExpiryTime() {
		switch(state) {
		case GROWING:	return planted + (Clock.MSECONDSADAY * crop.getTime());
		case READY:		return planted + (Clock.MSECONDSADAY * crop.getTime() * 3 / 2);
		case INFECTED:	return infected + (Clock.MSECONDSADAY * infectioncount);
		default:		return 0;
		}
	}
	
	public TileInfo getInfo() {
		return new TileInfo(getClass().getSimpleName(), crop.name(), state.name());
	}

	public String getState() {
		return state.name();
	}
}