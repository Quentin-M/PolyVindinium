package vindinium.bot.core.alphabot;

import java.util.ArrayList;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.core.SecuredBot;
import vindinium.game.core.Action;
import vindinium.game.core.Game;
import vindinium.simulation.Simulator;

/**
 * An alpha-beta Vindinium bot
 */
public class AlphaBot extends SecuredBot {	
	final static Logger logger = LogManager.getLogger();
	
	private HeuristicInterface heuristic;
	private int depth;
	
	/**
	 * Create a new AlphaBot
	 */
	public AlphaBot(HeuristicInterface heuristic, int depth) {
		this.heuristic = heuristic;
		this.depth = depth;
	}
	
	/**
	 * Get AlphaBot's name!
	 */
	public String getName() {
		return "AlphaBot";
	}

	@Override
	/**
	 * Get bot's next move using Alpha-Beta algorithm
	 * @param game
	 * @return the action we play
	 */
	public Action playSafe(final Game game) {
		return alphaBeta(game, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1).move;
	}

	@Override
	/**
	 * Get bot's next move when Alpha-Beta algorithm timed-out
	 * @param game
	 * @return the action we play
	 */
	public Action playQuickly(final Game game) {
		logger.warn(getName()+" played randomly !");
		
		return Action.values()[new Random().nextInt(Action.values().length)];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Define the return type of alpha-beta algorithm
	 */
	private class AlphaBetaResult {
		public int score;
		public Action move;
		
		public AlphaBetaResult(int s, Action m) {
			score = s;
			move = m;
		}
	}
	
	/**
	 * Define a child game after specified move
	 */
	private class AlphaBetaChild {
		public Action move;
		public Game game;
		
		public AlphaBetaChild(Action m, Game g) {
			move = m;
			game = g;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	private AlphaBetaResult alphaBeta(Game currentGame, int depth, int a, int b, int maximizing) {
		// Leaf node, compute score
		if(depth==0 || currentGame.isFinished()) {
			return new AlphaBetaResult(maximizing*heuristic.evaluate(currentGame), null);
		}
		
		AlphaBetaResult best = new AlphaBetaResult(Integer.MIN_VALUE, null);
		ArrayList<AlphaBetaChild> children = generateNextGames(currentGame, maximizing);
		
		if(depth==this.depth) logger.trace("Possible move at root : " + children.size());
		
		for(int i = 0; i<children.size(); i++) {
			AlphaBetaResult child_result = alphaBeta(children.get(i).game, depth - 1, -b, -a, -maximizing);
			child_result.score = - child_result.score;
			child_result.move = children.get(i).move;
			
			if(depth==this.depth) logger.trace("Score for " + children.get(i).move + " = " + child_result.score);
			
			if(best.score < child_result.score) {
				best = child_result;
			}
			
			a = Math.max(child_result.score, a);
			
			if(a >= b) break;
		}
		
		return best;
	}
	
	private ArrayList<AlphaBetaChild> generateNextGames(Game currentGame, int maximizing) {
		ArrayList<AlphaBetaChild> children;
		
		if(maximizing == 1) {
			children = new ArrayList<AlphaBetaChild>(5);
			
			for(Action action : Action.values()) {
				Game child = Simulator.simulate(currentGame, action, currentGame.getHero());
				
				if(child!=null) {				
					children.add(new AlphaBetaChild(action, child));
				}
			}
		} else {
			children = new ArrayList<AlphaBetaChild>(125);
			
			for(Action action1 : Action.values()) {
				for(Action action2 : Action.values()) {
					for(Action action3 : Action.values()) {
						Game child = currentGame;
						child = Simulator.simulate(child, action1, currentGame.getHeroes()[(currentGame.getHero().getId()+1)%4]);
						if(child!=null) child = Simulator.simulate(child, action2, currentGame.getHeroes()[(currentGame.getHero().getId()+2)%4]);
						if(child!=null) child = Simulator.simulate(child, action3, currentGame.getHeroes()[(currentGame.getHero().getId()+3)%4]);
						
						if(child!=null) children.add(new AlphaBetaChild(null, child));
					}
				}
			}
		}

		return children;
	}
}
