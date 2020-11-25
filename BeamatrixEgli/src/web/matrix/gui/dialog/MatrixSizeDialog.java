package web.matrix.gui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import web.matrix.util.ConsoleMessageHandler;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixDimensionException;
import web.matrix.util.matrices.MatrixException;

@SuppressWarnings("serial")
public class MatrixSizeDialog extends JDialog implements ActionListener {
	
	private Matrix m;
	private JTextField height, width;
	
	private final int WIDTH = 275;
	private final int HEIGHT = 175;
	private final int PADDING = 10;
	private final int TEXTWIDTH = 10;
	
	public MatrixSizeDialog(JPanel parent, Matrix m) {
		super(SwingUtilities.getWindowAncestor(parent));
		this.setLocationRelativeTo(parent);
		this.setTitle("Resize");
		this.setLayout(new BorderLayout());
		
		JLabel info = new JLabel("Enter new values for width and height.");
		info.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
		height = new JTextField("New Height", TEXTWIDTH);
		height.requestFocusInWindow();
		height.selectAll();
		addMouseEvent(height);
		width = new JTextField("New Width", TEXTWIDTH);
		addMouseEvent(width);
		
		JButton ok = new JButton("Ok");
		ok.addActionListener(this);
		
		JButton Cancel = new JButton("Cancel");
		Cancel.addActionListener(this);
		
		JPanel ButtonPanel = new JPanel(new FlowLayout());
		ButtonPanel.add(ok);
		ButtonPanel.add(Cancel);
		
		JLabel insertHeight = new JLabel("Height:");
		JPanel heightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		heightPanel.add(insertHeight);
		heightPanel.add(height);
		heightPanel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, PADDING));
		
		JLabel insertWidth = new JLabel("Width: ");
		JPanel widthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		widthPanel.add(insertWidth);
		widthPanel.add(width);
		widthPanel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, PADDING));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		panel.add(heightPanel);
		panel.add(widthPanel);
		panel.add(heightPanel);
		panel.add(widthPanel);
		
		this.m = m;
		
		this.add(info, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		this.add(ButtonPanel, BorderLayout.SOUTH);
		this.setModal(true);
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);
		this.setVisible(true);
		this.setModal(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Ok"))
			onOk();
		if (e.getActionCommand().equals("Cancel")) {
			onCancel();
		}
	}
	
	private void addMouseEvent(JTextField txt) {
		txt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txt.selectAll();
			}
		});
	}

	private void onCancel() {
		ConsoleMessageHandler.instance().showMessage("Action was canceled.");
		this.dispose();
	}

	private void onOk() {
		int h = 0, w = 0;
		try {
			if (!height.getText().equals("Insert Height")) {
				if (!height.getText().isEmpty())
					h = Integer.parseInt(height.getText());
			}
			if (!width.getText().equals("Insert Width")) {
				if (!width.getText().isEmpty())
					w = Integer.parseInt(width.getText());
				}
			
			m.setSize(h, w);
			
		} catch (NumberFormatException e) {
			ConsoleMessageHandler.instance().showMessage("Insert only natural numbers!");
			this.dispose();
		} catch (MatrixException | MatrixDimensionException e) {
			ConsoleMessageHandler.instance().showMessage(e.getMessage());
			this.dispose();
		}
		this.dispose();
	}

}
