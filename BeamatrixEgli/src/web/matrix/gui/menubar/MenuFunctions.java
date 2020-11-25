package web.matrix.gui.menubar;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import web.matrix.data.LoadSaveException;
import web.matrix.gui.Assets;
import web.matrix.gui.dialog.JManualDialog;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;

public class MenuFunctions {
	private static String filename;
	
	public static void manual(JFrame frame) {
		new JManualDialog(frame, "Manual");
	}
	public static void about(Component menu) {
		JOptionPane.showMessageDialog(menu, "MatrixCalc 18.07.2019 V1.0\n" +
				"Bühler Jonas\nEh Tobias\nWitoschek Dominik\n", 
				"About", JOptionPane.INFORMATION_MESSAGE, Assets.getIcon("matrix.png", Assets.BIGICON));
	}
	public static void save(Component menu, String filname) {
		filename = filname;
		save(menu);
	}
	
	public static void load(Component menu) {
		ObjectContainer objectContainer = ObjectContainer.instance();
		Iterator <Matrix> matrices = objectContainer.iterator(); 
		ArrayList <Matrix> m = new ArrayList<Matrix>();
		int x = 0;
		if (matrices.hasNext()) { 
			x = JOptionPane.showOptionDialog(menu, "Unsaved matrices will be discarded. Continue?", "Warning",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.WARNING_MESSAGE , null, new String [] {"Continue", "Cancel"}, "Continue");
			}
		if (x == 0|| JOptionPane.OK_OPTION == x) {
			int i = 0;
			while (matrices.hasNext()) {
				m.add( matrices.next());
				i++;
			}
			for (int j = 0; j < i; j++) {
				objectContainer.removeObject(m.get(j));
			}
			JFileChooser openChooser = new JFileChooser();
		    int valChooser = openChooser.showOpenDialog(menu);
		    if (valChooser == JFileChooser.APPROVE_OPTION) {
		    	File file = openChooser.getSelectedFile();
		    	filename = file.getPath();
		    	try {
		    		objectContainer.load(filename);
		    	} catch (LoadSaveException e) {
		    		Iterator<Matrix> it = objectContainer.iterator();
		    		ArrayList<Matrix> array = new ArrayList<Matrix>();
		    		while (it.hasNext()) {
		    			Matrix next = it.next();
		    			array.add(next);
		    		}
		    		for (int a = 0; a < array.size(); a++) {
		    			objectContainer.removeObject(array.get(a));
		    		}
		    		for (int j = 0; j < i; j++) {
						objectContainer.addObject(m.get(j));
					}
		    		JOptionPane.showMessageDialog(menu, "Loading failure!", "IO ERROR", JOptionPane.ERROR_MESSAGE);
		    		ConsoleMessageHandler.instance().showMessage(e.getMessage());
		    	}
		   } else {
			   ConsoleMessageHandler.instance().showMessage("No file selected");
			   for (int j = 0; j < i; j++) {
					objectContainer.addObject(m.get(j));
			   }
		   }
		} else {
			ConsoleMessageHandler.instance().showMessage("Loading Canceled");
			JOptionPane.showMessageDialog(menu, "Loading failure!", "IO ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static void save (Component menu) {
		ObjectContainer objectContainer = ObjectContainer.instance();
		Iterator <Matrix> matrices = objectContainer.iterator();
		if (!matrices.hasNext()) {
			ConsoleMessageHandler.instance().showMessage("No data-objects.");
		}else {
			if (filename == null) {
				JFileChooser saveChooser = new JFileChooser();
				int valChooser = saveChooser.showSaveDialog(menu);
				if (valChooser == JFileChooser.APPROVE_OPTION) {
				    File file = saveChooser.getSelectedFile();
				    filename = file.getPath();
				} else {
					ConsoleMessageHandler.instance().showMessage("No file selected");
					JOptionPane.showMessageDialog(menu, "Saving failure! No file selected", "IO ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (filename != null ) {
				try {
					objectContainer.save(filename);
					ConsoleMessageHandler.instance().showMessage("File saved");
				} catch (LoadSaveException e) {
					ConsoleMessageHandler.instance().showMessage(e.getMessage());
					JOptionPane.showMessageDialog(menu, "Saving wasn´t possible!", "IO ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
