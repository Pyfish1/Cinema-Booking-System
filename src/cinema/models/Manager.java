package cinema.models;

/**
 *
 * @author Ivan
 */
public class Manager extends User {

	public Manager(String userID, String name, String email, String password) {
		super(userID, name, email, password, Role.MANAGER);
	}

}
