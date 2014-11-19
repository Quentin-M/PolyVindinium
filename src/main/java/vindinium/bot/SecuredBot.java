package vindinium.bot;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import vindinium.client.api.Response;
import vindinium.game.core.Action;

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
	public Action getMoveDecision(final Response response) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Callable<Action> alphaBetaAlgorithm = new Callable<Action>() {
		    public Action call() throws Exception {
		    	return getAction(response);
			}
		};
		
		Future<Action> future = executor.submit(alphaBetaAlgorithm);
		executor.shutdown();
		
		Action action = null;
		try {
			action = future.get(timeOut, TimeUnit.MILLISECONDS);
		} catch(TimeoutException te) {
			action = getTimeoutAction(response);
		} catch(Exception e) { }
		
		if(!executor.isTerminated()) executor.shutdownNow();
		
		return action;
	}

	public abstract Action getAction(Response response);
	public abstract Action getTimeoutAction(Response response);
}
