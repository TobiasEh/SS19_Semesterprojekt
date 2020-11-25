package web.matrix.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Vector;

import web.matrix.util.matrices.Matrix;
import web.matrix.util.matrices.ObjectContainer;
import web.matrix.util.numeric.Complex;
import web.matrix.util.parser.NumberParser;
import web.matrix.util.parser.NumberParserException;
import web.matrix.util.parser.ParserException;
/**
 * 
 * @author Tobias Eh Jonas Bühler, Dominik Witoschek
 * Data Manager of this Project
 */
public class PersistenceText implements DataManagement {
	
	private String filename;
	/**
	 * constructer of PersistenceText sets filename if possible
	 * @param filename filelokation of the file
	 * @throws LoadSaveException if filename == null
	 */
	public PersistenceText(String filename) throws LoadSaveException {
		if (filename == null)
		    throw new LoadSaveException("no filename");
		this.filename = filename;
	    }
	/**
	 * Overrides load 
	 * loads a text Dokument in the Project.
	 */
	@Override
	public void load() throws LoadSaveException {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
			ObjectContainer objectContainer = ObjectContainer.instance();
			NumberParser numberParser = new NumberParser();
			Matrix m = null;
			int h = 0, w = 0;
			String name = null;
			Vector  <String> value = new Vector <String>();
			
			reader.mark(1);
			
			String line = reader.readLine();
			
			if (line == null)
				return;
						
			while (!line.equals("end"))	{
				if (line.equals("new")) {
					try {
						line = reader.readLine();
						if (line == null) {
						    throw new LoadSaveException("Invalid format");
						}
						h = Integer.parseInt(line);
						
						line = reader.readLine();
						if (line == null) {
						    throw new LoadSaveException("Invalid format");
						}
						w = Integer.parseInt(line);
						
						line = reader.readLine();
						if (line == null){
						    throw new LoadSaveException("Invalid format");
						}
						name = line;
						
						line = reader.readLine();
						if (line == null){
						    throw new LoadSaveException("Invalid format");
						}
						value = new Vector <String>();
						while (!line.equals("matrix")) {
							value.add(line);
							line = reader.readLine();
							if (line == null){
							    throw new LoadSaveException("Invalid format");
							}
						}
						
						StringBuilder builderName = new StringBuilder(name);
						if (builderName.charAt(0) == '<') 
							builderName = builderName.deleteCharAt(0);
						else {
							throw new LoadSaveException("Invalid format");
						}
						if (builderName.charAt(builderName.length() - 1) == '>')
							builderName = builderName.deleteCharAt(builderName.length() - 1);
						else{
							throw new LoadSaveException("Invalid format");
						}
						name = new String(builderName);
						m = new Matrix(name, h, w);
						for (int i = 0; i < h; i++) {
							String [] values = value.get(i).split(",");
							for (int j = 0; j < w; j++) {
								String val = values[j];
								StringBuilder b = new StringBuilder(val);
							if (j == 0) {
								if (b.charAt(0) == '[') {
									b = b.deleteCharAt(0);
									val = new String(b);
								} else {
									throw new LoadSaveException("Invalid format");
								}
							}
							if (b.charAt(b.length() - 1) == ']') {
								b.deleteCharAt(b.length() - 1);
								val = new String(b);
							}
							Complex c = numberParser.parseToComplexNumber(val);
							m.setValue(i, j, c);
						}
							
					}
					objectContainer.addObject(m);
					line = reader.readLine();
					
					} catch (ParserException e) {
						throw new LoadSaveException("Invalid format");
					} catch (NumberParserException e) {
						e.printStackTrace();
						throw new LoadSaveException("Invalid format");
					} catch (LoadSaveException e) {
						throw new LoadSaveException("Invalid format");
					} catch (IOException e) {
						throw new LoadSaveException("Invalid format");
					}
				} else {
					throw new LoadSaveException("Invalid format");
				}
			}
			
		} catch (IOException e2) {
			throw new LoadSaveException("Invalid fomat");
		}
	}
	/**
	 * Overrides save
	 * Saves the Project in a textFiele
	 */
	@Override
	public void save() throws LoadSaveException { 
		ObjectContainer objectContainer = ObjectContainer.instance();
		Iterator <Matrix> matrixIterator = objectContainer.iterator();
		try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(filename)))) {
			while (matrixIterator.hasNext()) {
				Matrix m = matrixIterator.next();
				writer.println("new");
				writer.println(m.getHeigth());
				writer.println(m.getWidth());
				writer.println(m);
				writer.println("matrix");
			}
			writer.println("end");
		}catch(IOException e) {
			throw new LoadSaveException("saving failed");
		}
	}
	/**
	 * Overrides close () 
	 * Empty
	 */
	@Override
	public void close() {
		
	}
	
	
}
