package vindinium.game.core;

/**
 * Simple implementation of the IPosition interface
 */
public class Position {
	private final int mX;
	private final int mY;
	
	/** 
	 * Create a new position object with x and y set
	 * 
	 * @param x The x value
	 * @param y The y value
	 * @throws IllegalArgumentException Thrown if x or y is less than 0
	 */
	public Position(int x, int y) {
		// Validate the x and y values
		if( x < 0 || y < 0 ) {
			throw new IllegalArgumentException("A Vindinium board (x, y) position cannot have negative values");
		}
		
		this.mX = x;
		this.mY = y;
	}
	
	/**
	 * Copy constructor
	 * @param position the position to copy
	 */
	public Position(Position position) {
		if(position == null) throw new IllegalArgumentException("Parameter can't be null");
		
		mX = position.mX;
		mY = position.mY;
	}

	/**
	 * Get the x value
	 */
	public int getX() {
		return mX;
	}
	
	/**
	 * Get the y value
	 */
	public int getY() {
		return mY;
	}
}
