package web.matrix.util.parser;
/**
 * The tokentype for numberparser and matrixparser
 * @author JonBu
 *
 */
public enum TokenType {
	//matrix and numberparser
	PLUS, MINUS, MULT, POW, LEFTBRACKET, RIGHTBRACKET, I, E, PI, END, ERROR, NUMBER, FUNCTION, CONSTANT,
	DIV, SIN, COS, TAN, LN, LOG10, LOG1P, EXP, SQRT, ROOT, ABS, ASIN, ACOS, ATAN, CBRT, CEIL, FLOOR, 
	SINH, COSH, TANH, SIGNUM, TODEGREES, TORADIANS, MINUSBRACKET,
	//matrix
	INVERSE, MATRIX, DET;
}
