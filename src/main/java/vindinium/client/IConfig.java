package vindinium.client;


/**
 * The interface for a Vindinium configuration. Contains the key, base url, 
 * game mode, and other settings necessary to play a Vindinium game.
 */
public interface IConfig {
	/**
	 * Get the private key associated with this game (and bot usually)
	 * 
	 * @return The private key
	 */
	public String getKey();
	
	/**
	 * Get the base url to use for this game
	 * 
	 * @return The base url of the Vindinium server
	 */
	public String getBaseUrl();
	
	/**
	 * Get the game mode for this game
	 * 
	 * @return The game mode this configuration is set to play
	 */
	public GameMode getGameMode();
	
	/**
	 * Get the number of turns to use for a game.
	 * 
	 * @return The number of turns in a game
	 */
	public Integer getTurns();
	
	/**
	 * Get the map id to use for a game.
	 * 
	 * @return The map id to use in a game.
	 */
	public String getMapId();
}
