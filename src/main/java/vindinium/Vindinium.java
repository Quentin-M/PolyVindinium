package vindinium;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.IBot;
import vindinium.client.Client;
import vindinium.client.api.Response;
import vindinium.client.core.Config;
import vindinium.exception.CrashedException;
import vindinium.exception.GameStateException;
import vindinium.game.core.Action;

/**
 * Play a game of Vindinium! All you need is a config and bot to start slaying.
 */
public class Vindinium {
	private final Client mClient;
	final static Logger logger = LogManager.getLogger();
	
	/**
	 * Create a new Vindinium game
	 * 
	 * @param config The game configuration
	 */
	public Vindinium(Config config) {
		mClient = new Client(config);
	}
	
	/**
	 * Play a game of Vindinium using the passed in Bot.
	 * 
	 * @param bot The bot to use in this game.
	 * @throws GameStateException Thrown if the game is in an invalid state (should only occur in multi-threaded environment)
	 * @throws IOException Thrown if the server cannot be reached
	 * @throws CrashedException Thrown if the bot takes too long to move and crashes
	 */
	public void playGame(IBot bot) throws GameStateException, IOException, CrashedException {
		// Start a new game
		Response response = mClient.startGame();
		
		// Log start of game
		logStart(response);
		
		// Make moves until game is finished
		while( !response.getGame().isFinished() ) {
			// Get bot's move and send to server
			Action nextMove = bot.getAction(response);
			response = mClient.sendMove(nextMove);
			
			// Log move and result
			logMove(nextMove, response);
		}
		
		// Log end of game
		logEnd(response);
	}
	
	public void logStart(Response response) {
		logger.info("Start a new game");
	}
	
	public void logMove(Action nextMove, Response response) {
		logger.info("["+(response.getGame().getTurn()-1)+" / "+response.getGame().getMaxTurns()+"] ");
	}
	
	public void logEnd(Response response) {
		logger.info("End a game");
	}
}
