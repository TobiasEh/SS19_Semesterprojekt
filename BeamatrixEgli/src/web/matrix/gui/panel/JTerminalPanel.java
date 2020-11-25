package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;

import web.matrix.gui.Assets;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.parser.Parser;
import web.matrix.util.parser.ParserException;

/**
 * A terminal that enables the user to accomplish things
 * otherwise done on the user interface.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */

@SuppressWarnings("serial")
public class JTerminalPanel extends JPanel implements DocumentListener, PropertyChangeListener {

	private JTextArea textPane, numbers;
	private Parser p;
	private JButton compile;
	private Highlighter highlighter;
	private DefaultHighlightPainter painter;
	private ObjectContainer objectContainer;

	/**
	 * 
	 * @param title Window's title.
	 */
	public JTerminalPanel(String title) {
		this.setLayout(new BorderLayout());
		JTitlePanel tab = new JTitlePanel(new String[] {title});
		JPanel panel = new JPanel(new BorderLayout());
		
		objectContainer = ObjectContainer.instance();
		objectContainer.addPropertyChangeListener(this);
		tab.setFocusable(false);
		this.p = new Parser(objectContainer);
		this.textPane = new JTextArea();
		this.numbers = new JTextArea();
		JPanel top = new JPanel(new BorderLayout());
		JPanel left = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(top);
		
		scroll.setBorder(BorderFactory.createEmptyBorder());
		
		
		left.add(numbers, BorderLayout.WEST);
		left.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);
		top.add(left, BorderLayout.WEST);
		top.add(textPane, BorderLayout.CENTER);
		numbers.setText("1");
		numbers.setEditable(false);
		textPane.setWrapStyleWord(false);
		textPane.getDocument().addDocumentListener(this);
		compile = new JButton("Run");
		compile.setIcon(Assets.getIcon("Run.png", Assets.SMALLICON));
		compile.addActionListener(e -> {
			parse();
		});
		
		textPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					if(quickParse())
						parse();
				}
			}
		});
		compile.setFocusable(false);
		compile.setEnabled(false);
		highlighter = textPane.getHighlighter();
	    painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		panel.add(scroll, BorderLayout.CENTER);
		tab.addPanel(panel, title);
		tab.addToToolBar(compile, title);
		this.add(tab);
	}
	
	private void parse() {
		try {
			highlighter.removeAllHighlights();
			textPane.setToolTipText(null);
			
			p.parse(textPane.getText());
			ConsoleMessageHandler.instance().showMessage("Compilation successful!");
		} catch (ParserException e) {
			setErrorLine(e.getLine());
			textPane.setToolTipText(e.getMessage());
			ConsoleMessageHandler.instance().showMessage(e.getMessage());
		}
	}

	private void setErrorLine(int line) {
		try {
			int startOffset = textPane.getLineStartOffset(line - 1);
			int endOffset = textPane.getLineEndOffset(line - 1);
		    highlighter.removeAllHighlights();
			highlighter.addHighlight(startOffset, endOffset, painter);

		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void setNumbers() {
		int rowCount = textPane.getDocument().getDefaultRootElement().getElementCount();

		StringBuilder s = new StringBuilder();
		for(int i = 1; i <= rowCount; i++) {
			s.append(i + "\n");
		}
		numbers.setText(s.toString());
	}

	private boolean quickParse() {
		setNumbers();
		if(textPane.getText().isBlank())
			return false;
		try {
			highlighter.removeAllHighlights();
			compile.setEnabled(false);
			textPane.setToolTipText(null);
			
			p.quickParse(textPane.getText());
			compile.setEnabled(true);
		} catch (ParserException e) {
			setErrorLine(e.getLine());
			textPane.setToolTipText(e.getMessage());
			return false;
		}
		return true;
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		quickParse();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		quickParse();
		
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		quickParse();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		objectContainer = ObjectContainer.instance();
		for(Matrix m : objectContainer) {
			m.removePropertyChangeListener(this);
			m.addPropertyChangeListener(this);
		}
		p.setObjects(objectContainer);
		quickParse();
	}
}
