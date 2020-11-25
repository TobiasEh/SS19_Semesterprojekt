package web.matrix.gui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import web.matrix.gui.Assets;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar implements ActionListener {
	private static String filename = null;
	
	public MenuBar(JFrame parent) {
		 
		 JMenu fileMenu = new JMenu("File");
		 JMenu helpMenu = new JMenu("Help");
		 
		 JMenuItem save = new JMenuItem("Save");
		 save.setIcon(Assets.getIcon("Save.png", Assets.MENUICON));
		 save.addActionListener(this);
		 
		 JMenuItem saveas = new JMenuItem("Save as...");
		 saveas.setIcon(Assets.getIcon("Save.png", Assets.MENUICON));
		 saveas.addActionListener(this);
		 
		 JMenuItem load = new JMenuItem("Load");
		 load.setIcon(Assets.getIcon("Load.png", Assets.MENUICON));
		 load.addActionListener(this);
		 
		 
		 JMenuItem exit = new JMenuItem("Exit");
		 exit.setIcon(Assets.getIcon("Exit.png", Assets.MENUICON));
		 exit.addActionListener(this);
		 
		 JMenuItem about = new JMenuItem("About");
		 about.setIcon(Assets.getIcon("About.png", Assets.MENUICON));
		 about.addActionListener(this);
		 
		 JMenuItem manual = new JMenuItem("Manual");
		 manual.setIcon(Assets.getIcon("Manual.png", Assets.MENUICON));
		 manual.addActionListener(this);
		
		 fileMenu.add(save);
		 fileMenu.add(saveas);
		 fileMenu.add(load);
		 fileMenu.addSeparator();
		 fileMenu.add(exit);
		 
		 helpMenu.add(about);
		 helpMenu.add(manual);
		 
		 add(fileMenu);
		 add(helpMenu);
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contentEquals("Save")) {
			onSave();
		} else if (e.getActionCommand().equals("Manual")) {
			MenuFunctions.manual(null);
		} else if (e.getActionCommand().equals("Save as...")) {
			onSaveas();
		} else if (e.getActionCommand().equals("Load")) {
			onLoad();
		} else if (e.getActionCommand().equals("Exit")) {
			int i = JOptionPane.showConfirmDialog(this.getParent(), "Do you want to proceed? Unsaved changes will be lost.", "Exit", JOptionPane.YES_NO_OPTION);
			if (i == 0) System.exit(0);
		} else if (e.getActionCommand().equals("About")) {
			onAbout();
		}
	}
	
	private void onLoad() {
		MenuFunctions.load(this);
	}

	private void onSaveas() {
		filename = null;
		onSave();
	}

	private void onSave() {
		MenuFunctions.save(this, filename);
	}
	
	private void onAbout() {
		MenuFunctions.about(this);
	}
	
	public static String getFilename() {
		return filename;
	}
}
