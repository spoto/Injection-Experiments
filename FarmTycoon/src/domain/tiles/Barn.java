package domain.tiles;

import api.TileAction;
import api.TileInfo;
import domain.Game;
import domain.Tile;
import domain.TileState;
import exceptions.InvalidStateException;

public class Barn implements TileState {

	@Override
	public TileAction[] getActions() throws InvalidStateException {
		return new TileAction[]{new TileAction(){
			public String name() { return "CLEAR";}
			public int getCost() { return 20;}
			public int getTime() { return 0;}
		}};
	}

	@Override
	public TileState executeAction(TileAction action, Tile tile, long timestamp){
		if(action.name().equals("CLEAR")){
			if(Game.getGame().getInv().spaceLeft() > domain.Inventory.BARNSPACE)
				return new None();
			domain.MsgQue.get().put("BARNINUSE", timestamp);
		}
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
