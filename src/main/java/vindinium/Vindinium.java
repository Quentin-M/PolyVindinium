package vindinium;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.IBot;
import vindinium.client.Client;
import vindinium.client.core.Config;
import vindinium.exception.GameStateException;
import vindinium.game.core.Action;
import vindinium.game.core.Game;

/**
 * Play a game of Vindinium! All you need is a config and bot to start slaying.
 */
public class Vindinium {
	final static Logger logger = LogManager.getLogger();
	
	private final Client client;
	
	/**
	 * Create a new Vindinium game
	 * 
	 * @param config The game configuration
	 */
	public Vindinium(Config config) {
		client = new Client(config);
	}
	
	/**
	 * Play a game of Vindinium using the passed in Bot.
	 * 
	 * @param bot The bot to use in this game.
	 * @throws GameStateException Thrown if the game is in an invalid state (should only occur in multi-threaded environment)
	 * @throws IOException Thrown if the server cannot be reached
	 */
	public void playGame(IBot bot) throws GameStateException, IOException {
		// Start a new game
		Game game = client.startGame();
		
		// Log start of game
		logStart(game);
		
		// Make moves until game is finished
		while(!game.isFinished()) {
			System.out.println(game);
			
			// Get bot's move
			Action nextMove;
			if(!game.getHero().isCrashed()) nextMove = bot.getAction(game);
			else nextMove = Action.STAY;
			
			// Send answer to server & game updated game
			game = client.sendMove(game, nextMove);
			
			// Log move and result
			logMove(nextMove, game);
		}
		
		// Log end of game
		logEnd(game);
	}
	
	public void logStart(Game game) {
		logger.info("Start a new game");
		
		// Open in Browser the game
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(game.getViewUrl()));
			} catch(Exception e) {
				logger.info("To see my game, open "+game.getViewUrl());
			}
		} else {
			logger.info("To see my game, open "+game.getViewUrl());
		}
	}
	
	public void logMove(Action nextMove, Game game) {
		logger.info("["+(game.getTurn()-1)+" / "+game.getMaxTurns()+"] Decision: " + nextMove.name());
	}
	
	public void logEnd(Game response) {
		logger.info("End a game");
	}
}
