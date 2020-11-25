package web.matrix.gui.matrix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import web.matrix.gui.Assets;
import web.matrix.gui.dialog.MatrixSizeDialog;
import web.matrix.gui.panel.MatrixPanel;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixDimensionException;
import web.matrix.util.matrices.MatrixIllegalInputException;
import web.matrix.util.matrices.MatrixOp;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberParserException;

@SuppressWarnings("serial")
public class MatrixWindowToolBar extends JToolBar implements ActionListener{

	private Matrix m;
	private MatrixWindow parent;
	private JPopupMenu pop;
	private JButton add, remove, settings, bin;
	ObjectContainer objectcontainer ;
	
	public MatrixWindowToolBar(Matrix m, MatrixWindow parent) {
		super(JToolBar.VERTICAL);
		this.setFloatable(false);
		
		objectcontainer = ObjectContainer.instance();
		this.m = m;
		this.parent = parent;
		
		add = toolBarButton("Add.png", "Increase dimension by one");
		remove = toolBarButton("Remove.png", "Decrease dimension by one");
		settings = toolBarButton("Settings.png", "Additional functions");
		bin = toolBarButton("Bin.png", "Delete this matrix");
		
		pop = new JPopupMenu();
		
		JMenuItem addRow = new JMenuItem("Add Row");
		addRow.addActionListener(this);
		
		JMenuItem remRow = new JMenuItem("Remove Row");
		remRow.addActionListener(this);
		
		JMenuItem addCol = new JMenuItem("Add Column");
		addCol.addActionListener(this);
		
		JMenuItem remCol = new JMenuItem("Remove Column");
		remCol.addActionListener(this);
		
		JMenuItem size = new JMenuItem("Set size");
		size.addActionListener(this);
		
		JMenuItem union = new JMenuItem("Union");
		union.addActionListener(this);
		
		JMenuItem invert = new JMenuItem("Invert");
		invert.addActionListener(this);
		
		JMenuItem random = new JMenuItem("Randomize");
		random.addActionListener(this);
		
		JMenuItem fill = new JMenuItem("Fill");
		fill.addActionListener(this);
		
		JMenuItem negate = new JMenuItem("Negate");
		negate.addActionListener(this);
		
		addRow.setIcon(Assets.getIcon("Add.png", Assets.SMALLICON));
		remRow.setIcon(Assets.getIcon("Remove.png", Assets.SMALLICON));
		addCol.setIcon(Assets.getIcon("Add.png", Assets.SMALLICON));
		remCol.setIcon(Assets.getIcon("Remove.png", Assets.SMALLICON));
		
		union.setIcon(Assets.getIcon("Function.png", Assets.SMALLICON));
		invert.setIcon(Assets.getIcon("Function.png", Assets.SMALLICON));
		random.setIcon(Assets.getIcon("Function.png", Assets.SMALLICON));
		fill.setIcon(Assets.getIcon("Function.png", Assets.SMALLICON));
		negate.setIcon(Assets.getIcon("Function.png", Assets.SMALLICON));
		
		pop.add(addRow);
		pop.add(remRow);
		pop.add(addCol);
		pop.add(remCol);
		pop.add(size);
		pop.addSeparator();
		pop.add(union);
		pop.add(invert);
		pop.add(random);
		pop.add(fill);
		pop.add(negate);
		
		this.add(add);
		this.add(remove);
		this.add(settings);
		this.add(bin);
	}
	
	private JButton toolBarButton(String filename, String tooltip) {
		ImageIcon buttonIcon = Assets.getIcon(filename, Assets.SMALLICON);
		JButton button = new JButton(buttonIcon);
		button.setFocusable(false);
		button.addActionListener(this);
		button.setToolTipText(tooltip);
		return button;
	}
	
	
	private void add() {
		m.setSize(m.getHeigth() + 1, m.getWidth() + 1);
	}
	
	private void minus() {
		try {
			m.setSize(m.getHeigth() - 1, m.getWidth() - 1);
		}catch (MatrixDimensionException e) {
			ConsoleMessageHandler.instance().showMessage("Dimension can´t be lower than that.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(add)) {
			add();
		}
		if (e.getSource().equals(remove)) {
			minus();
		}
		if (e.getSource().equals(settings)) {
			pop.show(settings, settings.getX(), settings.getY());
		}
		if (e.getSource().equals(bin)) {
			ObjectContainer.instance().removeObject(parent.getMatrix());
		}
		if (e.getActionCommand().equals("Add Row")) {
			onAddRow();
		}
		if (e.getActionCommand().equals("Remove Row")) {
			onRemoveRow();
		}
		if (e.getActionCommand().equals("Add Column")) {
			onAddCol();
		}
		if (e.getActionCommand().equals("Remove Column")) {
			onRemoveCol();
		}
		if (e.getActionCommand().equals("Set size")) {
			onSetSize();
		}
		if (e.getActionCommand().equals("Union")) {
			onUnion();
		}
		if (e.getActionCommand().equals("Invert")) {
			onInvert();
		}
		if (e.getActionCommand().equals("Randomize")) {
			onRand();
		}
		if (e.getActionCommand().equals("Fill")) {
			onFill();
		}
		if (e.getActionCommand().equals("Negate")) {
			onNegate();
		}
	}

	private void onNegate() {
		Matrix neu;
		neu = MatrixOp.negate(m);
		neu.setName(m.getName());
		m = neu;
		parent.setMatrix(m);
		objectcontainer.addObject(neu);
	}

	private void onFill() {
		String x = "";
		try {
			x = JOptionPane.showInputDialog(parent.getParent(),"Insert number", "Fill", JOptionPane.QUESTION_MESSAGE);
			Complex c = MatrixPanel.parseExpressiontoComplex(x);
			MatrixOp.flush(m, c);
		} catch (NumberFormatException | NullPointerException e) {
			ConsoleMessageHandler.instance().showMessage("The input: " + x + " isn´t a Number!");
		} catch(NumberParserException e2) {
			ConsoleMessageHandler.instance().showMessage(e2.getMessage());
		}
	}

	private void onRand() {
		String stepwith = "";
		try {
			stepwith = JOptionPane.showInputDialog(parent.getParent(),"Insert increment", "Randomize", JOptionPane.QUESTION_MESSAGE);
			double x = Double.parseDouble(stepwith);
			MatrixOp.randomize(m, x);
		} catch (NumberFormatException | NullPointerException e) {
			ConsoleMessageHandler.instance().showMessage("The input: " + stepwith + " isn´t a Number!");
		}
	}

	private void onInvert() {
		Matrix neu;
		try {
			neu = MatrixOp.invert(m);
			neu.setName(m.getName());
			m = neu;
			parent.setMatrix(m);
			objectcontainer.addObject(neu);
			
		} catch(MatrixDimensionException | MatrixIllegalInputException e) {
			ConsoleMessageHandler.instance().showMessage(e.getMessage());
		}
	}

	private void onUnion() {
		Matrix neu;
		try {
			if(m.isQuadratic()) {
				neu = MatrixOp.identity(m.getHeigth());
				neu.setName(m.getName());
				m = neu;
				parent.setMatrix(m);
				objectcontainer.addObject(neu);
			} else {
				ConsoleMessageHandler.instance().showMessage("The matrix has to be quadratic!");
			}
		} catch(MatrixDimensionException | MatrixIllegalInputException e) {
			ConsoleMessageHandler.instance().showMessage(e.getMessage());
		}
	}

	private void onSetSize() {
		new MatrixSizeDialog(parent, m);
	}

	private void onRemoveCol() {
		try {
			m.setSize(m.getHeigth(), m.getWidth() - 1);
		}catch (MatrixDimensionException e) {
			ConsoleMessageHandler.instance().showMessage("Dimension can´t be lower than that.");
		}
	}

	private void onAddCol() {
		m.setSize(m.getHeigth(), m.getWidth() + 1);

	}

	private void onRemoveRow() {
		try {
			m.setSize(m.getHeigth() - 1, m.getWidth());
		}catch (MatrixDimensionException e) {
			ConsoleMessageHandler.instance().showMessage("Dimension can´t be lower than that.");
		}
	}

	private void onAddRow() {
		m.setSize(m.getHeigth() + 1, m.getWidth());
	}
}
