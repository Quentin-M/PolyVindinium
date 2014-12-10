package vindinium;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.core.IBot;
import vindinium.client.Client;
import vindinium.client.Config;
import vindinium.exception.CrashedException;
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
	 * @throws CrashedException  Thrown if the bot took too long to play
	 */
	public void playGame(IBot bot) throws GameStateException, IOException, CrashedException {
		// Start a new game
		Game game = client.startGame();
		
		// Log start of game
		logStart(game);
		
		// Make moves until game is finished
		while(!game.isFinished()) {
			System.out.println(game);
			
			// Play!
			Action nextMove;
			bot.prePlay(game);
			nextMove = bot.play(game);
			bot.postPlay(game);
			
			// Send answer to server & game updated game
			game = client.sendMove(game, nextMove);
			
			// Log move and result
			logMove(nextMove, game);
		}
		
		// Log end of game
		logEnd(game);
	}
	
	public void logStart(Game game) {
		// Compute average ELO score
		int avgELO = 0;
		for(int i = 0; i<4; i++) {
			if(game.getHero().getId() == i) continue;
			avgELO += game.getHeroes()[i].getELO();
		}
		avgELO = (int) (avgELO / 3d);
		
		logger.info("Starting a new game in " + client.getConfig().getGameMode() + " mode. My current ELO is : " + game.getHero().getELO() + " and the average opponents' ELO is " + avgELO+". You can watch my game on : " + game.getViewUrl());
		
		// Open in Browser the game
		/*if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(game.getViewUrl()));
			} catch(Exception e) {}
		}*/
	}
	
	public void logMove(Action nextMove, Game game) {
		logger.debug("["+(game.getTurn()-1)+" / "+game.getMaxTurns()+"] Decision: " + nextMove.name());
	}
	
	public void logEnd(Game game) {
		// Verify if our bot won the game
		boolean isWon = true;
		for(int i = 0; i<4; i++) {
			if(game.getHeroes()[i].getGold() > game.getHero().getGold()) {
				isWon = false;
				break;
			}
		}
		
		String output = "I just finished a game and I ";
		if(isWon) output +="won it ! Well done ~";
		else output += "lost. Better luck next time ~";
		
		logger.info(output);
	}
}
