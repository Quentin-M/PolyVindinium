package vindinium.game.core;

import java.util.HashMap;

/**
 * A simple implementation of a Vindinium board to help
 * query and interact with the game board.
 */
public class Board {
	private static final HashMap<String, Tile> TILE_MAP = new HashMap<String, Tile>();
	private int mSize = 1;
	private String mTiles = null;
	
	static {
		TILE_MAP.put("  ", Tile.AIR);
		TILE_MAP.put("##", Tile.WOODS);
		TILE_MAP.put("[]", Tile.TAVERN);
		TILE_MAP.put("@1", Tile.HERO1);
		TILE_MAP.put("@2", Tile.HERO2);
		TILE_MAP.put("@3", Tile.HERO3);
		TILE_MAP.put("@4", Tile.HERO4);
		TILE_MAP.put("$-", Tile.MINE_FREE);
		TILE_MAP.put("$1", Tile.MINE_HERO1);
		TILE_MAP.put("$2", Tile.MINE_HERO2);
		TILE_MAP.put("$3", Tile.MINE_HERO3);
		TILE_MAP.put("$4", Tile.MINE_HERO4);
	}

	public Board() { }
	
	/**
	 * Copy construct
	 * @param board the board to copy
	 */
	public Board(Board board) {
		if(board == null) throw new IllegalArgumentException("Parameter can't be null");
		
		mSize = board.mSize;
		mTiles = new String(board.mTiles);
	}

	/**
	 * Get the size of the board (all boards are square)
	 */
	public int getSize() {
		return mSize;
	}
	
	/**
	 * Set the size of the board (all boards are square)
	 * @param size The size of one dimension of the board
	 * @throws IllegalArgumentException Thrown if the size is less than 1
	 */
	public void setSize(int size) {
		// Validate the size parameter
		if( size < 1 ) {
			throw new IllegalArgumentException("A game board cannot have a grid size smaller than 1");
		}
		
		mSize = size;
	}

	/**
	 * Get the string representation of all tiles on this board
	 */
	public String getTiles() {
		return mTiles;
	}
	
	/**
	 * Set the string representation of the tiles on this board
	 * 
	 * @param tiles The tiles on this board as a string
	 * @throws IllegalArgumentException Thrown if the tiles are null or not the correct length
	 */
	public void setTiles(String tiles) {
		if( tiles == null || (tiles.length() !=  (mSize * mSize * 2)) ) {
			throw new IllegalArgumentException("Game board tiles cannot be null and must have a length of the board size squared x2");
		}
		
		mTiles = tiles;
	}
	
	public Tile getTile(int x, int y) {
		// Validate the x and y parameter
		if( x < 0 || y < 0 || x >= mSize || y >= mSize ) {
			throw new IllegalArgumentException("Cannot get Tile for an out of bounds (x, y) position");
		}
		
		int start = x + (y * mSize);
		String tile = mTiles.substring(start, start + 2);
		
		return getTile(tile);
	}
	
	protected Tile getTile(String tileString) {
		Tile tile = TILE_MAP.get(tileString);
		
		// If a tile isn't returned, we don't know what the heck the string is
		if( tile == null ) {
			throw new IllegalArgumentException(String.format("Unkown tile: %s", tileString));
		}
		
		return tile;
	}
	/**
	 * return a string with the situation of the board (only 1 char for each tile)
	 * Player's color: red for hero1, blue for hero2, green for hero3, yellow for hero4.
	 */
	public String toString(){
		int boardSize = this.getSize();
		String board = "", boardTiles = this.getTiles();
		for (int i = 0; i < (boardTiles.length()) - 1; i+=2){
			if(boardTiles.charAt(i) == '@'){
				board += boardTiles.charAt(i+1);
			}
			else if(boardTiles.charAt(i) == '$')
			{
				switch(boardTiles.charAt(i+1)){
				case '1':
					board += 'a';
					break;
				case '2':
					board += 'b';
					break;
				case '3':
					board += 'c';
					break;
				case '4':
					board += 'd';
					break;
				case '-':
					board += '$';
					break;
				}
			}
			else
			{
				board += boardTiles.charAt(i);
			}
			
			if((i/2)%boardSize == boardSize-1){
				board += "\n";
			}
		}
		return board;
	}
}
