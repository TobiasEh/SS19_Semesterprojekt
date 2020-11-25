package web.matrix.util.matrices;

import web.matrix.util.MatrixAppException;

/**
 * Throws if a matrix is not valid
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixException extends MatrixAppException {
	/**
	 * Creates a new error message
	 * @param message The error message
	 */
	public MatrixException(String message) {
		super(message);
	}
}