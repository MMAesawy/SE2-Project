package project;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class UserManager {
    protected ArrayList<User> users;
    protected DatabaseManager dbManager;

    public UserManager(){
        dbManager = DatabaseManager.getInstance();
        loadUsers();
    }

    /**
     * Inserts the given user into the database
     * @param user the user object to register in the database
     */
    public void registerUser(User user){

    }

    /**
     * Deletes the given user from the database,
     * as well as everything relating to the user.
     * @param user the user to be deleted
     */
    public void deleteUser(User user){

    }

    /**
     * Checks if the username is available for use for a new user
     * @param username the username to check
     * @return whether or not the username is available
     */
    public boolean checkUsernameAvailability(String username){
        return false;
    }

    /**
     * Checks if the email is available for use for a new user
     * @param email the email to check
     * @return whether or not the email is available
     */
    public boolean checkEmailAvailability(String email){
        return false;
    }

    /**
     * Searches for and returns a User with a given identifier and password.
     * If the user does not exist or the identifier/password combo
     * is incorrect, returns Null.
     * @param identifier email/username of the User
     * @param password password of the User
     * @return the corresponding User object if it exists
     * and the password is correct, otherwise Null.
     */
    public User login(String identifier, String password){
        return null;
    }

    /**
     * Searches for and returns a User with a given identifier.
     * @param identifier email/username of the User
     * @return the corresponding User object if it exists, otherwise Null.
     */
    private User searchUsers(String identifier){
        return null;
    }

    /**
     * Checks whether the given identifier is an email (as opposed to a username).
     * @param identifier the identifier
     * @return True if the given identifier is an email, otherwise false.
     */
    private boolean isEmail(String identifier){
        return false;
    }

    /**
     * Checks whether the given email is valid (from a formatting perspective).
     * @param email the email to check.
     * @return whether or not it is a valid email string.
     */
    private boolean verifyEmail(String email){
        return false;
    }

    /**
     * Checks whether the give username is valid (from a formatting perspective).
     * @param username the username to check
     * @return whether or not it is a valid username string.
     */
    private boolean verifyUsername(String username){
        return false;
    }

    /**
     * Loads the users from the database into the ArrayList users object.
     */
    private void loadUsers(){

    }
}
