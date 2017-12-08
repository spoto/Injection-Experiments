package domain;

import java.util.Date;

/**
 * Class which controls the in game clock.
 * 
 * The in game clock is based on the realtime clock. When a new Clock is created
 * without any arguments the current time and a multiplier of 1 is used. The
 * multiplier defines the rate at which in game time advances in comparison to
 * the real time. Through the use of a saved offset and the multiplier the in
 * game time also advances when the game is not running, forcing people run the
 * game often enough to get the maximum out of it.
 * 
 * @author Rigès De Witte
 * @author Simon Peeters
 * @author Barny Pieters
 * @author Laurens Van Damme
 * 
 */
public class Clock extends Savable {
	/**
	 * the time at which the clock starts
	 */
	private static final long STARTTIME = 1325419200000L; // 1 jan. 2012 6:00
	/**
	 * the amount of milliseconds that add up to one day.
	 */
	public static final long MSECONDSADAY = 86400000L;
	/**
	 * the speed multiplier of the clock
	 */
	private double multiplier;
	/**
	 * the offset between unix epoch and the time the clock was started
	 */
	private long offset;

	/**
	 * Creates a default clock. This clock is linked to the current time and
	 * starting at January 1st 2012 at 6:00 The multiplier is set to 1
	 */
	public Clock() {
		this(1);
	}

	/**
	 * Creates a clock with a custom multiplier. This clock will start at the
	 * current time at January 1st 2012 at 6:00, and will then further advance
	 * {@code multiplier} times as fast as the real time.
	 * 
	 * @param multiplier
	 *            the multiplier to use in this clock
	 */
	public Clock(double multiplier) {
		this(multiplier, new Date().getTime());
	}

	/**
	 * Creates a clock with custom multiplier and offset. This constructor
	 * initiates a clock as if it was started at {@code offset} seconds since
	 * unix epoch. The clock will advance at a speed of {@code multiplier} times
	 * the real time.
	 * 
	 * @param multiplier
	 *            the multiplier to use in this clock.
	 * @param offset
	 *            the time this clock was started in milliseconds since unix epoch.
	 */
	public Clock(double multiplier, long offset) {
		this.multiplier = multiplier;
		this.offset = offset;
	}

	/**
	 * 
	 * @return the offset value used by the current clock.
	 */
	public long getOffset() {
		return this.offset;
	}

	/**
	 * 
	 * @return The currently used multiplier of this clock.
	 */
	public double getMultiplier() {
		return this.multiplier;
	}

	/**
	 * Gets the current game time.
	 * 
	 * @return the current game time in milliseconds since unix epoch.
	 */
	public long getTime() {
		long delta = new Date().getTime() - offset;
		return ((long) ((double) delta * multiplier)) + STARTTIME;
	}

	/**
	 * Gets the current game time.
	 * 
	 * @return the current game time as a Date object.
	 */
	public Date getDate() {
		return new Date(this.getTime());
	}

	/**
	 * Changes the multiplier and recalculate the offset in such a fashion that
	 * the time does not jump.
	 * 
	 * @param multiplier
	 *            the new multiplier to apply
	 */
	public void setMultiplier(double multiplier) {
		// recalculate offset
		this.offset = (long) ((
				( new Date().getTime() * (multiplier - this.multiplier))
				+ this.offset * this.multiplier) / multiplier);
		this.multiplier = multiplier;
	}

	/**
	 * set the current in game time to the date passed
	 * 
	 * @param date
	 *            the new in game time as a Date object
	 */
	public void setTime(Date date) {
		setTime(date.getTime());
	}
	/**
	 * set the current in game time to the date passed
	 * 
	 * @param date
	 *            the new in game time in milliseconds since unix epoch
	 */
	public void setTime(long time) {
		this.offset = new Date().getTime()
				- (long) ((time - STARTTIME) / multiplier);
	}
	
	/**
	 * This makes the clock skip a complete day.
	 */
	public void skipDay() {
		this.setTime(this.getTime()+MSECONDSADAY);
	}
}
