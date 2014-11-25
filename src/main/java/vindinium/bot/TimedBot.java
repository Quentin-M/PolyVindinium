package vindinium.bot;

import vindinium.game.core.Action;
import vindinium.game.core.Game;

public abstract class TimedBot implements IBot {
	private long time = Long.MAX_VALUE;
	
	public Action getAction(final Game game) {
		long t = System.currentTimeMillis();
		Action action = getMoveDecision(game);
		time = System.currentTimeMillis()-t;

		return action;
	}

	/**
	 * Get the execution time used to make a decision
	 * @return time in ms or Long.MAX_VALUE if no decision yet
	 */
	public long getExecutionTime() {
		return time;
	}
	
	public abstract Action getMoveDecision(final Game game);
}
