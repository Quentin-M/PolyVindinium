package vindinium.logger.core;

import java.awt.Desktop;
import java.net.URI;

import vindinium.client.api.Response;
import vindinium.game.core.Action;
import vindinium.logger.*;

/**
 * A basic logger who just output some useful informations
 */
public class DebugLogger implements ILogger {

	public void logStart(Response response) {
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(response.getViewUrl()));
			} catch(Exception e) {
				System.out.println("To see my game, open "+response.getViewUrl());
			}
		} else {
			System.out.println("To see my game, open "+response.getViewUrl());
		}
	}

	public void logMove(Action move, Response response) {
		// TODO Auto-generated method stub
	}

	public void logEnd(Response response) {
		// TODO Auto-generated method stub
	}

}
