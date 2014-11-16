package vindinium.game.core;

/**
 * A Java POJO that represents a Vindinium Game
 */
public class Game {
	private String mId = null;
	private int mTurn = -1;
	private int mMaxTurns = -1;
	private Hero[] mHeroes = null;
	private Board mBoard = null;
	private boolean mFinished = false;
	
	/**
	 * Basic constructor for creating a default Vinidinium Game POJO
	 */
	public Game() {
		this(null, -1, -1, null, null, false);
	}
	
	/**
	 * Copy constructor
	 * @param game the game to copy
	 */
	public Game(Game game) {
		if(game == null) throw new IllegalArgumentException("Parameter can't be null");
		
		// Copy array of heroes
		Hero[] heroes = new Hero[game.mHeroes.length];
		for(int i = 0; i<heroes.length; i++) {
			if(game.mHeroes[i]!=null) heroes[i] = new Hero(game.mHeroes[i]);
		}
		
		setId(game.mId);
		setTurn(game.mTurn);
		setMaxTurns(game.mMaxTurns);
		setHeroes(heroes);
		setBoard(new Board(game.mBoard));
		setFinished(game.mFinished);
	}
	
	/**
	 * Full constructor for creating a Vindinium Game POJO
	 * @param id
	 * @param turn
	 * @param maxTurns
	 * @param heroes
	 * @param board
	 * @param finished
	 */
	public Game(String id, int turn, int maxTurns, Hero[] heroes, Board board, boolean finished) {
		setId(id);
		setTurn(turn);
		setMaxTurns(maxTurns);
		setHeroes(heroes);
		setBoard(board);
		setFinished(finished);
	}

	/**
	 * Get the game's id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Set the game's id
	 * 
	 * @param id The id of the game
	 */
	public void setId(String id) {
		this.mId = id;
	}

	/**
	 * Get the game's current turn
	 */
	public int getTurn() {
		return mTurn;
	}

	/**
	 * Set the game's current turn
	 * 
	 * @param turn The current turn of the game
	 */
	public void setTurn(int turn) {
		this.mTurn = turn;
	}

	/**
	 * Get the game's maximum number of turns
	 */
	public int getMaxTurns() {
		return mMaxTurns;
	}

	/**
	 * Set the game's maximum number of turns
	 * 
	 * @param maxTurns The maximum number of turns in the game
	 */
	public void setMaxTurns(int maxTurns) {
		this.mMaxTurns = maxTurns;
	}

	/**
	 * Get the game's heroes
	 */
	public Hero[] getHeroes() {
		return mHeroes;
	}

	/**
	 * Set the game's heroes
	 * 
	 * @param heroes The heroes in this game
	 */
	public void setHeroes(Hero[] heroes) {
		this.mHeroes = heroes;
	}

	/**
	 * Get the game's board
	 */
	public Board getBoard() {
		return mBoard;
	}

	/**
	 * Set the game's board
	 * 
	 * @param board The board in this game
	 */
	public void setBoard(Board board) {
		this.mBoard = board;
	}

	/**
	 * Get whether the game is finished or not
	 */
	public boolean isFinished() {
		return mFinished;
	}

	/**
	 * Set the game to finished (TRUE) or in-progress (FALSE)
	 * 
	 * @param finished The game's state (TRUE = finished)
	 */
	public void setFinished(boolean finished) {
		this.mFinished = finished;
	}
}
