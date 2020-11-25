package web.matrix.util.matrices;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

import web.matrix.util.numeric.Complex;
/**
 * Symbolizes Matrices.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoscheck.
 *
 */
public class Matrix {
	
	private Vector<Vector<Complex>> matrix;
	private String name;
	private int h, w;
	
	public static boolean precise = false;
	
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	/**
	 * Generates a new Matrix, sets name width and height.
	 * @param name The name of this matrix.
	 * @param height The height of this matrix.
	 * @param width The width of this matrix.
	 * @throws MatrixDimensionException Throws if Dimensions didn`t fit or Dimension-Input is wrong.
	 * @throws MatrixIllegalInputException Throws if the Input of an methode isn´t correct.
	 */
	public Matrix(String name, int height, int width) throws MatrixDimensionException, MatrixIllegalInputException {
		
		this.setName(name);
		this.setHeight(height);
		this.setWidth(width);
		this.matrix = init(w, h);	
	}
	/**
	 * Generates a new Matrix, sets height and with
	 * @param height The height of this matrix.
	 * @param width The width of this matrix.
	 */
	
	public Matrix(int height, int width) {
		
		this.setHeight(height);
		this.setWidth(width);
		this.matrix = init(w, h);
	}
	
	
	private Vector<Vector<Complex>> init(int w, int h) {
		Vector<Vector<Complex>> matrix = new Vector<Vector<Complex>>();
		
		for (int i = 0; i < h; i++) {
			Vector<Complex> row = new Vector<Complex>();
			for (int j = 0; j < w; j++) {
				row.add(new Complex(0));
			}
			matrix.add(row);
		}
		
		return matrix;
	}
	
	private void setHeight(int h) {
		int oldh = this.h;
		if(checkHeight(h)) {
			this.h = h;
			changes.firePropertyChange("matrixheight", oldh, h);
		} else {
			throw new MatrixDimensionException("Height needs to be > 0.");
		}
	}
	
	private boolean checkHeight(int h) {
		if (h < 1) return false;
		return true;
	}
	
	private void setWidth(int w){
		int oldw = this.w;
		if (checkWidth(w)) {
			this.w = w;
			changes.firePropertyChange("matrixwidth", oldw, w);
		} else {
			throw new MatrixDimensionException("Width needs to be > 0.");
		}
	}
	
	private boolean checkWidth(int w) {
		if (w < 1) 
			return false;
		return true;
	}
	/**
	 * Sets the name of Matrix fires probertyChange event, throws MatrixIllegalInputException if name is null.
	 * @param name The new name of this matrix.
	 */
	public void setName(String name) {

		if (checkName(name)) {
			String oldname = this.name;
			this.name = name;
			changes.firePropertyChange("matrixname", oldname, name);
		} else
			throw new MatrixIllegalInputException("Name is null.");
	}
	
	private boolean checkName(String name) {
		if (name.isBlank()) return false;
		return true;
	}
	/**
	 * Returns the name of this Matrix.
	 * @return Returns the name of this Matrix.
	 */
	public String getName() {
		return this.name;
	}
	/**
	 *  Returns the width of this Matrix.
	 * @return Returns the width of this Matrix.
	 */
	public int getWidth() {
		return this.w;
	}
	/**
	 *  Returns the height of this Matrix.
	 * @return Returns the height of this Matrix.
	 */
	public int getHeigth() {
		return this.h;
	}
	/**
	 * Returns true if this Matrix is quadratic, if not it returns false.
	 * @return Returns true if this Matrix is quadratic, if not it returns false.
	 */
	public boolean isQuadratic() {
		if (this.w == this.h) return true;
		return false;
	}
	/**
	 * Sets the value at the specific position, fires aPropertyChange.
	 * @param x marks the height of the position.
	 * @param y marks the width of the position.
	 * @param val the new value at this position.
	 */
	public void setValue(int x, int y, Complex val) {
		if (x < 0|| y < 0 || x >= this.h || y >= this.w) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		this.matrix.elementAt(x).set(y, val);
		changes.firePropertyChange("matrixvalue", null, new Object[] {x, y, val});
	}
	/**
	 * Returns the Value at the specific position, throws MatrixIlleagalInputException if the input is wrong.
	 * @param x marks the height of the position.
	 * @param y marks the width of the position.
	 * @return Returns the Value at the specific position.
	 */
	public Complex getValue(int x, int y) {
		if (x < 0|| y < 0 || x >= this.h || y >= this.w) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		return this.matrix.elementAt(x).elementAt(y);
	} 
	
	/**
	 * Inserts the given row at the index, throws MatrixIlleagalInputException if the input is wrong.
	 * @param row  the row which will be added.
	 * @param index marks the place the row will be added.
	 */
	public void insertRow(Vector<Complex> row, int index) {
		if (index < 0 || index > this.h || row.size() != this.w) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		this.matrix.add(index, row);
		setHeight(h+1);
	}
	/**
	 * Removes the specific row, throws MatrixIlleagalInputException if the input is wrong
	 * @param index Specifies the column which will be deleted.
	 */
	public void removeRow(int index) {
		if (index < 0 || index >= this.h) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		if (this.h - 1 < 0) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		this.matrix.remove(index);
		setHeight(h-1);
	}
	/**
	 * Inserts the given column at the index, throws MatrixIlleagalInputException if the input is wrong.
	 * @param column  the column which will be added.
	 * @param index marks the place the column will be added.
	 */
	
	public void insertColumn(Vector<Complex> column, int index) {
		if (index < 0 || index > this.w || column.size() != this.h) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		for (int i = 0; i < this.h; i++) 
			this.matrix.elementAt(i).add(index,column.elementAt(i));
		setWidth(w+1);
	}
	/**
	 * Removes the specific column, throws MatrixIlleagalInputException if the input is wrong.
	 * @param index Specifies the column which will be deleted.
	 */
	public void removeColumn(int index) {
		if (index < 0 || index >= this.w) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		if (this.w - 1 < 0) {
			throw new MatrixIllegalInputException("Illegal Input.");
		}
		for (int i = 0;  i < this.h; i++)
			this.matrix.elementAt(i).remove(index);
		setWidth(w-1);
	}
	
	/**
	 * Overrides equals(Object obj).
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (h != other.h)
			return false;
		if (matrix == null) {
			if (other.matrix != null)
				return false;
		} else if (!matrix.equals(other.matrix))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (w != other.w)
			return false;
		return true;
	}
	/**
	 * Sets a new height an width deletes or adds the missing rows, throws MatrixDimensionException if Dimension is to low.
	 * @param height The new height of this Matrix.
	 * @param width The new width of this Matrix.
	 */
	public void setSize(int height, int width) {
		if(height < 1 || width < 1) {
			throw new MatrixDimensionException("Dimension needs to be > 0");
		}
		if (height == 1 && width == 1) {
			throw new MatrixDimensionException("Cannot create 1x1 matrices");
		}
		if(h < height) {
			for(int i = h; i < height; i++) {
				Vector<Complex> row = new Vector<Complex>();
				for(int j = 0; j < w; j++) {
					row.add(new Complex(0));
				}
				matrix.add(row);
			}

		} else if(h > height) {
			for(int i = height; i < h; i++) {
				matrix.remove(matrix.size() - 1);
			}
		}
		
		if(w < width) {
			for(int i = 0; i < height; i++) {
				for(int j = w; j < width; j++) {
					matrix.get(i).add(new Complex(0));
				}
			}
			
		} else if(w > width) {
			for(int i = 0; i < height; i++) {
				for(int j = width; j < w; j++) {
					matrix.get(i).remove(matrix.get(i).size() - 1);
				}
			}
		}
		int wold = w;
		int hold = h;
		h = height;
		w = width;
		System.out.println(this);
		changes.firePropertyChange("matrixsize", new int[] {wold,  hold}, new int[] {w,  h});
		//changes.firePropertyChange("matrixheight", hold, h);
		//changes.firePropertyChange("matrixwidth", wold, w);

	}
	/**
	 * Overrides toString().
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("<"+name+">");
		for (int i = 0; i < h; i++) {
			s.append("\n" + matrix.elementAt(i).toString());
		}
		
		return s.toString();
	}
	/**
	 * Returns if this Matrix is a field.
	 * @return returns true if Matrix is a field, false if Matrix isn´t a Field.
	 */
	public boolean isField() {
		if(this.w == 1 & this.h == 1) return true;
		return false;
	}
	/**
	 * Adds PropertyChangeListener.
	 * @param l ProbertyChangelistener which is going to be added.
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l); 
	}
	/**
	 * removes the specific PropertyChgangeListener
	 * @param l specifies theProbertyChangeListener
	 */
	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}
}
