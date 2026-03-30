package cinema.models;

import java.util.List;
import java.util.stream.Collectors;

public class Movie extends Entity {
    // Moved Status enum inside to delete the separate Status.java file
    public enum Status { SHOWING, UPCOMING, ARCHIVED }

    private String movieID, title, genre, posterPath;
    private int duration;
    private double rating;
    private Status status;
    private static final String FILE_PATH = "data/movies.txt";

    public Movie(String movieID, String title, String genre, int duration, double rating, Status status, String posterPath) {
        super(FILE_PATH);
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.status = status;
        this.posterPath = posterPath;
    }
	
	// --- Getters ---
    public String getMovieID() { return movieID; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public double getRating() { return rating; }
    public Status getStatus() { return status; }
    public String getPosterPath() { return posterPath; }

    // --- Data Logic (Replaces MovieManager) ---

    public static List<Movie> getAll() {
        return Entity.getAll(FILE_PATH, Movie::fromFileString);
    }
	
	public static Object[][] get2DArray() {
    List<Movie> all = getAll();
    return Entity.create2DArray(all, 7, (movie, row) -> {
        row[0] = movie.getMovieID();
        row[1] = movie.getTitle();
        row[2] = movie.getGenre();
        row[3] = movie.getDuration();
        row[4] = movie.getRating();
        row[5] = movie.getStatus();
        row[6] = movie.getPosterPath();
    });
}

    public static void update(String id, Movie updated) {
        Entity.update(FILE_PATH, id, updated);
    }

    public static void delete(String id) {
        Entity.delete(FILE_PATH, id, getAll(), Movie::getMovieID);
    }

    private static Movie fromFileString(String line) {
        String[] p = line.split(",");
        return new Movie(
            p[0], // ID
            p[1], // Title
            p[2], // Genre
            Integer.parseInt(p[3]), // Duration
            Double.parseDouble(p[4]), // Rating
            Status.valueOf(p[5].toUpperCase()), // Status
            p[6] // Poster Path
        );
    }

    @Override
    public String toString() {
        return String.join(",", 
            movieID, title, genre, 
            String.valueOf(duration), 
            String.valueOf(rating), 
            status.name(),
            posterPath
        );
    }

    // --- UI Helpers ---
    public static String generateNextID() {
        return String.format("%03d", getAll().stream()
                .mapToInt(m -> Integer.parseInt(m.getMovieID()))
                .max().orElse(0) + 1);
    }
}