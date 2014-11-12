package vindinium;

import java.io.IOException;

import vindinium.bot.IBot;
import vindinium.client.Client;
import vindinium.client.IConfig;
import vindinium.client.api.Response;
import vindinium.exception.CrashedException;
import vindinium.exception.GameStateException;
import vindinium.game.Action;
import vindinium.logger.ILogger;
import vindinium.logger.core.NullLogger;

/**
 * Play a game of Vindinium! All you need is a config and bot to start slaying.
 */
public class Vindinium {
	private final Client mClient;
	private ILogger mLogger;
	
	/**
	 * Create a new Vindinium game
	 * 
	 * @param config The game configuration
	 */
	public Vindinium(IConfig config) {
		this(config, null);
	}
	
	/**
	 * Create a new Vindinium game with a logger
	 * 
	 * @param config The game configuration
	 * @param logger A logger to use for logging game progress 
	 */
	public Vindinium(IConfig config, ILogger logger) {
		mClient = new Client(config);
		setLogger(logger);
	}
	
	/**
	 * Set a logger for logging game progress. Set null to clear the current logger.
	 * 
	 * @param logger A logger to use for logging game progress. Use null to clear.
	 */
	public void setLogger(ILogger logger) {
		// If logger is set to null, default it to NullLogger
		if( logger == null ) {
			logger = new NullLogger();
		}
		
		mLogger = logger;
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
		if( mLogger != null ) {
			mLogger.logStart(response);
		}
		
		// Make moves until game is finished
		while( !response.getGame().isFinished() ) {
			// Get bot's move and send to server
			Action nextMove = bot.getAction(response);
			response = mClient.sendMove(nextMove);
			
			// Log move and result
			if( mLogger != null ) {
				mLogger.logMove(nextMove, response);
			}
		}
		
		// Log end of game
		if( mLogger != null ) {
			mLogger.logEnd(response);
		}
	}
}
