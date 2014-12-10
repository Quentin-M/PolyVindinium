package vindinium.bot.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.Console;
import vindinium.game.core.Game;

public abstract class TimedBot implements IBot {
	final static Logger logger = LogManager.getLogger(Console.class);
	
	private long time = Long.MAX_VALUE;
	
	public void prePlay(Game game) {
		time = System.currentTimeMillis();
	}
	
	public void postPlay(Game game) {
		time = System.currentTimeMillis()-time;
		
		logger.info("The bot answered in " + time + "ms.");
	}
	
	/**
	 * Get the execution time used to make a decision
	 * @return time in ms or Long.MAX_VALUE if no decision yet
	 */
	public long getExecutionTime() {
		return time;
	}
}
