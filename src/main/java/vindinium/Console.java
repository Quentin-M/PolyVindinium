package vindinium;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.bot.core.IBot;
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
		String secret;
		GameMode mode;
		int threads;
		int depth;
		
		if(args.length == 4) {
			secret = args[0];
			if(args[1].equals("ARENA")) mode = GameMode.ARENA;
			else mode = GameMode.TRAINING;
			threads = Integer.parseInt(args[2]);
			depth = Integer.parseInt(args[3]);
		} else {
			logger.info("Usage: [Secret] [ARENA|TRAINING] [Threads] [Depth]");
			logger.info("Launching with default values");
			
			secret = "t6nnmjlf";
			mode = GameMode.ARENA;
			threads = 4;
			depth = 5;
		}
				
		while(true) {
			IBot bot = new AlphaBot(new FloydHeuristic(), depth);
			
			Config config = new Config();
			config.setKey(secret);
			config.setGameMode(mode);
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
		}
	}
}
