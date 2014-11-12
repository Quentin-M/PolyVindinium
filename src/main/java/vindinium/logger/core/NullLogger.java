package vindinium.logger.core;

import vindinium.client.api.Response;
import vindinium.game.Action;
import vindinium.logger.ILogger;

/**
 * A logger that doesn't log squat! Used by Vindinium to reduce null checks.
 */
public class NullLogger implements ILogger {

	public void logStart(Response response) { }

	public void logMove(Action move, Response response) { }

	public void logEnd(Response response) { }

}
