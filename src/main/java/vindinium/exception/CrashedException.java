package vindinium.exception;

/**
 * An exception that can be used when a Bot takes to long to make
 * a move and crashes.
 */
public class CrashedException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor sets error message for us
	 */
	public CrashedException() {
		super("Oh no! You took too long to make a move. You've crashed for this game. You can still win though!");
	}
}
