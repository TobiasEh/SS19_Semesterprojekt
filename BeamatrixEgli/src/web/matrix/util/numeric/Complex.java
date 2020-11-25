package web.matrix.util.numeric;

import java.text.DecimalFormat;

/**
 * A complex number with a real and img part
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
public class Complex {
	private final double im;
	private final double re;

	/**
	 * Creates a new complex number
	 * @param re The real part
	 * @param im The img part
	 */
	public Complex(double re, double im) {
		this.im = im;
		this.re = re;
	}
	
	/**
	 * Creates a new complex number with img = 0
	 * @param re The real part
	 */
	public Complex(double re) {
		this.re = re;
		this.im = 0;
	}
	/**
	 * Negates the complex number
	 * @return The negated complex number
	 */
	public Complex negate() {
		return new Complex(-this.re, -this.im);
	}
	
	/**
	 * Adds two complex numbers
	 * @param z The second summand
	 * @return The sum
	 */
	public Complex add(Complex z) {
		return new Complex(this.re + z.re, this.im + z.im);
	}
	
	
	/**
	 * Substracts two complex numbers
	 * @param z The subtrahend
	 * @return The substraction
	 */
	public Complex sub(Complex z) {
		return new Complex(this.re - z.re, this.im - z.im);
	}
	
	/**
	 * Multiplies two complex numbers
	 * @param z The factor
	 * @return The multiplication
	 */
 	public Complex mult(Complex z) {
 		// IR || IC
 		if (this.im == 0 && z.im == 0) {
 			return new Complex(this.re * z.re, 0);
 		} else {
 	 		double re = this.re * z.re - this.im * z.im;
 	 		double im = this.re * z.im + this.im * z.re;
 	 		return new Complex(re, im);
 		}
 	}
 	/**
 	 * Divides two complex numbers
 	 * @param z The divider
 	 * @return The division
 	 * @throws ArithmeticException If your divide by zero
 	 */
 	public Complex div(Complex z) throws ArithmeticException {
 		if (z.re == 0 && z.im == 0) 
 			throw new ArithmeticException("Division by zero");
 		
 		// IR || IC
 		if (this.im == 0 && z.im == 0) {
 			return new Complex(this.re / z.re, 0);
 		} else {
 			double re = (this.re * z.re + this.im * z.im) / (z.re * z.re + z.im * z.im);
 			double im = (this.im * z.re - this.re * z.im) / (z.re * z.re + z.im * z.im);
 	 		return new Complex(re, im);
 		}
 	}
 	/**
 	 * Returns the absolute value of the complex number
 	 * @return The absolute value of the complex number
 	 */
 	public Complex abs() {
 		return new Complex(Math.sqrt(this.re * this.re + this.im * this.im));
 	}
 	
 	/**
 	 * Returns the real part
 	 * @return real
 	 */
 	public double getRe() {
 		return this.re;
 	}
 	/**
 	 * Returns the img part
 	 * @return The img part
 	 */
 	public double getIm() {
 		return this.im;
 	}
 	
 	@Override
 	public String toString() {
 		DecimalFormat df = new DecimalFormat("#.##########");
 		String reS = df.format(this.re);
 	 	String imS = df.format(this.im);
 	 	reS = reS.replace(',', '.');
 	 	imS = imS.replace(',', '.');
 	 	if (this.im == 0) return reS + "";
 	 	if (this.re == 0) return imS + "i";
 	 	if (this.im < 0) return reS + " - " + "-" + imS + "*i";
 	 	return reS + " + " + imS + "*i";
 	}

 	@Override
 	public boolean equals(Object o) {
 		if (o == null || !o.getClass().equals(Complex.class)) return false;
 		Complex z = (Complex) o;
 		return this.re == z.re && this.im == z.im;
 	}

}
