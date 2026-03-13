package cinema.services;

import cinema.models.*;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class AuthService {
	public static User authenticate(String email, String password) {
		UserManager um = new UserManager();
		for (User user : um.getAll()) {
			if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null; 
	}

}


