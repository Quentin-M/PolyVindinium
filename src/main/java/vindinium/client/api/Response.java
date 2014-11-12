package vindinium.client.api;

import vindinium.game.IGame;
import vindinium.game.IHero;

/**
 * A Java POJO for a Vindinium server response.
 */
public class Response {
	private final String mToken;
	private final String mViewUrl;
	private final String mPlayUrl;
	private final IGame mGame;
	private final IHero mHero;
	
	/**
	 * Create a new Response object
	 * 
	 * @param token The token for this response
	 * @param viewUrl A url to view the game
	 * @param playUrl The play url used to send moves
	 * @param game A Java POJO for the game of this response
	 * @param hero A Java POJO for the hero of this response
	 */
	public Response(String token, String viewUrl, String playUrl, IGame game, IHero hero) {
		mToken = token;
		mViewUrl = viewUrl;
		mPlayUrl = playUrl;
		mGame = game;
		mHero = hero;
	}
	
	/**
	 * Get the token for this response
	 * 
	 * @return The token for this response
	 */
	public String getToken() {
		return mToken;
	}
	
	/**
	 * Get the view url for this response. The url can be used to view the game.
	 * 
	 * @return The view url for this response
	 */
	public String getViewUrl() {
		return mViewUrl;
	}
	
	/**
	 * Get the play url for this response. The url can be used to send moves and interact with the game.
	 * 
	 * @return The play url for this response
	 */
	public String getPlayUrl() {
		return mPlayUrl;
	}
	
	/**
	 * Get a Java POJO representing the Game associated with this response.
	 * 
	 * @return The Game POJO
	 */
	public IGame getGame() {
		return mGame;
	}
	
	/**
	 * Get a Java POJO representing the Hero associated with this response.
	 * 
	 * @return The Hero POJO
	 */
	public IHero getHero() {
		return mHero;
	}
}
