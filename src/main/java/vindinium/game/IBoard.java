package vindinium.game;

/**
 * An interface for Vindinium game board data
 */
public interface IBoard {
	/**
	 * Get the size of the board
	 * 
	 * @return The board's size
	 */
	public int getSize();
	
	/**
	 * Get the string representation of the board's tiles
	 * 
	 * @return The board's tiles as a string
	 */
	public String getTiles();
	
	/**
	 * Get the tile at a specific point on the board
	 * 
	 * @param x The x position of the tile
	 * @param y The y position of the tile
	 * @return The type of tile at the specified position
	 * @throws IllegalArgumentException Thrown if the x or y parameter are out of bounds
	 */
	public Tile getTile(int x, int y);
}
