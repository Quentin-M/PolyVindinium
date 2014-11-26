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

	@Override
	/**
	 * Return a string representation of this Position
	 */
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + x;
		result = prime * result + y;
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Position)) return false;
		
		Position other = (Position) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		
		return true;
	}
}
