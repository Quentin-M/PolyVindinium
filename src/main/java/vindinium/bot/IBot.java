package vindinium.bot;

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
	 * Get the next action from the bot for the current game state (response)
	 * 
	 * @param response The current game state (response)
	 * @return The bot's next action to take
	 */
	public Action getAction(final Game response);
}
