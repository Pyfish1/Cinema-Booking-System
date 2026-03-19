package cinema.services;

import cinema.models.Movie;
import cinema.models.Status;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class MovieManager extends EntityManager<Movie> {

	public MovieManager() {
		super("data/movies.txt");
	}

	@Override
	protected Movie stringToObject(String line) {
		String[] p = line.split(",");
		
		String ID = p[0];
		String TITLE = p[1];
		String GENRE = p[2];
		Integer DURATION = Integer.parseInt(p[3]);
		Double RATING = Double.parseDouble(p[4]);
		Status STATUS = Status.valueOf(p[5]);
		String POSTERPATH = p[6];
		
		return new Movie(ID, TITLE, GENRE, DURATION, RATING, STATUS, POSTERPATH);
	}

	@Override
	protected String objectToString(Movie movie) {
		return String.join(",", movie.getMovieID(), movie.getTitle(), movie.getGenre(), movie.getDurationString(), movie.getRatingString(), movie.getStatusString(), movie.getPosterPath());
	}

	@Override
	protected String getEntityID(Movie movie) {
		return movie.getMovieID();
	}
	
	
}
