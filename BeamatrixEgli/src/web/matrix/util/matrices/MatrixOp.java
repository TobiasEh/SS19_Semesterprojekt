package web.matrix.util.matrices;

import java.util.Random;

import web.matrix.util.numeric.Complex;

/**
 * Contains mathematical methods for matrices
 * @author Tobias Eh , Jonas Bühler , Dominik Witoschek
 *
 */

public class MatrixOp {
	/**
	 * Fills sets every value of am Matrix to c.
	 * @param matrix Marix which is going to be filled.
	 * @param c New Value of every value in Matrix. 
	 */
	public static void flush(Matrix matrix, Complex c) {
		for (int i = 0; i < matrix.getHeigth(); i++) {
			for (int j = 0; j < matrix.getWidth(); j++) {
				try {
					matrix.setValue(i, j, c);
				} catch (MatrixIllegalInputException e) {}
			}
		}
	}
	/**
	 * Copies the given Matrix.
	 * @param matrix Matrix which is going to be copied.
	 * @return Copy of matrix.
	 */
	public static Matrix copy(Matrix matrix) {
		Matrix copy = null;
			copy = new Matrix(matrix.getHeigth(), matrix.getWidth());
		for (int i = 0; i < matrix.getHeigth(); i++) {
			for (int j = 0; j <matrix.getWidth(); j++) {
				copy.setValue(i, j, matrix.getValue(i, j));
			}
		}
		return copy;
	}
	/**
	 * Negates every value of the Matrix
	 * @param matrix Matrix that will be negated.
	 * @return New Matrix object with negated values.
	 */
	public static Matrix negate(Matrix matrix) {
		Matrix res = null;
			res = new Matrix(matrix.getHeigth(), matrix.getWidth());
		for (int i = 0; i < matrix.getHeigth(); i++) {
			for (int j = 0; j < matrix.getWidth(); j++) {
				res.setValue(i, j, matrix.getValue(i, j).negate());
			}
		}
		return res;
	}
	/**
	 * Adds two matrices.
	 * @param matrix Summand one.
	 * @param summand summand two
	 * @return returns a new Matrix witch is the result of the addition.
	 * @throws MatrixDimensionException if MatrixDimensions aren`t equal.
	 */
	
	public static Matrix add(Matrix matrix, Matrix summand) throws MatrixDimensionException {
		if(matrix.getWidth() != summand.getWidth()|| matrix.getHeigth() != summand.getHeigth()) {
			throw new MatrixDimensionException("The dimensions of the matrices don`t match.");
		}
		Matrix res = new Matrix(matrix.getHeigth(), matrix.getWidth());
		for (int i = 0; i < res.getHeigth(); i++) {
			for (int j = 0; j < res.getWidth(); j++) {
					res.setValue(i, j, matrix.getValue(i, j).add(summand.getValue(i, j)));
			}
		}
		return res;
	}
	/**
	 * Adds multiple matrices.
	 * @param matrix summand one
	 * @param summand array of matrices wich needs to be added. 
	 * @return returns a new Matrix witch is the result of the addition.
	 * @throws MatrixDimensionException if MatrixDimensions aren`t equal.
	 */
	public static Matrix add(Matrix matrix, Matrix[] summand) throws MatrixDimensionException {
		Matrix res = matrix;
		for (Matrix m : summand) {
			res = MatrixOp.add(res, m);
		}
		return res;
	}
	/**
	 * Substracts two matrices.
	 * @param matrix minuend.
	 * @param minuend Substrahend
	 * @return returns a new Matrix witch is the result of the substraction.
	 * @throws MatrixDimensionException
	 */
	public static Matrix sub(Matrix matrix, Matrix minuend) throws MatrixDimensionException{
		if(matrix.getWidth() != minuend.getWidth()|| matrix.getHeigth() != minuend.getHeigth()) {
			throw new MatrixDimensionException("The dimensions of the matrices don`t match.");
		}
		Matrix res = new Matrix(matrix.getHeigth(), matrix.getWidth());
		for (int i = 0; i < res.getHeigth(); i++) {
			for (int j = 0; j < res.getWidth(); j++) {
				res.setValue(i, j, matrix.getValue(i, j).sub(minuend.getValue(i, j)));
			}
		}
		return res;
	}
	/**
	 * Substracts multiple matrices
	 * @param matrix Minuend.
	 * @param minuend Substrahends.
	 * @return returns a new Matrix witch is the result of the substraction.
	 * @throws MatrixDimensionException if MatrixDimensions aren`t equal.
	 */
	public static Matrix sub(Matrix matrix, Matrix[] minuend) throws MatrixDimensionException {
		Matrix res = matrix;
		for (Matrix m : minuend) {
			res = MatrixOp.sub(res, m);
		}
		return res;
	}
	/**
	 * Multiplies two Matrices
	 * @param matrix factor one.
	 * @param factor factor two
	 * @return returns a new Matrix witch is the result of the multiplication
	 * @throws MatrixDimensionException if Matrix Dimensions doesn`t fit.
	 */
	public static Matrix mult(Matrix matrix, Matrix factor) throws MatrixDimensionException{
		if (matrix.isField()) {
			return scale(factor,matrix.getValue(0, 0));
		} else if (factor.isField()) {
			return scale(matrix, factor.getValue(0, 0));
		}
		if (matrix.getWidth() != factor.getHeigth()) {
			throw new MatrixDimensionException("The dimensions of the matrices don`t match.");
		}
		Matrix result = new Matrix(matrix.getHeigth(), factor.getWidth());
		for (int i = 0; i < result.getHeigth(); i++) {
			for (int j = 0; j < result.getWidth();j++) {
				Complex value =  new Complex(0.0);
				// no other way because there is no setReal or setImg
				for (int k = 0; k < matrix.getWidth(); k ++) {
						value = value.add(matrix.getValue(i, k).mult(factor.getValue(k, j)));
				}
					result.setValue(i, j, value);
			}
		}
		return result;
	}
	
	/**
	 * Multiplies Matrices
	 * @param matrix factor one
	 * @param factor array of matrices which are factors
	 * @return returns a new Matrix witch is the result of the multiplication
	 * @throws MatrixDimensionException if Matrix Dimensions doesn`t fit.
	 */
	public static Matrix mult(Matrix matrix, Matrix[] factor) throws MatrixDimensionException {
		Matrix res = matrix;
		for (Matrix m : factor) {
			res = MatrixOp.mult(res, m);
		}
		return res;
	}
	/**
	 * Multiplies a matrix n times with it self.
	 * @param matrix matrix wich are going to be powed.
	 * @param exponent is the exponent.
	 * @return returns a new Matrix witch is the result of the multiplication.
	 * @throws MatrixDimensionException if Matrix isn`t quadratic
	 * @throws MatrixIllegalInputException if exponent isn`t an Integer.
	 */
	public static Matrix pow(Matrix matrix, Complex exponent) throws MatrixDimensionException, MatrixIllegalInputException {
		if (!matrix.isQuadratic()) {
			throw new MatrixDimensionException("Pow is only allowed for quadratic matrices!");
		}
		if (exponent.getIm() != 0) {
			throw new MatrixIllegalInputException("No Complex Numbers Allowed");
		}
		double x = exponent.getRe();
		int e = (int) x;
		if ( x - e != 0) {
			throw new MatrixIllegalInputException("Exponent isn`t an Integer.");
		}
		Matrix res = matrix;
		
		if (e == 0)
			return identity(matrix.getWidth()); 
		boolean negative = false;
		if (e < 0) {
			negative = true;
			e = e * (-1);
		}
		for (int i = 1; i < e; i++) {
			res = MatrixOp.mult(res, matrix);
		}
		if (negative) {
			res = invert(res);
		}
		return res;
	}
	/**
	 * scales the values of this matrix
	 * @param matrix matrix which is going to be scaled
	 * @param scalar scalar of this action.
	 * @return  Returns a new Matrix witch is the result of this action.
	 */
	public static Matrix scale(Matrix matrix, Complex scalar) {
		Matrix res = null;
			res = new Matrix(matrix.getHeigth(), matrix.getWidth());
		for (int i = 0; i < res.getHeigth(); i++) {
			for (int j = 0; j < res.getWidth(); j++) {
					res.setValue(i, j, matrix.getValue(i, j).mult(scalar));
			}
		}
		return res;
	}
	/**
	 * Adds a the values of row source to the values of row destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source Row
	 * @param destination specifies the destination Row
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix addRow(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getHeigth() || source < 0|| destination < 0|| destination >= matrix.getHeigth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getWidth(); i++) {
			result.setValue(destination, i, matrix.getValue(destination, i).add( matrix.getValue(source, i)));
		}
		return result;
	}
	/**
	 * Adds a the values of row source scaled by scalar to the values of row destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source Row.
	 * @param destination specifies the destination Row.
	 * @param scalar multiplied with every value of the source Row.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix addRow(Matrix matrix, int source, int destination, Complex scalar) throws MatrixIllegalInputException {
		if (source >= matrix.getHeigth() || source < 0|| destination < 0|| destination >= matrix.getHeigth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		if (!scalar.equals(new Complex(0.0))) {
			for (int i = 0; i < matrix.getWidth(); i++) {
				result.setValue(destination, i, matrix.getValue(destination, i).add(matrix.getValue(source, i).mult(scalar)));
			}
		}
		return result;
	}
	/**
	 * Subs the values of row source to the values of row destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source Row.
	 * @param destination specifies the destination Row.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix subRow(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getHeigth() || source < 0|| destination < 0|| destination >= matrix.getHeigth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getWidth(); i++) {
			result.setValue(destination, i, matrix.getValue(destination, i).sub(matrix.getValue(source, i)));
		}
		return result;
	}
	/**
	 *  subs the values of row source scaled by scalar to the values of row destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source Row.
	 * @param destination specifies the destination Row.
	 * @param scalar multiplied with every value of the source Row. 
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix subRow(Matrix matrix, int source, int destination, Complex scalar) throws MatrixIllegalInputException {
		if (source >= matrix.getHeigth() || source < 0|| destination < 0|| destination >= matrix.getHeigth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		if (!scalar.equals(new Complex(0.0))) {
			for (int i = 0; i < matrix.getWidth(); i++) {
				result.setValue(destination, i, matrix.getValue(destination, i).sub((matrix.getValue(source, i).mult( scalar ))));
			}
		} 
		return result;
	}
	/**
	 * Adds a the values of column source to the values of row destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source column.
	 * @param destination specifies the destination column.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix addCol(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getWidth()|| source < 0|| destination < 0|| destination >= matrix.getWidth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getHeigth(); i ++) {
			result.setValue(i, destination, matrix.getValue(i, destination).add(matrix.getValue(i, source)));
		}
		return result;
	}
	/**
	 * Adds a the values of column source scaled by scalar to the values of column destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source column.
	 * @param destination specifies the destination column.
	 * @param scalar multiplied with every value of the source column.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix addCol(Matrix matrix, int source, int destination, Complex scalar) throws MatrixIllegalInputException {
		if (source >= matrix.getWidth()|| source < 0|| destination < 0|| destination >= matrix.getWidth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		if (!scalar.equals(new Complex(0.0))) {
			for (int i = 0; i < matrix.getHeigth(); i ++) {
				result.setValue(i, destination, matrix.getValue(i, destination).add((matrix.getValue(i, source).mult(scalar))));
			}
		}
		return result;
	}

	/**
	 * Subs the values of column source to the values of column destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source column.
	 * @param destination specifies the destination column.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix subCol(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getWidth()|| source < 0|| destination < 0|| destination >= matrix.getWidth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getHeigth(); i ++) {
			result.setValue(i, destination, matrix.getValue(i, destination).sub( matrix.getValue(i, source)));
		}
		return result;
	}

	/**
	 *  subs the values of column source scaled by scalar to the values of column destination.
	 * @param matrix matrix of this Action.
	 * @param source specifies the source column.
	 * @param destination specifies the destination column.
	 * @param scalar multiplied with every value of the source column. 
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix subCol(Matrix matrix, int source, int destination, Complex scalar) throws MatrixIllegalInputException {
		if (source >= matrix.getWidth()|| source < 0|| destination < 0|| destination >= matrix.getWidth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		if (!scalar.equals(new Complex(0.0))) {
			for (int i = 0; i < matrix.getHeigth(); i ++) {
				result.setValue(i, destination, matrix.getValue(i, destination).sub((matrix.getValue(i, source).mult(scalar))));
			}
		}
		return result;
	}
	/**
	 * scales a row of a Matrix
	 * @param matrix matrix of this Action.
	 * @param position position of the row in the Matrix
	 * @param scalar scalar of this Action
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix scaleRow(Matrix matrix, int position, Complex scalar) throws MatrixIllegalInputException {
		if (position >= matrix.getHeigth()|| position < 0) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getWidth(); i++) {
			result.setValue(position, i, matrix.getValue(position, i).mult(scalar));
		}
		return result;
	}
	/**
	 * scales a column of a Matrix
	 * @param matrix matrix of this Action.
	 * @param position  position of the Column in the Matrix.
	 * @param scalar scalar of this Action
	 * @return  Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix scaleCol(Matrix matrix, int position, Complex scalar) throws MatrixIllegalInputException {
		if (position >= matrix.getWidth()|| position < 0) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getHeigth(); i++) {
			result.setValue(i, position, matrix.getValue(i, position).mult(scalar));
		}
		return result;
	}
	
	/**
	 * Swaps two Rows
	 * @param matrix matrix of this Action.
	 * @param source Index of Row one.
	 * @param destination Index of Row two.
	 * @return Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix swapRow(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getHeigth()|| source < 0|| destination < 0|| destination >= matrix.getHeigth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getWidth(); i++) {
			result.setValue(source, i, matrix.getValue(destination, i));
			result.setValue(destination, i, matrix.getValue(source, i));
		}
		return result;
	}
	/**
	 * Swaps two columns.
	 * @param matrix matrix of this Action.
	 * @param source Index of column one.
	 * @param destination Index of column two.
	 * @return  Returns a new Matrix witch is the result of this action.
	 * @throws MatrixIllegalInputException If Input is Wrong.
	 */
	public static Matrix swapCol(Matrix matrix, int source, int destination) throws MatrixIllegalInputException {
		if (source >= matrix.getWidth()|| source < 0|| destination < 0|| destination >= matrix.getWidth()) {
			throw new MatrixIllegalInputException("Input Wrong.");
		}
		Matrix result = copy(matrix);
		for (int i = 0; i < matrix.getHeigth(); i++) {
			result.setValue(i, source, matrix.getValue(i, destination));
			result.setValue(i, destination, matrix.getValue(i, source));
		}
		return result;
	}
	/**
	 * calculates the determinant of this Matrix
	 * @param matrix  matrix of this Action.
	 * @return the deteminant of this Matrix
	 * @throws MatrixDimensionException if Matrix isn`t quadratic.
	 * @throws MatrixIllegalInputException if Input is Wrong
	 */
	public static Matrix calcDet(Matrix matrix) throws MatrixDimensionException, MatrixIllegalInputException{
		if (!matrix.isQuadratic()) { 
			throw new MatrixDimensionException("The dimensions of the matrices don`t match.");
		}
		Complex value = new Complex(1.0);
		if (matrix.getHeigth() == 2) {
			value = matrix.getValue(0, 0).mult(matrix.getValue(1, 1));
			value = value.sub(matrix.getValue(1, 0).mult(matrix.getValue(0,1)));
			return new Field(value);
		}
		
		if (matrix.getHeigth() == 3) {
			value = matrix.getValue(0, 0).mult(matrix.getValue(1, 1).mult(matrix.getValue(2, 2)));
			value = value.add(matrix.getValue(0, 1).mult(matrix.getValue(1, 2).mult(matrix.getValue(2, 0))));
			value = value.add(matrix.getValue(0, 2).mult(matrix.getValue(1, 0).mult(matrix.getValue(2, 1))));
			value = value.sub(matrix.getValue(2, 0).mult(matrix.getValue(1,1).mult(matrix.getValue(0, 2))));
			value = value.sub(matrix.getValue(2, 1).mult(matrix.getValue(1,2).mult(matrix.getValue(0, 0))));
			value = value.sub(matrix.getValue(2, 2).mult(matrix.getValue(1,0).mult(matrix.getValue(0, 1))));
			return new Field(value);
		}
		Matrix calcTriangelMatrix = calcTriangelMatrix(matrix, true);
		for (int i = 0; i < matrix.getHeigth(); i++) {
			value = value.mult(calcTriangelMatrix.getValue(i, i)) ;
		}
		return new Field(value);
	}
	/**
	 * calculates a triangelMatrix of the matrix
	 * @param matrix matrix of this Action.
	 * @param det true if this methode is used to caculated determinante normally false
	 * @return Returns a new Matrix witch is a triangelMatrix
	 * @throws MatrixIllegalInputException Input is Wrong.
	 */
	public static Matrix calcTriangelMatrix(Matrix matrix, boolean det) throws MatrixIllegalInputException {
		
		Matrix result = MatrixOp.copy(matrix);
		for (int i = 0; i < result.getHeigth(); i++) {
			if (result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int k = i + 1; k < result.getHeigth()&& result.getValue(i, i).equals(new Complex(0.0, 0.0)); k++) {
					if (!result.getValue(k, i).equals(new Complex(0.0, 0.0))) {
							result = swapRow(result,k ,i);
							if (det) {
								result.setValue(i, i, result.getValue(i, i).negate());
							}
					}
					
				}
			}
			if (!result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int j = i + 1; j < result.getHeigth(); j++) {
					Complex c = new Complex(0.0);
					c = c.add(result.getValue(j, i));
					c = c.negate();
					c = c.div( result.getValue(i, i));
					result = addRow(result, i, j,c);
				}
			}
		}
		return result;
	}
	/**
	 * calculates the triangelMatrixwith 0 in the right upper corner
	 * @param matrix matrix of this Action.
	 * @return Returns a new Matrix witch is a triangelMatrix
	 * @throws MatrixIllegalInputException Input is Wrong.
	 */
	public static Matrix calcTriangelMatrixUp(Matrix matrix) throws MatrixIllegalInputException {
		
		Matrix result = MatrixOp.copy(matrix);
		for (int i = result.getHeigth()-1; i >= 0; i--) {
			if (result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int k = i - 1; k >=0 && result.getValue(i, i).equals(new Complex(0.0, 0.0)); k--)
				{
					if (!result.getValue(k, i).equals(new Complex(0.0, 0.0))) {
							result = swapRow(result,k ,i);
					}
					
				}
			}
			if (!result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int j = i - 1; j >= 0; j--) {
					Complex c = new Complex(0.0);
					c = c.add(result.getValue(j, i));
					c = c.negate();
					c = c.div( result.getValue(i, i));
					result = addRow(result, i, j,c);
				}
			}
		}
		return result;
	}

	/**
	 * calculates the inverse of a Matrix
	 * @param matrix matrix of this Action.
	 * @return  Returns a new Matrix witch is the inverse of the input Matrix
	 * @throws MatrixDimensionException if calcDet of this Matrix is null.
	 * @throws MatrixIllegalInputException Wrong Input.
	 */
	public static Matrix invert(Matrix matrix) throws MatrixDimensionException, MatrixIllegalInputException {
		if (calcDet(matrix).getValue(0, 0).equals(new Complex(0.0))) {
			throw new MatrixIllegalInputException("The matrix is no invertable!");
		}
		Matrix invert = identity(matrix.getHeigth());
		Matrix[] x =calcTriangelMatrixInvert(matrix, invert);
		System.out.println(x[1]);
		Matrix result = x[0];
		invert = x[1];
		x = calcTriangelMatrixUpInvert(result, invert);
		invert = x[1];
		System.out.println(x[1]);
		return invert;
	}
	
	private static Matrix[] calcTriangelMatrixInvert(Matrix matrix, Matrix invert) throws MatrixIllegalInputException {
		
		Matrix result = MatrixOp.copy(matrix);
		for (int i = 0; i < result.getHeigth(); i++) {
			if (result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int k = i + 1; k < result.getHeigth()&& result.getValue(i, i).equals(new Complex(0.0, 0.0)); k++)
				{
					if (!result.getValue(k, i).equals(new Complex(0.0, 0.0))) {
							result = swapRow(result,k ,i);
							invert = swapRow(invert,k ,i);
					}
					
				}
			}
			if (!result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				if (!result.getValue(i, i).equals(new Complex(1))) {
					Complex c = new Complex(1);
					c = c.div(result.getValue(i, i));
					result = scaleRow(result, i, c);
					invert = scaleRow(invert, i, c);
				}
				for (int j = i + 1; j < result.getHeigth(); j++) {
					Complex c = new Complex(0.0);
					c = c.add(result.getValue(j, i));
					c = c.negate();
					c = c.div( result.getValue(i, i));
					result = addRow(result, i, j,c);
					invert = addRow(invert, i, j,c);
				}
			}
		}
		Matrix[] x= {result, invert};
		return x;
	}
	
	private static Matrix[] calcTriangelMatrixUpInvert(Matrix matrix, Matrix invert) throws MatrixIllegalInputException {
		Matrix result = MatrixOp.copy(matrix);
		for (int i = result.getHeigth()-1; i >= 0; i--) {
			if (result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				for (int k = i - 1; k >=0 && result.getValue(i, i).equals(new Complex(0.0, 0.0)); k--)
				{
					if (!result.getValue(k, i).equals(new Complex(0.0, 0.0))) {
							result = swapRow(result,k ,i);
							invert = swapRow(invert,k ,i);
					}
					
				}
			}
			if (!result.getValue(i, i).equals(new Complex(0.0, 0.0))) {
				if (!result.getValue(i, i).equals(new Complex(1))) {
					Complex c = new Complex(1);
					c = c.div(result.getValue(i, i));
					result = scaleRow(result, i, c);
					invert = scaleRow(invert, i, c);
					
				}
				for (int j = i - 1; j >= 0; j--) {
					Complex c = new Complex(0.0);
					c = c.add(result.getValue(j, i));
					c = c.negate();
					c = c.div( result.getValue(i, i));
					result = addRow(result, i, j,c);
					invert = addRow(invert, i, j,c);
				}
			}
		}
		Matrix[] x = {result, invert};
		return x;
	}
	/**
	 * Create a identity Matrix with the given size as width and height.
	 * @param size the new Matrix will have this as height and with.
	 * @return  a identity Matrix with the given size as width and height.
	 */
	public static Matrix identity(int size) {
		Matrix identity = null;
		try {
			identity = new Matrix ( size, size);
		} catch (MatrixDimensionException e) {}
		
		for (int i = 0; i < size; i++) {
			try {
				identity.setValue(i, i,new Complex(1));
			} catch (MatrixIllegalInputException e) {}
		}
		return identity;
	}
	/**
	 * Fills the matrix with random generated values fromm 0 to 100 with stepwith as stepwith
	 * @param m matrix which is goiing to be filled
	 * @param stepwith minimal difference between to values except 0.
	 */
	public static void randomize(Matrix m, double stepwith) {
		Random rand = new Random();
		int r = 0;
		int max = (int) (100 / stepwith);
		for (int i = 0; i < m.getHeigth(); i++) {
			for (int j = 0; j < m.getWidth(); j++) {
				r = rand.nextInt(max + 1);
				m.setValue(i, j, new Complex(r / (1/stepwith)));
			}
		}
	}
}
