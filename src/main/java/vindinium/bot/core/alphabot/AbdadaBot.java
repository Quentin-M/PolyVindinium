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

import vindinium.bot.SecuredBot;
import vindinium.bot.core.alphabot.Simulator;
import vindinium.bot.core.alphabot.heuristics.AlphaHeuristic;
import vindinium.game.core.Action;
import vindinium.game.core.Game;

public class AbdadaBot extends SecuredBot {
	final static Logger logger = LogManager.getLogger();
	public final static int ON_EVALUATION = 66666;
	
	private AlphaHeuristic heuristic;
	private int depth;
	private int threads;
	
	ExecutorService executor;
	private ConcurrentHashMap<Game, AbdadaTTNode> TT;
	
	/**
	 * Create a new AbdadaBot
	 */
	public AbdadaBot(AlphaHeuristic heuristic, int depth, int threads) {
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
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
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
			this.depth = d;
			nproc = 1;
		}
		
		public AbdadaTTNode(AbdadaTTNode obj) {
			flag = obj.flag;
			value = obj.value;
			depth = obj.depth;
			nproc = obj.nproc;
		}
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////

	@Override
	/**
	 * Get bot's next move using Alpha-Beta algorithm
	 * @param game
	 * @return
	 */
	public Action getSecuredAction(final Game game) {	
		TT = new ConcurrentHashMap<Game, AbdadaTTNode>();
		
		Future<Action>[] futures = new Future[threads];
		for(int i = 0; i<threads; i++) {
			final int ii = i;
        	futures[i] = executor.submit(
	    		new Callable<Action>() {
	    		    public Action call() throws Exception {
	    		    	Thread.currentThread().setName("AlphaBeta N°" + ii);
	    		    	
	    		    	return alphaBeta(game, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, false).move;
	    			}
	    		}
	    	);
        }

        // Wait until first is done
        Action action = null;
        while(action == null) {
        	for(int i = 0; i<threads; i++) {
        		if(futures[i].isDone()) {
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
		
		logger.info("Abdada ran in " + this.getExecutionTime() + "ms");
		
		return action;
	}
	
	@Override
	/**
	 * Get bot's next move when Alpha-Beta algorithm timed-out
	 * @param game
	 * @return
	 */
	public Action getTimeoutAction(final Game game) {
		logger.info("AlphaBeta played randomly !");
		
		return Action.values()[new Random().nextInt(Action.values().length)];
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
	private AlphaBetaResult alphaBeta(Game currentGame, int depth, int a, int b, int maximizing, boolean exclusiveP) {
		// Leaf node, compute score
		if(depth==0 || currentGame.isFinished()) {
			return new AlphaBetaResult(maximizing*heuristic.evaluate(currentGame), null);
		}
		
		AlphaBetaResult best = new AlphaBetaResult(Integer.MIN_VALUE+1, null);
		
		// TT Lookup
		AbdadaTTNode ttEntry;
		//synchronized(TT) {
			ttEntry = TT.get(currentGame);
			
			// - Exclusivity case
			if(ttEntry != null && ttEntry.depth == depth && exclusiveP && ttEntry.nproc > 0) {
				best.score = ON_EVALUATION;
			}
			// - Else
			else if(ttEntry != null && ttEntry.depth >= depth) {				
				if(ttEntry.flag == AbdadaTTNode.EXACT) {
					best = ttEntry.value;
					a = ttEntry.value.score;
					b = ttEntry.value.score;
				}
				else if(ttEntry.flag == AbdadaTTNode.LOWERBOUND && ttEntry.value.score > a) {				
					best = ttEntry.value;
					a = ttEntry.value.score;
				}
				else if(ttEntry.flag == AbdadaTTNode.UPPERBOUND && ttEntry.value.score < b) {
					best = ttEntry.value;
					b = ttEntry.value.score;
				}
				
				// A new worker on this node
				if(ttEntry.depth == depth && a < b) {
					ttEntry.nproc++;
				}
			} else {
				// Doesn't exist yet, create it and add it with nproc=1, flag=UNSET, value=null, depth=depth
				ttEntry = new AbdadaTTNode(depth);
				TT.put(currentGame, ttEntry);
			}
		//}
		
		// Alpha-beta cutoff or exclusivity case
		if(a>=b || best.score == ON_EVALUATION) return best;
		
		// Generate list of children
		ArrayList<AlphaBetaChild> children = generateNextGames(currentGame, maximizing);

		// Main algorithm
		boolean allDone = false;
		for (int iteration = 0; iteration < 2 && a < b && !allDone; iteration++) {
			allDone = true;
			
			int i = 0;
			AlphaBetaChild M = children.get(i);
			while(M != null && a < b) {
				boolean exclusive = ((iteration==0) && (i!=0));
								
				AlphaBetaResult child_result = alphaBeta(M.game, depth - 1, -b, -Math.max(a, best.score), -maximizing, exclusive);
				child_result.score = -child_result.score;
				child_result.move = M.move;
								
				if(child_result.score == -ON_EVALUATION) {
					allDone = false;
				} else if(child_result.score > best.score) {
					best = child_result;

					if(best.score > b) break;
				}
				
				i++;
				if(i>=children.size()) M = null;
				else M = children.get(i);
			}
			
			if(best.score > b) break;
		}
		
		// Transposition Table Store; node is the lookup key for ttEntry
		//synchronized(TT) {
			ttEntry = TT.get(currentGame);
			
			if(ttEntry==null) ttEntry = new AbdadaTTNode(depth);
			else ttEntry = new AbdadaTTNode(ttEntry);
			
			if(ttEntry.depth <= depth) {			
				if(ttEntry.depth == depth) ttEntry.nproc--;
				else ttEntry.nproc = 0;
				
				if(best.score > b) ttEntry.flag = AbdadaTTNode.LOWERBOUND;
				else if(best.score <= a) ttEntry.flag = AbdadaTTNode.UPPERBOUND;
				else ttEntry.flag = AbdadaTTNode.EXACT;
				
				ttEntry.value = best;
				ttEntry.depth = depth;
				
				TT.put(currentGame, ttEntry);
			}
		//}

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
						child = Simulator.simulate(child, action1, currentGame.getHeroes()[currentGame.getHero().getId()+1]);
						if(child!=null) child = Simulator.simulate(child, action2, currentGame.getHeroes()[currentGame.getHero().getId()+2]);
						if(child!=null) child = Simulator.simulate(child, action3, currentGame.getHeroes()[currentGame.getHero().getId()+3]);
						
						if(child!=null) children.add(new AlphaBetaChild(null, child));
					}
				}
			}
		}

		return children;
	}
}