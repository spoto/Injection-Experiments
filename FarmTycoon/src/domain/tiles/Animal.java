package domain.tiles;

import java.util.Date;

import api.TileAction;
import api.TileInfo;
import domain.Clock;
import domain.Game;
import domain.MsgQue;
import domain.Product;
import domain.Savable;
import domain.TileState;
import exceptions.InvalidStateException;
import exceptions.InventoryFullException;
public class Animal extends Savable implements TileState{

	private enum Animals implements TileAction {
		NONE(50,0,null),
		Cow(200,12,Product.MILK),
		Chicken(50,10,Product.EGGS);

		private int cost;
		private Product product;
		private int time;
		Animals(int cost, int time, Product product){
			this.product = product;
			this.cost = cost;
			this.time = time;
		}
		
		public Product getProduct() { return product; }
		public int getCost() { return cost; }
		public int getTime() { return time; }
	}
	private enum State {
		NORMAL, READY, DEATH;
	}
	private enum Actions implements TileAction {
		CLEAR(0, 20), 
		COLLECT(0, 0);

		private int time, cost;

		public int getCost() {return cost;}
		public int getTime() {return time;}

		Actions(int time, int cost) {
			this.time = time;
			this.cost = cost;
		}
	}

	private Animals animal;
	private State state;
	private Date start;

	public Animal() {
		this(Animals.NONE);
	}
	public Animal(String type, long start) {
		this(Animals.valueOf(type), new Date(start));
	}

	public Animal(Animals animal) {
		this(animal,  Game.getGame().getClock().getDate());
	}

	public Animal(Animals animal, Date date) {
		this.animal = animal;
		this.start = date;
		this.state = State.NORMAL;
	}

	public String getType() {
		return animal.name();
	}
	
	public Date getStart() { return start; }

	@Override
	public TileAction[] getActions() throws InvalidStateException {
		if(animal == Animals.NONE)
			return new TileAction[]{Actions.CLEAR, Animals.Chicken, Animals.Cow};
		switch(state) {
		case READY:	return new TileAction[]{Actions.CLEAR, Actions.COLLECT};
		case NORMAL:
		case DEATH:	return new TileAction[]{Actions.CLEAR};
		default: throw new InvalidStateException();
		}
	}

	@Override
	public TileState executeAction(TileAction action, domain.Tile tile, long timestamp) throws InventoryFullException {
		if(action instanceof Animals)
			return new Animal((Animals) action);
		if(action == TileAction.Defaults.EXPIRE) {
			if(getExpiryTime() > Game.getGame().getClock().getTime())
				return null;
			switch(state) {
			case NORMAL:	this.state=State.READY;  break;
			case READY:		this.state=State.DEATH;
							MsgQue.get().put("MSG_ANIMAL_DEATH", timestamp);
							break;
			default:	return null;
			}
			return this;
		}
		if(action == Actions.CLEAR) {
			return new None();
		}
		if( this.state == State.READY && (Actions) action == Actions.COLLECT) {
			domain.Game.getGame().getInv().add(animal.getProduct());
			this.state = State.NORMAL;
			this.start = Game.getGame().getClock().getDate();
			return this;
		}
		return null;
	}

	@Override
	public long getExpiryTime() {
		if(animal== Animals.NONE)
			return 0;
		switch(state) {
		case NORMAL:	return start.getTime() + (Clock.MSECONDSADAY * animal.getTime());
		case READY:	return start.getTime() + (Clock.MSECONDSADAY * animal.getTime() * 2);
		default:	return 0;
		}
	}

	public TileInfo getInfo() {
		if(animal== Animals.NONE)
			return new TileInfo(getClass().getSimpleName(),null,null);
		return new TileInfo(getClass().getSimpleName(), animal.name(), state.name());
	}
}