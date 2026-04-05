package cinema.models;

import cinema.services.DataHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Ivan
 */
public abstract class User extends Entity {     //polymorphism for diff roles
	public enum Role { MANAGER, CLERK, CUSTOMER }
	
	private String userID, name, email, password;
    private Role role;
	
	private static final String FILE_PATH = "data/users.txt";

    public User(String userID, String name, String email, String password, Role role) {
		super(FILE_PATH);
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
	// -- Getters -- 
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
	public String getRoleString() { return role.toString(); }
    
	// -- Data Logic	
	private static User fromFileString(String line) {       
		String[] p = line.split(",");
		
		String ID = p[0];
		String NAME = p[1];
		String EMAIL = p[2];
		String PASSWORD = p[3];
		Role ROLE = Role.valueOf(p[4].toUpperCase());

		return switch (ROLE) {
			case MANAGER -> new Manager(ID, NAME, EMAIL, PASSWORD);
			case CLERK -> new Clerk(ID, NAME, EMAIL, PASSWORD);
			case CUSTOMER -> new Customer(ID, NAME, EMAIL, PASSWORD);
		};
	}
	
	
	public static List<User> getAll() {
		return Entity.getAll(FILE_PATH, User::fromFileString);
	}
	
	public static Object[][] get2DArray() {
		List<User> all = getAll();
		return Entity.create2DArray(all, 5, (user, row) -> {
			row[0] = user.getUserID();
			row[1] = user.getName();
			row[2] = user.getEmail();
			row[3] = user.getPassword();
			row[4] = user.getRole();
		});
	}
	
	public void add(User user) {
		this.append();
	}
	
	public static void update(String id, User updated) {
		Entity.update(FILE_PATH, id, updated);
	}
	
	public static void delete(String id) {
		Entity.delete(FILE_PATH, id, getAll(), User::getUserID);
	}
	
    @Override
    public String toString() {
        return String.join(",", userID, name, email, password, getRoleString());
    }
	
	// Auxilliary Methods
	public static String generateNextID() { // TODO : Look into how this works
		return String.format("%03d", getAll().stream()
				.mapToInt(u -> Integer.parseInt(u.getUserID()))
				.max().orElse(0) + 1);
	}
	
	public static boolean isEmailTaken(String email) {
		return getAll().stream().anyMatch(u -> u.email.equalsIgnoreCase(email));
	}
	
	public static void registerCustomer(String name, String email, String password) {
		if (!isEmailTaken(email)) {
			new Customer(generateNextID(), name, email, password).append();
		}
	}
	
	public static User authenticate(String email, String password) {    // validates login credentials with stored user data
		return getAll().stream()
				.filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
				.findFirst().orElse(null);
	}
	
	public static User create(String id, String name, String email, String pass, Role role) {
		return switch (role) {
			case CUSTOMER -> new Customer(id, name, email, pass);
			case CLERK    -> new Clerk(id, name, email, pass);
			case MANAGER  -> new Manager(id, name, email, pass);
		};
	}
}

// Inheritee Models
class Manager extends User {
	public Manager(String userID, String name, String email, String password) {
		super(userID, name, email, password, Role.MANAGER);
	}
}

class Clerk extends User {
	public Clerk(String userID, String name, String email, String password) {
		super(userID, name, email, password, Role.CLERK);
	}
}

class Customer extends User {
    public Customer(String userID, String name, String email, String password) {
        super(userID, name, email, password, Role.CUSTOMER);
    }   
}