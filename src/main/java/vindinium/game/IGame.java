package vindinium.game;


/**
 * An interface for a Vindinium Game
 */
public interface IGame {
	/**
	 * Get the game's id
	 * 
	 * @return The id of the game
	 */
	public String getId();
	
	/**
	 * Get the game's current turn
	 * 
	 * @return The current turn in the game
	 */
	public int getTurn();
	
	/**
	 * Get the game's maximum number of turns
	 * 
	 * @return The maximum number of turns in the game
	 */
	public int getMaxTurns();
	
	/**
	 * Get the game's heroes
	 * 
	 * @return The heroes in the game
	 */
	public IHero[] getHeroes();
	
	/**
	 * Get the game's board
	 * 
	 * @return The board in the game
	 */
	public IBoard getBoard();
	
	/**
	 * Get the game's finished state
	 * 
	 * @return The game's state (TRUE = finished)
	 */
	public boolean isFinished();
}
