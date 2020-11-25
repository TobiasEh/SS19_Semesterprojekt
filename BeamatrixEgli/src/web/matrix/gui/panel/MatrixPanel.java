package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import web.matrix.gui.MatrixLayout;
import web.matrix.gui.matrix.MatrixWindow;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberNode;
import web.matrix.util.parser.NumberParser;
import web.matrix.util.parser.NumberParserException;
/**
 * Panel that holds all editable matrices.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixPanel extends JPanel implements PropertyChangeListener {
	private static NumberParser np;
	private ArrayList<MatrixWindow> displayedMatrices;
	private JScrollPane scroll;
	private JPanel display;
	
	/**
	 * Creates and initializes matrix panel.
	 */
	public MatrixPanel() {
		this.setLayout(new BorderLayout());
		np = new NumberParser();
		displayedMatrices = new ArrayList<MatrixWindow>();
		ObjectContainer.instance().addPropertyChangeListener(this);
		
		display = new JPanel();
		display.setLayout(new MatrixLayout(0));
		display.setOpaque(true);
		display.setBackground(Color.WHITE);
		display.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		scroll = new JScrollPane(display);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		
		add(scroll, BorderLayout.CENTER);
	}
	
	/**
	 * Checks if a mathematical expression string is parseable.
	 * @param exp Expression to be parsed.
	 * @param Input Textfield that hold the expression to be parsed.
	 * @return True if expression could be parsed succesfully. False otherwise.
	 */
	public static boolean quickParseExpression(String exp, JTextField Input) {
		try {
			np.parseToComplexNumber(exp);
			return true;
		} catch (NumberParserException e) {
			Input.setToolTipText(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Parses a mathematical string.
	 * @param exp Expression to be parsed.
	 * @return NumberNode that holds the root node of the tree created by parsing the expression.
	 * @throws NumberParserException
	 */
	public static NumberNode parseExpression(String exp) throws NumberParserException {
			return np.parse(exp);
	}
	
	/**
	 * Parses a string to a complex number and returns it.
	 * @param exp Expression to be parsed.
	 * @return Returns the parsed complex number.
	 * @throws NumberParserException
	 */
	public static Complex parseExpressiontoComplex(String exp) throws NumberParserException {
		return np.parseToComplexNumber(exp);
	}
	
	/**
	 * Reloads the Panel.
	 */
	public void build() {
		this.removeAll();
		display = new JPanel();
		display.setLayout(new MatrixLayout(0));
		display.setOpaque(true);
		display.setBackground(Color.WHITE);
		display.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		scroll = new JScrollPane(display);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		for (MatrixWindow window : displayedMatrices) {
			display.add(window);
		}
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		display.updateUI();
		this.add(scroll);
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		MatrixWindow w = null;
		Matrix m = null; 
		if (evt.getPropertyName().equals("matrixadd")) {
			matrixadd(evt); 
		} else if (evt.getPropertyName().equals("matrixremove")) {
			m = (Matrix)evt.getOldValue();
			for (MatrixWindow window : displayedMatrices) {
				if (window.getName().equals(m.getName())) {
					w = window;
					break;
				}
			}
			display.remove(w);
			displayedMatrices.remove(w);
			display.updateUI();
			build();
		} else;
	}

	private void matrixadd(PropertyChangeEvent evt) {
		Matrix m = (Matrix)evt.getNewValue();
		for (int i = 0; i < displayedMatrices.size(); i++) {
			if (m.getName().equals( displayedMatrices.get(i).getMatrix().getName())){
				display.remove(displayedMatrices.get(i));
				displayedMatrices.remove(i);
			}
		}
		MatrixWindow w = new MatrixWindow(m);
		displayedMatrices.add(w);
		display.add(w);
		build();
	}
}
