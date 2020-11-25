package web.matrix.util.matrices;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Pattern;

import web.matrix.data.LoadSaveException;
import web.matrix.data.PersistenceText;

	/**
	 * The container of the matrices and fields
	 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
	 *
	 */
	public class ObjectContainer implements Iterable<Matrix> {

		private static ObjectContainer unique = null;
		private ArrayList<Matrix> objects;
		
		private PropertyChangeSupport changes = new PropertyChangeSupport(this);
		
		private ObjectContainer() {
			objects = new ArrayList<Matrix>();
		}
		/**
		 * Returns a objectContainer object
		 * @return A objectContainer object
		 */
		public static ObjectContainer instance() {
			if (unique == null) unique = new ObjectContainer();
			return unique;
		}
		
		/**
		 * Adds a matrix to the container
		 * @param m The matrix to be added
		 * @throws MatrixException If the matrix couldnt be added
		 */
		public void addObject(Matrix m) throws MatrixException {
			Matrix c = null;
			int i = getObjectIndex(m.getName());
			if(i != -1)
				c = objects.get(i);
			
			
			if (c != null) {
				if((c.isField() & !m.isField()))
					throw new MatrixException("Field with this name already exists!");
				
				if(!c.isField() & m.isField())
						throw new MatrixException("Matrix with this name already exists!");
				
				if(c.getHeigth() != m.getHeigth() || c.getWidth() != m.getWidth())
					throw new MatrixException("You can only overwrite a matrix with the same dimension.");
				
				this.objects.remove(i);
				this.objects.add(i, m);
				changes.firePropertyChange("modify", null, m);
			} else {
				if(checkName(m))
					this.objects.add(m);
				String s;
				if(m.isField())
					s = "fieldadd";
				else
					s = "matrixadd";
				changes.firePropertyChange(s, null, m);
			}
		}
		
		/**
		 * Removes a matrix from the container
		 * @param m The matrix to be removed
		 */
		public void removeObject(Matrix m) {
			if(!this.objects.contains(m))
				return;
			String s;
			if(m.isField())
				s = "fieldremove";
			else
				s = "matrixremove";
			this.objects.remove(m);
			changes.firePropertyChange(s, m, null);
		}
		
		/**
		 * Gets the matrix with a specific name
		 * @param name The name
		 * @return The matrix with this name
		 */
		public Matrix getObject(String name) {
			for(Matrix m : objects) {
				if(m.getName().equalsIgnoreCase(name)) {
					return m;
				}
			}
			return null;
		}
		
		private int getObjectIndex(String name) {
			for(int i = 0; i < objects.size(); i++) {
				if(objects.get(i).getName().equalsIgnoreCase(name)) {
					return i;
				}
			}
			return -1;
		}
		/**
		 * Test if a matrix with this name exists in the container
		 * @param name The name to be tested
		 * @return If the container contains a matrix with this name
		 */
		public boolean containsName(String name) {
			for(Matrix m : objects) {
				if(m.getName().equalsIgnoreCase(name))
					return true;
			}
			return false;
		}
		
		/**
		 * Checks if the name of the matrix is valid
		 * @param m The matrix
		 * @return If the name is valid
		 * @throws MatrixException If the name is not valid
		 */
		public boolean checkName(Matrix m) throws MatrixException {
			String name = m.getName();
			
			if (name == null  || name.isBlank()) {
				if(m.isField())
					m.setName(generateName("F"));
				else
					m.setName(generateName("M"));
				
				name = m.getName();
			}
			
			if(!name.matches("[a-zA-Z]+[a-zA-Z0-9]*"))
				throw new MatrixException("The name must consist of letters or numbers and has to start with a letter!");
			
			
			switch(name.toLowerCase()) {
				case "det" : break;
				case "inv" : break;
				case "sin" : break;
				case "cos" : break;
				case "tan" : break;
				case "exp" : break;
				case "ln" : break;
				case "e" : break;
				case "pi" : break;
				case "sqrt" : break;
				case "complex" : break;
				default: return true;
			}
			throw new MatrixException("The Name '"+ name + "' is not allowed!");
		}
		
		//generates Names: F1, F2, F3... or M1, M2, M3...
		// prefix = F or M
		private String generateName(String prefix) {
			String lowerPrefix = prefix.toLowerCase();
			ArrayList<Integer> names = new ArrayList<Integer>();
			for(Matrix m : objects) {
				if(Pattern.matches("["+lowerPrefix+"]{1}\\d+", m.getName()) || Pattern.matches("["+prefix+"]{1}\\d+", m.getName()))
					names.add(Integer.parseInt(m.getName().substring(prefix.length())));
			}
			if(names.size() == 0)
				return prefix + "1";
			
			Collections.sort(names);
			if(names.get(0) != 1)
				return prefix + "1";
			int i;
			for(i = 1; i < names.size(); i++) {
				if(names.get(i - 1) + 1 != names.get(i))
					return prefix + (names.get(i - 1) + 1);
			}
			return prefix + (names.get(i - 1) + 1);
		}

		/**
		 * The iterator for the container
		 */
		public Iterator<Matrix> iterator() {
			return this.objects.iterator();
		}
		
		/**
		 * Adds a propertyChangeListener
		 * @param l A propertyChangeListener
		 */
		public void addPropertyChangeListener(PropertyChangeListener l) {
			changes.addPropertyChangeListener(l);
		}
		/**
		 * Removes a propertyChangeListener
		 * @param l A propertyChangeListener
		 */
		public void removePropertyChangeListener(PropertyChangeListener l) {
			changes.removePropertyChangeListener(l);
		}
		/**
		 * Saves the container in the file
		 * @param filename The filename
		 * @throws LoadSaveException If the file couldnt be saved
		 */
		public void save (String filename) throws LoadSaveException {
			PersistenceText data = new PersistenceText(filename);
			data.save();
		}
		
		/**
		 * Loads the container from a file
		 * @param filename The filename
		 * @throws LoadSaveException If the file couldnt be loaded
		 */
		public void load (String filename) throws LoadSaveException { 
			PersistenceText data = new PersistenceText(filename);
			data.load();
		}
}
