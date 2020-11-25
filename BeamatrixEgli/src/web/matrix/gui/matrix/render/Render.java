package web.matrix.gui.matrix.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import web.matrix.gui.matrix.Cell;
import web.matrix.gui.panel.MatrixPanel;
import web.matrix.util.parser.NumberNode;
import web.matrix.util.parser.NumberParserException;
/**
 * Matrix cell render panel. Renders mathematical expressions.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class Render extends JPanel {
	private String userInput = "";
	private Cell parent = null;
	private NumberNode tree;
	
	/**
	 * 
	 * @param parent Requires parent.
	 */
	public Render(Cell parent) {
		super();
		this.parent = parent;
		this.setBackground(Color.WHITE);
	}
	
	/**
	 * Sets user input to be rendered as mathematical expression.
	 * @param input A string that represents a mathematical expression entered by the user.
	 */
	public void setUserInput(String input) {
		this.userInput = input;
		try {
			tree = MatrixPanel.parseExpression(input);
		} catch (NumberParserException e) {
			System.err.println(e.getMessage());
			tree = null;
		}
	}
	
	/**
	 * 
	 * @return Content being rendered.
	 */
	public String getUserInput() {
		return userInput;
	}

	/**
	 * Sets draw tree.
	 * @param tree Root node.
	 */
	public void setTree(NumberNode tree) {
		this.tree = tree;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		DrawTerm dt = new DrawTerm(g);
		if (tree != null) {
			Rectangle2D bounds = dt.drawTree(tree, parent.getRenderSize());
			parent.setRenderSize((int)bounds.getWidth() + 10, (int)bounds.getHeight() + 10);
		}
	}
}
