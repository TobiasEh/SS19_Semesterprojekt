package web.matrix.gui.matrix;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import web.matrix.gui.matrix.render.Render;
/**
 * A cell that solely displays its linked matrices values..
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class DisplayCell extends JPanel implements Cell {
	protected Render render;
	protected Dimension renderSize = new Dimension(50, 50);
	protected final Dimension MINSIZE = new Dimension(50, 50);
	protected final int PADDING = 25;
	
	/**
	 * Initializes cell.
	 * @param matrix Matrix to linked to this cell.
	 * @param x	 Cell's position on x axis.
	 * @param y Cell's position on y axis.
	 */
	public DisplayCell(MatrixWindow matrix, int x, int y) {
		this.setLayout(new BorderLayout());
		render = new Render(this);
		this.add(render, BorderLayout.CENTER);
	}
	
	/**
	 * Returns this cell's render component.
	 */
	public Render getRender() {
		return this.render;
	}
	
	@Override
	public void setRenderSize(int width, int height) {
		if (width > MINSIZE.width && height > MINSIZE.height) 
			this.renderSize = new Dimension(width, height);
		else if (width > MINSIZE.width)
			this.renderSize = new Dimension(width, this.renderSize.height);
		else if (height > MINSIZE.height)
			this.renderSize = new Dimension(this.renderSize.width, height);
	}
	
	@Override
	public Dimension getRenderSize() {
		return renderSize;
	}
	
	@Override
	public void setValue(String s) {
		DecimalFormat df = new DecimalFormat("#.##########");
		if (!s.contains("i")) {
			s = df.format(Double.valueOf(s));
			s = s.replace(",", ".");
		}
		
		this.render.setUserInput(s);
		
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics();
		int width = fm.stringWidth(s);
		this.setRenderSize(width + PADDING, MINSIZE.height);
	}
	
	@Override
	public String getValue() {
		return render.getUserInput();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.renderSize;
	}
}
