package web.matrix.util.parser;

import java.util.ArrayList;


/**
 * Is used to split the parserString
 * @author JonBu
 *
 */
public class CalculateParser {
	
	private int index;
	private String parserString;
	
	/**
	 * Is used to split the parserString
	 */
	public CalculateParser() {
		index = 0;
	}
	

	/**
	 * Returns an ArrayList of the elements of the parserString
	 * @param parserString The string to be parsed
	 * @return An ArrayList of the elements of the parserString
	 */
	public  ArrayList<String> parse(String parserString) {
		this.index = 0;
		this.parserString = parserString;
		this.parserString = this.parserString.replaceAll(" ", "");
		ArrayList<String> list = new ArrayList<String>();
		while(index < this.parserString.length())
			list.add(getNextSubString());
		return list;
	}
	
	private String getNextSubString() {
		if(Character.isDigit(parserString.charAt(index)))
			return getNumber();
		
		
		if(Character.isAlphabetic(parserString.charAt(index)))
			return getFunction();
		
		return parserString.charAt(index++)+"";
	}
	
	
	private char readCharAt(int index) {
		if(index >= parserString.length()) return (char) -1;
		return parserString.charAt(index);
	}
	
	private String getNumber()  {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			if(Character.isDigit(c))
				val.append(c - '0');
			else
				val.append(c);
			c = readCharAt(++index);
		} while(c != (char) -1 & (Character.isDigit(c) | c == '.'));
		
		return val.toString();
	}
	
	private String getFunction() {
		StringBuilder val = new StringBuilder();
		char c = readCharAt(index);
		do {
			val.append(c);
			c = readCharAt(++index);
		} while(c != (char) -1 & (Character.isAlphabetic(c) | Character.isDigit(c)));
		
		return val.toString();
	}
}
