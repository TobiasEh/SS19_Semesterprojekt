package web.matrix.util.parser;

import java.util.ArrayList;
import java.util.Arrays;
import web.matrix.util.matrices.Field;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixDimensionException;
import web.matrix.util.matrices.MatrixIllegalInputException;
import web.matrix.util.matrices.MatrixOp;
import web.matrix.util.numeric.Complex;

/**
 * A interface to quickly add new functions, operations, constants for matrixParser
 * @author JonBu
 *
 */

public interface MatrixFunctions extends NumberFunctions {

	/**
	 * Converts the string in a TokenType
	 * @param type The string to be converted
	 * @return The converted TokenType
	 */
	public static TokenType convertFunction(String type) {
		TokenType numberType = NumberFunctions.convertFunction(type);
		if(numberType != TokenType.ERROR) return numberType;
		
		switch(type.toLowerCase()) {	
			//functions
			case "det": return TokenType.DET;
			case "inv": return TokenType.INVERSE;	
			default: return TokenType.ERROR;
		}
	}
	/**
	 * Returns all functions as an ArrayList
	 * @return An ArrayList of all functions
	 */
	public static ArrayList<String> getFunctions() {
		ArrayList<String> s = new ArrayList<String>(Arrays.asList(NumberFunctions.getFunctions()));
		s.add(0, "det");
		s.add(1, "inv");
		return s;
	}
	
	/**
	 * Specifies the type of the TokenType (function, constant, etc.)
	 * @param type The type to be converted
	 * @return TokenType function, constant or type if the type is not a function or a constant
	 */
	public static TokenType convertType(TokenType type) {
		TokenType numberType = NumberFunctions.convertType(type);
		switch(type) {
			case DET: return TokenType.FUNCTION;
			case INVERSE: return TokenType.FUNCTION;
			default: return numberType;
		}
	}
	
	public static Matrix constant(TokenType type) {
		double constant = NumberFunctions.constant(type);
		Field f = new Field(new Complex(constant));
		return f;
	}
	
	/**
	 * Calculates the value of the function
	 * @param type The type of function
	 * @param val The input value
	 * @return The calculated matrix
	 * @throws MatrixParserException Throws if the matrix couldnt be calculated
	 */
	public static Matrix function(TokenType type, Matrix val) throws MatrixParserException {
		if(val.isField()) {
			double re, im;
			re = val.getValue(0, 0).getRe();
			im = val.getValue(0, 0).getIm();
			if(im != 0)
				throw new MatrixParserException("Function with complex number is not allowed!");
			Field f;
			try {
				f = new Field(new Complex(NumberFunctions.function(type, re)));
			} catch (NumberParserException e) {
				throw new MatrixParserException(e.getMessage());
			}
			return f;
		}
		
		switch(type) {
			case DET:
				try {
					return MatrixOp.calcDet(val);
				} catch(MatrixDimensionException | MatrixIllegalInputException e) {
					throw new MatrixParserException(e.getMessage());
				}
			case INVERSE: 
				try {
					return MatrixOp.invert(val);
				} catch(MatrixDimensionException | MatrixIllegalInputException e) {
					throw new MatrixParserException(e.getMessage());
				}
			default: throw new MatrixParserException("This function is not allowed with a matrix as parameter!");
		}
	}
}
