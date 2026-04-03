package cinema.models;

import java.math.BigInteger;
import java.util.List;
import cinema.models.Entity;

public class Showtime extends Entity {
    private String showtimeID, movieID, dateTime;
    private int hallNum;
    private String[][] seats; // 0 for empty, 1 for booked
    private double price;
    
    private static final String FILE_PATH = "data/showtimes.txt";
    private static final int ROWS = 5;
    private static final int COLS = 10;

    // Constructor for NEW showtimes (All empty)
    public Showtime(String showtimeID, String movieID, int hallNum, String dateTime, double price) {
        super(FILE_PATH);
        this.showtimeID = showtimeID;
        this.movieID = movieID;
        this.hallNum = hallNum;
        this.dateTime = dateTime;
        this.price = price;
        String initialHex = cinema.services.SeatManager.getEmptyHallHex();
        this.seats = decodeSeats(initialHex, ROWS, COLS); 
        }
    

    // Constructor for LOADING from file (With Hex string)
    public Showtime(String showtimeID, String movieID, int hallNum, String dateTime, String hexSeats, double price) {
        super(FILE_PATH);
        this.showtimeID = showtimeID;
        this.movieID = movieID;
        this.hallNum = hallNum;
        this.dateTime = dateTime;
        this.price = price;
        this.seats = decodeSeats(hexSeats, ROWS, COLS);
    }
	
	// Getters
    public String getShowtimeID() { return showtimeID; }
    public String getMovieID() { return movieID; }
    public int getHallNum() { return hallNum; }
    public String getDateTime() { return dateTime; }
    public String[][] getSeats() { return seats; }
    public double getPrice() { return price; }

    // --- Hex Logic ---
    public static String encodeSeats(String[][] seats) {
        StringBuilder binary = new StringBuilder();
        for (String[] row : seats) {
            for (String seat : row) binary.append(seat.equals("1") ? "1" : "0");
        }
        String hex =  new BigInteger(binary.toString(), 2).toString(16).toUpperCase();
        while (hex.length() < 13){
            hex = "0" + hex;
        }
        return hex;
    }

    public static String[][] decodeSeats(String hex, int rows, int cols) {
        String binary = new BigInteger(hex, 16).toString(2);
        while (binary.length() < rows * cols) {
            binary = "0" + binary; // itsy bitsy whoopsie before this was fixed
        }
        
        String[][] grid = new String[rows][cols];
        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) grid[i][j] = String.valueOf(binary.charAt(k++));
        }
        return grid;
    }

    // --- Methods ---
    public void bookSeat(int row, int col) {
        this.seats[row][col] = "1";
    }

    public static List<Showtime> getAll() {
        return Entity.getAll(FILE_PATH, line -> {
            String[] p = line.split(",");
            return new Showtime(p[0], p[1], Integer.parseInt(p[2]), p[3], p[4], Double.parseDouble(p[5]));
        });
    }
	
	public static void update(String id, Showtime updated) {
        Entity.update(FILE_PATH, id, updated);
    }
        public static void delete(String id) {
            Entity.delete(FILE_PATH, id, getAll(), Showtime::getShowtimeID);
        }
	
	public Movie getMovie() {
		return Movie.getAll().stream()
				.filter(m -> m.getMovieID().equals(this.movieID))
				.findFirst()
				.orElse(null);
	}
	
	public static Object[][] get2DArray() {
		List<Showtime> all = getAll();

		return Entity.create2DArray(all, 6, (s, row) -> {
			Movie m = s.getMovie();

			row[0] = s.getShowtimeID();
			row[1] = (m != null) ? m.getTitle() : "Unknown (" + s.getMovieID() + ")";
			row[2] = s.getHallNum();
			row[3] = s.getDateTime();


			long bookedCount = java.util.Arrays.stream(s.getSeats())
								.flatMap(java.util.Arrays::stream)
								.filter(seat -> seat.equals("1"))
								.count();
			row[4] = bookedCount + "/50";
		});
	}

    @Override
    public String toString() {
        return String.join(",", showtimeID, movieID, String.valueOf(hallNum), dateTime, encodeSeats(seats), String.valueOf(price));
    }
    
    public static String generateNextID() {
    return String.format("%03d", getAll().stream()
            .mapToInt(s -> Integer.parseInt(s.getShowtimeID()))
            .max().orElse(0) + 1);
    }
    
}