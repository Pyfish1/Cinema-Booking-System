package cinema.models;

/**
 *
 * @author Ivan
 */
public class Movie {

	public Movie(String movieID, String title, String genre, int duration, double rating, Status status, String posterPath) {
		this.movieID = movieID;
		this.title = title;
		this.genre = genre;
		this.posterPath = posterPath;
		this.duration = duration;
		this.rating = rating;
		this.status = status;
	}
	private String movieID, title, genre; // Might make genre an ENUM
	private String posterPath;
	private int duration; // Minutes
	private double rating;
	private Status status;
	
	public String getMovieID() { return movieID; }
	public String getTitle() { return title; }
	public String getGenre() { return genre; }
	public String getPosterPath() { return posterPath; }
	
	public String getDurationString() { return String.valueOf(duration); }
	public String getRatingString() { return String.valueOf(rating); }
	
	public String getStatus() { return status.toString(); }
	
	public int getDuration() { return duration; }
	public double getRating() { return rating; } // Not really sure the uses of these. Just in case.
	
	@Override 
	public String toString() {
		return String.join("|", movieID, title, genre, getDurationString(), getRatingString(), getStatus(), posterPath);
	}
}
