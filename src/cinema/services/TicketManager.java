package cinema.services;

import cinema.models.Seat;
import cinema.models.Ticket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan
 */
public class TicketManager extends EntityManager<Ticket> {

    public TicketManager() {
        super("data/tickets.txt");
    }

    @Override
    protected Ticket stringToObject(String line) {
        String[] p = line.split(",");
        
        String seatData = p[3];
        List<Seat> seats = new ArrayList<>();
        
        if (!seatData.isEmpty()) {
            for (String s : seatData.split("\\|")) {
                String[] coord = s.split(":");
                if (coord.length == 2) {
                    seats.add(new Seat(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])));
                }
            }
        }
        return new Ticket(p[0], p[1], p[2], seats, Double.parseDouble(p[4]), p[5]);
    }

    @Override
    protected String objectToString(Ticket ticket) {
        return ticket.toString();
    }

    @Override
    protected String getEntityID(Ticket ticket) {
        return ticket.getTicketID();
    }
}