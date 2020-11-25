package web.matrix.gui.matrix;

import java.util.ArrayList;

/**
 * Display contains cells.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public interface Display {
	/**
	 * 
	 * @return List of all cells that it holds.
	 */
	public ArrayList<Cell> getCells();
}
