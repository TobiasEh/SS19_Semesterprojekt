package web.matrix.data;

import web.matrix.util.MatrixAppException;

/**
 * An error when loading or saving fails
 * @author JonBu
 *
 */
@SuppressWarnings("serial")
public class LoadSaveException extends MatrixAppException {

	/**
	 * Creates a new error message
	 * @param message The message
	 */
	public LoadSaveException(String message) {
		super(message);
	}
}
