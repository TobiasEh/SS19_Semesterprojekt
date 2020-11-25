package web.matrix.gui.matrix;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
/**
 * Matrix display. Contains cells.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixDisplay extends JPanel implements Display {
	private final int HGAP = 1;
	private final int VGAP = 1;
	private ArrayList<Cell> cells;
	private MatrixWindow matrix;
	/**
	 * 
	 * @param matrix Hold a parent reference.
	 * @param width
	 * @param height
	 */
	public MatrixDisplay(MatrixWindow matrix, int width, int height) {
		super();
		this.setLayout(new GridLayout(height, width, HGAP, VGAP)); 
		this.cells = new ArrayList<Cell>();
		this.matrix = matrix;
		init(width, height);
	}
	
	private void init(int width, int height) {
		int cellCount = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (matrix.getEditable())
					cells.add(new EditorCell(matrix, i, j));
				else
					cells.add(new DisplayCell(matrix, i, j));
				this.add((Component) cells.get(cellCount));
				cellCount++;
			}
		}
	}
	
	/**
	 * Returns cells held.
	 */
	public ArrayList<Cell> getCells() {
		return this.cells;
	}
}
