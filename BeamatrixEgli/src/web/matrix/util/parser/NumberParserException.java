package web.matrix.util.parser;

/**
 * Is used if NumberParser couldnt parse the string
 * @author JonBu
 *
 */
@SuppressWarnings("serial")
public class NumberParserException extends Exception {
	
	/**
	 * Is used if NumberParser couldnt parse the string
	 * @param message The error message
	 */
	public NumberParserException(String message) {
		super(message);
	}
}
