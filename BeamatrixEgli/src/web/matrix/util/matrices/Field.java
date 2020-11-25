package web.matrix.util.matrices;

import web.matrix.util.numeric.Complex;
/**
 * A 1x1 Matrix
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public class Field extends Matrix {
	/**
	 * Creates a new Field
	 * @param complex The complex number
	 * @param name A name
	 */
	public Field(Complex complex, String name) {
		super(name, 1, 1);
		this.setValue(0, 0, new Complex(complex.getRe(), complex.getIm()));
	}
	
	/**
	 * Creates a new Field
	 * @param complex The complex number
	 */
	public Field(Complex complex) {
		super(1, 1);
		this.setValue(0, 0, new Complex(complex.getRe(), complex.getIm()));
	}	
}
