package web.matrix.gui.linearsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import web.matrix.gui.matrix.MatrixWindow;
import web.matrix.util.matrices.LinearSystem;
import web.matrix.util.matrices.Matrix;

@SuppressWarnings("serial")
/**
 * 
 * @author Tobias Eh Jonas Bühler Dominik Witoschek
 * Linear System Window Shows the linear system and its Solution
 */
public class LinearSystemWindow extends JPanel {
	
	private MatrixWindow matrix;
	private MatrixWindow vector;
	private JPanel panel;
	JScrollPane scrol ;
	
	private boolean solved = false;
	/**
	 * creates a new linearsystemPanel
	 */
	public LinearSystemWindow() {
		this.setLayout(new BorderLayout());
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBackground(Color.WHITE);
		
		scrol = new JScrollPane(panel);
		scrol.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrol.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrol.setBackground(Color.WHITE);
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
        scrol.setBorder(eb);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		this.add(scrol);	}
	/**
	 * adds a new Matrix window for matrix , delets old matrix windows
	 * @param matrix new Matrix window which is going to be added if not null
	 */
	public void setMatrix(MatrixWindow matrix) {
		if (!solved) {
			if (this.matrix != null) {
				panel.remove(this.matrix);
			}
			if(vector != null) {
				panel.remove(vector);
				panel.add(matrix);
				panel.add(vector);
			} else {
				panel.add(matrix);
			}
		} else {
			panel.removeAll();
			panel.add(matrix);
			panel.add(vector);
			solved = false;
		}
		this.matrix = matrix;
		updateUI();
		
	}
	/**
	 * adds a new Matrix window for a vector , delets old vector windows
	 * @param vector new MatrixWindow which is going to be added if not null
	 */
	public void setVector(MatrixWindow vector) {
		if (!solved) {
			if (this.vector != null)
				panel.remove(this.vector);
		} else {
			panel.removeAll();
			panel.add(matrix);
			solved = false;
		}
		this.vector = vector;
		panel.add(vector);
		updateUI();
	}
	/**
	 * returns MatrixWindow of vector
	 * @return returns MatrixWindow of vector
	 */
	public MatrixWindow getVector() {
		return vector;
	}
	/**
	 * returns MatrixWindow of matrix
	 * @return returns MatrixWindow of matrix
	 */
	public MatrixWindow getMatrix() {
		return matrix;
	}
	/**
	 * removes the Matrix Window of matrix
	 */
	public void removeMatrix() {
		if (!solved) {
			if (matrix != null) {
				panel.remove(matrix);
				matrix = null;
			}
		} else {
			panel.removeAll();
			panel.add(vector);
			solved = false;
		}
		updateUI();
	}
	/**
	 * removes the MatrixWindow of matrix
	 */
	public void removeVector() {
		if (!solved) {
			if (vector != null) {
				panel.remove(vector);
				vector = null;
			}
		} else {
			panel.removeAll();
			panel.add(matrix);
			solved = false;
		}
		updateUI();
	}
	/**
	 * prints the Solution of the Linear system if possible
	 */
	public void solve() {
		LinearSystem linear = new LinearSystem(matrix.getMatrix(), vector.getMatrix());
		ArrayList<String> steps = linear.getSteps();
		ArrayList<Matrix> matrices = linear.getInterimsMatrix();
		ArrayList<Matrix> vectores = linear.getInterimsVector();
		for (int i = 0; i < steps.size(); i++) {
			panel.add(new Arrow(linear.getSteps().get(i), this));
			panel.add(new MatrixWindow(matrices.get(i), false));
			panel.add(new MatrixWindow(vectores.get(i), false));
		}
		solved = true;
		panel.updateUI();
		updateUI();
	}
}
