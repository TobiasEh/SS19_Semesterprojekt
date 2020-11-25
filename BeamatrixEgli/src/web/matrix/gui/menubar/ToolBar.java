package web.matrix.gui.menubar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import web.matrix.gui.Assets;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;

@SuppressWarnings("serial")
public class ToolBar extends JToolBar implements ActionListener {
	
	public ToolBar() {
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, 36));
		this.setFloatable(false);
		
		JButton save = addButton("Save.png", "Save current file", "save", Assets.MEDICON);
		JButton load = addButton("Load.png", "Load a file", "load", Assets.MEDICON);
		
		this.addSeparator();
		
		JButton manual = addButton("Manual.png", "Everything you need to know", "manual", Assets.MEDICON);
		JButton about = addButton("About.png", "About this program", "about", Assets.MEDICON);
		
		this.addSeparator();
		
		JButton addMatrix = addButton("AddMatrix.png", "Adds an editable matrix", "addMatrix", Assets.MEDICON);
		JButton addVector = addButton("AddVector.png", "Adds an editable vector", "addVector", Assets.MEDICON);
	}
	
	private JButton addButton(String filename, String tooltiptext, String actionCommand, int size) {
		JButton button = new JButton(Assets.getIcon(filename, size));
		button.setFocusable(false);
		button.addActionListener(this);
		button.setToolTipText(tooltiptext);
		button.setActionCommand(actionCommand);
		this.add(button);
		return button;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addMatrix")) {
			ObjectContainer.instance().addObject(new Matrix(2, 2));
		}
		if (e.getActionCommand().equals("addVector")) {
			ObjectContainer.instance().addObject(new Matrix(2, 1));
		}
		if(e.getActionCommand().equals("manual")) {
			MenuFunctions.manual(null);
		}
		if (e.getActionCommand().equals("save")) {
			MenuFunctions.save(this);
		}
		if (e.getActionCommand().equals("load")) {
			MenuFunctions.load(this);
		}
		if (e.getActionCommand().equals("about")) {
			MenuFunctions.about(this);
		}
	}
}
