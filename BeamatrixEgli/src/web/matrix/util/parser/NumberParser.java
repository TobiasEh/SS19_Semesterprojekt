package web.matrix.util.parser;

import web.matrix.util.numeric.Complex;

/**
 * Parses a string to a tree or a complex number
 * @author JonBu
 *
 */
public class NumberParser implements NumberFunctions {

	private String parserString;
	private TokenType currentToken;
	private int index;
	private double currentNumber;
	private boolean calculate;
	private int iCounter;

	/**
	 * Creates a new NumberParser
	 */
	public NumberParser() {
		resetSettings();
		this.calculate = false;
	}
	
	private void resetSettings() {
		this.currentToken = TokenType.ERROR;
		this.index = 0;
		this.iCounter = 0;
		this.currentNumber = 0;
	}
	
	/**
	 * Parses a string and builds a tree
	 * @param parserString The string to be parsed
	 * @return A tree, which contains all connections and values
	 * @throws NumberParserException If the parserString is not valid
	 */
	public NumberNode parse(String parserString) throws NumberParserException {
		this.parserString = parserString;
		this.parserString = this.parserString.replaceAll(" ", "");
		resetSettings();
		getNextToken();
		NumberNode rootNode = expression();
		if(currentToken != TokenType.END) throw new NumberParserException("Syntaxerror!");
		
		if(iCounter > 1)
			throw new NumberParserException("Complex numbers have to be of the format: <expression>+<expression>*i");
		return rootNode;
	}
	/**
	 * Parses a complex number and returns a complex number
	 * @param parserString The string to be parsed
	 * @return The complex number
	 * @throws NumberParserException If the parsed string is not valid
	 */
	public Complex parseToComplexNumber(String parserString) throws NumberParserException {
		NumberNode n = parse(parserString);
		if(iCounter <= 0)
			return new Complex(n.getValue());
		//iCounter > 0
		return getComplex(n);
	}
	/**
	 * Parses the parserString and tests for validation
	 * @param parserString The string to be parsed
	 * @return If the parserString is valid
	 * @throws NumberParserException If the parsed string is not valid
	 */
	public boolean quickParse(String parserString) throws NumberParserException {
		this.calculate = true;
		parse(parserString);
		this.calculate = false;
		return true;
	}
	
	private Complex getComplex(NumberNode node) throws NumberParserException {
		String error = "Expecting complex number! Try: <expression> + <expression>*i";
		
		if(node == null || (node.getConnection() != TokenType.PLUS && node.getConnection() != TokenType.MINUS) || iCounter != 1)
			throw new NumberParserException(error);
		
		
		if(node.getLeft() == null || node.getRight() == null || (node.getLeft().getConnection() != TokenType.MULT && node.getRight().getConnection() != TokenType.MULT))
			throw new NumberParserException(error);
		
		NumberNode left = node.getLeft();
		NumberNode right = node.getRight();
		double real = 0, img = 0;
		
		if(left.getConnection() == TokenType.MULT) {
			if(left.getLeft().getConnection() == TokenType.I) {
				img = left.getRight().getValue();
				real = right.getValue();
				
			} else if(left.getRight().getConnection() == TokenType.I) {
				img = left.getLeft().getValue();
				real = right.getValue();
			}
			
			if(node.getConnection() == TokenType.MINUS)
				real = -real;
		}
		if(right.getConnection() == TokenType.MULT ) {
			if(right.getLeft().getConnection() == TokenType.I) {
				img = right.getRight().getValue();
				real = left.getValue();
				if(node.getConnection() == TokenType.MINUS)
					img = -img;
			} else if(right.getRight().getConnection() == TokenType.I) {
				img = right.getLeft().getValue();
				real = left.getValue();
				if(node.getConnection() == TokenType.MINUS)
					img = -img;
			}
		}
		return new Complex(real, img);
	}

	
	private NumberNode expression() throws NumberParserException {
		NumberNode val = term();
		while(currentToken == TokenType.PLUS | currentToken == TokenType.MINUS) {
			NumberNode leftNode = new NumberNode(val, null);
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.PLUS) {
				NumberNode n = term();
				if(!calculate) {
					leftNode.setRight(n);
					leftNode.setConnection(TokenType.PLUS);
					leftNode.setValue(val.getValue() + n.getValue());
					val = leftNode;
				}
			} else if(lastToken == TokenType.MINUS) {
				NumberNode n = term();
				if(!calculate) {
					leftNode.setRight(n);
					leftNode.setConnection(TokenType.MINUS);
					leftNode.setValue(val.getValue() - n.getValue());
					val = leftNode;
				}
			}
		}
		return val;
	}
	
	private NumberNode term() throws NumberParserException {
		NumberNode val = pow();
		while(currentToken == TokenType.MULT | currentToken == TokenType.DIV) {
			NumberNode leftNode = new NumberNode(val, null);
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.MULT) {
				NumberNode n = pow();
				if(!calculate) {
					leftNode.setRight(n);
					leftNode.setConnection(TokenType.MULT);
					leftNode.setValue(val.getValue() * n.getValue());
					val = leftNode;
				}
			} else if(lastToken == TokenType.DIV) {
				NumberNode n = pow();
				if(!calculate) {
					leftNode.setRight(n);
					leftNode.setConnection(TokenType.DIV);
					leftNode.setValue(val.getValue() / n.getValue());
					val = leftNode;
				}
			}
		}
		return val;
	}
	
	private NumberNode pow() throws NumberParserException {
		NumberNode val = primary();
		while(currentToken == TokenType.POW) {
			NumberNode leftNode = new NumberNode(val, null);
			TokenType lastToken = currentToken;
			getNextToken();
			if(lastToken == TokenType.POW) {
				NumberNode n = primary();
				if(!calculate) {
					if(testForRoot(n)) {
						if(n.getLeft().getValue() != 1) {
							NumberNode node = new NumberNode(val, n.getLeft());
							node.setConnection(TokenType.POW);
							leftNode.setLeft(node);
						}
						leftNode.setRight(n.getRight());
						leftNode.setConnection(TokenType.ROOT);
					} else {
					leftNode.setRight(n);
					leftNode.setConnection(TokenType.POW);
					}
					leftNode.setValue(Math.pow(val.getValue(), n.getValue()));
					val = leftNode;
					}
			}
		}
		return val;
	}
	
	private boolean testForRoot(NumberNode n) {
		if(n.getConnection() != TokenType.DIV) return false;
		if(n.getLeft() == null | n.getRight() == null) return false;
		if(n.getLeft().getConnection() != TokenType.NUMBER | n.getRight().getConnection() != TokenType.NUMBER) return false;
		return true;
	}
	
	private NumberNode primary() throws NumberParserException {
		NumberNode val;
		TokenType type = currentToken;
		switch(NumberFunctions.convertType(currentToken)) {
		case NUMBER:
			getNextToken();
			return new NumberNode(null, null, TokenType.NUMBER, currentNumber);
			
		case MINUS:
			getNextToken();
			NumberNode n1 = primary();
			return new NumberNode(n1, null, TokenType.MINUSBRACKET, -n1.getValue());
			
		case LEFTBRACKET:
			getNextToken();
			val = expression();
			rightBracketMissingTest();
			break;
			
		case CONSTANT:
			getNextToken();
			if(type == TokenType.I) {
				iCounter++;
				return new NumberNode(null, null, type, 1);	
			}
			return new NumberNode(null, null, type, NumberFunctions.constant(type));
		
		

			
		case FUNCTION:
			getNextToken();
			leftBracketMissingTest();
			NumberNode expression = expression();
			NumberNode expression2 = null;
			double calc = NumberFunctions.function(type, expression.getValue());
			if(type == TokenType.SQRT) {
				expression2 = new NumberNode(null, null, TokenType.NUMBER, 2);
				type = TokenType.ROOT;
				
			} else if(type == TokenType.CBRT) {
				expression2 = new NumberNode(null, null, TokenType.NUMBER, 3);
				type = TokenType.ROOT;
			}
			
			val =  new NumberNode(expression, expression2, type, calc);
			rightBracketMissingTest();
			break;
			
		default:
			throw new NumberParserException("Expecting Number or '('!");
		}
		return val;
	}
	
	private void rightBracketMissingTest() throws NumberParserException {
		if(currentToken != TokenType.RIGHTBRACKET) {
			throw new NumberParserException("Missing brackets!");
		}
		getNextToken();
	}
	
	private void leftBracketMissingTest() throws NumberParserException {
		if(currentToken != TokenType.LEFTBRACKET) {
			throw new NumberParserException("Missing brackets!");
		}
		getNextToken();
	}
	
	private void getNextToken() throws NumberParserException {
		char c = readCharAt(index);
		if(c == (char)-1) {
			currentToken = TokenType.END;
			return;
		}
		//numbers
		if(Character.isDigit(c) | c == '.') {
			currentNumber = getNumber();
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
			throw new NumberParserException(String.format("'%c' is not a specified character!", c));
		}
	}

	private char readCharAt(int index) {
		if(index >= parserString.length()) return (char) -1;
		return parserString.charAt(index);
	}
	
	
	private double getNumber() throws NumberParserException {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			if(Character.isDigit(c))
				val.append(c - '0');
			else
				val.append(c);
			c = readCharAt(++index);
		} while(c != (char) -1 & (Character.isDigit(c) || c == '.'));
		
		double result;
		try {
		result = Double.parseDouble(val.toString());
		} catch (NumberFormatException e) {
			throw new NumberParserException("Numberformat is wrong!");
		}
		return result;
	}
	
	private TokenType getFunction() throws NumberParserException {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			val.append(c);
			c = readCharAt(++index);

		} while(c != (char) -1 & (Character.isAlphabetic(c)));
		
		TokenType type = NumberFunctions.convertFunction(val.toString());
		if(type != TokenType.ERROR)
			return type;
		
		throw new NumberParserException("Type '"+ val.toString() +"' could not be resolved!");
	}
}
