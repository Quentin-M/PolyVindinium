package vindinium.exception;

/**
 * An exception that can be used to represent an invalid configuration.
 */
public class InvalidConfigurationException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor to set the error message 
	 * 
	 * @param msg The cause of the exception
	 */
	public InvalidConfigurationException(String msg) {
		super(msg);
	}
}
