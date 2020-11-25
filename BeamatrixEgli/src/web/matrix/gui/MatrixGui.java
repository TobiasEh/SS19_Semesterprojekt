package web.matrix.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import web.matrix.gui.menubar.MenuBar;
import web.matrix.gui.menubar.ToolBar;
import web.matrix.gui.panel.JCalculatePanel;
import web.matrix.gui.panel.JConsolePanel;
import web.matrix.gui.panel.JObjectPanel;
import web.matrix.gui.panel.JTerminalPanel;
import web.matrix.gui.panel.MatrixPanel;
/**
 * Main window.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixGui extends JFrame {
	private MenuBar menu;
	private ToolBar tool;
	
	/**
	 * Creates and initializes main window.
	 */
	public MatrixGui() {
		super("MatrixCalc");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 500));
		this.setLayout(new BorderLayout());
		this.setIconImage(Assets.getIcon("Matrix.png", 32).getImage());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int i = JOptionPane.showConfirmDialog((Component)e.getSource(), 
						"Do you want to proceed? Unsaved changes will be lost.", "Exit", JOptionPane.YES_NO_OPTION);
				if (i == 0) System.exit(0);
			}
		});
		
		JSplitPane topBottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		TopSide top = new TopSide();
		
		topBottomSplit.setTopComponent(top);
		topBottomSplit.setBottomComponent(new JCalculatePanel());
		topBottomSplit.setResizeWeight(0.7);
		
		menu = new MenuBar(this);
		tool = new ToolBar();
		JConsolePanel console = new JConsolePanel();
		
		this.setJMenuBar(menu);
		this.add(tool, BorderLayout.NORTH);
		this.add(console, BorderLayout.SOUTH);
		this.add(topBottomSplit, BorderLayout.CENTER);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.pack();
		this.setVisible(true);
	
	}

	private class TopSide extends JPanel implements PropertyChangeListener {
		
		JSplitPane vertSplit;
		MatrixPanel matrixpanel;
		
		public TopSide() {
			this.setLayout(new BorderLayout());
			vertSplit = new JSplitPane();
			
			matrixpanel = new MatrixPanel();
			
			vertSplit.setLeftComponent(matrixpanel);
			vertSplit.setRightComponent(new RightSide());
			vertSplit.setResizeWeight(0.75);
			vertSplit.setMinimumSize(matrixpanel.getPreferredSize());
			vertSplit.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, this);
			this.add(vertSplit);
		}
		
		private class RightSide extends JPanel {
			 public RightSide() {
				 this.setLayout(new BorderLayout());
				 JSplitPane vertSplit = new JSplitPane();
				 JPanel terminal = new JTerminalPanel("Terminal");
				 JPanel objects = new JObjectPanel();
				 terminal.setBorder(new EmptyBorder(0, 5, 10, 10));
				 objects.setBorder(new EmptyBorder(0, 5, 10, 10));
				 objects.setPreferredSize(new Dimension(100, this.getPreferredSize().height));
				
				 vertSplit.setLeftComponent(terminal);
				 vertSplit.setRightComponent(objects);
				 vertSplit.setResizeWeight(0.9);
				 vertSplit.setDividerLocation(0.33);
				 
				 this.add(vertSplit);
			 }
		}
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			matrixpanel.build();
			
		}
	}
	
	
	
	
	
}
