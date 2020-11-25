package web.matrix.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;



@SuppressWarnings("serial")
public class JManualDialog extends JDialog implements TreeSelectionListener {
	
	private JTree tree;
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode terminal;
	private DefaultMutableTreeNode objects;
	private DefaultMutableTreeNode matrixPanel;
	private DefaultMutableTreeNode calculate;
	private DefaultMutableTreeNode linearSystem;
	private DefaultMutableTreeNode console;
	private DefaultMutableTreeNode commands;
	private DefaultMutableTreeNode cmd1;
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode expression;
	private DefaultMutableTreeNode operation;
	private DefaultMutableTreeNode number;
	private DefaultMutableTreeNode matrix;
	private DefaultMutableTreeNode field;
	private DefaultMutableTreeNode constant;
	private DefaultMutableTreeNode function;
	private DefaultMutableTreeNode complex;
	private DefaultMutableTreeNode numberExpression;
	private DefaultMutableTreeNode numberOperation;
	private DefaultMutableTreeNode number2;
	private DefaultMutableTreeNode constant2;
	private DefaultMutableTreeNode numberFunction;
	private JTextArea text;

	public JManualDialog(JFrame menu, String title) {
		super(menu, title);
		setLayout(new BorderLayout());
		setLocationRelativeTo(menu);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(false);
		text = new JTextArea();
		JPanel choose = new JPanel();
		createJTree();
		tree.addTreeSelectionListener(this);
		choose.add(tree);
		text.setWrapStyleWord(false);
		text.setEditable(false);
		this.add(text, BorderLayout.CENTER);
		this.add(tree, BorderLayout.WEST);
		setSize(new Dimension(1200, 550));
		setResizable(false);
		setVisible(true);
	}
	
	private void createJTree() {
		
		root = new DefaultMutableTreeNode();
		terminal = new DefaultMutableTreeNode("Terminal");
		objects = new DefaultMutableTreeNode("Objects");
		matrixPanel = new DefaultMutableTreeNode("Matrix Panel");
		calculate = new DefaultMutableTreeNode("Calculate");
		linearSystem = new DefaultMutableTreeNode("Linear System");
		console = new DefaultMutableTreeNode("Console");

		
		commands = new DefaultMutableTreeNode("Commands");
		cmd1 = new DefaultMutableTreeNode("<name>=<expression>");
		name = new DefaultMutableTreeNode("<name>");
		expression = new DefaultMutableTreeNode("<expression>");
		operation = new DefaultMutableTreeNode("<expression><operation><expression>");
		number = new DefaultMutableTreeNode("<number>");
		matrix = new DefaultMutableTreeNode("<matrix>");
		field = new DefaultMutableTreeNode("<field>");
		constant = new DefaultMutableTreeNode("<constant>");
		function = new DefaultMutableTreeNode("<function>(<expression>)");

		complex = new DefaultMutableTreeNode("complex(<numberexpression>+<numberexpression>*i)");
		numberExpression = new DefaultMutableTreeNode("<numberexpression>");
		numberOperation = new DefaultMutableTreeNode("<numberexpression><operation><numberexpression>");
		number2 = new DefaultMutableTreeNode("<number>");
		constant2 = new DefaultMutableTreeNode("<constant>");
		numberFunction = new DefaultMutableTreeNode("<function>(<numberexpression>)");


		terminal.add(commands);
		commands.add(cmd1);
		cmd1.add(name);
		cmd1.add(expression);
		expression.add(operation);
		expression.add(number);
		expression.add(matrix);
		expression.add(field);
		expression.add(constant);
		expression.add(function);
		complex.add(numberExpression);
		numberExpression.add(numberOperation);
		numberExpression.add(number2);
		numberExpression.add(constant2);
		numberExpression.add(numberFunction);
		expression.add(complex);
		root.add(matrixPanel);
		root.add(calculate);
		root.add(linearSystem);
		root.add(objects);
		root.add(console);
		root.add(terminal);

		
		tree = new JTree(root);
		expandAllNodes();
	}
	
	private void expandAllNodes(){
	    for(int i = 0; i < tree.getRowCount(); i++){
	        tree.expandRow(i);
	    }
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(e == null) return;
		Object node = e.getNewLeadSelectionPath().getLastPathComponent();
		if(node == null) return;
		
			if(node.equals(calculate)) {
				text.setText("Is used to calculate matrices and numbers.");
				
			} else if(node.equals(matrixPanel)) {
				text.setText("Is used to display matrices. You can edit rows/columns, delete matrices etc.");
				
			} else if(node.equals(linearSystem)) {
				text.setText("Is used to solve linear systems!");
				
			} else if(node.equals(objects)) {
				text.setText("The objects-panel shows all matrices and fields!");
	
			} else if(node.equals(console)) {
				text.setText("The console shows error messages!");
				
			} else if(node.equals(name)) {
				text.setText("The name you want to save the matrix or number.");
				
			} else if(node.equals(operation)) {
				text.setText("Operations can be: +, -, *, /, ^");
			
			} else if(node.equals(numberOperation)) {
				text.setText("Operations can be: +, -, *, /, ^");
				
			} else if(node.equals(number)) {
				text.setText("Any real number.");
				
			} else if(node.equals(number2)) {
				text.setText("Any real number.");
	
			} else if(node.equals(matrix)) {
				text.setText("The name of the matrix.");
				
			} else if(node.equals(field)) {
				text.setText("The name of the field.");
	
			} else if(node.equals(constant)) {
				text.setText("The name of the constant: e, pi.");
				
			} else if(node.equals(constant2)) {
				text.setText("The name of the constant: e, pi.");
				
			} else if(node.equals(function)) {
				text.setText("det(<matrix>) calculates the determinante of the matrix.\n"+
				"inv(<matrix>)   calculates the inverse of the matrix.\n"+
				"sin(<number>)   Returns the trigonometric sine of an angle\n"+
				"cos(<number>)   Returns the trigonometric cosine of an angle\n"+
				"tan(<number>)   Returns the trigonometric tangent of an angle.\n"+
				"ln(<number>)   Returns the natural logarithm (base e) of a value.\n"+
				"exp(<number>)   Returns Euler's number e raised to the power of a value.\n"+
				"sqrt(<number>)   Returns the correctly rounded positive square root of a value.\n"+
				"log10(<number>)   Returns the base 10 logarithm of a value.\n"+
				"log1p(<number>)   Returns the natural logarithm of the sum of the argument and 1.\n"+
				"abs(<number>)   Returns the absolute value of a value.\n"+
				"arcsin(<number>)   Returns the arc sine of a value.\n"+
				"arccos(<number>)   Returns the arc cosine of a value.\n"+
				"arctan(<number>)   Returns the arc tangent of a value.\n"+
				"cbrt(<number>)   Returns the cube root of a value.\n"+
				"ceil(<number>)   Returns the smallest (closest to negative infinity) value \n"
				+ "    that is greater than or equal to the argument and is equal to a mathematical integer.\n"+
				"floor(<number>)   Returns the largest (closest to positive infinity) value \n"
				+ "    that is less than or equal to the argument and is equal to a mathematical integer.\n"+
				"sinh(<number>)   Returns the hyperbolic sine of a value.\n"+
				"cosh(<number>)   Returns the hyperbolic tangent of value.\n"+
				"tanh(<number>)   Returns the hyperbolic tangent of a value.\n"+
				"signum(<number>)   Returns the signum function of a value.\n"+
				"todegrees(<number>)   Converts an angle measured in radians to an approximately \n"
				+ "    equivalent angle measured in degrees.\n"+
				"toradians(<number>)   Converts an angle measured in degrees to an approximately \n"
				+ "    equivalent angle measured in radians.\n");
				
			} else if(node.equals(numberFunction)) {
				text.setText("sin(<number>)   Returns the trigonometric sine of an angle\n"+
				"cos(<number>)   Returns the trigonometric cosine of an angle\n"+
				"tan(<number>)   Returns the trigonometric tangent of an angle.\n"+
				"ln(<number>)   Returns the natural logarithm (base e) of a value.\n"+
				"exp(<number>)   Returns Euler's number e raised to the power of a value.\n"+
				"sqrt(<number>)   Returns the correctly rounded positive square root of a value.\n"+
				"log10(<number>)   Returns the base 10 logarithm of a value.\n"+
				"log1p(<number>)   Returns the natural logarithm of the sum of the argument and 1.\n"+
				"abs(<number>)   Returns the absolute value of a value.\n"+
				"arcsin(<number>)   Returns the arc sine of a value.\n"+
				"arccos(<number>)   Returns the arc cosine of a value.\n"+
				"arctan(<number>)   Returns the arc tangent of a value.\n"+
				"cbrt(<number>)   Returns the cube root of a value.\n"+
				"ceil(<number>)   Returns the smallest (closest to negative infinity) value \n"
				+ "    that is greater than or equal to the argument and is equal to a mathematical integer.\n"+
				"floor(<number>)   Returns the largest (closest to positive infinity) value \n"
				+ "    that is less than or equal to the argument and is equal to a mathematical integer.\n"+
				"sinh(<number>)   Returns the hyperbolic sine of a value.\n"+
				"cosh(<number>)   Returns the hyperbolic tangent of value.\n"+
				"tanh(<number>)   Returns the hyperbolic tangent of a value.\n"+
				"signum(<number>)   Returns the signum function of a value.\n"+
				"todegrees(<number>)   Converts an angle measured in radians to an approximately \n"
				+ "    equivalent angle measured in degrees.\n"+
				"toradians(<number>)   Converts an angle measured in degrees to an approximately \n"
				+ "    equivalent angle measured in radians.\n");
				
			} else if(node.equals(terminal)) {
				text.setText("In the terminal you can type in commands. Each command has to be in a seperate\n"
						+ " line.");
			} else {
				text.setText("");
		}
	}
}
