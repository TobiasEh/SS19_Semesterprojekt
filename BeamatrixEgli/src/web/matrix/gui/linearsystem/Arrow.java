package web.matrix.gui.linearsystem;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
/**
 * 
 * @author Tobias Eh , Jonas Bühler ,Dominik Witoschek
 *draws an Arrow for JLinearWindow
 */
public class Arrow extends JPanel {
	private String instructions;
	private int width;
	private final int height = 50;
	private final int PADDING = 80;
	private final int ARROWWIDTH = 20;
	private final int ARROWHEIGHT = 8;
	private final int LINETHICKNESS = 1;
	/**
	 * creates a new Arrow 
	 * @param instructions Instructions of this arrow
	 * @param parent parent Object 
	 */
	public Arrow(String instructions, JPanel parent) {
		this.setLayout(new BorderLayout());
		this.instructions = instructions;
		this.setBackground(Color.WHITE);
		}
	/**
	 * overrides PreferetSize
	 * calculates Dimension which will be needed and returns it as prefered size
	 */
	@Override 
	public Dimension getPreferredSize() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics();
		width = fm.stringWidth(instructions);
		return new Dimension(width + PADDING, height);
	}
/**
 * overrides paint Componend 
 * paints arrow and Instructions
 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		    RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(LINETHICKNESS));
		
		g.drawString(instructions, PADDING / 4, this.height / 2 - 10);
		g.drawLine(0, this.height / 2, this.width + PADDING, this.height / 2);
		g.drawLine(this.width + PADDING - ARROWWIDTH, this.height / 2 + ARROWHEIGHT, this.width + PADDING, this.height / 2);
		g.drawLine(this.width + PADDING - ARROWWIDTH, this.height / 2 - ARROWHEIGHT, this.width + PADDING, this.height / 2);
	}
}
