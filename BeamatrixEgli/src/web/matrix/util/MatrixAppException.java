package web.matrix.util;

/**
 * A Exception that can show a message in the console
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixAppException extends IllegalArgumentException {

	/**
	 * Creates a new error 
	 * @param message The error message
	 */
	public MatrixAppException(String message) {
		super(message);
	}
	
	/**
	 * Shows the message in the console
	 */
	public void showMessageInConsole() {
		ConsoleMessageHandler.instance().showMessage(super.getMessage());
	}
}