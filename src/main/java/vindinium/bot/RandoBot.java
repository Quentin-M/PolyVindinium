package vindinium.bot;

import java.util.Random;

import vindinium.bot.core.IBot;
import vindinium.game.core.Action;
import vindinium.game.core.Game;

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
	public Action play(Game Game) {
		return mActions[mRandom.nextInt(mActions.length)];
	}

	public void prePlay(Game game) {}
	public void postPlay(Game game) {}
}
