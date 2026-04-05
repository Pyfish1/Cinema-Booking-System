package cinema.debug;

import cinema.models.Entity;
import cinema.models.Showtime;
import cinema.services.DataHandler;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ivan
 */
public class DummyData {
	private static final String DATA_DIR = "data";
	private static final String USER_FILE = DATA_DIR  + "/users.txt";
	private static final String MOVIE_FILE = DATA_DIR + "/movies.txt";
	private static final String SHOWTIMES_FILE = DATA_DIR + "/showtimes.txt";
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
	// paths and formatter
	public static void ensureDirectoryExists() {    //this just makes sure that the data folder exist and creats it if not found
		File directory = new File(DATA_DIR);
		if (!directory.exists()) { directory.mkdir(); }				
	}
	
	public static void generateUserDummyData() {    //generates data in users.txt for debugging blah blah
		
		// SCHEMA : ID | NAME | EMAIL | PASSWORD | ROLE
		
		ensureDirectoryExists();
		
		System.out.println("Generating Dummy Data for users.txt");	
		
		List<String> users = new ArrayList<>();
        users.add("001,Ivan,ivanho@mail.com,admin,MANAGER");
        users.add("002,Ian,charlieclerk@mail.com,clerk,CLERK");
        users.add("003,Isaac,tanjiro@mail.com,customer,CUSTOMER");
        DataHandler.saveToFile(USER_FILE, users);
	}
	
	public static void generateMovieDummyData() {       //same as user but with movies instead
		
		// SCHEMA : ID, TITLE, GENRE, DURATION, RATING, STATUS, POSTERPATH
		// ENUM STATUS : SHOWING, UPCOMING, ARCHIVED
		
		ensureDirectoryExists();

        System.out.println("Generating Dummy Data for movies.txt");
        List<String> movies = new ArrayList<>();
		
		movies.add("001,Dune: Part Two,Sci-Fi,166,8.8,SHOWING,data/assets/posters/dune2.jpg");
		movies.add("002,The Dark Knight,Action,152,9.0,SHOWING,data/assets/posters/tdk.jpg");
		movies.add("003,Inside Out 2,Animation,96,7.9,SHOWING,data/assets/posters/insideout2.jpg");
		movies.add("004,Interstellar,Sci-Fi,169,8.7,SHOWING,data/assets/posters/interstellar.jpg");
		movies.add("005,Deadpool & Wolverine,Action,127,8.1,SHOWING,data/assets/posters/deadpool.jpg");
		movies.add("006,The Menu,Thriller,107,7.2,ARCHIVED,data/assets/posters/menu.jpg");
		movies.add("007,Beyond the Horizon,Adventure,135,0.0,UPCOMING,data/assets/posters/beyond.jpg");
		movies.add("008,Spirited Away,Fantasy,125,8.6,SHOWING,data/assets/posters/spirited.jpg");
		movies.add("009,A Quiet Place: Day One,Horror,99,6.8,SHOWING,data/assets/posters/quietplace.jpg");
		movies.add("010,Gladiator II,Action,150,0.0,SHOWING,data/assets/posters/gladiator2.jpg");
		movies.add("011,Everything Everywhere All At Once,Sci-Fi,139,7.8,SHOWING,data/assets/posters/eeaaow.jpg");
		movies.add("012,The Grand Budapest Hotel,Comedy,99,8.1,SHOWING,data/assets/posters/grandbudapest.jpg");
		movies.add("013,Oppenheimer,Biography,180,8.4,SHOWING,data/assets/posters/oppenheimer.jpg");
		movies.add("014,Mufasa: The Lion King,Animation,120,0.0,SHOWING,data/assets/posters/mufasa.jpg");
		movies.add("015,Parasite,Thriller,132,8.5,SHOWING,data/assets/posters/parasite.jpg");
        
        DataHandler.saveToFile(MOVIE_FILE, movies);

	}
	
	public static void generateShowtimeDummyData() {    // ts one quite cool, nested loop for the selected movies to make it so that 
        ensureDirectoryExists();                            // 12 random bookings everytime, but lowkey ts js for debugging
        System.out.println("Generating Showtimes...");
        
        List<Showtime> showtimes = new ArrayList<>();
        Random rand = new Random();
        String[] showingMovieIDs = {"001", "002", "003"}; // Use IDs from above
        String[] times = {"10:00", "13:00", "16:00", "19:00", "22:00"};
        
        int idCounter = 1;
        for (String movieID : showingMovieIDs) {
            for (String time : times) {
                String sID = String.format("%03d", idCounter++);
                int hall = (idCounter % 10) + 1;
                String dateStr = "26-03-16 " + time;
                
                // New constructor: Includes Price (25.0)
                Showtime s = new Showtime(sID, movieID, hall, dateStr, 25.0);
                
                // Randomly book 12 seats
                for (int i = 0; i < 12; i++) {
                    s.bookSeat(rand.nextInt(5), rand.nextInt(10));
                }
                showtimes.add(s);
            }
        }
        
        Entity.saveAll("data/showtimes.txt", showtimes);    //saves showtime objects
    }
	
			
}
