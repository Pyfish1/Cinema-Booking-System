package cinema.services;

import cinema.models.*;
import java.util.List;

/**
 *
 * @author Ivan
 */
public class UserManager extends EntityManager<User> {
	public UserManager() {
		super("data/users.txt");
	}

	@Override
	protected User stringToObject(String line) {
		String[] p = line.split(",");
		
		String ID = p[0];
		String NAME = p[1];
		String EMAIL = p[2];
		String PASSWORD = p[3];
		Role role = Role.valueOf(p[4]);
		
		switch (role) {
			case MANAGER: return new Manager(ID, NAME, EMAIL, PASSWORD);
			case CLERK: return new Clerk(ID, NAME, EMAIL, PASSWORD);
			case CUSTOMER: return new Customer(ID, NAME, EMAIL, PASSWORD);
			default: return null; // Need to handle this properly.
		}
	}

	@Override
	protected String objectToString(User user) {
		return String.join(",", user.getUserID(), user.getName(), user.getEmail(), 
				user.getPassword(), user.getRole());
	}

	@Override
	protected String getEntityID(User user) {
		return user.getUserID();
	}
	
	public boolean isEmailTaken(String email) {
		List<User> allUsers = getAll();
		for (User user : allUsers) {
			if (user.getEmail().equalsIgnoreCase(email.trim())) {
				return true; // Email is taken
			}
		}
		return false; // Email is not taken
	}
	
	public String generateNextID() {
		List<User> allUsers = getAll();
		int maxID = 0;
		
		for (User user : allUsers) {
			try {
				String idString = user.getUserID();
				int IDNum = Integer.parseInt(idString);
				if (IDNum > maxID) {
					maxID = IDNum;
				}
				
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}
		
		String number = String.format("%03d", maxID + 1); 
		System.out.println(number);
		return number;
	}
	
	
	
	public void registerCustomer(String name, String email, String password) {
		String newID = generateNextID();
		Customer newCustomer = new Customer(newID, name, email, password);
		this.add(newCustomer);
	}

}
