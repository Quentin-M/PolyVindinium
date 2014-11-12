package vindinium.client.core;

import vindinium.client.GameMode;
import vindinium.client.IConfig;
import vindinium.exception.InvalidConfigurationException;

/**
 * A configuration POJO that can store and validate configuration settings.
 */
public class Config implements IConfig {
	public static final GameMode DEFAULT_GAME_MODE = GameMode.TRAINING;
	public static final String DEFAULT_BASE_URL = "http://vindinium.org/api";
	
	private String mKey = null;
	private String mBaseUrl = DEFAULT_BASE_URL;
	private GameMode mGameMode = DEFAULT_GAME_MODE;
	private Integer mTurns = null;
	private String mMapId = null;
	
	/**
	 * Get the key for this configuration
	 */
	public String getKey() {
		return mKey;
	}
	
	/**
	 * Set the key for this configuration
	 * 
	 * @param key The key for this configuration
	 * @throws IllegalArgumentException Thrown if key is null
	 */
	public void setKey(String key) {
		// Validate the key
		if( key == null ) {
			throw new IllegalArgumentException("Cannot set a null key in the configuration");
		}
		
		mKey = key;
	}
	
	/**
	 * Get the base url for this configuration (not including game mode)
	 */
	public String getBaseUrl() {
		return mBaseUrl;
	}
	
	/**
	 * Set the base url for this configuration (not including game mode)
	 * 
	 * @param baseUrl The base url for this configuration
	 * @throws IllegalArgumentException Thrown if baseUrl is null
	 */
	public void setBaseUrl(String baseUrl) {
		// Validate the base url
		if( baseUrl == null ) {
			throw new IllegalArgumentException("Cannot set configuration base url to null");
		}
		
		mBaseUrl = baseUrl;
	}
	
	/**
	 * Get the game mode for this configuration
	 */
	public GameMode getGameMode() {
		return mGameMode;
	}
	
	/**
	 * Set the game mode for this configuration (can't be null)
	 * 
	 * @param gameMode The game mode for this configuration
	 * @throws IllegalArgumentException Thrown if game mode is set to null.
	 */
	public void setGameMode(GameMode gameMode) {
		// Validate the game mode is not null
		if( gameMode == null ) {
			throw new IllegalArgumentException("Game mode cannot be set to null");
		}
		
		mGameMode = gameMode;
	}
	
	/**
	 * Get the number of turns for this configuration
	 */
	public Integer getTurns() {
		return mTurns;
	}
	
	/**
	 * Set the number of turns for this configuration
	 * 
	 * @param turns The number of turns for this configuration
	 */
	public void setTurns(Integer turns) {
		mTurns = turns;
	}
	
	/**
	 * Get the map id for this configuration
	 */
	public String getMapId() {
		return mMapId;
	}
	
	/**
	 * Set the map id for this configuration
	 * 
	 * @param mapId The map id for this configuration
	 */
	public void setMapId(String mapId) {
		mMapId = mapId;
	}
	
	/**
	 * Validate the current configuration settings.
	 * 
	 * @throws InvalidConfigurationException Thrown if the configuration settings are invalid (i.e. missing key)
	 */
	public void validateConfiguration() throws InvalidConfigurationException {
		if( mKey == null ) {
			throw new InvalidConfigurationException("No key provided in configuration");
		}
		
		if( mTurns != null && mTurns < 1 ) {
			throw new InvalidConfigurationException("Turns cannot be less than 1");
		}
	}
	
	@Override
	public String toString() {
		return String.format("Key: %1$s\nHost: %2$s\nMode: %3$s\nTurns: %4$s\nMap Id: %5$s", mKey, mBaseUrl, mGameMode, mTurns, mMapId);
	}
}
