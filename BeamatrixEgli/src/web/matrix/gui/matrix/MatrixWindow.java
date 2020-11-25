package web.matrix.gui.matrix;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import web.matrix.gui.panel.MatrixPanel;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberParserException;

@SuppressWarnings("serial")
public class MatrixWindow extends JPanel implements PropertyChangeListener {
	private Display display;
	private String name = "";
	private Matrix m;
	private boolean isEditable = true;
	
	public MatrixWindow(Matrix m) {
		super();
		this.isEditable = true;
		init(m);
		ObjectContainer.instance().addPropertyChangeListener(this);
		display = new MatrixDisplay(this, m.getWidth(), m.getHeigth());
		initDisplay(display);
		
		this.add((Component) display, BorderLayout.CENTER);
		this.add(new MatrixWindowToolBar(m, this), BorderLayout.EAST);
		
		setVisible(true);
	}
	
	public MatrixWindow(Matrix m, boolean isEditable) {
		super();
		this.isEditable = isEditable;
		init(m);
		
		if (isEditable) {
			display = new MatrixDisplay(this, m.getWidth(), m.getHeigth());
			initDisplay(display);
			
			this.add(new MatrixWindowToolBar(m, this), BorderLayout.EAST);
			this.add((Component) display, BorderLayout.CENTER);
		} else {
			display = new MatrixDisplay(this, m.getWidth(), m.getHeigth());
			initDisplay(display);
			this.add((Component) display, BorderLayout.CENTER);
		}
		setVisible(true);
	}
	
	public void setMatrix(Matrix m) {
		this.m = m;
		m.addPropertyChangeListener(this);
		initDisplay(display);
		updateUI();
	}
	
	private void init(Matrix m) { 
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.m = m;
		
		m.addPropertyChangeListener(this);
		setName(m.getName());
	}
	
	private void initDisplay(Display display) {
		int n = 0;
		System.out.println("W:" + m.getWidth());
		System.out.println("H:" + m.getHeigth());
		System.out.println(m);
		for (int i = 0; i < m.getHeigth(); ++i) {
			for (int j = 0; j < m.getWidth(); ++j) {
				Complex c = m.getValue(i, j);
				String s = c.toString();
				display.getCells().get(n).setValue(s);
				++n;
			}
		}
	}
	
	public void setName(String s) {
		this.name = s;
		this.setBorder(BorderFactory.createTitledBorder(name));
	}
	
	public String getName() {
		return this.name;
	}
	
	public Matrix getMatrix() {
		return this.m; 
	}
	
	public boolean getEditable() {
		return this.isEditable;
	}
	
	public void setDisplay(Display d) {
		this.display = d;
		initDisplay(d);
	}
	
	public  Display copyExpressions(Display display, int w, int h, int wOld, int hOld) {
		MatrixDisplay d = new MatrixDisplay(this, w, h);
		ArrayList<ArrayList<Cell>> cao = new ArrayList<ArrayList<Cell>>();
		ArrayList<ArrayList<Cell>> can = new ArrayList<ArrayList<Cell>>();
		int cnt = 0;
		
		for (Cell c : d.getCells()) {
			c.setValue("0");
		}

		for (int i = 0; i < h; i++) {
			can.add(new ArrayList<Cell>());
			for (int j = 0; j < w; ++j) {
				can.get(i).add(d.getCells().get(cnt));
				++cnt;
			}
		}
		
		cnt = 0;
		for (int i = 0; i < hOld; i++) {
			cao.add(new ArrayList<Cell>());
			for (int j = 0; j < wOld; ++j) {
				cao.get(i).add(display.getCells().get(cnt));
				++cnt;
			}
		}
		
		int wb = w > wOld ? wOld : w;
		int wh = h > hOld ? hOld : h;

		for (int i = 0; i < wh; ++i) {
			for (int j = 0; j < wb; ++j) {
				can.get(i).get(j).setValue(cao.get(i).get(j).getValue());
			}
		}

		return d;
	}

	public void setValue(Complex z, int x, int y) {
		m.setValue(x, y, z);
	}
	
	public void rebuild(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("modify")) {
			setMatrix((Matrix) evt.getNewValue()); 
			return;
		}

		this.remove((Component)display);
		if (evt.getPropertyName().equals("matrixsize")) {
			int[] nVal = (int[])evt.getNewValue();
			int[] oVal = (int[])evt.getOldValue();
			int w = nVal[0];
			int h = nVal[1];
			int wo = oVal[0];
			int ho = oVal[1];
			display = copyExpressions(display, w, h, wo, ho);
		} else if (evt.getPropertyName().equals("matrixheight")) {
			display = copyExpressions(display,m.getWidth(), m.getHeigth(),m.getWidth(),(int) evt.getOldValue());
		} else if(evt.getPropertyName().equals("matrixwidth")) {
			display = copyExpressions(display,m.getWidth(), m.getHeigth(),(int) evt.getOldValue(), m.getHeigth());
		}
		removeAll();
		this.add(new MatrixWindowToolBar(m, this), BorderLayout.EAST);
		add((Component) display, BorderLayout.CENTER);
		for (int i = 0; i < display.getCells().size(); i++) {
			display.getCells().get(i).setValue(display.getCells().get(i).getValue());
		}
		repaint();
	}
	
	private void setValue(PropertyChangeEvent evt) {
		Object[] o = (Object[])evt.getNewValue();
		int cellNumber = (int)o[0] * m.getWidth() + (int)o[1];
		Complex z = (Complex)o[2];
		try {
			if (!MatrixPanel.parseExpressiontoComplex(this.display.getCells().get(cellNumber).getValue()).equals(z))
				this.display.getCells().get(cellNumber).setValue(z.toString());
			else 
				this.display.getCells().get(cellNumber).setValue(this.display.getCells().get(cellNumber).getValue());
		} catch (NumberParserException e) {
			ConsoleMessageHandler.instance().showMessage(e.getMessage());
		}
		repaint();
	}
	
	public Display getDisplay() {
		return display;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource().equals(m)) {
			if (evt.getPropertyName().equals("matrixsize")) {
				rebuild(evt);
			}
			if (evt.getPropertyName().equals("modify")) {
				rebuild(evt);
			}
			if (evt.getPropertyName().equals("matrixname")) {
				this.setName((String)evt.getNewValue());
			}
			if (evt.getPropertyName().equals("matrixvalue")) {
				setValue(evt);
			}
			if (evt.getPropertyName().equals("matrixheight")) {
				rebuild(evt);
			}
			if (evt.getPropertyName().equals("matrixwidth") ) {
				rebuild(evt);
			}
		}
	}
}
