package web.matrix.util.parser;

import web.matrix.util.matrices.Field;
import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.MatrixException;
import web.matrix.util.matrices.MatrixOp;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
/**
 * Parses a string and adds its value to the objectContainer
 * @author JonBu
 *
 */
public class Parser {

	private ObjectContainer objects;
	private NumberParser np;
	private MatrixParser mp;
	private boolean parseValue;

	/**
	 * Creates a new Parser
	 * @param objects The objectContainer
	 */
	public Parser(ObjectContainer objects) {
		this.objects = objects;
		this.parseValue = false;
		np = new NumberParser();
		mp = new MatrixParser(objects);
	}

	/**
	 * Sets the objectContainer
	 * @param objects The objectContainer
	 */
	public void setObjects(ObjectContainer objects) {
		this.objects = objects;
		mp.setObjects(objects);
	}

	/**
	 * Parses a list of commands and adds the values to the container
	 * @param parserString The string to be parsed
	 * @throws ParserException If the string couldnt be parsed
	 */
	public void parse(String parserString) throws ParserException {
		String[] subParserStrings = parserString.replace(" ", "").split("\n");
		for(int i = 0; i < subParserStrings.length; i++) {
			if(!subParserStrings[i].isEmpty())
				parseLine(subParserStrings[i], i + 1);
		}
	}
	/**
	 * Parses a list of commands, but doesnt adds the values to the container
	 * @param parserString The string to be parsed
	 * @return If the string is valid
	 * @throws ParserException If the string couldnt be parsed
	 */
	public boolean quickParse(String parserString) throws ParserException {
		this.parseValue = true;
		parse(parserString);
		this.parseValue = false;
		return true;
	}


	private void parseLine(String parserString, int line) throws ParserException {
		if(!parserString.contains("="))
			throw new ParserException("Expecting '<name>=<expression>'!", line);
		
		String[] subString = parserString.split("=");
		if(subString.length != 2)
			throw new ParserException("Expecting '<name>=<expression>' or '<name>=complex(<numberexpression>, <numberexpression>)'!", line);
		
		
		
		if(subString[0].isBlank())
			throw new ParserException("Expecting a name, <name>=<expression>!", line);

		
		if(parserString.contains("complex("))
			parseComplexVariable(subString[0], subString[1], line);
				
		else
			parseTerm(subString[0], subString[1], line);
	}
	
	
	private void parseComplexVariable(String name, String parserString, int line) throws ParserException {
		if(!parserString.startsWith("complex("))
			throw new ParserException("Expecting '<name>=complex(<numberexpression> + <numberexpression> * i)'!", line);
		
		StringBuilder sb = new StringBuilder(parserString);
		parserString = sb.delete(0, 8).toString();

		try {
			if(sb.length() > 0) {
				if(sb.charAt(sb.length() - 1) != ')')
					throw new NumberParserException("Expecting '<name>=complex(<numberexpression> + <numberexpression> * i)'!");
				parserString = sb.deleteCharAt(sb.length() - 1).toString();
			}
			setObjects(ObjectContainer.instance());
			np.quickParse(parserString);

			if(!parseValue) {
				Complex c = np.parseToComplexNumber(parserString);
				if(c.getIm() == 0)
					throw new NumberParserException("Number is not a complex number!");
				//calculate value
				objects.addObject(new Field(c, name));
			}
		} catch (NumberParserException | MatrixException e) {
			throw new ParserException(e.getMessage(), line);
		}
	}
	

	private void parseTerm(String name, String parserString, int line) throws ParserException{
		
		try {	
			setObjects(ObjectContainer.instance());
			mp.quickParse(parserString);

		
			if(!parseValue) {
				//calculate value	
				Matrix m = MatrixOp.copy(mp.parse(parserString));
				m.setName(name);
					
				objects.addObject(m);
				}
		} catch (MatrixParserException | MatrixException e) {
			throw new ParserException(e.getMessage(), line);
		}
	}
}
