package vindinium.bot.core;

import java.util.Random;

import vindinium.bot.IBot;
import vindinium.client.api.Response;
import vindinium.game.core.Action;

/**
 * An example Vindinium bot called RandoBot.
 * Can you predict it's next move?
 */
public class RandoBot implements IBot {
	private static final Random mRandom = new Random();
	private static final Action[] mActions = Action.values();
	
	/**
	 * Create a new RandoBot
	 */
	public RandoBot() { }
	
	/**
	 * Get RandoBot's name!
	 */
	public String getName() {
		return "RandoBot";
	}

	/**
	 * Get RandoBot's next move, randomly!
	 */
	public Action getAction(Response response) {
		return mActions[mRandom.nextInt(mActions.length)];
	}
}
