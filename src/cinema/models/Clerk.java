package cinema.models;

/**
 *
 * @author Ivan
 */
public class Clerk extends User {

	public Clerk(String userID, String name, String email, String password) {
		super(userID, name, email, password, Role.CLERK);
	}
	
}
