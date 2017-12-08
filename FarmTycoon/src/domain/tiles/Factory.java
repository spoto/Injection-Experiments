package domain.tiles;

import java.util.EnumMap;
import java.util.Map;

import api.TileAction;
import api.TileInfo;
import domain.Clock;
import domain.Game;
import domain.MsgQue;
import domain.Product;
import domain.Savable;
import domain.Tile;
import domain.TileState;
import exceptions.InvalidStateException;
import exceptions.InventoryFullException;

public class Factory extends Savable implements TileState {
	private enum State{
		NONE,IDLE,WORKING,DONE,DAMAGED;
	}

	private enum Actions implements TileAction {
		FACTORY_START(0),
		CLEAR(500),
		COLLECT(0);
		private Actions(int cost) {
			this.cost = cost;
		}
		public int cost;
		public int time=0;
		public int getCost() {return cost;}
		public int getTime() {return time;}
		
	}
	public static enum Factories implements TileAction {
		BUTTERFACTORY(750,Product.BUTTER,3,Product.MILK),
		CHEESEFACTORY(1000,Product.CHEESE,4,Product.MILK,Product.MILK),
		FLOURFACTORY(3000,Product.FLOUR,5,Product.CORN,Product.WHEAT,Product.WHEAT),
		JUICEFACTORY(5000,Product.JUICE,6,Product.STRAWBERRY,Product.RASPBERRY,Product.GRAPE,Product.GRAPE),
		SALADFACTORY(15000,Product.SALAD,8,Product.CARROT,Product.LETTUCE,Product.TOMATO,Product.TOMATO),
		OILFACTORY(25000,Product.CORNOIL,8,Product.CORN,Product.CORN,Product.CORN,Product.CORN,Product.CORN),
		CHOCOLATEFACTORY(40000,Product.CHOCOLATE,7,Product.COCOA,Product.COCOA,Product.MILK),
		BREADFACTORY(50000,Product.BREAD,6,Product.WHEAT,Product.WHEAT,Product.MILK,Product.EGGS),
		TRUFFLEFACTORY(100000,Product.TRUFFLES,12,Product.COCOA,Product.COCOA,Product.CORNOIL);
		
		EnumMap<Product,Integer> input;
		Product output;
		int price,time;
		Factories(int price, Product output, int time, Product...input){
			this.input = new EnumMap<Product,Integer>(Product.class);
			for(Product prod:input){
				if(this.input.containsKey(prod))
					this.input.put(prod, this.input.get(prod)+1);
				else
					this.input.put(prod, 1);
			}
			this.output=output;
			this.price=price;
			this.time=time;
		}

		public Map<Product,Integer> getInput()	{return input;}
		public Product getOutput()	{return output;}
		public int getCost()		{return price;}
		public int getTime()		{return time;}
	}

	Factories factory;
	State state=State.NONE;
	long damage;
	long start;
	
	public Factory(String type, long start, long damage, String state) {
		if(type.equals("NONE"))
			this.factory=null;
		else
			this.factory = Factories.valueOf(type);
		this.start = start;
		this.damage = damage;
		this.state = State.valueOf(state);
	}
	public Factory(){
		state=State.NONE;
	}

	public TileAction[] getActions() throws InvalidStateException {
		switch(state){
		case NONE:
			TileAction[] actions = new TileAction[Factories.values().length+1];
			int i=0;
			for(Factories f:Factories.values())
				actions[i++]=(TileAction) f;		
			actions[actions.length-1] = TileAction.Defaults.CANCEL;
			return actions;
		case IDLE:
			Actions.FACTORY_START.time=factory.getTime();
			return new TileAction[]{Actions.FACTORY_START,Actions.CLEAR};
		case DONE:
			return new TileAction[]{Actions.COLLECT};
		default:
			return null;
		}
	}

	public TileState executeAction(TileAction action, Tile tile, long timestamp) throws InventoryFullException {
		if(action instanceof domain.Storm && state == State.WORKING){
			damage=timestamp;
			state=State.DAMAGED;
			return this;
		}
		if(action instanceof Factories) {
			factory = (Factories) action;
			state = State.IDLE;
			return this;
		}
		if(action == TileAction.Defaults.EXPIRE) {
			switch (state){
			case DAMAGED:
				state=State.WORKING;
				start += Clock.MSECONDSADAY*2;
				return this;
			case WORKING:
				state=State.DONE;
				return this;
			default:
				return null;
			}
		}
		if(action == Actions.FACTORY_START) {
			if(!checkInput()){
				MsgQue.get().put("MSG_FACTORY_NOINPUT", timestamp);
				return null;
			}
			takeInput();
			state=State.WORKING;
			start=timestamp;
			return this;
		}
		if(action == Actions.COLLECT) {
			domain.Game.getGame().getInv().add(factory.getOutput());
			state = State.IDLE;
			return this;
		}
		if(action == Actions.CLEAR || action == TileAction.Defaults.CANCEL) {
			return new None();
		}
		return null;
	}

	private void takeInput() {
		for(Map.Entry<Product, Integer> entry:factory.getInput().entrySet()){
			Game.getGame().getInv().remove(entry.getKey(), entry.getValue());
		}
	}

	private boolean checkInput() {
		for(Map.Entry<Product, Integer> entry:factory.getInput().entrySet()){
			if(Game.getGame().getInv().get(entry.getKey()) < entry.getValue())
				return false;
		}
		return true;
	}

	public long getExpiryTime() {
		switch(state){
		case DAMAGED:	return damage+ (Clock.MSECONDSADAY * 2);
		case WORKING:	return start + (Clock.MSECONDSADAY * factory.getTime());
		default:		return 0;
		}
	}

	public TileInfo getInfo() {
		if(factory==null)
			return new TileInfo(getClass().getSimpleName(), null, state.name());
		return new TileInfo(getClass().getSimpleName(), factory.name(), state.name());
	}
	public String getType() {
		if(factory==null)
			return "NONE";
		return factory.name();
	}
	public long getStart() {
		return start;
	}
	public String getState() {
		return state.name();
	}
	public long getDamage() {
		return damage;
	}

}
