package vindinium.bot;

import vindinium.client.api.Response;
import vindinium.game.core.Action;

public abstract class TimedBot implements IBot {
	private long time = Long.MAX_VALUE;
	
	public Action getAction(final Response response) {
		long time = System.currentTimeMillis();
		Action action = getMoveDecision(response);
		time = System.currentTimeMillis()-time;
		
		return action;
	}

	/**
	 * Get the execution time used to make a decision
	 * @return time in ms or Long.MAX_VALUE if no decision yet
	 */
	public long getExecutionTime() {
		return time;
	}
	
	public abstract Action getMoveDecision(final Response response);
}
