package web.matrix.gui.matrix.render;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import web.matrix.util.parser.NumberFunctions;
import web.matrix.util.parser.NumberNode;
import web.matrix.util.parser.TokenType;
/**
 * Class for drawing mathematical.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public class DrawTerm implements NumberFunctions {
	private Graphics g;
	
	private final double rootGap = 5, bracketGap = 10, sepGap = 5, fracHorizontalGap = 8, bracketWidth = 5, fracVerticalGap = 3;
	
	/**
	 * Draws a term
	 * @param g
	 */
	public DrawTerm(Graphics g) {
		this.g = g;
		this.g.setFont(new Font(Font.SERIF, Font.PLAIN, 14));

	}
	
	/**
	 * Draws a rectangle tree.
	 * @param tree A parsed mathematical expression's tree node.
	 * @param toCenter Cimensions of the center.
	 * @return
	 */
	public Rectangle2D drawTree(NumberNode tree, Dimension toCenter) {
		Rect2DNode node = buildTree(tree);
		node.setX((toCenter.getWidth() - node.getRect().getWidth()) / 2);
		node.setY((toCenter.getHeight() - node.getRect().getHeight()) / 2);
		setPosition(node);
		drawTree(node);
		return node.getRect();
	}
	
	/**
	 * Return the mathematical expression's Dimensions as Rectangle2D.
	 * @param tree Root node of mathematical expression.
	 * @return Rectangle2D that contains width and height of the mathematical expression.
	 */
	public Rectangle2D getSize(NumberNode tree) {
		return buildTree(tree).getRect();
	}
	
	private void drawTree(Rect2DNode tree) {
		if(tree == null || tree.getConnection() == null) return;
			TokenType con = tree.getConnection();
			double x = tree.getRect().getX();
			double y = tree.getRect().getY();
			//NUMBERS
			if(tree.getConnection() == TokenType.NUMBER) {
				drawString(tree.getValue(), x,  y);
			//MINUSBRACKET
			} else if(con == TokenType.MINUSBRACKET) {
				drawFunction("-", tree);
				
			//CONSTANTS
			} else if(NumberFunctions.convertType(con) == TokenType.CONSTANT) {
				drawString(con.name().toLowerCase(), x, y);
				
			//FUNCTIONS
			} else if(NumberFunctions.convertType(con) == TokenType.FUNCTION) {
				drawFunction(con.name().toLowerCase(), tree);

			} else if(con == TokenType.PLUS | con == TokenType.MINUS | con == TokenType.MULT | con == TokenType.DIV) {
				if(con == TokenType.PLUS) {
					drawOperation("+", tree);
	
				} else if(con == TokenType.MINUS) {
					drawOperation("-", tree);
					
				} else if(con == TokenType.MULT) {
					drawOperation("*", tree);

				} else if(con == TokenType.DIV) {
					drawFracBar(tree);
				}
			} else if(con == TokenType.ROOT) {
				drawRoot(tree);
			}


		drawBrackets(tree);

		drawTree(tree.getLeft());
		drawTree(tree.getRight());
		
	}
	
	private void drawFunction(String function, Rect2DNode tree) {
		double x = tree.getRect().getX();
		double y = tree.getRect().getY();
		Rectangle2D functionSize = getStringSize(function);
		double functionWidth = functionSize.getWidth();
		double functionHeight = functionSize.getHeight();
		drawString(function, x, y + center(tree.getRect().getHeight(), functionHeight));
		drawLeftBracket(x + functionWidth + bracketGap / 2 - bracketWidth, y, tree.getRect().getHeight());
		drawRightBracket(x + functionWidth + bracketGap * 1.5 + tree.getLeft().getRect().getWidth(), y, tree.getRect().getHeight());
	}
	
	private void drawFracBar(Rect2DNode tree) {
		Rectangle2D rect = tree.getRect();
		double x = rect.getX();
		double y = rect.getY();
		Rectangle2D left = tree.getLeft().getRect();
		
		g.drawLine((int) x, (int) (y + left.getHeight() + fracVerticalGap), (int) (x + rect.getWidth()), (int) (y + left.getHeight() + fracVerticalGap));

	}
	private void drawRoot(Rect2DNode tree) {
		Rectangle2D exponent = tree.getRight().getRect();
		double x = tree.getRect().getX();
		double y = tree.getRect().getY();

		Path2D path = new Path2D.Double();
		path.moveTo(x, y + tree.getRect().getHeight() - rootGap);
		path.lineTo(x + exponent.getWidth(), y + tree.getRect().getHeight() - rootGap);
		path.lineTo(x + exponent.getWidth() + 0.75 * rootGap, y + tree.getRect().getHeight());
		path.lineTo(x + exponent.getWidth() + 2 * rootGap, y + rootGap);
		path.lineTo(x + tree.getRect().getWidth(), y + rootGap);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.draw(path);
	}

	
	private void drawLeftBracket(double x, double y, double height) {
		Path2D path = new Path2D.Double();
		path.moveTo(x + bracketWidth, y);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		path.curveTo(x + bracketWidth, y, x, y + height / 2, x + bracketWidth, y + height);
		g2.draw(path);

	}
	private void drawRightBracket(double x, double y, double height) {
		Path2D path = new Path2D.Double();
		path.moveTo(x, y);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		path.curveTo(x, y, x + bracketWidth, y + height / 2, x, y + height);
		g2.draw(path);

	}
	
	private void drawBrackets(Rect2DNode rect) {
		if(!rect.isBrackets()) return;
		double x = rect.getRect().getX() - bracketWidth - bracketGap/2;
		double y = rect.getRect().getY();
		drawLeftBracket(x, y, rect.getRect().getHeight());
		drawRightBracket(x + rect.getRect().getWidth() + 1.5 * bracketGap, y, rect.getRect().getHeight());

	}

	private void drawOperation(String op, Rect2DNode tree) {
		Rect2DNode left = tree.getLeft();
		double x = tree.getRect().getX();
		double h =  getStringSize(op).getHeight();
		double newX = x + left.getRect().getWidth() + sepGap;
		if(left.isBrackets())
			newX += 2 * bracketGap;
		
		drawString(op, newX, tree.getRect().getY() + tree.getyCenter() - h/2);
	}
	
	private void drawString(String s, double x, double y) {
		int h = (int) getStringSize(s).getHeight();
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.drawString(s, (int) x, (int) y + h);
	}
	
	private void drawString(double d, double x, double y) {
		DecimalFormat df = new DecimalFormat("#.##########");
		String term = df.format(d);
		term = term.replace(",", ".");
		
		drawString(term, x, y);
	}
	
	private Rectangle2D getStringSize(String s) {
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(s, g);
		return new Rectangle2D.Double(0, 0, bounds.getWidth(), bounds.getHeight() - 7);
	}
	
	private Rectangle2D getStringSize(double d) {
		DecimalFormat df = new DecimalFormat("#.##########");
		String term = df.format(d);
		term = term.replace(",", ".");
		
		return getStringSize(term);
	}
	
	private void drawRect(Rectangle2D rect) {
		int x = (int) rect.getX();
		int y = (int) rect.getY();
		int w = (int) rect.getWidth();
		int h = (int) rect.getHeight();

		g.drawLine(x, y, x + w, y);
		g.drawLine(x + w, y, x + w, y + h);
		g.drawLine(x, y, x, y + h);
		g.drawLine(x, y + h, x + w, y + h);


	}
	
	private double center(double toCenterLength, double length) {
		return Math.abs(toCenterLength - length) / 2;
	}
	
	
	private void setPosition(Rect2DNode node) {
		if(node.getLeft() == null & node.getRight() == null) return;
		
			TokenType con = node.getConnection();
	
			Rect2DNode left = node.getLeft();
			Rectangle2D rectangle = node.getRect();
	
			double leftWidth = left.getRect().getWidth();
			double leftHeight = left.getRect().getHeight();
		
			//FUNCTIONS
			if(NumberFunctions.convertType(con) == TokenType.FUNCTION) {
				int w = (int) getStringSize(con.name().toLowerCase()).getWidth();
				left.setX(rectangle.getX() + w + bracketGap);
				left.setY(node.getRect().getY());
				setPosition(node.getLeft());
				return;
			}
			//MINUSBRACKET
			if(con == TokenType.MINUSBRACKET) {
				int w = (int) getStringSize("-").getWidth();
				left.setX(rectangle.getX() + w + bracketGap);
				left.setY(node.getRect().getY());
				setPosition(node.getLeft());
				return;
			}
			Rect2DNode right = node.getRight();
			double rightWidth = right.getRect().getWidth();
			double rightHeight = right.getRect().getHeight();	
			//PLUS MINUS MULT
			 if(con == TokenType.PLUS | con == TokenType.MINUS | con == TokenType.MULT) {
				
				double separator = 0;
				if(con == TokenType.PLUS)
					separator = getStringSize("+").getWidth();
				
				else if(con == TokenType.MINUS)
					separator = getStringSize("-").getWidth();
	
				else if(con == TokenType.MULT) {
					separator = getStringSize("*").getWidth();					
				}
				double leftx = rectangle.getX();
				double rightx = rectangle.getX() + sepGap + separator + sepGap + leftWidth;
				
				if(left.isBrackets()) {
					leftx += bracketGap;
					rightx += 2 * bracketGap;
				}
				if(right.isBrackets()) {
					rightx += bracketGap;
				}
				left.setX(leftx);
				right.setX(rightx);
				
				if(left.getyCenter() < right.getyCenter()) {
					left.setY(node.getRect().getY() + node.getyCenter() - left.getyCenter());
					right.setY(node.getRect().getY());
				} else {
					left.setY(node.getRect().getY());
					right.setY(node.getRect().getY() + node.getyCenter() - right.getyCenter());
				}
				

			//DIV
			} else if(con == TokenType.DIV) {
				left.setX(rectangle.getX() +  center(leftWidth, rectangle.getWidth()));
				left.setY(rectangle.getY());
				right.setX(rectangle.getX() + center(rightWidth, rectangle.getWidth()));
				right.setY(rectangle.getY() + leftHeight + 2 * fracVerticalGap);
							
			//ROOT
			} else if(con == TokenType.ROOT) {
				left.setX(rectangle.getX() + rightWidth + 2 * rootGap);
				left.setY(rectangle.getY() + rectangle.getHeight() - leftHeight);
				right.setX(rectangle.getX());
				right.setY(rectangle.getY() + leftHeight - rightHeight);
				
				
						
			//POW
			} else if(con == TokenType.POW) {
				if(left.isBrackets()) {
					left.setX(rectangle.getX() + bracketGap);
					right.setX(rectangle.getX() + leftWidth + 2 * bracketGap);
				}else {
					left.setX(rectangle.getX());
					right.setX(rectangle.getX() + leftWidth);
				}
				left.setY(rectangle.getY() + rightHeight);
				right.setY(rectangle.getY());
			}
			setPosition(node.getLeft());
			setPosition(node.getRight());
	}
	
	
	private Rect2DNode buildTree(NumberNode node) {
		if(node == null) return null;
		TokenType con = node.getConnection();
		Rect2DNode rect = new Rect2DNode();

		//NUMBER
		if(con == TokenType.NUMBER) {
			Rectangle2D bounds = getStringSize(node.getValue());
			rect.setRect(bounds);
			rect.setX(0);
			rect.setY(0);
			rect.setyCenter(bounds.getHeight()/2);
			rect.setConnection(TokenType.NUMBER);
			rect.setValue(node.getValue());
			return rect;
		}

		if(NumberFunctions.convertType(con) == TokenType.CONSTANT) {
			Rectangle2D bounds = getStringSize(con.name().toLowerCase());
			rect.setRect(bounds);
			rect.setX(0);
			rect.setY(0);
			rect.setyCenter(bounds.getHeight()/2);
			rect.setConnection(con);
			rect.setValue(node.getValue());
			return rect;
		}

		Rect2DNode left = buildTree(node.getLeft());
		double leftWidth = left.getRect().getWidth();
		double leftHeight = left.getRect().getHeight();
		rect.setLeft(left);
		rect.setConnection(con);
		left.setConnection(node.getLeft().getConnection());
		left.setValue(node.getLeft().getValue());
		//FUNCTIONS
		if(NumberFunctions.convertType(con) == TokenType.FUNCTION) {
			Rectangle2D bounds = getStringSize(con.name().toLowerCase());
			rect.setRect(new Rectangle2D.Double(0, 0, bounds.getWidth() + bracketGap + leftWidth + bracketGap, leftHeight));
			rect.setyCenter(left.getRect().getHeight()/2);
			setTermBrackets(node.getLeft().getConnection(), left);
			return rect;
		}
		//MINUSBRACKET
		if(con == TokenType.MINUSBRACKET) {
			Rectangle2D bounds = getStringSize("-");
			rect.setRect(new Rectangle2D.Double(0, 0, bounds.getWidth() + bracketGap + leftWidth + bracketGap, leftHeight));
			rect.setyCenter(left.getRect().getHeight()/2);
			return rect;
		}
		
		Rect2DNode right = buildTree(node.getRight());
		double rightWidth = right.getRect().getWidth();
		double rightHeight = right.getRect().getHeight();
		rect.setRight(right);
		right.setValue(node.getRight().getValue());
		right.setConnection(node.getRight().getConnection());

		//PLUS MINUS MULT
		if(con == TokenType.PLUS || con == TokenType.MINUS || con == TokenType.MULT) {

			double height = Double.max(left.getRect().getHeight(), right.getRect().getHeight());
			double separator = 0;
			if(con == TokenType.PLUS)
				separator = getStringSize("+").getWidth();
			
			else if(con == TokenType.MINUS) {
				separator = getStringSize("-").getWidth();
				setTermBrackets(node.getLeft().getConnection(), left);
				setTermBrackets(node.getRight().getConnection(), right);

				
			} else if(con == TokenType.MULT) {
				separator = getStringSize("*").getWidth();
				setTermBrackets(node.getLeft().getConnection(), left);
				setTermBrackets(node.getRight().getConnection(), right);

			}
			double width = leftWidth + sepGap + separator + sepGap + rightWidth;
			if(left.isBrackets())
				width += 2 * bracketGap;
			
			if(right.isBrackets())
				width += 2 * bracketGap;
			
			rect.setyCenter(Math.max(left.getyCenter(), right.getyCenter()));


				height = Math.max(left.getyCenter(), right.getyCenter()) 
				+ Math.max(left.getRect().getHeight() - left.getyCenter(), right.getRect().getHeight() - right.getyCenter());
			
			rect.setRect(new Rectangle2D.Double(0, 0, width, height));
			return rect;
			
		//DIV
		} else if(con == TokenType.DIV) {
			double width = Double.max(leftWidth, rightWidth);
			rect.setRect(new Rectangle2D.Double(0, 0, width + 2 * fracHorizontalGap, leftHeight + rightHeight + 2* fracVerticalGap));
			rect.setyCenter(leftHeight + fracVerticalGap);
			return rect;
			
		//ROOT
		} else if(con == TokenType.ROOT) {
			rect.setRect(new Rectangle2D.Double(0, 0, leftWidth + 2 * rootGap + rightWidth, leftHeight + rightHeight - rootGap/2));
			rect.setyCenter(rect.getRect().getHeight() - rightHeight / 2);
			return rect;
		
		//POW
		} else if(con == TokenType.POW) {
			TokenType conPow = node.getLeft().getConnection();
			if(conPow == TokenType.DIV || conPow == TokenType.MULT || conPow == TokenType.MINUS || conPow == TokenType.PLUS || conPow == TokenType.ROOT) {
				left.setBrackets(true);
			}
			double width = leftWidth + rightWidth;

			if(left.isBrackets())
				width += 2 * bracketGap;
			
			rect.setyCenter(rightHeight + leftHeight / 2);
			rect.setRect(new Rectangle2D.Double(0, 0, width, leftHeight + rightHeight));

			return rect;
		}
		return null;
	}
	
	private void setTermBrackets(TokenType con, Rect2DNode rect) {
		if(con == TokenType.PLUS || con == TokenType.MINUS)
			rect.setBrackets(true);
	}
}
