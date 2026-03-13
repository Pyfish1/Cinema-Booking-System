package cinema.debug;

import cinema.services.DataHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class DummyData {
	private static final String DATA_DIR = "data";
	private static final String USER_FILE = DATA_DIR  + "/users.txt";
	private static final String MOVIE_FILE = DATA_DIR + "/movies.txt";
	
	public static void ensureDirectoryExists() {
		File directory = new File(DATA_DIR);
		if (!directory.exists()) { directory.mkdir(); }				
	}
	
	public static void generateUserDummyData() {
		
		// SCHEMA : ID | NAME | EMAIL | PASSWORD | ROLE
		
		ensureDirectoryExists();
		
		System.out.println("Generating Dummy Data for users.txt");	
		
		List<String> users = new ArrayList<>();
        users.add("001,Ivan,admin,admin,MANAGER");
        users.add("002,Ian,clerk,clerk,CLERK");
        users.add("003,Isaac,customer,customer,CUSTOMER");
        DataHandler.saveToFile(USER_FILE, users);
	}
	
	public static void generateMovieDummyData() {
		
		// SCHEMA : ID, TITLE, GENRE, DURATION, RATING, STATUS, POSTERPATH
		// ENUM STATUS : SHOWING, UPCOMING, ARCHIVED
		
		ensureDirectoryExists();

        System.out.println("Generating Dummy Data for movies.txt");
        List<String> movies = new ArrayList<>();
		
		movies.add("001,Dune: Part Two,Sci-Fi,166,8.8,SHOWING,assets/posters/dune2.jpg");
		movies.add("002,The Dark Knight,Action,152,9.0,ARCHIVED,assets/posters/tdk.jpg");
		movies.add("003,Inside Out 2,Animation,96,7.9,SHOWING,assets/posters/insideout2.jpg");
		movies.add("004,Interstellar,Sci-Fi,169,8.7,ARCHIVED,assets/posters/interstellar.jpg");
		movies.add("005,Deadpool & Wolverine,Action,127,8.1,SHOWING,assets/posters/deadpool.jpg");
		movies.add("006,The Menu,Thriller,107,7.2,ARCHIVED,assets/posters/menu.jpg");
		movies.add("007,Beyond the Horizon,Adventure,135,0.0,UPCOMING,assets/posters/beyond.jpg");
		movies.add("008,Spirited Away,Fantasy,125,8.6,ARCHIVED,assets/posters/spirited.jpg");
		movies.add("009,A Quiet Place: Day One,Horror,99,6.8,SHOWING,assets/posters/quietplace.jpg");
		movies.add("010,Gladiator II,Action,150,0.0,UPCOMING,assets/posters/gladiator2.jpg");
		movies.add("011,Everything Everywhere All At Once,Sci-Fi,139,7.8,ARCHIVED,assets/posters/eeaaow.jpg");
		movies.add("012,The Grand Budapest Hotel,Comedy,99,8.1,ARCHIVED,assets/posters/grandbudapest.jpg");
		movies.add("013,Oppenheimer,Biography,180,8.4,SHOWING,assets/posters/oppenheimer.jpg");
		movies.add("014,Mufasa: The Lion King,Animation,120,0.0,UPCOMING,assets/posters/mufasa.jpg");
		movies.add("015,Parasite,Thriller,132,8.5,ARCHIVED,assets/posters/parasite.jpg");
        
        DataHandler.saveToFile(MOVIE_FILE, movies);

	}
	
			
}
