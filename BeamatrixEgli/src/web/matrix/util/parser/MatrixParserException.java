package web.matrix.util.parser;


@SuppressWarnings("serial")
/**
 * Is used if MatrixParser couldnt parse the string
 * @author JonBu
 *
 */
public class MatrixParserException extends Exception {

	/**
	 * Is used if MatrixParser couldnt parse the string
	 * @param message The error message
	 */
	public MatrixParserException(String message) {
		super(message);
	}
}
