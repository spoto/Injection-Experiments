package api;

/**
 * Action which can be executed on a tile.
 * @author Rigès De Witte, Simon Peeters,Barny Pieters,Laurens Van Damme
 *
 */
public interface TileAction {
	public enum Defaults implements TileAction {
		EXPIRE,
		CANCEL;
		public int getCost(){
			return 0;
		}
		public int getTime(){
			return 0;
		}
	}
	/**
	 * Get the name of the action.
	 * @return the name;
	 */
	public String name();
	/**
	 * Get the total cost for executing this action.
	 * @return the cost
	 */
	public int getCost();
	/**
	 * Get the time in days it takes to complete this action. 
	 * @return the time in days.
	 */
	public int getTime();
}
