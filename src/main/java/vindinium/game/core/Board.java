package vindinium.game.core;

import java.util.ArrayList;

/**
 * A simple implementation of a Vindinium board to help
 * query and interact with the game board.
 */
public class Board {
	private int size;
	
	private ArrayList<Position> mines;
	private ArrayList<Position> taverns;
	
	private Tile data[][];
	
	public Board(int size) {
		this.size = size;
		data = new Tile[size][size];
		mines = new ArrayList<Position>();
		taverns = new ArrayList<Position>();
	}
	
	/**
	 * Copy constructor
	 * @param board the board to copy
	 */
	public Board(Board cpy) {
		if(cpy == null) throw new IllegalArgumentException("Parameter can't be null");
		
		size = cpy.size;

		mines = new ArrayList<Position>(cpy.mines);
		taverns = new ArrayList<Position>(cpy.taverns);

		data = new Tile[size][size];
		for(int x=0; x<size; x++)
			for(int y=0; y<size; y++)
				data[x][y] = cpy.data[x][y];
	}
	
	/**
	 * Get the size of the board (all boards are square)
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * Return the positions of mines
	 * @return
	 */
	public ArrayList<Position> getMines() {
		return mines;
	}

	/**
	 * Get the positions of taverns
	 * @return
	 */
	public ArrayList<Position> getTaverns() {
		return taverns;
	}
	
	/**
	 * Get the tile corresponding to the (x,y) coordinate on the board or throw an exception if coordinates are out of bounds
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the corresponding tile
	 */
	public Tile getTile(int x, int y) {
		// Validate the x and y parameter
		if( x < 0 || y < 0 || x >= size || y >= size ) {
			throw new IllegalArgumentException("Cannot get Tile for an out of bounds (x, y) position");
		}
		
		return data[x][y];
	}
	
	/**
	 * Set a tile at defined coordinates
	 * 
	 * @param x
	 * @param y
	 * @param t
	 */
	public void setTile(int x, int y, Tile t) {
		// Validate the x and y parameter
		if( x < 0 || y < 0 || x >= size || y >= size ) {
			throw new IllegalArgumentException("Cannot set Tile for an out of bounds (x, y) position");
		}

		data[x][y] = t;
	}

	/**
	 * Return a string representing the board
	 * #: Woods
	 * 0, 1, 2, 3: Heroes
	 * A, B, C, D: Owned mines
	 * X: Neutral mines
	 * $: Tavern
	 */
	@Override
	public String toString(){
		String board = "";
		
		for(int x = 0; x<getSize(); x++) {
			for(int y = 0; y<getSize(); y++) {
				Tile t = getTile(x, y);

				if(t == Tile.AIR) board += "·";
				else if(t == Tile.WOODS) board += "#";
				else if(Tile.isHero(t)) board += (char)('0'+Tile.getOwner(t));
				else if(t == Tile.TAVERN) board += "$";
				else if(Tile.isMine(t)) {
					if(t == Tile.MINE_NEUTRAL) board += "X";
					else board += (char)('A'+Tile.getOwner(t));
				}				
			}
			board += "\r\n";
		}
		return board;
	}
}
