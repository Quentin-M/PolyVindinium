package vindinium.game.core;

/**
 * An exception for non-existence of any path
 */
public class NoPathException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new NoPathException with the passed in exception message
	 * 
	 * @param error The reason the exception was thrown
	 */
	public NoPathException(String error) {
		super(error);
	}
}
