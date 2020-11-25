package web.matrix.gui.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * Displays text.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class JTextPanel extends JPanel {
	/**
	 * 
	 * @param title Title of JPanel.
	 */
	public JTextPanel(String title) {
		JLabel label = new JLabel(title);
		this.setBackground(Color.WHITE);
		label.setFont(new Font("Serif", Font.PLAIN, 20));
		this.add(label);
	}
}
