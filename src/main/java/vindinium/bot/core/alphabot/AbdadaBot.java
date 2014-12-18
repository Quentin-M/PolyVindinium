package vindinium.bot.core.alphabot;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.core.SecuredBot;
import vindinium.game.core.Action;
import vindinium.game.core.Game;
import vindinium.simulation.Simulator;

public class AbdadaBot extends SecuredBot {
	final static Logger logger = LogManager.getLogger();
	public final static int ON_EVALUATION = 66666;
	
	private HeuristicInterface heuristic;
	private int depth;
	private int threads;
	
	ExecutorService executor;
	private ConcurrentHashMap<Game, AbdadaTTNode> TT;
	private Future<Action>[] futures;
	
	/**
	 * Create a new AbdadaBot
	 */
	public AbdadaBot(HeuristicInterface heuristic, int depth, int threads) {
		this.heuristic = heuristic;
		this.depth = depth;
		this.threads = threads;
		
		executor =  Executors.newFixedThreadPool(threads);
	}
	
	/**
	 * Get AbdadaBot's name!
	 */
	public String getName() {
		return "AbdadaBot";
	}
	
	@Override
	/**
	 * Get bot's next move using Abdada algorithm
	 * @param game
	 * @return the action we play
	 */
	public Action playSafe(final Game game) {
		TT = new ConcurrentHashMap<Game, AbdadaTTNode>();

		futures = new Future[threads];
		for(int i = 0; i<threads; i++) {
			final int ii = i;
        	futures[i] = executor.submit(
	    		new Callable<Action>() {
	    		    public Action call() throws Exception {
	    		    	Thread.currentThread().setName("AlphaBeta N°" + ii);
	    		    	
	    		    	return alphaBeta(game, game, depth, Integer.MIN_VALUE+1, Integer.MAX_VALUE, 1, false).move;
	    			}
	    		}
	    	);
        }
		
        // Wait until first is done
        Action action = null;
        boolean isDone = false;
        while(!isDone) {
        	for(int i = 0; i<threads; i++) {
        		if(futures[i].isDone()) {
        			isDone = true;
        			try {
						action = futures[i].get();
					} catch (Exception e) {}
        			break;
        		}
        	}		
        }
        
        // Cancel all tasks
        for(int i = 0; i<threads; i++) {
        	futures[i].cancel(true);
        }
        
		return action;
	}

	@Override
	/**
	 * Get bot's next move when Abdada algorithm timed-out
	 * @param game
	 * @return the action we play
	 */
	public Action playQuickly(Game game) {
		logger.warn(getName()+" played randomly !");
		
		// Cancel all tasks
        for(int i = 0; i<threads; i++) {
        	futures[i].cancel(true);
        }
        
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


		public AlphaBetaResult(AlphaBetaResult best) {
			score = best.score;
			move = best.move;
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
	
	private class AbdadaTTNode {	
		public final static char UNSET = 99;
		public final static char EXACT = 0;
		public final static char UPPERBOUND = 1;
		public final static char LOWERBOUND = 2;
		
		public char flag;
		public AlphaBetaResult value;
		public int depth;
		public int nproc;
		
		public AbdadaTTNode(int d) {
			flag = UNSET;
			value = null;
			depth = d;
			nproc = 1;
		}

		public AbdadaTTNode(AbdadaTTNode ttEntry) {
			flag = ttEntry.flag;
			value = ttEntry.value;
			depth = ttEntry.depth;
			nproc = ttEntry.nproc;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////
	
	private AlphaBetaResult alphaBeta(Game initialGame, Game currentGame, int depth, int a, int b, int maximizing, boolean exclusiveP) {				
		// Leaf node, compute score
		if(depth==0 || currentGame.isFinished()) {
			return new AlphaBetaResult(maximizing*heuristic.evaluate(initialGame, currentGame), null);
		}
		
		AlphaBetaResult best = new AlphaBetaResult(Integer.MIN_VALUE+1, null);
		
		// TT Lookup
		AbdadaTTNode ttEntry = TT.get(currentGame);
		boolean ttEntryIsNew = false;
		
		if(ttEntry==null) {
			// Doesn't exist yet, create it and add it with nproc=1, flag=UNSET, value=null, depth=depth
			ttEntry = new AbdadaTTNode(depth);
			TT.put(currentGame, ttEntry);
			
			ttEntryIsNew = true;
		}
		
		// - Exclusivity case
		if(ttEntry.depth == depth && exclusiveP && ttEntry.nproc > 0) {
			best.score = ON_EVALUATION;
			return best;
		}
		// - Else if not new and depth >= depth
		else if(!ttEntryIsNew && ttEntry.depth >= depth) {		
			if(ttEntry.flag == AbdadaTTNode.EXACT) {
				best = ttEntry.value;
				a = ttEntry.value.score;
				b = ttEntry.value.score;
				//System.out.print("E");
			}
			else if(ttEntry.flag == AbdadaTTNode.UPPERBOUND && ttEntry.value.score < b) {
				best = ttEntry.value;
				b = ttEntry.value.score;
				//System.out.print("U");
			}
			else if(ttEntry.flag == AbdadaTTNode.LOWERBOUND && ttEntry.value.score > a) {				
				best = ttEntry.value;
				a = ttEntry.value.score;
				//System.out.print("L");
			}
			
			// A new worker on this node
			if(ttEntry.depth == depth && a < b) {
				ttEntry = new AbdadaTTNode(ttEntry);
				ttEntry.nproc++;
				TT.put(currentGame, ttEntry);
			}
			
			// Alpha-beta cutoff or exclusivity case
			if(a>=b) return new AlphaBetaResult(best);
		}
		else if(ttEntry.depth < depth) {
			ttEntry.flag = AbdadaTTNode.UNSET;
			ttEntry.depth = depth;
			ttEntry.nproc = 1;
		}
		
		// Generate list of children
		ArrayList<AlphaBetaChild> children = generateNextGames(currentGame, maximizing);
				
		// Main algorithm
		boolean allDone = false;
		for (int iteration = 0; iteration < 2 && a < b && !allDone; iteration++) {
			allDone = true;

			//if(depth==this.depth) logger.debug("Possible move at root : " + children.size());

			int i = 0;
			AlphaBetaChild M = children.get(i);
			while(M != null) {
				// Skip first son on 2nd iteration
				if(iteration==1 && i==0) {
					i++;
					if(i>=children.size()) M = null;
					else M = children.get(i);
				}
				
				boolean exclusive = ((iteration==0) && (i>0));
				
				AlphaBetaResult child_result = alphaBeta(initialGame, M.game, depth - 1, -b, -a, -maximizing, exclusive);
				child_result.score = -child_result.score;
				child_result.move = M.move;

				//if(depth==this.depth && iteration == 1) logger.info("Score for " + children.get(i).move + " = " + child_result.score);

				if(child_result.score == -ON_EVALUATION) {
					allDone = false;
				} else {
					if(best.score < child_result.score) {
						best = child_result;
					}
					
					a = Math.max(child_result.score, a);
					
					if(a >= b) break;
				}
				
				i++;
				if(i>=children.size()) M = null;
				else M = children.get(i);
			}
		}
		
		//if(depth==this.depth) logger.info("Decision:" + best.score + " " + best.move);
		
		// Transposition Table Store; node is the lookup key for ttEntry
		ttEntry = TT.get(currentGame);
		if(ttEntry.depth <= depth) {
			ttEntry = new AbdadaTTNode(ttEntry);
			
			if(ttEntry.depth == depth) ttEntry.nproc--;
			else ttEntry.nproc = 0;

			if(best.score <= a) ttEntry.flag = AbdadaTTNode.UPPERBOUND;
			else if(best.score >= b) ttEntry.flag = AbdadaTTNode.LOWERBOUND;
			else ttEntry.flag = AbdadaTTNode.EXACT;
			
			ttEntry.value = new AlphaBetaResult(best);
			ttEntry.depth = depth;
			
			TT.put(currentGame, ttEntry);
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