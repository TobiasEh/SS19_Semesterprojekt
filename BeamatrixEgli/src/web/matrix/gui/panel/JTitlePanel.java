package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
/**
 * A JPanel that can hold multiple tabs.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class JTitlePanel extends JPanel implements ActionListener {

	private JPanel panel;
	private JPanel contentContainer;
	private JPanel toolbarContainer;
	
	private CardLayout contentLayout;
	private CardLayout toolbarLayout;
	
	private ArrayList<JPanel> contentPanels;
	private ArrayList<JButton> tabs;
	private ArrayList<String> titles;
	private ArrayList<JPanel> toolbars;
	
	/**
	 * 
	 * @param titles Titles of all the tabs that are going to be held.
	 */
	public JTitlePanel(String[] titles) {
		setLayout(new BorderLayout());
		contentLayout = new CardLayout();
		toolbarLayout = new CardLayout();
		
		this.titles = new ArrayList<String>(Arrays.asList(titles));
		this.contentPanels = new ArrayList<JPanel>();
		this.toolbars = new ArrayList<JPanel>();
		this.tabs = new ArrayList<JButton>();
		this.panel = new JPanel(new BorderLayout());
		
		contentContainer = new JPanel();
		contentContainer.setLayout(contentLayout);
		toolbarContainer = new JPanel();
		toolbarContainer.setLayout(toolbarLayout);
		
		JPanel bar = new JPanel(new BorderLayout());
		JPanel tabbar = new JPanel(new FlowLayout(0, 0, 0));
		
		for (int i = 0; i < titles.length; ++i) {
			contentPanels.add(new JPanel(new BorderLayout()));
			tabs.add(addTab(tabbar, this.titles.get(i)));
			toolbars.add(new JPanel(new FlowLayout(0)));
			tabbar.add(tabs.get(i));
			contentContainer.add(contentPanels.get(i), this.titles.get(i));
			toolbarContainer.add(toolbars.get(i), this.titles.get(i));
		}
		
		tabbar.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
		
		bar.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		bar.add(tabbar, BorderLayout.WEST);
		bar.add(toolbarContainer, BorderLayout.EAST);
		
		contentLayout.show(contentContainer, this.titles.get(0));
		toolbarLayout.show(toolbarContainer, this.titles.get(0));
		panel.add(bar, BorderLayout.NORTH);
		panel.add(contentContainer, BorderLayout.CENTER);
		this.add(panel, BorderLayout.CENTER);
	}
	
	private JButton addTab(JPanel tabbar, String title) {
		JButton btn = new JButton(title);
		btn.setOpaque(true);
		btn.setBorderPainted(false);
		btn.setBackground(Color.WHITE);
		btn.addActionListener(this);
		tabbar.add(btn);
		return btn;
	}
	
	/**
	 * 
	 * @param component Component to be added to a tab.
	 * @param tab Name of the tab that is going to hold the component.
	 */
	public void addPanel(JComponent component, String tab) {
		contentPanels.get(titles.indexOf(tab)).add(component);
	}
	
	/**
	 * 
	 * @param component Component to be added to the toolbar.
	 * @param tab Name of the tab that is going to hold the component.
	 */
	public void addToToolBar(JComponent component, String tab) {
		toolbars.get(titles.indexOf(tab)).add(component);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		contentLayout.show(contentContainer, titles.get(tabs.indexOf(e.getSource())));
		toolbarLayout.show(toolbarContainer, titles.get(tabs.indexOf(e.getSource())));
	}
}
