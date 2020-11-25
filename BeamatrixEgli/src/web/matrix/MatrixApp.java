package web.matrix;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import web.matrix.gui.MatrixGui;
/**
 * 
 * @author Tobias Eh Jonas Bühler Dominik Witoschek
 * starts the Application
 */
public class MatrixApp {
/**
 * the main of this Application generates the Gui
 * starts it
 * @param args null
 */
	public static void main(String[] args) {
		try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());} 
	    catch (UnsupportedLookAndFeelException e) {}
	    catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {}
		new MatrixGui();
	}

}
