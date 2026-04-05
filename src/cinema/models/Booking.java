package cinema.models;

import java.util.List;
import java.util.Arrays;

public class Booking extends Entity {   // stores booking data
    private String bookingID;
    private String userID;
    private String showtimeID;
    private String seats; // Stored as "A1;A2;A3"
    private double totalAmount;

    private static final String FILE_PATH = "data/bookings.txt";

    public Booking(String bookingID, String userID, String showtimeID, String seats, double totalAmount) {
        super(FILE_PATH);
        this.bookingID = bookingID;
        this.userID = userID;
        this.showtimeID = showtimeID;
        this.seats = seats;
        this.totalAmount = totalAmount;
    }

    // --- Data Logic ---

    public static List<Booking> getAll() {
        return Entity.getAll(FILE_PATH, Booking::fromFileString);
    }
    
    public static void delete(String id) {
        Entity.delete(FILE_PATH, id, getAll(), Booking::getBookingID);
    }
    
    public static void update(String id, Booking updated) {
        Entity.update(FILE_PATH, id, updated);
    }
    private static Booking fromFileString(String line) {
        String[] p = line.split(",");
        return new Booking(
            p[0], // BookingID
            p[1], // UserID
            p[2], // ShowtimeID;
            p[3], // Seats
            Double.parseDouble(p[4]) // TotalAmount
        );
    }
    

    @Override
    public String toString() {
        return String.join(",", 
            bookingID, 
            userID, 
            showtimeID, 
            seats, 
            String.valueOf(totalAmount)
        );
    }

    // --- Linking Logic (Cross-Model) ---
    public User getUser() {
        return User.getAll().stream()
                .filter(u -> u.getUserID().equals(this.userID))
                .findFirst().orElse(null);
    }

    public Showtime getShowtime() {
        return Showtime.getAll().stream()
                .filter(s -> s.getShowtimeID().equals(this.showtimeID))
                .findFirst().orElse(null);
    }

    // --- UI Helpers ---
    
    public static Object[][] get2DArray() {
        List<Booking> all = getAll();
            return Entity.create2DArray(all, 6, (b, row) -> {
            row[0] = b.getBookingID();
            row[1] = (b.getUser() != null) ? b.getUser().getName() : "Unknown";
            row[2] = (b.getShowtime() != null) ? b.getShowtime().getMovie().getTitle() : "N/A";
            row[3] = b.getSeats().replace(";", ", ");
            row[4] = (b.getShowtime() != null) ? b.getShowtime().getDateTime() : "N/A";
            row[5] = String.format("RM %.2f", b.getTotalAmount());
        });
}
    public static Object[][] get2DArray(String currentUserID) {
        List<Booking> userBookings = getAll().stream()
                .filter(b -> b.getUserID().equals(currentUserID))
                .toList();
                
        
        return Entity.create2DArray(userBookings, 5, (b, row) -> {
            Showtime s = b.getShowtime();
            Movie m = (s != null) ? s.getMovie() : null;
            User u = b.getUser();
            
            row[0] = b.bookingID;
            row[1] = (m != null) ? m.getTitle() : "Unknown Movie";
            row[2] = b.seats.replace(";", ", "); // Format "A1;A2" -> "A1, A2"
            row[3] = (s != null) ? s.getDateTime() : "N/A";
            row[4] = String.format("RM %.2f", b.totalAmount);
        });
    }

    public static String generateNextID() {
        return String.format("%03d", getAll().stream()
                .mapToInt(b -> Integer.parseInt(b.bookingID))
                .max().orElse(0) + 1);
    }
    

    // --- Getters ---
    public String getBookingID() { return bookingID; }
    public String getUserID() { return userID; }
    public String getShowtimeID() { return showtimeID; }
    public String getSeats() { return seats; }
    public double getTotalAmount() { return totalAmount; }
}