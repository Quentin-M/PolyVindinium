package vindinium.bot.core;

import vindinium.game.core.Action;
import vindinium.game.core.Game;

/**
 * Interface for a Vindinium bot. Your bot must implement this interface!
 */
public interface IBot {
	/**
	 * Get the bot's name
	 * 
	 * @return The name of the bot
	 */
	public String getName();
	
	/**
	 * Get the bot ready to play for the current game state
	 * Called right before play method, we have the entire game state and it counts within in 1s timer
	 * 
	 * @param game The current game state
	 */
	public void prePlay(final Game game);
	
	/**
	 * Get the next action from the bot for the current game state
	 * 
	 * @param game The current game state
	 * @return The bot's next action to take
	 */
	public Action play(final Game game);
	
	/**
	 * Do some things after we played
	 * Called right after play method
	 * 
	 * @param game The current game state, before we played on it
	 */
	public void postPlay(final Game game);
}
