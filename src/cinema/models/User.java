package cinema.models;

/**
 *
 * @author Ivan
 */
public abstract class User { // abstract because there is no "User" only roles that extend from User
    private String userID, name, email, password;
    private Role role;

    public User(String userID, String name, String email, String password, Role role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role.toString(); }
    
    @Override
    public String toString() {
        return String.join(",", userID, name, email, password, getRole());
    }
}
