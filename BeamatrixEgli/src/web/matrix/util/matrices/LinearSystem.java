package web.matrix.util.matrices;

import java.util.ArrayList;

import web.matrix.util.numeric.Complex;
/**
 * Sysmbolisise Linear Systems
 * @author Tobias Eh , Jonas Bühler, Dominik Witoschek
 *
 */
public class LinearSystem {
	private Matrix matrix = null;
	private Matrix vector = null;
	private ArrayList<String> steps = null;
	private ArrayList<Matrix> mInterims = null;
	private ArrayList<Matrix> vInterims = null;
	private boolean hasSolution = true;
	
	/**
	 * Creates a new Linear System
	 * @param matrix the matrix of this Linear System
	 * @param vector the vector of this new Linear System
	 */
	public LinearSystem(Matrix matrix, Matrix vector) {
		if (!matrix.isQuadratic()) {
			throw new MatrixIllegalInputException("Matrix must be quadratic");
		} else if (matrix.getHeigth() != vector.getHeigth()) {
			throw new MatrixIllegalInputException("Height must be equal");
		} else if (matrix == null || vector == null ) {
			throw new MatrixIllegalInputException("Argument is null");
		}
		
		this.matrix = matrix;
		this.vector = vector;
		this.steps = new ArrayList<String>();
		vInterims = new ArrayList<Matrix>();
		mInterims = new ArrayList<Matrix>();
		
		solve();
	}
	/**
	 * returns the ArrayList of the steps
	 * @return  returns the ArrayList of the steps
	 */
	public ArrayList<String> getSteps() {
		return this.steps;
	}
	/**
	 * returns the ArrayList of the interimMatrices
	 * @return returns the ArrayList of the interimMatrices
	 */
	public ArrayList<Matrix> getInterimsMatrix() {
		return this.mInterims;
	}
	/**
	 * returns the ArrayList of the interim vectors
	 * @return returns the ArrayList of the interimMatrices
	 */
	public ArrayList<Matrix> getInterimsVector() {
		return this.vInterims;
	}
	/**
	 * returns true if the Linear system has a solution if not false
	 * @return returns true if the Linear system has a solution if not false
	 */
	public boolean hasSolution() {
		return hasSolution;
	}
	/**
	 * solves the Linear System an saves the staps and the solution in Arrays
	 */
	public void solve() {
		Matrix result = MatrixOp.copy(matrix);
		for (int i = 0; i < result.getHeigth(); i++) {
			System.out.println(i);
			if (result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int k = i + 1; k < result.getHeigth() && result.getValue(i, i).equals(new Complex(0.0, 0.0)); k++)
				{
					if (!result.getValue(k, i).equals(new Complex(0.0, 0.0))) {
						steps.add("Line " + (k + 1) + " switched with " + (i + 1));
						result = MatrixOp.swapRow(result,k ,i);
						vector = MatrixOp.swapRow(vector,k ,i);	
						vInterims.add(vector);
						mInterims.add(result);
					}
					
				}
			}
			if (!result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int j = i + 1; j < result.getHeigth(); j++) {
					Complex c = new Complex(0.0);
					c = c.add(result.getValue(j, i));
					c = c.negate();
					c = c.div( result.getValue(i, i));
					steps.add("Line " + (j + 1) + " plus line " + (i + 1) + " times (" + c + ")");
					result = MatrixOp.addRow(result, i, j,c);
					vector = MatrixOp.addRow(vector, i, j,c);
					for (int e = 0; e < result.getWidth(); e++) {
						if (result.getValue(i, e).abs().getRe() < e) {
							result.setValue(i, e, new Complex(0));
						}
						if (result.getValue(j, e).abs().getRe() < e) {
							result.setValue(j, e, new Complex(0));
						}
					}
					vInterims.add(vector);
					mInterims.add(result);
				}
			} else {
				hasSolution = false;
			}
		}
		if (hasSolution) {
			for (int i = result.getHeigth() - 1; i >= 0; i--) {
				result = MatrixOp.copy(result);
				vector = MatrixOp.copy(vector);
				Complex c = vector.getValue(i, 0);
				for (int k = i + 1; k < result.getWidth(); k++) {
					c = c.sub(result.getValue(i, k));
					result.setValue(i, k, new Complex(0));
				}
				c = c.div(result.getValue(i, i));
				vector.setValue(i, 0, c);
				for (int j = i - 1 ; j >= 0; j--) {
					result.setValue(i, j, c.mult(result.getValue(i, j)));
					
				}
				result.setValue(i, i, new Complex(1));
				steps.add("Lines 0.." + (i + 1) + " applied in line "+ i + ", substract(a[0]x[0] + ... + a[I-1]x[I-1]), divide(a[" + i + "])");
				vInterims.add(vector);
				mInterims.add(result);
			}
			
		}
	}
	
}
