package vindinium.logger;

import vindinium.client.api.Response;
import vindinium.game.core.Action;

/**
 * Interface for Vindinium game loggers. Used to log game progress.
 */
public interface ILogger {
	/**
	 * Logs the start of a new game.
	 * 
	 * @param response The server response to a new game request
	 */
	public void logStart(Response response);
	
	/**
	 * Logs a bot's next move to a game.
	 * 
	 * @param move The move being executed
	 * @param response The response to executing the move
	 */
	public void logMove(Action move, Response response);
	
	/**
	 * Log's the end of the current game.
	 * 
	 * @param response The last response received from the server
	 */
	public void logEnd(Response response);
}
