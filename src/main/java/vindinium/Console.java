package vindinium;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.core.IBot;
import vindinium.bot.core.alphabot.AbdadaBot;
import vindinium.bot.core.alphabot.AlphaBot;
import vindinium.bot.core.alphabot.FloydHeuristic;
import vindinium.client.Config;
import vindinium.client.GameMode;
import vindinium.exception.CrashedException;
import vindinium.exception.GameStateException;
import vindinium.exception.InvalidConfigurationException;

/**
 * A console app that runs a Vindinium game with RandoBot.
 */
public class Console {
	final static Logger logger = LogManager.getLogger(Console.class);
	
	// Poly_BayonBool
	// oclehzwz
	// http://vindinium.org/ai/wrfnsl2j
	
	// Polyb00bs
	// t6nnmjlf
	// http://vindinium.org/ai/rz3j6rr6
	
	public static void main(String[] args) throws InvalidConfigurationException {		
		String secret = "t6nnmjlf";
		GameMode mode = GameMode.ARENA;
		int gamesToPlay = 0;
		int threads = 1;
		int depth = 5;
		
		String serverURL = null;
		
		if(args.length >= 5) {
			secret = args[0];
			
			if(args[1].equals("ARENA")) mode = GameMode.ARENA;
			else if(args[1].equals("TRAINING")) mode = GameMode.TRAINING;
			else { printUsage(); System.exit(1); }
			
			try {
				gamesToPlay = Integer.parseInt(args[2]);
				threads = Integer.parseInt(args[3]);
				depth = Integer.parseInt(args[4]);
			} catch(Exception e) {
				printUsage();
				System.exit(1);
			}
			
			if(args.length==6) serverURL = args[5];
			
			if(depth<1 || threads<1 || gamesToPlay<0) {
				logger.error("Depth & thread values must be 1+. The number of games to play must be 0+.");
				System.exit(1);
			}
		} else {
			printUsage();
			logger.info("Launching with default values");
		}
				
		int gamesPlayed = 0;
		while(gamesToPlay == 0 || gamesPlayed < gamesToPlay) {
			IBot bot;
			if(threads==1) bot = new AlphaBot(new FloydHeuristic(), depth);
			else bot = new AbdadaBot(new FloydHeuristic(), depth, threads);
			
			Config config = new Config();
			config.setKey(secret);
			config.setGameMode(mode);
			if(serverURL!=null) config.setBaseUrl(serverURL);
			config.validateConfiguration();
			
			Vindinium vindinium = new Vindinium(config);
			
			try {
				vindinium.playGame(bot);
			} catch (GameStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CrashedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			gamesPlayed++;
		}
	}
	
	private static void printUsage() {
		logger.info("Usage: [Secret] [ARENA|TRAINING] [Number of Games (0 <=> No Limit)] [Threads] [Depth] {Server URL}");
	}
}
