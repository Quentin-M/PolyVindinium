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
import org.json.JSONObject;

import vindinium.client.api.Response;
import vindinium.client.api.ResponseParser;
import vindinium.client.core.Config;
import vindinium.exception.CrashedException;
import vindinium.exception.GameStateException;
import vindinium.game.core.Action;

/**
 * A client for connection to and interacting with a Vindinium server and game.
 * NOT THREAD SAFE (I'm ashamed)
 */
public class Client {
	private static final String KEY_KEY = "key";
	private static final String KEY_TURNS = "turns";
	private static final String KEY_MAP = "map";
	private static final String KEY_DIRECTION = "dir";
	
	private final Config mConfig;
	private final HttpClient mHttpClient;
	private final String mBaseUrl;
	private final ResponseParser mParser = new ResponseParser();
	private boolean mGameStarted = false;
	private String mNextPlayUrl = null;
	
	/**
	 * Create a new client with the passed in configuration.
	 * 
	 * @param config The configuration to use for this client
	 */
	public Client(Config config) {
		mConfig = config;
		mHttpClient = HttpClientBuilder.create().build();
		mBaseUrl = String.format("%1$s/%2$s", mConfig.getBaseUrl(), mConfig.getGameMode().toString().toLowerCase());
	}
	
	/**
	 * Starts a new Vindinium game.
	 * 
	 * @return The response to a new game request
	 * @throws GameStateException Thrown if a game is already in progress
	 * @throws IOException Thrown if the client fails to communicate with the Vindinium server
	 */
	public Response startGame() throws GameStateException, IOException {
		// Check if a game is already started
		if( mGameStarted ) {
			throw new GameStateException("Game already in progress");
		}
		
		// Create a start game request
		HttpPost startGameRequest = createStartGameRequest();
		
		try {
			// Send the request, get a response, YAY!
			HttpResponse startGameResponse = mHttpClient.execute(startGameRequest);

			// Parse the JSON response
			JSONObject jsonResponse = new JSONObject(EntityUtils.toString(startGameResponse.getEntity()));
			Response response = mParser.parseResponseJson(jsonResponse);
			
			// Get the next play url and store it
			mNextPlayUrl = response.getPlayUrl();
			
			// Set the game to started, let's roll!
			mGameStarted = true;
			return response;
		} catch (IOException e) {
			System.err.println("Failed to start game");
			throw e;
		}
	}
	
	/**
	 * Sends a move for the current Vindinium game to the server.
	 * 
	 * @param action The move action to send
	 * @return The response to the move
	 * @throws CrashedException Thrown if the move was sent too late, and the hero has crashed
	 * @throws GameStateException Thrown if a game is not currently in progress
	 * @throws IOException Thrown if the client fails to communicate with the Vindinium server
	 */
	public Response sendMove(Action action) throws CrashedException, GameStateException, IOException {
		// Check if a game is started (if not BURN IT TO THE GROUND)
		if( !mGameStarted ) {
			throw new GameStateException("Game has not started yet");
		}
		
		// Create the move request
		HttpPost sendMoveRequest = createSendMoveRequest(action);
		
		try {
			// Send the move request, get a response
			HttpResponse sendMoveResponse = mHttpClient.execute(sendMoveRequest);
			
			// Parse the response
			JSONObject jsonResponse = new JSONObject(EntityUtils.toString(sendMoveResponse.getEntity()));
			Response response = mParser.parseResponseJson(jsonResponse);
			
			// Get the next play url and store it
			mNextPlayUrl = response.getPlayUrl();
			
			// If the game is finished, set game started to false so we can play again
			if( response.getGame().isFinished() ) {
				mGameStarted = false;
			}
			return response;
		} catch (IOException e) {
			System.err.println("Failed to start game");
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
		HttpPost startGameRequest = new HttpPost(mBaseUrl);
		
		// Add the key from the config as a parameter
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(KEY_KEY, mConfig.getKey()));
		
		// Set the number of turns parameter (if set)
		Integer turns = mConfig.getTurns();
		if( turns != null && turns > 0 ) {
			postParams.add(new BasicNameValuePair(KEY_TURNS, turns.toString()));
		}
		
		// Set the map id parameter (if set)
		String mapId = mConfig.getMapId();
		if( mapId != null ) {
			postParams.add(new BasicNameValuePair(KEY_MAP, mConfig.getMapId()));
		}
		
		// Attach the parameters as a url encoded form to the POST
		startGameRequest.setEntity(new UrlEncodedFormEntity(postParams));
		
		return startGameRequest;
	}
	
	/**
	 * Creates a send move request
	 * 
	 * @param action The action for this move
	 * @return A send move request
	 * @throws UnsupportedEncodingException Thrown if the url encoding fails
	 */
	private HttpPost createSendMoveRequest(Action action) throws UnsupportedEncodingException {
		// Create a POST request from the base url
		HttpPost startGameRequest = new HttpPost(mNextPlayUrl);
		
		// Add the key and move as parameters
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(KEY_KEY, mConfig.getKey()));
		postParams.add(new BasicNameValuePair(KEY_DIRECTION, action.toString()));
		
		// Attach the parameters to the POST
		startGameRequest.setEntity(new UrlEncodedFormEntity(postParams));
		
		return startGameRequest;
	}
}
