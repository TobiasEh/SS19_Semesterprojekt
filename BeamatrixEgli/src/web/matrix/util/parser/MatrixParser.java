package web.matrix.util.parser;

import web.matrix.util.matrices.Field;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixDimensionException;
import web.matrix.util.matrices.MatrixIllegalInputException;
import web.matrix.util.matrices.MatrixOp;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
/**
 * Is used for parsing Strings and calculating the matrix
 * @author JonBu
 *
 */
public class MatrixParser implements MatrixFunctions {
	private String parserString;
	private TokenType currentToken;
	private int index;
	private Matrix currentMatrix;
	private ObjectContainer objects;
	private boolean calculate;
	
	/**
	 * Is used for parsing Strings and calculating the matrix
	 * @param objects The objects which can be used while parsing
	 */
	public MatrixParser(ObjectContainer objects) {
		this.objects = objects;
		this.calculate = false;
		resetSettings();
	}


	/**
	 * Sets a new ObjectContainer
	 * @param objects A ObjectContainer
	 */
	public void setObjects(ObjectContainer objects) {
		this.objects = objects;
	}


	private void resetSettings() {
		this.currentToken = TokenType.ERROR;
		this.index = 0;
		this.currentMatrix = null;
	}

	/**
	 * Parses a string and returns the calculated matrix
	 * @param parserString The String which is parsed
	 * @return returns The calculated matrix
	 * @throws MatrixParserException Throws when parserString is not valid
	 */
	public Matrix parse(String parserString) throws MatrixParserException {
		this.parserString = parserString;
		this.parserString = this.parserString.replaceAll(" ", "");
		resetSettings();
		getNextToken();
		Matrix result = expression();
		if(currentToken != TokenType.END) throw new MatrixParserException("Syntaxerror!");
		return result;
	}

	/**
	 * Parses a string without calculating its value
	 * @param parserString The String which is parsed
	 * @return If the string is valid
	 * @throws MatrixParserException Throws when parserString is not valid
	 */
	public boolean quickParse(String parserString) throws MatrixParserException {
		this.calculate = true;
		parse(parserString);
		this.calculate = false;
		return true;
	}
	
	
	private Matrix expression() throws MatrixParserException {
		Matrix val = term();
		while(currentToken == TokenType.PLUS | currentToken == TokenType.MINUS) {
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.PLUS) {
				Matrix add = term();
				
				if(!calculate) {
					if((val.isField() & !add.isField()) | (!val.isField() & add.isField()))
						throw new MatrixParserException("Addition of numbers and matrices is not allowed!");
					try {
						val = MatrixOp.add(val, add);
					} catch (MatrixDimensionException e) {
						throw new MatrixParserException(e.getMessage());
					}
				}
			} else if(lastToken == TokenType.MINUS) {
				Matrix sub = term();
				
				if(!calculate) {
					if((val.isField() & !sub.isField()) | (!val.isField() & sub.isField()))
						throw new MatrixParserException("Subtraction of numbers and matrices is not allowed!");
					try {
						val = MatrixOp.sub(val, sub);
					} catch (MatrixDimensionException e) {
						throw new MatrixParserException(e.getMessage());
					}
				}
			}
		}
		return val;
	}
	
	private Matrix term() throws MatrixParserException {
		Matrix val = pow();
		while(currentToken == TokenType.MULT | currentToken == TokenType.DIV) {
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.MULT) {
				Matrix mult = pow();
	
				if(!calculate) {
					try {
						val = MatrixOp.mult(val, mult);
					} catch (MatrixDimensionException e) {
						throw new MatrixParserException(e.getMessage());
					}
				}
				
			} else if(lastToken == TokenType.DIV) {
				Matrix div = pow();
				if(!calculate) {
					if(val.isField() & !div.isField())
						throw new MatrixParserException("Matrix divided by number is not allowed!");
					
					if(!val.isField() & !div.isField())
						throw new MatrixParserException("Matrix divided by matrix is not allowed!");
					
					if(val.isField() & div.isField()) {
						try {
						val = new Field(val.getValue(0, 0).div(div.getValue(0, 0)));
						} catch(ArithmeticException e) {
							throw new MatrixParserException(e.getMessage());
						}
					}
					if(!val.isField() & div.isField()) {
						Complex one = new Complex(1);
						try {
							val =  MatrixOp.scale(val, one.div(div.getValue(0, 0)));
						} catch(ArithmeticException e) {
							throw new MatrixParserException(e.getMessage());
						}
					}
				}
			}
		}
		return val;
	}
	
	
	private Matrix pow() throws MatrixParserException {
		Matrix val = primary();
		while(currentToken == TokenType.POW) {
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.POW) {
				Matrix pow = primary();
				
				if(!calculate) {
					if(!pow.isField())
						throw new MatrixParserException("Pow matrix is not allowed!");
					
					if(val.isField() & pow.isField()) {
						if(val.getValue(0, 0).getIm() != 0 & pow.getValue(0, 0).getIm() != 0)
							throw new MatrixParserException("Pow with complex numbers is not allowed!");

						val = new Field(new Complex(Math.pow(val.getValue(0, 0).getRe(), pow.getValue(0, 0).getRe())));
					}
					
					if(!val.isField() & pow.isField()) {
						try {
							val =  MatrixOp.pow(val, pow.getValue(0, 0));
						} catch (MatrixIllegalInputException | MatrixDimensionException e) {
							throw new MatrixParserException(e.getMessage());
						}
					}
				}
			}
		}
		return val;
	}
	
	
	private Matrix primary() throws MatrixParserException {
		Matrix val = null;
		TokenType type = currentToken;
		switch(MatrixFunctions.convertType(currentToken)) {
		case NUMBER:
			getNextToken();
			return MatrixOp.copy(currentMatrix);
		
		case MATRIX:
			getNextToken();
			return MatrixOp.copy(currentMatrix);
			
		case MINUS:
			getNextToken();
			return MatrixOp.negate(primary());
			
		case LEFTBRACKET:
			getNextToken();
			val = expression();
			rightBracketMissingTest();
			break;
		
		case CONSTANT:
			getNextToken();
			return MatrixFunctions.constant(type);
			
		case FUNCTION:
			getNextToken();
			leftBracketMissingTest();
			Matrix expression = expression();
			//if(!calculate)
				val = MatrixFunctions.function(type, expression);
			rightBracketMissingTest();
			break;		
			
		default:
			throw new MatrixParserException("Expecting Number or '('!");
		}
		return val;
	}
	
	private void leftBracketMissingTest() throws MatrixParserException {
		if(currentToken != TokenType.LEFTBRACKET) {
			throw new MatrixParserException("Missing brackets!");
		}
		getNextToken();
	}
	
	private void rightBracketMissingTest() throws MatrixParserException {
		if(currentToken != TokenType.RIGHTBRACKET) {
			throw new MatrixParserException("Missing brackets!");
		}
		getNextToken();
	}
	
	private void getNextToken() throws MatrixParserException {
		char c = readCharAt(index);
		if(c == (char)-1) {
			currentToken = TokenType.END;
			return;
		}
		//numbers
		if(Character.isDigit(c) | c == '.') {
			currentMatrix = getNumber();
			currentToken = TokenType.NUMBER;
			return;
		
		//Functions
		} else if(Character.isAlphabetic(c)) {
			currentToken = getFunction();
			return;
		} else {
			index++;
			TokenType type = NumberFunctions.convertOperation(String.valueOf(c));
			if(type != TokenType.ERROR) {
				currentToken =  type;
				return;
			}
			throw new MatrixParserException(String.format("'%c' is not a specified character!", c));
		}
	}

	private char readCharAt(int index) {
		if(index >= parserString.length()) return (char) -1;
		return parserString.charAt(index);
	}
	
	
	private Matrix getNumber() throws MatrixParserException {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			if(Character.isDigit(c))
				val.append(c - '0');
			else
				val.append(c);
			c = readCharAt(++index);
		} while(c != (char) -1 & (Character.isDigit(c) | c == '.'));
		
		double number;
		try {
			number = Double.parseDouble(val.toString());
		} catch (NumberFormatException e) {
			throw new MatrixParserException("To many '.'!");
		}
		
		return new Field(new Complex(number));
	}
	
	private TokenType getFunction() throws MatrixParserException {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			val.append(c);
			c = readCharAt(++index);
		} while(c != (char) -1 & (Character.isAlphabetic(c) | Character.isDigit(c)));
		
		
		//MATRICES
		if(objects.containsName(val.toString())) {
			currentMatrix = MatrixOp.copy(objects.getObject(val.toString()));
			if(currentMatrix.isField())
				return TokenType.NUMBER;
			return TokenType.MATRIX;
		}
		
		//FUNCTIONS
		TokenType type = MatrixFunctions.convertFunction(val.toString());
		if(type != TokenType.ERROR)
			return type;
		
		throw new MatrixParserException("Type '"+ val.toString() +"' could not be resolved!");
	}
}
