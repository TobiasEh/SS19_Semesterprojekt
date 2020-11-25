package web.matrix.gui.matrix.render;

import java.awt.geom.Rectangle2D;

import web.matrix.util.parser.NumberFunctions;
import web.matrix.util.parser.TokenType;
/**
 * Tree to draw mathematical expression.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public class Rect2DNode implements NumberFunctions {

	private Rectangle2D rect;
	private TokenType connection;
	private Rect2DNode left, right;	
	private double value;
	private double yCenter;
	private boolean brackets = false;
	
	/**
	 * Node for the draw tree.
	 */
	public Rect2DNode() {
		left = null;
		right = null;
	}	
	
	/**
	 * 
	 * @return Center on y axis.
	 */
	public double getyCenter() {
		return yCenter;
	}

	/**
	 * Sets center on the y axis.
	 * @param yCenter
	 */
	public void setyCenter(double yCenter) {
		this.yCenter = yCenter;
	}

	/**
	 * Returns brackets.
	 * @return 
	 */
	public boolean isBrackets() {
		return brackets;
	}

	/**
	 * Sets brackets.
	 * @param brackets
	 */
	public void setBrackets(boolean brackets) {
		this.brackets = brackets;
	}

	/**
	 * Returns value.
	 * @return
	 */
	public double getValue() {
		return value;
	}

	/**
	 * Sets value.
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * Returns connection.
	 * @return
	 */
	public TokenType getConnection() {
		return connection;
	}

	/**
	 * Sets connection.
	 * @param connection
	 */
	public void setConnection(TokenType connection) {
		this.connection = connection;
	}

	/**
	 * Sets x coordinate.
	 * @param x
	 */
	public void setX(double x) {
		rect = new Rectangle2D.Double(x, rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * Sets y coordinate.
	 * @param y
	 */
	public void setY(double y) {
		rect = new Rectangle2D.Double(rect.getX(), y, rect.getWidth(), rect.getHeight());

	}
	
	/**
	 * Sets width.
	 * @param width
	 */
	public void setWidth(double width) {
		rect = new Rectangle2D.Double(rect.getX(), rect.getY(), width, rect.getHeight());
	}
	
	/**
	 * Sets height.
	 * @param height
	 */
	public void setHeight(double height) {
		rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), height);
	}
	
	/**
	 * Returns the box around the expression.
	 * @return
	 */
	public Rectangle2D getRect() {
		return rect;
	}

	/**
	 * Sets the box around the expression.
	 * @param rect
	 */
	public void setRect(Rectangle2D rect) {
		this.rect = rect;
	}

	/**
	 * Returns left node.
	 * @return
	 */
	public Rect2DNode getLeft() {
		return left;
	}

	/**
	 * Sets left node.
	 * @param left
	 */
	public void setLeft(Rect2DNode left) {
		this.left = left;
	}

	/**
	 * Returns right node.
	 * @return
	 */
	public Rect2DNode getRight() {
		return right;
	}

	/**
	 * Sets right node.
	 * @param right
	 */
	public void setRight(Rect2DNode right) {
		this.right = right;
	}
}
