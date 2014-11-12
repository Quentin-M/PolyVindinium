package vindinium.logger.core;

import vindinium.client.api.Response;
import vindinium.game.Action;
import vindinium.logger.*;

/**
 * A basic logger who just output some useful informations
 */
public class BasicLogger implements ILogger {

	public void logStart(Response response) {
		System.out.println("To see my game, open "+response.getViewUrl());
	}

	public void logMove(Action move, Response response) {
		// TODO Auto-generated method stub
	}

	public void logEnd(Response response) {
		// TODO Auto-generated method stub
	}

}
