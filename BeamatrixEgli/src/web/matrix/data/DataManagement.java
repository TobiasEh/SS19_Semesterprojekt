package web.matrix.data;
/**
 * A interface for save and load
 * @author JonBu
 *
 */
public interface DataManagement {
	/**
	 * The load method
	 * @throws LoadSaveException If the file couldnt be loaded
	 */
	public void load() throws LoadSaveException;
	/**
	 * the save method
	 * @throws LoadSaveException If the file couldnt be saved
	 */
	public void save() throws LoadSaveException;
	/**
	 * Closes the file
	 */
	public void close();
}
