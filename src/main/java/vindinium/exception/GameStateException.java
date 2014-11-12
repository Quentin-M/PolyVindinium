package vindinium.exception;

/**
 * An exception for invalid game states (i.e. Starting a game already in progress)
 */
public class GameStateException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new GameStateException with the passed in exception message
	 * 
	 * @param error The reason the exception was thrown
	 */
	public GameStateException(String error) {
		super(error);
	}
}
