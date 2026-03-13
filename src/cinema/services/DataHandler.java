package cinema.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class DataHandler {
	public static void saveToFile(String filePath, List<String> data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String line : data) {
				writer.write(line); writer.newLine();
			} // TODO : look into a better way to do this. 
		} catch (IOException e) {
			System.err.println("Error saving to " + filePath + " : " + e.getMessage());
		} // TODO : Replace with mroe elegant error handling. Logs? 
	}
	
	public static List<String> readFromFile(String filePath) {
		List<String> lines = new ArrayList<>();
		File file = new File(filePath);
		if (!file.exists()) {
			System.err.println("File does not exist, wrong name maybe.");
			return lines; // Empty list if file does not exist
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			 } // TODO : look into better ways to do this.
		} catch (IOException e) {
			System.err.println("Error reading from " + filePath + " : " + e.getMessage());
		} 
		
		return lines;
	}
}
