package web.matrix.gui.panel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import web.matrix.gui.Assets;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixException;
import web.matrix.util.matrices.MatrixIllegalInputException;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberParser;
import web.matrix.util.parser.NumberParserException;
/**
 * Holds a table that displays all the matrices and fields currently available.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class JObjectPanel extends JPanel implements PropertyChangeListener, KeyListener, ActionListener {

	private ObjectContainer objectContainer;
	private JTable fields, matrices;
	private JPanel fieldsP, matricesP, fieldsButtonP, matricesButtonP;
	private JButton matricesDeleteButton, fieldsDeleteButton, fieldsAddButton;
	private ArrayList<Matrix> fieldsArrayList, matricesArrayList;
	private NumberParser numberParser;
	
	/**
	 * Creates and initializes the object view.
	 */
	public JObjectPanel() {
		JTabbedPane tab = new JTabbedPane();
		JPanel panel = new JPanel();
		
		numberParser = new NumberParser();

		objectContainer = ObjectContainer.instance();
		objectContainer.addPropertyChangeListener(this);
		
		this.setLayout(new BorderLayout());
		
		matricesDeleteButton = new JButton ("Delete");
		matricesDeleteButton.setIcon(Assets.getIcon("Bin.png", Assets.SMALLICON));
		matricesDeleteButton.addActionListener(this);
		
		matricesButtonP = new JPanel();
		
		fieldsDeleteButton = new JButton ("Delete");
		fieldsDeleteButton.setIcon(Assets.getIcon("Bin.png", Assets.SMALLICON));
		fieldsDeleteButton.addActionListener(this);
		
		fieldsAddButton = new JButton("Add");
		fieldsAddButton.setIcon(Assets.getIcon("Add.png", Assets.SMALLICON));
		fieldsAddButton.addActionListener(this);
		
		fieldsButtonP = new JPanel();
		
		matricesP = new JPanel();
		matricesP.setBorder(BorderFactory.createTitledBorder("Matrices"));
		matricesP.setLayout(new BorderLayout());
		
		fieldsP = new JPanel();
		fieldsP.setBorder(BorderFactory.createTitledBorder("Fields"));
		fieldsP.setLayout(new BorderLayout());
		
		fields = new JTable(10, 2);
		matrices = new JTable(10, 2);
		
		fillTabel();
		
		JScrollPane paneFields = new JScrollPane(fields);
		JScrollPane paneMatrices = new JScrollPane(matrices);
		paneFields.setBorder(BorderFactory.createEmptyBorder());
		paneMatrices.setBorder(BorderFactory.createEmptyBorder());
		matricesButtonP.add(matricesDeleteButton);
		
		
		fieldsButtonP.add(fieldsAddButton);
		fieldsButtonP.add(fieldsDeleteButton);
		
		build();
		
		panel.setLayout(new GridLayout(2,1));
		panel.add(matricesP);
		panel.add(fieldsP);
		
		tab.addTab("Objects", panel);
		tab.setFocusable(false);
		
		this.add(tab, BorderLayout.CENTER);
	}
	
	private void fillTabel()  {
		Iterator<Matrix> iterator = objectContainer.iterator();
		fieldsArrayList = new ArrayList <Matrix>();
		 matricesArrayList = new ArrayList <Matrix>(); 
		int matrix = 0, field = 0;
		while(iterator.hasNext()) {
			Matrix next = iterator.next();
			next.addPropertyChangeListener(this);
			if (next.isField()) { 
				fieldsArrayList.add(next);
				field++;
			}
			else {
				 matricesArrayList.add(next);
				matrix++;
			}
		}
		
		iterator = objectContainer.iterator();
		String[][] s1 = new String[field][2];
		String[][] s2 = new String[matrix][2];
		for( int i = 0, j = 0; iterator.hasNext(); i++, j++) {
			Matrix next = iterator.next();
			if (next.isField()) {
				s1[i][0] = next.getName();
				s1[i][1] = next.getValue(0, 0).toString();
				j--;
			} else {
				s2[j][0] = next.getName();
				s2[j][1] = String.valueOf(next.getHeigth()).concat("x").concat(String.valueOf(next.getWidth()));
				i--;
			}
		}
		String [] names1 = {"Name", "Value"};
		fields = new JTable(s1,names1);
	
		String [] names2 = {"Name", "Size"};
		matrices = new JTable(s2,names2);
	}
	
	private void fieldChange() {
		int row = fields.getSelectedRow();
		int col = fields.getSelectedColumn();
		if(col == 0 && row >= 0) {
			Matrix m = fieldsArrayList.get(row);
			String name = m.getName();
			if (!objectContainer.containsName((String) fields.getValueAt(row, col))) {
				try {
					m.setName((String) fields.getValueAt(row, col));
				} catch (MatrixIllegalInputException e) {
					fields.setValueAt(name, row, col);
					ConsoleMessageHandler.instance().showMessage(e.getMessage());
				}
				try {
					objectContainer.checkName(m);
				} catch (MatrixException e){
					m.setName(name);
					fields.setValueAt(name, row, col);
					ConsoleMessageHandler.instance().showMessage(e.getMessage());
				}
			} else if (!fields.getValueAt(row, col).equals(fieldsArrayList.get(row).getName())){
				fields.setValueAt(fieldsArrayList.get(row).getName(), row, col);
				ConsoleMessageHandler.instance().showMessage("Name not possible.");
				//TODO: check name public
			}
		}
		if(col == 1 && row >= 0) {
			Matrix m = objectContainer.getObject((String) fields.getValueAt(row, 0));
			Complex d;
			String s = (String) fields.getValueAt(row, col);
			try {
				numberParser.quickParse(s);
				d = numberParser.parseToComplexNumber(s);
				m.setValue(0, 0, d);
			} catch (NumberParserException e) {
				ConsoleMessageHandler.instance().showMessage(e.getMessage());
				fields.setValueAt(String.valueOf(m.getValue(0, 0)), row, 1);
			}
		}
	}
	
	private void matricesChange() {
		int row = matrices.getSelectedRow();
		int col = matrices.getSelectedColumn();
		if(col == 0 && row >= 0) {
			Matrix m = matricesArrayList.get(row);
			String name = m.getName();
			if (!objectContainer.containsName((String) matrices.getValueAt(row, col))) {
				try {
					m.setName((String) matrices.getValueAt(row, col));
				} catch (MatrixIllegalInputException e){
					matrices.setValueAt(name, row, col);
					ConsoleMessageHandler.instance().showMessage(e.getMessage());
				}
				try {
					objectContainer.checkName(m);
				} catch (MatrixException e) {
					m.setName(name);
					matrices.setValueAt(name, row, col);
					ConsoleMessageHandler.instance().showMessage(e.getMessage());
				}
			} else if (!matricesArrayList.get(row).getName().equals((String) matrices.getValueAt(row, col))){
				matrices.setValueAt(matricesArrayList.get(row).getName(), row, col);
				ConsoleMessageHandler.instance().showMessage("Name not possible.");
				//TODO: check name public			
			} 
		}
		if(col == 1 && row >= 0) {
			Matrix m = objectContainer.getObject((String) matrices.getValueAt(row, 0));
			matrices.setValueAt(String.valueOf(m.getHeigth()).concat("x").concat(String.valueOf(m.getWidth())), row, col);
		}
	}
	
	private void deletMatrix(Matrix m) {
		objectContainer.removeObject(m);
	}
	
	private void build() {
		matricesP.removeAll();
		fieldsP.removeAll();
		
		fields.addPropertyChangeListener(this);
		matrices.addPropertyChangeListener(this);
		
		fields.addKeyListener(this);
		matrices.addKeyListener(this);
		
		fields.getTableHeader().setReorderingAllowed(false);
		matrices.getTableHeader().setReorderingAllowed(false);
		
		fields.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,11));

		TableCellRenderer rendererFromHeaderf = fields.getTableHeader().getDefaultRenderer();
		JLabel headerLabelf = (JLabel) rendererFromHeaderf;
		headerLabelf.setHorizontalAlignment(JLabel.CENTER);
		
		matrices.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,11));

		TableCellRenderer rendererFromHeaderm = matrices.getTableHeader().getDefaultRenderer();
		JLabel headerLabelm = (JLabel) rendererFromHeaderm;
		headerLabelm.setHorizontalAlignment(JLabel.CENTER);

	
		JScrollPane paneMatrices = new JScrollPane(matrices);
		JScrollPane paneFields = new JScrollPane(fields);
		
		paneFields.setBorder(BorderFactory.createEmptyBorder());
		paneMatrices.setBorder(BorderFactory.createEmptyBorder());
		
		matricesP.add(paneMatrices, BorderLayout.CENTER);
		matricesP.add(matricesButtonP, BorderLayout.SOUTH);
		
		fieldsP.add(paneFields, BorderLayout.CENTER);
		fieldsP.add(fieldsButtonP, BorderLayout.SOUTH);
		this.updateUI();
		
	}
	
	private void deleteFieldButton() {
		if (fields.getSelectedRow() >= 0) {
			Matrix m = objectContainer.getObject((String) fields.getValueAt(fields.getSelectedRow(), 0));
			objectContainer.removeObject(m);
		}
	}
	
	private void deleteMatrixButton() {
		if (matrices.getSelectedRow() >= 0) {
			Matrix m = objectContainer.getObject((String) matrices.getValueAt(matrices.getSelectedRow(), 0));
			objectContainer.removeObject(m);
		}
	}
	
	private void addFieldButton() {	
		Matrix m = null;
		m = new Matrix (1,1);
		m.setValue(0, 0, new Complex (1));
		objectContainer.addObject(m);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource().getClass().equals(objectContainer.getClass())) {
			fillTabel();
			build();	
		}
		if(evt.getSource().equals(fields)) 
			fieldChange();
		if (evt.getSource().equals(matrices)) 
			matricesChange();
		if(evt.getPropertyName().equals("matrixheight")||evt.getPropertyName().equals("matrixwidth") ||evt.getPropertyName().equals("matrixsize") ) {
			setDimension(evt);
		}
		this.updateUI();
	}

	private void setDimension(PropertyChangeEvent evt) {
		Matrix m = (Matrix) evt.getSource();
		if (!m.isField()) {
			int x = matricesArrayList.indexOf(m);
			matrices.setValueAt(String.valueOf(m.getHeigth()).concat("x").concat(String.valueOf(m.getWidth())), x, 1);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(KeyEvent.getKeyText(e.getKeyCode()) == "Delete") {
			if (e.getSource().equals(fields)) {
				deletMatrix(objectContainer.getObject((String) fields.getValueAt(fields.getSelectedRow(), 0)));
			}
			if (e.getSource().equals(matrices)) {
				deletMatrix(objectContainer.getObject((String) matrices.getValueAt(matrices.getSelectedRow(), 0)));
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(fieldsDeleteButton)) 
			deleteFieldButton();
		if (e.getSource().equals(matricesDeleteButton))
			deleteMatrixButton();
		if (e.getSource().equals(fieldsAddButton))
			addFieldButton();
	}

}
