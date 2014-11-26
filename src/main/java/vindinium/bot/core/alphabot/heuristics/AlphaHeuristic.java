package vindinium.bot.core.alphabot.heuristics;

import vindinium.game.core.Game;

/**
 * Describes an interface to implements heuristics for AlphaBeta bots
 */
public interface AlphaHeuristic {
	public int evaluate(Game game);
}
