package vindinium.bot;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import vindinium.game.core.Action;
import vindinium.game.core.Game;

public abstract class SecuredBot extends TimedBot {
	long timeOut = 950;
	
	/**
	 * Initialize a new bot with time-out management and default time-out value
	 */
	public SecuredBot() {}
	
	/**
	 * Initialize a new bot with time-out management
	 * @param timeout the timeout value in ms
	 */
	public SecuredBot(long timeOut) {
		this.timeOut = timeOut;
	}
	
	@Override
	public Action getMoveDecision(final Game game) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Callable<Action> alphaBetaAlgorithm = new Callable<Action>() {
		    public Action call() throws Exception {
		    	return getAction(game);
			}
		};
		
		Future<Action> future = executor.submit(alphaBetaAlgorithm);
		executor.shutdown();
		
		Action action = null;
		try {
			action = future.get(timeOut, TimeUnit.MILLISECONDS);
		} catch(TimeoutException te) {
			action = getTimeoutAction(game);
		} catch(Exception e) { }
		
		if(!executor.isTerminated()) executor.shutdownNow();
		
		return action;
	}

	@Override
	public abstract Action getAction(Game game);
	public abstract Action getTimeoutAction(Game game);
}
