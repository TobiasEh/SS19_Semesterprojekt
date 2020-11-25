package web.matrix.gui;

import java.awt.Image;

import javax.swing.ImageIcon;
/**
 * Implements self made icons
 * @author Tobias Eh Jonas Bühler Dominik Witoscheck
 * 
 */
public class Assets {
	
	public static final String ICONPATH = "/web/resources/icons/";
	public static final String IMAGEPATH ="/web/resources/screenshots/";
	public static final int SMALLICON = 16;
	public static final int MENUICON = 20;
	public static final int MEDICON = 24;
	public static final int BIGICON = 32;
	/**
	 * Returns the Icon from the filename  in the right siz
	 * @param filename the datapath of the Icon
	 * @param size the size of the Icon
	 * @return The choosed Icon in the right size
	 */
	public static ImageIcon getIcon(String filename, int size) {
		ImageIcon icon = new ImageIcon(Assets.class.getResource(ICONPATH + filename)); 
		Image image = icon.getImage(); 
		Image tmpimg = image.getScaledInstance(size, size,  Image.SCALE_SMOOTH);   
		return new ImageIcon(tmpimg);
	}
}