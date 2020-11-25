package web.matrix.gui.matrix;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import web.matrix.gui.matrix.render.Render;
import web.matrix.gui.panel.MatrixPanel;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberParserException;

/**
 * Cell that displays rendered mathematical expressions 
 * and stores their numerical values in the matrices
 * linked.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class EditorCell extends DisplayCell implements Cell, FocusListener {
	private CardLayout layout;
	private Input input;
	private MatrixWindow matrix;
	private int x, y;
	
	private final String RENDER = "render";
	private final String INPUT = "input";
	
	private boolean allowSwitch = true;
	/**
	 * Creates and initializes EditorCell
	 * @param matrix Matrix to be linked.
	 * @param x Cell's position on x axis.
	 * @param y Cell's position on y axis.
	 */
	public EditorCell(MatrixWindow matrix, int x, int y) {
		super(matrix, x, y);
		this.removeAll();
		
		layout = new CardLayout();
		this.setLayout(layout);
		this.addMouseEvents();
		
		this.x = x;
		this.y = y;
		this.matrix = matrix;
		
		render = new Render(this);
		input = new Input(this);
		input.addFocusListener(this);
		
		this.add(render, RENDER);
		this.add(input, INPUT);
		
		layout.show(this, RENDER);
	}
	
	/**
	 * Returns this cell's textfield.
	 * @return Input component.
	 */
	public Input getInput() {
		return this.input;
	}
	
	/**
	 * Allows cell to switch from input to render view.
	 * @param b
	 */
	public void setAllowSwitch(boolean b) {
		this.allowSwitch = b;
	}
	
	private void addMouseEvents() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (allowSwitch) layout.next((Container)e.getSource());
				input.requestFocusInWindow();
				input.selectAll();
			}
		});
	}

	@Override
	public void setValue(String s) {
		this.input.setText(s);
		this.render.setUserInput(s);
	}
	
	@Override
	public String getValue() {
		return this.input.getText();
	}

	@Override
	public void focusLost(FocusEvent e) {
		render.setUserInput(input.getText());
		
		if (allowSwitch) {
			if (matrix.getMatrix().getHeigth() > y && matrix.getMatrix().getWidth() > x) {
				if (!((Input)e.getSource()).getText().isBlank()) {
					Complex c;
					try {
						c = MatrixPanel.parseExpressiontoComplex(((Input)e.getSource()).getText());
						matrix.setValue(c, x, y);
					} catch (NumberParserException e1) {
						allowSwitch = false;
					}
				}
			}
			layout.next(this);
		}
		
		if (input.getText().isBlank()) {
			DecimalFormat df = new DecimalFormat("#.##########");
			String s = df.format(Double.valueOf(matrix.getMatrix().getValue(x, y).toString()));
			setValue(s);
			layout.next(this);
		}
	}

	@Override public void focusGained(FocusEvent e) {}
}
