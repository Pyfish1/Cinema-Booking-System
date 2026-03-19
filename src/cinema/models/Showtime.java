package cinema.models;

import java.math.BigInteger;

/**
 *
 * @author Ivan
 */
public class Showtime {
	private String showtimeID;
	private String movieID;
	private int hallNum;
	private String dateTime;
	private String[][] seats; 
	
	// Assumption : there are exactly 50 seats per hall. 10 Halls.

	public Showtime(String showtimeID, String movieID, int hallNum, String dateTime, int rows, int cols) {
		this.showtimeID = showtimeID;
		this.movieID = movieID;
		this.hallNum = hallNum;
		this.dateTime = dateTime;
		this.seats = new String[rows][cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				seats[i][j] = "0"; // Set all seats to be empty.
			}
		}
		
	}
	
	public Showtime(String showtimeID, String movieID, int hallNum, String dateTime, String seats) {
		this.showtimeID = showtimeID;
		this.movieID = movieID;
		this.hallNum = hallNum;
		this.dateTime = dateTime;
		this.seats = decodeSeats(seats, 5, 10);
	}
	
	public String getShowtimeID() { return showtimeID; }
	public String getMovieID() { return movieID; }
	public int getHallNum() { return hallNum; }
	public String getDateTime() { return dateTime; }
	public String getSeatsString() { return encodeSeats(seats); }
	public String[][] getSeats() { return seats; }
	
	public void setSeats(String hex) {
		this.seats = decodeSeats(hex, 5, 10);
	}
	
	public void bookSeat(int row, int col) {
		this.seats[row][col] = "1";
	}	
	
	public static String encodeSeats(String[][] seats) {
		StringBuilder binary = new StringBuilder();
		for (String[] row : seats) {
			for (String seat : row) {
				binary.append(seat.equals("1") ? "1" : "0");
			}
		}
		BigInteger bi = new BigInteger(binary.toString(), 2);
		return bi.toString(16).toUpperCase();
	}
	
	public static String[][] decodeSeats(String hex, int rows, int cols) {
		BigInteger bi = new BigInteger(hex, 16);
		String binary = bi.toString(2);
		
		while (binary.length() < rows * cols) {
			binary = "0" + binary;
		}
		
		String[][] seats = new String[rows][cols];
		int k = 0;
		for (int i = 0; i < rows; i++ ) {
			for (int j = 0; j < cols; j++) {
				seats[i][j] = String.valueOf(binary.charAt(k++));
			}
		}
		return seats;
	}
	
	public String toFileString() {
		String encodedSeats = encodeSeats(this.seats);
		return String.join(",", showtimeID, movieID, String.valueOf(getHallNum()), dateTime, encodedSeats);
	}
}
