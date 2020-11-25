package web.matrix.util.parser;

import web.matrix.util.MatrixAppException;

/**
 * Throws if the parserString couldnt be parsed
 * @author JonBu
 *
 */
@SuppressWarnings("serial")
public class ParserException extends MatrixAppException {

	private int line;
	/**
	 * Creates a new Exception in a specific line
	 * @param message The error message
	 * @param line The line of the error in the terminal
	 */
	public ParserException(String message, int line) {
		super("Syntaxerror in line " + line + ": " + message);
		this.line = line;
	}
	
	/**
	 * Creates a new line without a speified line
	 * @param message The error message
	 */
	public ParserException(String message) {
		super("Syntaxerror: " + message);
		this.line = -1;
	}
	/**
	 * Returns the line number of the error
	 * @return The line number of the error
	 */
	public int getLine() {
		return line;
	}
}
