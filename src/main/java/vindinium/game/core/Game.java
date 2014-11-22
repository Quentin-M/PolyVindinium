package vindinium.game.core;

/**
 * A Java POJO that represents a Vindinium Game
 */
public class Game {
	private final String id;
	
	private int turn = -1;
	private final int maxTurns;
	
	private int heroIndex;
	private Hero[] heroes;
	private Board board;
	private boolean finished;
	
	private final String token;
	private final String viewUrl;
	private final String playUrl;
	
	/**
	 * Basic constructor to create a Vinidinium Game
	 */
	public Game(String id, int maxTurns, String token, String viewUrl, String playUrl, int size, int heroIndex) {
		this.id = id;
		this.maxTurns = maxTurns;
		this.token = token;
		this.viewUrl = viewUrl;
		this.playUrl = playUrl;
		this.heroIndex = heroIndex;
		
		board = new Board(size);
		heroes = new Hero[4];
		
		finished = false;
	}
	
	/**
	 * Copy constructor
	 * @param game the game to copy
	 */
	/**
	 * Copy constructor
	 * @param game the game to copy
	 */
	public Game(Game game) {
		if(game == null) throw new IllegalArgumentException("Parameter can't be null");
		
		id = game.id;
		
		turn = game.turn;
		maxTurns = game.maxTurns;
		
		heroIndex = game.heroIndex;
		Hero[] heroes = new Hero[game.heroes.length];
		for(int i = 0; i<heroes.length; i++) {
			if(game.heroes[i]!=null) heroes[i] = new Hero(game.heroes[i]);
		}
		board = new Board(game.board);
		finished = game.finished;
		
		token = null;
		viewUrl = null;
		playUrl = null;
	}

	/**
	 * Get the Play URL
	 * @return the play URL
	 */
	public String getPlayUrl() {
		return playUrl;
	}
	
	/**
	 * Get the View URL
	 * @return the view URL
	 */
	public String getViewUrl() {
		return viewUrl;
	}
	
	/**
	 * Get the game's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the game's current turn
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Set the game's current turn
	 * 
	 * @param turn The current turn of the game
	 */
	public void setTurn(int turn) {
		this.turn = turn;
	}

	/**
	 * Get the game's maximum number of turns
	 */
	public int getMaxTurns() {
		return maxTurns;
	}

	public Hero getHero() {
		return heroes[heroIndex];
	}
	
	/**
	 * Get the game's heroes
	 */
	public Hero[] getHeroes() {
		return heroes;
	}

	/**
	 * Get the game's board
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Get whether the game is finished or not
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Set the game to finished (TRUE) or in-progress (FALSE)
	 * 
	 * @param finished The game's state (TRUE = finished)
	 */
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	/**
	 * Move an hero on the map from its current position to a new one
	 * 
	 * @param heroIndex index of the target hero. Must be [0..3]
	 * @param target the new position of the hero. Must be a valid & free position
	 */
	public void moveHero(int heroIndex, Position target) {
		if(heroIndex<0 || heroIndex > 3) throw new IllegalArgumentException("Hero is invalid");
		if(getBoard().getTile(target.getX(), target.getY()) != Tile.AIR) throw new IllegalArgumentException("Position isn't free");
		
		getHeroes()[heroIndex].setPosition(target);
		getBoard().setTile(target.getX(), target.getY(), Tile.getHero(heroIndex));
	}
	
	/**
	 * Transfer a mine from a player/neutral to another player/neutral
	 * @param position
	 * @param dstIndex
	 */
	public void transferMine(Position position, int dstIndex) {
		transferMine(position, dstIndex, true);
	}
	
	/**
	 * Transfer a mine from a player/neutral to another player/neutral
	 * @param game
	 * @param position the position of the mine we transfer
	 * @param dstIndex index of the target owner. Must be [-1..3] where -1 means neutral and 3 the forth hero
	 * @param fight a boolean value to determine if the target owner loose 20 HP in the transfer
	 */
	public void transferMine(Position position, int dstIndex, boolean fight) {
		if(dstIndex<-1 || dstIndex > 3) throw new IllegalArgumentException("Destination hero is invalid");
		if(!Tile.isMine(getBoard().getTile(position.getX(), position.getY()))) throw new IllegalArgumentException("This position doesn't correspond to a mine");
		
		// Decrease the mine count of the current owner
		int currentOwnerIndex = Tile.getOwner(getBoard().getTile(position.getX(), position.getY()));
		if(currentOwnerIndex>=0) getHeroes()[currentOwnerIndex].setMineCount(getHeroes()[currentOwnerIndex].getMineCount()-1);
		
		// Set tile
		getBoard().setTile(position.getX(), position.getY(), Tile.getMine(dstIndex));
		
		// Update destination player life & mine count (if not neutral)
		if(dstIndex>=0) {
			getHeroes()[dstIndex].setMineCount(getHeroes()[dstIndex].getMineCount()+1);
			if(fight) getHeroes()[dstIndex].setLife(getHeroes()[dstIndex].getLife()-20);
		}
	}
	
	@Override
	public String toString() {
		String output = "********************************************************************************************************\r\n";
		
		// General informations
		output += String.format("%-16s", "Turns: "+turn+"/"+maxTurns);
		output += String.format("%88s", "Playing on: " + viewUrl);
		output += "\r\n\r\n";
		
		// Heroes
		for(int i = 0; i<4; i++) {
			if(i==heroIndex) output += "My Hero: ";
			else output += "Hero #"+i+": ";
			
			output += String.format("%-40s", heroes[i].getName() + " (ELO:" + heroes[i].getELO() + ")");
			output += String.format("%-15s", "HP:" + heroes[i].getLife());
			output += String.format("%-15s", "Gold:" + heroes[i].getGold());
			output += String.format("%-10s", "Mines:" + heroes[i].getMineCount());
			
			if(!heroes[i].isCrashed()) output += String.format("%15s", "[Running]");
			else output += String.format("%15s", "[Crashed]");
			
			if(heroes[i].isCrashed()) output += "[Crashed]";
			
			output += "\r\n";
		}
		
		output += "\r\n";
		
		// Board
		output += getBoard();
		
		output += "********************************************************************************************************\r\n";
		
		return output;
	}
}
