package web.matrix.util.matrices;

import web.matrix.util.MatrixAppException;
/**
 * Throws if to matrices dont have the right dimension
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixDimensionException extends MatrixAppException {
	/**
	 * Creates a new error message
	 * @param message The error message
	 */
	public MatrixDimensionException(String message) {
		super(message);
	}
}