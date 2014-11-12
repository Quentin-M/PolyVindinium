package vindinium.game;

import vindinium.game.core.Position;

/**
 * An interface for Vindinium Hero data
 */
public interface IHero {
	/**
	 * Get hero's id
	 * 
	 * @return The id of the hero
	 */
	public int getId();
	
	/**
	 * Get hero's name
	 * 
	 * @return The name of the hero
	 */
	public String getName();
	
	/**
	 * Get the hero's user id
	 * 
	 * @return The id of the hero's user
	 */
	public String getUserId();
	
	/**
	 * Get the hero's ELO rating
	 * 
	 * @return The ELO rating of this hero
	 */
	public int getELO();
	
	/**
	 * Get the hero's position on the board
	 * 
	 * @return The position of this hero on the board
	 */
	public Position getPosition();
	
	/**
	 * Get the hero's life points
	 * 
	 * @return The life points of this hero
	 */
	public int getLife();
	
	/**
	 * Get the hero's gold count
	 * 
	 * @return The gold count for this hero
	 */
	public int getGold();
	
	/**
	 * Get the hero's owned mine count
	 * 
	 * @return The owned mine count for this hero
	 */
	public int getMineCount();
	
	/**
	 * Get the hero's spawn position
	 * 
	 * @return The spawn position of this hero
	 */
	public Position getSpawnPosition();
	
	/**
	 * Get the hero's crash state
	 * 
	 * @return TRUE if the hero has crashed
	 */
	public boolean isCrashed();
}
