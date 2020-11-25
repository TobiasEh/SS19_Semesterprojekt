package web.matrix.gui.matrix;

import java.awt.Dimension;

import web.matrix.gui.matrix.render.Render;
/**
 * Cell interface. Enables addition of custom cells easily.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public interface Cell {
	/**
	 * Sets the cell's render size.
	 * @param width Width to be set.
	 * @param height Height to be set.
	 */
	public void setRenderSize(int width, int height);
	/**
	 * Sets the cell's value.
	 * @param s String containing the value.
	 */
	public void setValue(String s);
	/**
	 * Returns the cell's 
	 * @return String that holds the new value.
	 */
	public String getValue();
	/**
	 * Returns the cell's render size.
	 * @return Cell's width and height as dimension.
	 */
	public Dimension getRenderSize();
	/**
	 * Preferred size has to be overwritten to return render size.
	 * @return Cell's preferred size.
	 */
	public Dimension getPreferredSize();
	/**
	 * Returns this cell's render component.
	 * @return
	 */
	public Render getRender();
}
