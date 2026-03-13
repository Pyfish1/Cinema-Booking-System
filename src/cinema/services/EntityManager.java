package cinema.services;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ivan
 */
public abstract class EntityManager<T> { // T is a placeholder for any Type
	protected String filePath;
	
	public EntityManager(String filePath) {
		this.filePath = filePath;
	}
	
	// Remember to implement these into successive classes + remove this line. TODO
	protected abstract T stringToObject(String line); 
	protected abstract String objectToString(T object);
	protected abstract String getEntityID(T object); // Ill see if I need this later.
	
	public List<T> getAll() {
		List<String> lines = DataHandler.readFromFile(filePath);
		List<T> objects = new ArrayList<>();
		for (String line : lines) {
			objects.add(stringToObject(line));
		}
		
		return objects;
	}
	
	public Object[][] get2DArray() {
		List<String> lines = DataHandler.readFromFile(filePath);
		if (lines.isEmpty()) {
			System.out.println("Lines are empty - get2DArray()");
			return new Object[0][0];
		}
		int rowCount = lines.size();
		int colCount= lines.get(0).split(",").length;
		
		Object[][] data = new Object[rowCount][colCount];
		
		for (int i = 0; i < rowCount; i++) {
			String line = lines.get(i);
			String[] parts = line.split(",");
			
			for (int j = 0; j < parts.length; j++) {
				data[i][j] = parts[j];
			}
		}
		
		return data;
	}
	
	private void saveAll(List<T> objects) {
		List<String> lines = new ArrayList<>();
		for (T object : objects) {
			lines.add(objectToString(object));
		}
		
		DataHandler.saveToFile(filePath, lines); // filePath gets passed down from the class.
	}
	
	public void add(T item) {
		List<T> all = getAll();
		all.add(item);
		saveAll(all);
	}
	
	public void update(String id, T updatedItem) {
		List<T> all = getAll();
		for (int i = 0; i < all.size(); i++) {
			if (getEntityID(all.get(i)).equals(id)) {
				all.set(i, updatedItem);
				break;
			}
		}
		saveAll(all);
	}
	
	public void delete(String id) {
		List<T> all = getAll();
		boolean removed = all.removeIf(item -> getEntityID(item).equals(id));
		
		if (removed) {
			saveAll(all);
		}
	}
	
	
	
}
