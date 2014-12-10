package vindinium.bot.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import vindinium.game.core.Action;
import vindinium.game.core.Game;

public abstract class SecuredBot extends TimedBot {
	long timeOut = 900;
	
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

	public Action play(final Game game) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Callable<Action> alphaBetaAlgorithm = new Callable<Action>() {
		    public Action call() throws Exception {
		    	return playSafe(game);
			}
		};
		
		Future<Action> future = executor.submit(alphaBetaAlgorithm);
		executor.shutdown();
		
		Action action = null;
		try {
			action = future.get(timeOut, TimeUnit.MILLISECONDS);
		} catch(TimeoutException te) {
			action = playQuickly(game);
		} catch(Exception e) { }
		
		if(!executor.isTerminated()) executor.shutdownNow();
		
		return action;
	}

	public abstract Action playSafe(final Game game);
	public abstract Action playQuickly(final Game game);
}
