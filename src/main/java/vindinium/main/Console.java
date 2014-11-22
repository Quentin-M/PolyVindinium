package vindinium.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vindinium.Vindinium;
import vindinium.bot.IBot;
import vindinium.bot.core.RandoBot;
import vindinium.client.core.Config;

/**
 * A console app that runs a Vindinium game with RandoBot.
 */
public class Console {
	private static final String SECRET_KEY = "oclehzwz";
	
	final static Logger logger = LogManager.getLogger(Console.class);
	
	public static void main(String[] args) throws Exception {		
		IBot bot = new RandoBot();
		
		Config config = new Config();
		config.setKey(SECRET_KEY);
		config.validateConfiguration();
		
		Vindinium vindinium = new Vindinium(config);
		vindinium.playGame(bot);
	}
}
