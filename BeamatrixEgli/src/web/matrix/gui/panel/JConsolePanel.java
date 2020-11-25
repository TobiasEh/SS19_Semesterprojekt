package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import web.matrix.util.ConsoleMessageHandler;
/**
 * Console panel. Provides feedback for the user and error messages..
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class JConsolePanel extends JPanel implements PropertyChangeListener {

	private JTextArea text;
	private ConsoleMessageHandler handler;
	
	/**
	 * Creates the console windows for displaying feedback.
	 */
	public JConsolePanel() {
		this.setLayout(new BorderLayout());
		
		JTitlePanel tab = new JTitlePanel(new String[] {"Console"});
		JPanel p = new JPanel(new BorderLayout());
		
		handler = ConsoleMessageHandler.instance();
		handler.addPropertyChangeListener(this);
		
		
		text = new JTextArea();
		text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		text.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		p.add(text);
		tab.setFocusable(false);
		tab.addPanel(p, "Console");
		this.add(tab);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("message")) {
			text.setText((String) evt.getNewValue());
		}
	}
}
