package vindinium.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import vindinium.client.api.ResponseParser;
import vindinium.client.core.Config;
import vindinium.exception.CrashedException;
import vindinium.exception.GameStateException;
import vindinium.game.core.Action;
import vindinium.game.core.Game;

/**
 * A client for connection to and interacting with a Vindinium server and game.
 * NOT THREAD SAFE (I'm ashamed)
 */
public class Client {
	final static Logger logger = LogManager.getLogger();
	
	private static final String KEY_KEY = "key";
	private static final String KEY_TURNS = "turns";
	private static final String KEY_MAP = "map";
	private static final String KEY_DIRECTION = "dir";
	
	private final Config config;
	private final HttpClient httpClient;
	private final String baseUrl;
	
	private boolean gameStarted = false;
	
	/**
	 * Create a new client with the passed in configuration.
	 * 
	 * @param config The configuration to use for this client
	 */
	public Client(Config cfg) {
		config = cfg;
		httpClient = HttpClientBuilder.create().build();
		baseUrl = String.format("%1$s/%2$s", config.getBaseUrl(), config.getGameMode().toString().toLowerCase());
	}
	
	/**
	 * Starts a new Vindinium game.
	 * 
	 * @return A new game instance
	 * @throws GameStateException Thrown if a game is already in progress
	 * @throws IOException Thrown if the client fails to communicate with the Vindinium server
	 */
	public Game startGame() throws GameStateException, IOException {
		// Check if a game is already started
		if(gameStarted) {
			throw new GameStateException("Game already in progress");
		}
		
		// Create a start game request
		HttpPost startGameRequest = createStartGameRequest();
		
		try {
			// Send the request, get a response, YAY!
			HttpResponse startGameResponse = httpClient.execute(startGameRequest);

			// Parse the JSON response
			JSONObject jsonResponse = new JSONObject(EntityUtils.toString(startGameResponse.getEntity()));
			Game game = ResponseParser.parseResponseJson(null, jsonResponse);
			
			// Set the game to started, let's roll!
			gameStarted = true;
			
			return game;
		} catch (IOException e) {
			logger.error("Failed to start game: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Sends a move for the current Vindinium game to the server and update game object
	 * @param game 
	 * 
	 * @param action The move action to send
	 * @return The response to the move
	 * @throws GameStateException Thrown if a game is not currently in progress
	 * @throws CrashedException Thrown if the move was sent too late, and the hero has crashed
	 * @throws IOException Thrown if the client fails to communicate with the Vindinium server
	 * @throws JSONException Thrown if the client fails to communicate with the Vindinium server

	 */
	public Game sendMove(Game game, Action action) throws GameStateException, IOException, CrashedException {
		// Check if a game is started (if not BURN IT TO THE GROUND)
		if(!gameStarted ) {
			throw new GameStateException("Game has not started yet");
		}
		
		// Create the move request
		HttpPost sendMoveRequest = createSendMoveRequest(game.getPlayUrl(), action);
		try {
			// Send the move request, get a response
			HttpResponse sendMoveResponse = httpClient.execute(sendMoveRequest);
			
			// Parse the response
			String sendMoveResponseEntity = EntityUtils.toString(sendMoveResponse.getEntity());
			if(sendMoveResponseEntity.startsWith("Vindinium - Time out!")) throw new CrashedException();
			
			JSONObject jsonResponse = new JSONObject(sendMoveResponseEntity);
			game = ResponseParser.parseResponseJson(game, jsonResponse);
			
			// If the game is finished, set game started to false so we can play again
			if(game.isFinished() ) {
				gameStarted = false;
			}
			
			return game;
		} catch (IOException e) {
			logger.error("Failed to send move: " + e.getMessage());
			throw e;
		} catch (JSONException e) {
			logger.error("Failed to send move: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Creates a start new game request
	 * 
	 * @return A start new game request
	 * @throws UnsupportedEncodingException Thrown if the url encoding fails
	 */
	protected HttpPost createStartGameRequest() throws UnsupportedEncodingException {
		// Create a POST request from the base url
		HttpPost startGameRequest = new HttpPost(baseUrl);
		
		// Add the key from the config as a parameter
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(KEY_KEY, config.getKey()));
		
		// Set the number of turns parameter (if set)
		Integer turns = config.getTurns();
		if( turns != null && turns > 0 ) {
			postParams.add(new BasicNameValuePair(KEY_TURNS, turns.toString()));
		}
		
		// Set the map id parameter (if set)
		String mapId = config.getMapId();
		if( mapId != null ) {
			postParams.add(new BasicNameValuePair(KEY_MAP, config.getMapId()));
		}
		
		// Attach the parameters as a url encoded form to the POST
		startGameRequest.setEntity(new UrlEncodedFormEntity(postParams));
		
		return startGameRequest;
	}
	
	/**
	 * Creates a send move request
	 * 
	 * @param playUrl the play url
	 * @param action The action for this move
	 * @return A send move request
	 * @throws UnsupportedEncodingException Thrown if the url encoding fails
	 */
	private HttpPost createSendMoveRequest(String playUrl, Action action) throws UnsupportedEncodingException {
		// Create a POST request from the base url
		HttpPost startGameRequest = new HttpPost(playUrl);
		
		// Add the key and move as parameters
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(KEY_KEY, config.getKey()));
		postParams.add(new BasicNameValuePair(KEY_DIRECTION, action.toString()));
		
		// Attach the parameters to the POST
		startGameRequest.setEntity(new UrlEncodedFormEntity(postParams));
		
		return startGameRequest;
	}
}
