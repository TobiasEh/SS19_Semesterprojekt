package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;

import web.matrix.gui.Assets;
import web.matrix.gui.linearsystem.JLinearSystemPanel;
import web.matrix.gui.matrix.MatrixWindow;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.parser.CalculateParser;
import web.matrix.util.parser.MatrixFunctions;
import web.matrix.util.parser.MatrixParser;
import web.matrix.util.parser.MatrixParserException;
import web.matrix.util.parser.NumberFunctions;
import web.matrix.util.parser.TokenType;
/**
 * Displays calculations involving matrices and linear systems..
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class JCalculatePanel extends JPanel implements ActionListener, DocumentListener, PropertyChangeListener, MatrixFunctions {

	private ObjectContainer objectContainer;
	private JPanel panel, other, operations;
	private JScrollPane scroll;
	private CalculateParser cp;
	private JTextField text;
	private MatrixParser mp;
	private JPanel suggestions;
	private JComboBox<String> matrices, fields, functions, constants;
	private boolean clickable = false, showMatricesB = true;
	private final String OPERATIONS = "OPERATIONS", OTHER = "OTHER", LEFTBRACKET = "LEFTBRACKET";
	private JButton plus, minus, mult, div, pow, leftBracket, rightBracketOperations;
	private JPanel leftBracketP;
	private JButton leftBracket2;
	private DefaultHighlightPainter painter;
	private Highlighter highlighter;
	private JButton run;
	


	private class CustomJComboBoxRenderer extends BasicComboBoxRenderer {
		private String title;

		public CustomJComboBoxRenderer(String title) {
			this.title = title;
		}

		public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

			if (value == null)
				setText(title);
			return this;
		}
	}
	
	/**
	 * Creates and initializes components.
	 */
	public JCalculatePanel() {
		objectContainer = ObjectContainer.instance();
		objectContainer.addPropertyChangeListener(this);
		
		
		cp = new CalculateParser();
		mp = new MatrixParser(objectContainer);
		setLayout(new BorderLayout());
		
		panel = new JPanel(new FlowLayout());
		scroll = new JScrollPane(panel);

		suggestions = new JPanel(new CardLayout());
		other = new JPanel();
		operations = new JPanel();
		leftBracketP = new JPanel();
		JTitlePanel tab = new JTitlePanel(new String[]{"Calculate", "Linear system"});
		JPanel bottom = new JPanel(new BorderLayout());
		JPanel btnTxt = new JPanel();
		JPanel p = new JPanel(new BorderLayout());
		text = new JTextField(35);
		rightBracketOperations = new JButton(")");
		leftBracket = new JButton("(");
		leftBracket2 = new JButton("(");
		plus = new JButton("+");
		minus = new JButton("-");
		mult = new JButton("*");
		div = new JButton("/");
		pow = new JButton("^");
		run = new JButton("Run");
		run.setIcon(Assets.getIcon("Run.png", Assets.SMALLICON));
		run.setFocusable(false);
		run.addActionListener(e -> {
			if(!text.getText().isBlank()) {
				parse(text.getText());
				run.setEnabled(false);
			}
		});
		highlighter = text.getHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		
		leftBracket.setFocusable(false);
		leftBracket.addActionListener(this);
		leftBracket2.setFocusable(false);
		leftBracket2.addActionListener(this);

		rightBracketOperations.setFocusable(false);
		rightBracketOperations.addActionListener(this);
		rightBracketOperations.setVisible(false);
		plus.setFocusable(false);
		plus.addActionListener(this);
		
		minus.setFocusable(false);
		minus.addActionListener(this);
		
		mult.setFocusable(false);
		mult.addActionListener(this);
		
		div.setFocusable(false);
		div.addActionListener(this);
		
		pow.setFocusable(false);
		pow.addActionListener(this);
		

		text.getDocument().addDocumentListener(this);


		scroll.setAutoscrolls(true);
		EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
        scroll.setBorder(eb);
        
        JCheckBox showMatrices = new JCheckBox();
        showMatrices.setSelected(true);
        showMatrices.addActionListener(e -> {
        	showMatricesB = showMatrices.isSelected();
        	quickParse(text.getText());
        });

		matrices = new JComboBox<String>();
		fields = new JComboBox<String>();
		functions = new JComboBox<String>();
		constants = new JComboBox<String>();	

		matrices.setPreferredSize(new Dimension(80, matrices.getPreferredSize().height));
		fields.setPreferredSize(new Dimension(80, fields.getPreferredSize().height));
		functions.setPreferredSize(new Dimension(80, functions.getPreferredSize().height));
		constants.setPreferredSize(new Dimension(80, constants.getPreferredSize().height));

		matrices.setEnabled(false);
		fields.setEnabled(false);
		
		matrices.addActionListener(this);
		fields.addActionListener(this);
		functions.addActionListener(this);
		constants.addActionListener(this);

		for(String s : NumberFunctions.getConstants()) {
			constants.addItem(s);
		}
		
		for(String s : MatrixFunctions.getFunctions()) {
			functions.addItem(s);
		}
		matrices.setRenderer(new CustomJComboBoxRenderer("Matrices"));
		matrices.setSelectedIndex(-1);
		
		fields.setRenderer(new CustomJComboBoxRenderer("Fields"));
		fields.setSelectedIndex(-1);
		
		functions.setRenderer(new CustomJComboBoxRenderer("Functions"));
		functions.setSelectedIndex(-1);
		
		constants.setRenderer(new CustomJComboBoxRenderer("Constants"));
		constants.setSelectedIndex(-1);
		
		
		other.add(matrices);
		other.add(fields);
		other.add(functions);
		other.add(constants);
		other.add(leftBracket);

		operations.add(plus);
		operations.add(minus);
		operations.add(mult);
		operations.add(div);
		operations.add(pow);
		operations.add(rightBracketOperations);
		
		leftBracketP.add(leftBracket2);
		
		suggestions.add(leftBracketP, LEFTBRACKET);
		suggestions.add(other, OTHER);
		suggestions.add(operations, OPERATIONS);
		((CardLayout)suggestions.getLayout()).show(suggestions, OTHER);
			
		
		bottom.add(suggestions, BorderLayout.CENTER);
		bottom.add(btnTxt, BorderLayout.SOUTH);
		btnTxt.add(text);
		btnTxt.add(run);
		scroll.setBackground(Color.WHITE);
		panel.setBackground(Color.WHITE);
		bottom.setBackground(Color.WHITE);
		suggestions.setBackground(Color.WHITE);
		operations.setBackground(Color.WHITE);
		other.setBackground(Color.WHITE);
		btnTxt.setBackground(Color.WHITE);
		text.setText("");
		clickable = true;
		p.add(scroll);
		p.add(bottom, BorderLayout.SOUTH);
		tab.addPanel(p, "Calculate");
		tab.addPanel(new JLinearSystemPanel(), "Linear system");
		tab.addToToolBar(showMatrices, "Calculate");
		tab.addToToolBar(new JLabel("Show Matrices"), "Calculate");
		this.add(tab, BorderLayout.CENTER);
	}

	
	private void parse(String parserString) {
		
		try {
			highlighter.removeAllHighlights();
			text.setToolTipText("");
			Matrix m = mp.parse(parserString);
			panel.add(new JTextPanel("="));
			if(m.isField())
				panel.add(new JTextPanel(m.getValue(0, 0) + ""));
			else
				panel.add(new MatrixWindow(m, false));
			updateUI();
		} catch(MatrixParserException ex) {
			text.setToolTipText(ex.getMessage());
			ConsoleMessageHandler.instance().showMessage(ex.getMessage());
			try {
				highlighter.addHighlight(0, text.getText().length(), painter);
			} catch (BadLocationException e) {
				System.err.println(e.getMessage());
			}
		}
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		clickable = false;
		objectContainer = ObjectContainer.instance();
		for(Matrix m : objectContainer) {
			m.removePropertyChangeListener(this);
			m.addPropertyChangeListener(this);
		}
		if (evt.getPropertyName().equals("matrixname")) {
			for(int i = 0; i < matrices.getItemCount(); i++) {
				if(matrices.getItemAt(i).equalsIgnoreCase((String) evt.getOldValue())) {
					matrices.removeItemAt(i);
					matrices.insertItemAt((String) evt.getNewValue(), i);
					break;
				}

			}
			
			for(int i = 0; i < fields.getItemCount(); i++) {
				if(fields.getItemAt(i).equalsIgnoreCase((String) evt.getOldValue())) {
					fields.removeItemAt(i);
					fields.insertItemAt((String) evt.getNewValue(), i);
					break;
				}
			}
			clickable = true;
			return;
		}
		fields.removeAllItems();
		matrices.removeAllItems();
		boolean matricesExist = false, fieldsExist = false;
		
		for(Matrix m: objectContainer) {
			if(m.isField()) {
				fields.addItem(m.getName());
				fieldsExist = true;
			} else {
				matrices.addItem(m.getName());
				matricesExist = true;
			}
		}
		matrices.setEnabled(matricesExist);
		matrices.setSelectedIndex(-1);

		fields.setEnabled(fieldsExist);
		fields.setSelectedIndex(-1);
		clickable = true;
		quickParse(text.getText());
		updateUI();
	}
	
	private void quickParse(String parserString) {
		run.setEnabled(false);
		setBracketButtons(operations);
		panel.removeAll();
		if(text.getText().isBlank()) {
			updateUI();
			((CardLayout)suggestions.getLayout()).show(suggestions, OTHER);
			return;
		}
		
		boolean matrix = false;
		ArrayList<String> list = cp.parse(parserString);
		for(String s : list) {
			for(Matrix m : objectContainer) {
				if(s.equalsIgnoreCase(m.getName())) {
					if(m.isField()) 
						panel.add(new JTextPanel(m.getName()));
					
					else {
						if(showMatricesB)
							panel.add(new MatrixWindow(m, false));
						else
							panel.add(new JTextPanel(m.getName()));
					}
					
					matrix = true;
					break;
				}
			}
			
			if(!matrix)
				panel.add(new JTextPanel(s));
			
			matrix = false;
		}
		
		
		if(list.size() == 0) {
			((CardLayout)suggestions.getLayout()).show(suggestions, OTHER);
			setBracketButtons(other);
			setBracketButtons(operations);
		}

		
		if(list.size() != 0) {
			changeLayout(list);
		}
			
		try {
			highlighter.removeAllHighlights();
			text.setToolTipText("");
			mp.quickParse(parserString);
			run.setEnabled(true);
		} catch(MatrixParserException ex) {
			text.setToolTipText(ex.getMessage());
			run.setEnabled(false);
			ConsoleMessageHandler.instance().showMessage(ex.getMessage());
			try {
				highlighter.addHighlight(0, text.getText().length(), painter);
			} catch (BadLocationException e) {
				System.err.println(e.getMessage());
			}
		}
		updateUI();
	}
	
	private void changeLayout(ArrayList<String> list) {

		TokenType type = NumberFunctions.convertOperation(list.get(list.size() - 1));
		if(type != TokenType.ERROR  & type != TokenType.RIGHTBRACKET) {
			((CardLayout)suggestions.getLayout()).show(suggestions, OTHER);
			return;
		}	
	
		if(type == TokenType.RIGHTBRACKET) {
			((CardLayout)suggestions.getLayout()).show(suggestions, OPERATIONS);
			return;
		}

		type = MatrixFunctions.convertFunction(list.get(list.size() - 1));
		if(type != TokenType.ERROR & type != TokenType.LEFTBRACKET) {
			if(MatrixFunctions.convertType(type) == TokenType.FUNCTION) {
				((CardLayout)suggestions.getLayout()).show(suggestions, LEFTBRACKET);
				return;
			}
		}
		((CardLayout)suggestions.getLayout()).show(suggestions, OPERATIONS);

	}
	
	private void setBracketButtons(JPanel p) {
		int leftBrackets = 0, rightBrackets = 0;
		String s = text.getText();
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '(')
				leftBrackets++;
			
			if(s.charAt(i) == ')')
				rightBrackets++;
		}
		if(leftBrackets - rightBrackets >= 1) {
			rightBracketOperations.setVisible(true);
		}
		
			
		if(leftBrackets - rightBrackets <= 0) {
			rightBracketOperations.setVisible(false);
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		quickParse(text.getText());
	}


	@Override
	public void removeUpdate(DocumentEvent e) {
		quickParse(text.getText());
		
	}


	@Override
	public void changedUpdate(DocumentEvent e) {
		quickParse(text.getText());
	}

	
	private void buttonEvents(JButton btn) {
		text.setText(text.getText() + btn.getText());
	}
	
	private void comboBoxEvents(ActionEvent e) {
		if(e.getSource().equals(matrices)) {
			if(matrices.getSelectedItem() != null) {
				text.setText(text.getText()+matrices.getSelectedItem().toString());
				matrices.setSelectedIndex(-1);
			}
		}
		if(e.getSource().equals(fields)) {
			if(fields.getSelectedItem() != null) {
				text.setText(text.getText()+fields.getSelectedItem().toString());
				fields.setSelectedIndex(-1);
			}
		}
		
		if(e.getSource().equals(functions)) {
			if(functions.getSelectedItem() != null) {
				text.setText(text.getText()+functions.getSelectedItem().toString()+"(");
				functions.setSelectedIndex(-1);
			}
		}
		
		if(e.getSource().equals(constants)) {
			if(constants.getSelectedItem() != null) {
				text.setText(text.getText()+constants.getSelectedItem().toString());
				constants.setSelectedIndex(-1);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(text != null & clickable) {
			comboBoxEvents(e);
		}

		if(e.getSource().getClass().equals(JButton.class)) {
			JButton btn = (JButton) e.getSource();
			buttonEvents(btn);
		}
	}
}
