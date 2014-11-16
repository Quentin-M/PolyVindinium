package vindinium.main;

import vindinium.Vindinium;
import vindinium.bot.core.RandoBot;
import vindinium.client.core.Config;
import vindinium.logger.core.DebugLogger;

/**
 * A console app that runs a Vindinium game with RandoBot.
 */
public class Console {
	private static final String SECRET_KEY = "oclehzwz";
	
	public static void main(String[] args) throws Exception {
		RandoBot bot = new RandoBot();
		
		Config config = new Config();
		config.setKey(SECRET_KEY);
		config.validateConfiguration();
		
		System.out.println(String.format("Starting game with configuration: %s", config.toString()));
		
		Vindinium vindinium = new Vindinium(config, new DebugLogger());
		vindinium.playGame(bot);
	}
}
