package cinema.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Ivan
 */
public class Ticket {
	private String ticketID;
	private String customerID;
	private String showtimeID; // Which is linked to MovieID.
	private List<Seat> seats;
	private double totalPrice;
	private String bookingDate; // Timestamp
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
	
	// Auto Generate Timestamps
	public Ticket(String ticketID, String customerID, String showtimeID, List<Seat> seats, double totalPrice) {
        this(ticketID, customerID, showtimeID, seats, totalPrice, LocalDateTime.now().format(FORMATTER));
    }
	
	// Existing records
	public Ticket(String ticketID, String customerID, String showtimeID, List<Seat> seats, double totalPrice, String bookingDate) {
		this.ticketID = ticketID;
		this.customerID = customerID;
		this.showtimeID = showtimeID;
		this.seats = seats;
		this.totalPrice = totalPrice;
		this.bookingDate = bookingDate;
	}
	
	public String getReadableSeats() {
		return seats.stream().map(Seat::getLabel).collect(Collectors.joining(","));
	}
	
	public String getTicketID() { return ticketID; }
	public String getCustomerID() { return customerID; }
	public String getShowtimeID() { return showtimeID; }
	public List<Seat> getSeatList() { return seats; }
	public double getTotalPrice() { return totalPrice; }
	public String getTotalPriceString() { return String.valueOf(totalPrice); }
	public String getBookingDate() { return bookingDate; }
	
	public String seatsToString() {
		return seats.stream().map(Seat::toString).collect(Collectors.joining("|")); // WTF
	}
	
	@Override
    public String toString() {
        return String.join(",", 
            ticketID, 
            customerID, 
            showtimeID, 
            seatsToString(), 
            String.format("%.2f", totalPrice), 
            bookingDate
        );
    }
	
	// Ticket newTicket = new Ticket(
    //	ticketManager.generateNextID(),
    //	currentCustomer.getUserID(),
    //	selectedShowtime.getShowtimeID(),
    //	selectedSeatsList,
    //	calculatedPrice
	// );
	
	
}
