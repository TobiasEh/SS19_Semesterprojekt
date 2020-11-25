package web.matrix.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Shows a message on the console with the singelton principle
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public class ConsoleMessageHandler {
	private String message = null;
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	private static ConsoleMessageHandler unique = null;
	
	private ConsoleMessageHandler() {}
	/**
	 * Returns a consoleMessageHandler object 
	 * @return A consoleMessageHandler object
	 */
	public static ConsoleMessageHandler instance() {
		if (unique == null)
			unique = new ConsoleMessageHandler();
		return unique;
	}

	/**
	 * Shows a message in the console
	 * @param s The message to show
	 */
	public void showMessage(String s) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		String m = "["+LocalTime.now().format(dtf)+"] " + s;
		changes.firePropertyChange("message", message, m);
		this.message = m;
	}
	/**
	 * Adds a propertyChangeListener
	 * @param l A propertyChangeListener
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}
	
	/**
	 * Removes a propertyChangeListener
	 * @param l A propertyChangeListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
}
