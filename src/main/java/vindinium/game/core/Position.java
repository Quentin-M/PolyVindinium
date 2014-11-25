package vindinium.game.core;

/**
 * Simple implementation of the IPosition interface
 */
public class Position {
	private final int x;
	private final int y;
	
	/** 
	 * Create a new position object with x and y set
	 * 
	 * @param x The x value
	 * @param y The y value
	 */
	public Position(int x, int y) {	
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Copy constructor
	 * @param position the position to copy
	 */
	public Position(Position position) {
		if(position == null) throw new IllegalArgumentException("Parameter can't be null");
		
		x = position.x;
		y = position.y;
	}

	/**
	 * Get the x value
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get the y value
	 */
	public int getY() {
		return y;
	}
	
	public boolean equals(Position p){
		return (x == p.x && y == p.y);
	}
}
