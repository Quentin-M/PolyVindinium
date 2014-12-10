package vindinium.bot.core.alphabot;

import vindinium.game.core.Game;

/**
 * Describes an interface to implements heuristics for AlphaBeta bots
 */
public interface HeuristicInterface {
	public int evaluate(Game game);
}
