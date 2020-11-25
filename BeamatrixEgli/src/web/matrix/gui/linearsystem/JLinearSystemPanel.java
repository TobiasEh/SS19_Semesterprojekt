package web.matrix.gui.linearsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import web.matrix.gui.matrix.MatrixWindow;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
/**
 * 
 * @author Tobias Eh Jonas Bühler Dominik Witoscheck
 *panel to show panel wich shows solution
 */
@SuppressWarnings("serial")
public class JLinearSystemPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private JComboBox <String> matrixSelection, vectorSelection;
	
	private JButton solve;
	private LinearSystemWindow linearSystemWindow;
	
	
	private ObjectContainer objectContainer;
	
	@SuppressWarnings("unchecked")
	/**
	 * creates a new JlinearSystemPanel
	 */
	public JLinearSystemPanel() {
		objectContainer = ObjectContainer.instance();
		objectContainer.addPropertyChangeListener(this);
		
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		
		solve = new JButton("Solve");
		solve.addActionListener(this);
		solve.setEnabled(false);
		
		vectorSelection = getMatrixComboBox("Vector");
		vectorSelection.addActionListener(this);
		matrixSelection = getMatrixComboBox("Matrices");
		matrixSelection.addActionListener(this);
		
		JPanel selectionPanel = new JPanel ();
		selectionPanel.add(matrixSelection);
		selectionPanel.add(vectorSelection);
		selectionPanel.add(solve);
		selectionPanel.setBackground(Color.WHITE);
		
		linearSystemWindow = new LinearSystemWindow();
		
		
		this.add(selectionPanel, BorderLayout.SOUTH);
		this.add(linearSystemWindow, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JComboBox getMatrixComboBox(String titel) {
		JComboBox matrices = new JComboBox<String>();
		matrices.setPreferredSize(new Dimension(80, matrices.getPreferredSize().height));matrices.setPreferredSize(new Dimension(80, matrices.getPreferredSize().height));
		matrices.setRenderer(new CustomJComboBoxRenderer(titel));
		matrices.setEnabled(false);
		matrices.setSelectedIndex(-1);
		
		return matrices;
	}
	
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
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().equals(JComboBox.class)) {
			comboBoxEvents(e);
		} else if (e.getActionCommand().equals("Solve")) {
			linearSystemWindow.solve();
			solve.setEnabled(false);
		}
	}
	
	private void comboBoxEvents(ActionEvent e) {
		if ( e.getSource().equals(vectorSelection)) {
			if (vectorSelection.getSelectedIndex() >= 0) {
				String Vector = (String) vectorSelection.getSelectedItem();
				if (linearSystemWindow.getVector() == null || !linearSystemWindow.getVector().getMatrix().equals(objectContainer.getObject(Vector))) {
					linearSystemWindow.setVector(new MatrixWindow(objectContainer.getObject(Vector), false));
				}
				if (matrixSelection.getSelectedIndex() >= 0) {
					String matrix = (String) matrixSelection.getSelectedItem();
					if (objectContainer.getObject(matrix).getHeigth() == objectContainer.getObject(Vector).getHeigth()) {
						solve.setEnabled(true);
					} else {
						solve.setEnabled(false);
					}
				} else {
					linearSystemWindow.removeMatrix();
					solve.setEnabled(false);
				}
			} else {
				linearSystemWindow.removeVector();
				solve.setEnabled(false);
			}
		}
		if (e.getSource().equals(matrixSelection)) {
			if (matrixSelection.getSelectedIndex() >= 0) {
				String matrix = (String) matrixSelection.getSelectedItem();
				if (linearSystemWindow.getMatrix() == null || !linearSystemWindow.getMatrix().equals(objectContainer.getObject(matrix))) {
					linearSystemWindow.setMatrix(new MatrixWindow(objectContainer.getObject(matrix), false));
				}
				if (vectorSelection.getSelectedIndex() >= 0) {
					String Vector = (String) vectorSelection.getSelectedItem();
					if (objectContainer.getObject(matrix).getHeigth() == objectContainer.getObject(Vector).getHeigth()) {
						solve.setEnabled(true);
					} else {
						solve.setEnabled(false);
					}
				} else {
					linearSystemWindow.removeVector();
					solve.setEnabled(false);
				}
			} else {
				linearSystemWindow.removeMatrix();
				solve.setEnabled(false);
			}
		}
		updateUI();
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		for(Matrix m : objectContainer) {
			m.removePropertyChangeListener(this);
			m.addPropertyChangeListener(this);
		}
		int ma = matrixSelection.getSelectedIndex();
		int v = vectorSelection.getSelectedIndex();

		if (evt.getPropertyName().equals("matrixname")) {
			for(int i = 0; i < matrixSelection.getItemCount(); i++) {
				if(matrixSelection.getItemAt(i).equalsIgnoreCase((String) evt.getOldValue())) {
					matrixSelection.removeItemAt(i);
					matrixSelection.insertItemAt((String) evt.getNewValue(), i);
					return;
				}
			}
			for(int i = 0; i < vectorSelection.getItemCount(); i++) {
				if(vectorSelection.getItemAt(i).equalsIgnoreCase((String) evt.getOldValue())) {
					vectorSelection.removeItemAt(i);
					vectorSelection.insertItemAt((String) evt.getNewValue(), i);
					return;
				}
			}

		}
		matrixSelection.removeAllItems();
		vectorSelection.removeAllItems();
		boolean matricesExist = false, vectorExist = false;
		
		for(Matrix m: objectContainer) {
			if(m.isQuadratic()) {
				matrixSelection.addItem(m.getName());
				matricesExist = true;
			} else if (m.getWidth() == 1){
				vectorSelection.addItem(m.getName());
				vectorExist = true;
			}
		}
		if (matrixSelection.getItemAt(ma) != null)
			matrixSelection.setSelectedIndex(ma);
		else 
			matrixSelection.setSelectedIndex(-1);
		if (vectorSelection.getItemAt(v) != null)
			vectorSelection.setSelectedIndex(v);
		else 
			vectorSelection.setSelectedIndex(-1);
		vectorSelection.setEnabled(vectorExist);
		matrixSelection.setEnabled(matricesExist);
		updateUI();
	}
}
