package web.matrix.util.parser;

/**
 * A interface to quickly add new functions, operations, constants for numberParser
 * @author JonBu
 *
 */
public interface NumberFunctions {

	/**
	 * Converts an operation string in a TokenType
	 * @param type The type to be converted
	 * @return The converted type
	 */
	public static TokenType convertOperation(String type) {
		switch(type.toLowerCase()) {
			//Operations
			case "+" : return TokenType.PLUS;
			case "-" : return TokenType.MINUS;
			case "*" : return TokenType.MULT;
			case "^" : return TokenType.POW;
			case "/" : return TokenType.DIV;
			case "(" : return TokenType.LEFTBRACKET;
			case ")" : return TokenType.RIGHTBRACKET;
			default: return TokenType.ERROR;
		}
	}
	/**
	 * Returns the operations
	 * @return The operations
	 */
	public static String[] getOperations() {
		String[] s = {"+", "-", "*", "^", "/"};
		return s;
	}
	
	/**
	 * Returns the constants
	 * @return The constants
	 */
	public static String[] getConstants() {
		String[] s = {"e", "pi"};
		return s;
	}
	
	/**
	 * Returns the functions
	 * @return The functions
	 */
	public static String[] getFunctions() {
		String[] s = {"sin", "cos", "tan", "ln", "exp", "sqrt", "abs", "arcsin", "arccos", "arctan", "sinh", "cosh", "tanh", "log10", "log1p",
				"cbrt", "ceil", "floor", "signum", "todegrees", "toradians"};
		return s;
	}
	
	/**
	 * Converts a function string in a TokenType
	 * @param type The type to be converted
	 * @return The converted type
	 */
	public static TokenType convertFunction(String type) {
		switch(type.toLowerCase()) {		
			//Constants
			case "i": return TokenType.I;
			case "e": return TokenType.E;
			case "pi": return TokenType.PI;
			
			//functions
			case "sin": return TokenType.SIN;
			case "cos": return TokenType.COS;
			case "tan": return TokenType.TAN;
			case "ln": return TokenType.LN;
			case "exp": return TokenType.EXP;
			case "sqrt": return TokenType.SQRT;
			case "log10": return TokenType.LOG10;
			case "log1p": return TokenType.LOG1P;
			case "abs": return TokenType.ABS;
			case "arcsin": return TokenType.ASIN;
			case "arccos": return TokenType.ACOS;
			case "arctan": return TokenType.ATAN;
			case "cbrt": return TokenType.CBRT;
			case "ceil": return TokenType.CEIL;
			case "floor": return TokenType.FLOOR;
			case "sinh": return TokenType.SINH;
			case "cosh": return TokenType.COSH;
			case "tanh": return TokenType.TANH;
			case "signum": return TokenType.SIGNUM;
			case "todegrees": return TokenType.TODEGREES;
			case "toradians": return TokenType.TORADIANS;

			
			default: return TokenType.ERROR;
		}
	}
	
	//converts to CONSTANT or FUNCTION if type is right
	public static TokenType convertType(TokenType type) {
		switch(type) {
			case I: return TokenType.CONSTANT;
			case E: return TokenType.CONSTANT;
			case PI: return TokenType.CONSTANT;
			
			case SIN: return TokenType.FUNCTION;
			case COS: return TokenType.FUNCTION;
			case TAN: return TokenType.FUNCTION;
			case EXP: return TokenType.FUNCTION;
			case LN: return TokenType.FUNCTION;
			case SQRT: return TokenType.FUNCTION;
			case LOG10: return TokenType.FUNCTION;
			case LOG1P: return TokenType.FUNCTION;
			case ABS: return TokenType.FUNCTION;
			case ASIN: return TokenType.FUNCTION;
			case ACOS: return TokenType.FUNCTION;
			case ATAN: return TokenType.FUNCTION;
			case CBRT: return TokenType.FUNCTION;
			case CEIL: return TokenType.FUNCTION;
			case FLOOR: return TokenType.FUNCTION;
			case SINH: return TokenType.FUNCTION;
			case COSH: return TokenType.FUNCTION;
			case TANH: return TokenType.FUNCTION;
			case SIGNUM: return TokenType.FUNCTION;
			case TODEGREES: return TokenType.FUNCTION;
			case TORADIANS: return TokenType.FUNCTION;
			default: return type;
		}
	}
	/**
	 * Returns the value of the specified constant
	 * @param type The type of the constant
	 * @return The value of the constant
	 */
	public static double constant(TokenType type) {
		switch(type) {
			case E: return Math.E;
			case PI: return Math.PI;
			default: return 1;
		}
	}
	/**
	 * Calculates the value of the function
	 * @param type The type of function
	 * @param val The input value
	 * @return The calculated value
	 * @throws NumberParserException Throws if the value couldnt be calculated
	 */
	public static double function(TokenType type, double val) throws NumberParserException {
		double result;
		switch(type) {
			case SIN: result = Math.sin(val); break;
			case COS: result = Math.cos(val); break;
			case TAN: result = Math.tan(val); break;
			case EXP: result = Math.exp(val); break;
			case LN: result = Math.log(val); break;
			case SQRT: result = Math.sqrt(val); break;
			case LOG10: result = Math.log10(val); break;
			case LOG1P: result = Math.log1p(val); break;
			case ABS: result = Math.abs(val); break;
			case ASIN: result = Math.asin(val); break;
			case ACOS: result = Math.acos(val); break;
			case ATAN: result = Math.atan(val); break;
			case CBRT: result = Math.cbrt(val); break;
			case CEIL: result = Math.ceil(val); break;
			case FLOOR: result = Math.floor(val); break;
			case SINH: result = Math.sinh(val); break;
			case COSH: result = Math.cosh(val); break;
			case TANH: result = Math.tanh(val); break;
			case SIGNUM: result = Math.signum(val); break;
			case TODEGREES: result = Math.toDegrees(val); break;
			case TORADIANS: result = Math.toRadians(val); break;
			default: return 1;
		}
		if(Double.isNaN(result))
			throw new NumberParserException("The result is not a number!");

		
		return result;
	}
}
