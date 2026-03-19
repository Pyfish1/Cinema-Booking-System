package cinema.services;

import cinema.models.Showtime;

/**
 *
 * @author Ivan
 */
public class ShowtimeManager extends EntityManager<Showtime> {

	public ShowtimeManager() {
		super("data/showtimes.txt");
	}

	@Override
	protected Showtime stringToObject(String line) {
		String[] p = line.split(",");
		
		String SHOWTIMEID = p[0];
		String MOVIEID = p[1];
		int HALLNUM = Integer.parseInt(p[2]);
		String DATETIME = p[3];
		String SEATS = p[4];
		
		return new Showtime(SHOWTIMEID, MOVIEID, HALLNUM, DATETIME, SEATS);
	}

	@Override
	protected String objectToString(Showtime showtime) {
		return String.join(",", showtime.getShowtimeID(), showtime.getMovieID(), String.valueOf(showtime.getHallNum()), showtime.getDateTime(), showtime.getSeatsString());
	}

	@Override
	protected String getEntityID(Showtime showtime) {
		return showtime.getShowtimeID();
	}
	
}
