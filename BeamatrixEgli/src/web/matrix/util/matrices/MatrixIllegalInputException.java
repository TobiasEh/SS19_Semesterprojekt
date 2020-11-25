package web.matrix.util.matrices;
/**
 * Throws if the input in a matrix is not legal
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixIllegalInputException extends MatrixException{

	/**
	 * Creates a new error message
	 * @param message The error message
	 */
	public MatrixIllegalInputException(String message) {
		super(message);
	}
}
