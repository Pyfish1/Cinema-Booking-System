package cinema.models;

/**
 *
 * @author Ivan
 */
public class Customer extends User {
    
    public Customer(String userID, String name, String email, String password) {
        super(userID, name, email, password, Role.CUSTOMER);
    }
    
}
