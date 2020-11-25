package web.matrix.util.parser;

/**
 * A node with subnodes for a tree, which contains the number structure
 * @author JonBu
 *
 */
public class NumberNode {

	private NumberNode left, right;
	private TokenType connection;
	private double value;

	/**
	 * Creates a new NumberNode with two subnodes
	 * @param left The left node
	 * @param right The right node
	 */
	public NumberNode(NumberNode left, NumberNode right) {
		this.left = left;
		this.right = right;
	}
	/**
	 * Creates a new NumberNode with two subnodes, connection and a value
	 * @param left The left node
	 * @param right The right node
	 * @param connection The connection
	 * @param value The value
	 */
	public NumberNode(NumberNode left, NumberNode right, TokenType connection, double value) {
		this.left = left;
		this.right = right;
		this.value = value;
		this.connection = connection;
	}

	/**
	 * Returns the left node
	 * @return The left node
	 */
	public NumberNode getLeft() {
		return left;
	}

	/**
	 * Returns the right node
	 * @return The right node
	 */
	public NumberNode getRight() {
		return right;
	}

	/**
	 * Returns the value
	 * @return The value
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * Returns the connection
	 * @return The connection
	 */
	public TokenType getConnection() {
		return connection;
	}

	/**
	 * Sets the connection
	 * @param connection The connection
	 */
	public void setConnection(TokenType connection) {
		this.connection = connection;
	}

	/**
	 * Sets the value
	 * @param value The value
	 */
	public void setValue(double value) {
		this.value = value;
	}
	/**
	 * Sets the left node
	 * @param left The left node
	 */
	public void setLeft(NumberNode left) {
		this.left = left;
	}
	/**
	 * Sets the right node
	 * @param right the right node
	 */
	public void setRight(NumberNode right) {
		this.right = right;
	}
}
