package web.matrix.gui.matrix;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import web.matrix.gui.matrix.render.DrawTerm;
import web.matrix.gui.panel.MatrixPanel;
import web.matrix.util.parser.NumberNode;
import web.matrix.util.parser.NumberParserException;
/**
 * Textfield that holds user input.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class Input extends JTextField implements DocumentListener {
	private EditorCell parent;
	private Highlighter highlighter;
	private DefaultHighlightPainter painter;
	private final int PADDING = 10;
	
	/**
	 * Creates and initializes input textfield.
	 * @param parent Holds a reference to its parent.
	 */
	public Input(EditorCell parent) {
		super();
		this.parent = parent;
		this.getDocument().addDocumentListener(this);
		highlighter = this.getHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		displayParseStatus();
		setRenderSize();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		displayParseStatus();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		displayParseStatus();
	}
	
	/**
	 * Able to set its parent's render size.
	 */
	public void setRenderSize() {
		BufferedImage output = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = output.createGraphics();
		NumberNode tree = null;
		try {
			tree = MatrixPanel.parseExpression(getText());
			DrawTerm dt = new DrawTerm(g2);
			Rectangle2D bounds = dt.getSize(tree);
			parent.setRenderSize((int)bounds.getWidth() + PADDING, (int)bounds.getHeight() + PADDING);
		} catch (NumberParserException e) {}
	}
	
	private void displayParseStatus() {
		if (MatrixPanel.quickParseExpression(this.getText(), this) == true) {
			highlighter.removeAllHighlights();
			this.setToolTipText(null);
			parent.setAllowSwitch(true);
		} else {
			try {
				highlighter.addHighlight(0, getText().length(), painter);
				parent.setAllowSwitch(false);
			} catch (BadLocationException e1) {
				System.err.println(e1.getMessage());
			}
		}
	}

}
