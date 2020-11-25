package web.matrix.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
/**
 * Layout for the MatrixPanel. Matrices do wrap now.
 * @author Tobias Eh, Jonas Bühler, Dominik Witoschek.
 *
 */
@SuppressWarnings("serial")
public class MatrixLayout extends FlowLayout {
	
	/**
	 * 
	 * @param align Alignment as in FlowLayout.
	 */
	public MatrixLayout(int align) {
		super(align);
	}
	
	@Override
	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			int w = 0;
			int h = 0;
			Dimension dim = new Dimension(0, 0);
			Insets in = target.getInsets();
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
						w = d.width;
						h = d.height;
					} else {
						if (w + d.width > target.getWidth() - in.left - in.right - getHgap() * 2) {
							dim.height += h + getVgap();
							dim.width = Math.max(dim.width, w);
							w = d.width;
							h = d.height;
						} else {
							w += d.width + getHgap();
							h = Math.max(d.height, h);
						}
					}
				}
			}
			dim.height += h + getVgap() * 2 + in.top + in.bottom;
			dim.width = Math.max(dim.width,  w) + getHgap() * 2 + in.left + in.right;
			return dim;
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			int w = 0;
			int h = 0;
			Dimension dim = new Dimension(0, 0);
			Insets in = target.getInsets();
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0; i < nmembers; i++) {
				Component m = target.getComponent(i);
				if (m.isVisible()) {
					Dimension d = m.getMinimumSize();
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
						w = d.width;
						h = d.height;
					} else {
						if (w + d.width > target.getWidth() - in.left - in.right - getHgap() * 2) {
							dim.height += h + getVgap();
							dim.width = Math.max(dim.width, w);
							w = d.width;
							h = d.height;
						} else {
							w += d.width + getHgap();
							h = Math.max(d.height, h);
						}
					}
				}
			}
			dim.height += h + getVgap() * 2 + in.top + in.bottom;
			dim.width = Math.max(dim.width,  w) + getHgap() * 2 + in.left + in.right;
			return dim;
		}
	}
	
	
}
