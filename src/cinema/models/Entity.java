package cinema.models;

import java.io.*;
import java.util.*;
import java.util.function.Function;

public abstract class Entity {
    protected final String filePath;

    public Entity(String filePath) { this.filePath = filePath; }

    @Override
    public abstract String toString();

    public void append() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            pw.println(this.toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static <T> void saveAll(String path, List<T> items) {
        List<String> lines = new ArrayList<>();
        for (T item : items) lines.add(item.toString());
        writeLines(path, lines);
    }

    private static void writeLines(String path, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            for (String line : lines) pw.println(line);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void update(String path, String id, Entity updatedObj) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(id + ",")) {
                    lines.add(updatedObj.toString());
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        writeLines(path, lines);
    }

    public static <T> void delete(String path, String id, List<T> all, Function<T, String> idGetter) {
        if (all.removeIf(item -> idGetter.apply(item).equals(id))) {
            saveAll(path, all);
        }
    }

    public static <T> List<T> getAll(String path, Function<String, T> decoder) {
        List<T> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) list.add(decoder.apply(line));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }
	
	protected static <T> Object[][] create2DArray(List<T> all, int columnCount, java.util.function.BiConsumer<T, Object[]> rowFiller) {
		Object[][] data = new Object[all.size()][columnCount];
		for (int i = 0; i < all.size(); i++) {
			rowFiller.accept(all.get(i), data[i]);
		}
		return data;
	}
}