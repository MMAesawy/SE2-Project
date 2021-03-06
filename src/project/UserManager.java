package project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.*;

public class UserManager {
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private static UserManager instance;

    final public static Pattern EMAIL_REGEX = Pattern.compile(
            "[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                    "(?:\\.[a-z0-9!#$%&'*+/=" +
                    "?^_`{|}~-]+)*@(?:[a-z0-9](?:" +
                    "[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9]" +
                    "(?:[a-z0-9-]*[a-z0-9])?");

    final public static Pattern USERNAME_REGEX = Pattern.compile(
            "^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,29}$",
            Pattern.CASE_INSENSITIVE);

    public static UserManager getInstance(){
        if (instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    private UserManager(){}

    /**
     * Inserts the given user into the database
     * @param user the user object to register in the database
     */
    public void insertUser(User user){
        String type = UserFactory.getType(user);
        String query =
                String.format(
                        "INSERT INTO" +
                                " users (email, username, password, type)" +
                                " VALUES (\"%s\", \"%s\", \"%s\", \"%s\")"
                        , user.getEmail(), user.getUsername(), user.getPassword(), type);
        dbManager.update(query);
        dbManager.close();
    }

    public ArrayList<UserManagerError> registerUser(User user){
        ArrayList<UserManagerError> errorList = registrationCheck(user);
        if (errorList.isEmpty() || errorList.get(0) == UserManagerError.OK){
            insertUser(user);
        }
        return errorList;
    }

    private ArrayList<UserManagerError> registrationCheck(User user){
        ArrayList<UserManagerError> errorList = new ArrayList<>();
        if (!verifyUsername(user.getUsername())){
            errorList.add(UserManagerError.USERNAME_INVALID);
        }
        if (!checkUsernameAvailability(user.getUsername())){
            errorList.add(UserManagerError.USERNAME_NOT_AVAILABLE);
        }
        if (!verifyEmail(user.getEmail())){
            errorList.add(UserManagerError.EMAIL_INVALID);
        }
        if (!checkEmailAvailability(user.getEmail())){
            errorList.add(UserManagerError.EMAIL_NOT_AVAILABLE);
        }

        if (errorList.isEmpty()) errorList.add(UserManagerError.OK);
        return errorList;
    }

    /**
     * Deletes the given user from the database,
     * as well as everything relating to the user.
     * @param user the user to be deleted
     */
    public void deleteUser(User user){
        String query =
                String.format(
                        "DELETE FROM" +
                                " users WHERE" +
                                " email = \"%s\" AND username = \"%s\""
                        , user.getEmail(), user.getUsername());
        dbManager.update(query);
    }

    /**
     * Checks if the username is available for use for a new user
     * @param username the username to check
     * @return whether or not the username is available
     */
    public boolean checkUsernameAvailability(String username)
    {
        String query =
                String.format(
                        "SELECT * FROM users WHERE username = \"%s\""
                        , username);
        return !checkExists(query);
    }

    /**
     * Checks if the email is available for use for a new user
     * @param email the email to check
     * @return whether or not the email is available
     */
    public boolean checkEmailAvailability(String email){
        String query =
                String.format(
                        "SELECT * FROM users WHERE email = \"%s\""
                        , email);
        return !checkExists(query);
    }


    /**
     * Generic record checker. Checks if the given select query returns any results.
     * @param selectQuery the selection query
     * @return true if the query returns any results, false otherwise.
     */
    private boolean checkExists(String selectQuery){
        ResultSet result = dbManager.select(selectQuery);
        boolean available = false;
        try {
            // result.next() returns false if there are no records
            available = result.next();
            result.close();
            dbManager.close();
        }
        catch( SQLException e ){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }

        return available;
    }

    public ArrayList<User> getAllUsers(){
        String query = "SELECT * FROM users;";
        ResultSet result = dbManager.select(query);
        ArrayList<User> users = UserFactory.makeUsers(result);
        dbManager.close();
        return users;
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
        User user = searchUser(identifier);
        System.out.print("Login returned user: ");
        System.out.println((user != null ? user : "null"));
        if (user != null && user.checkPassword(password)){
            return user;
        }
        return null;
    }

    /**
     * Searches for and returns a User with a given identifier.
     * @param identifier email/username of the User
     * @return the corresponding User object if it exists, otherwise Null.
     */
    public User searchUser(String identifier){
        String clause;
        if (isEmail(identifier)){
            clause = String.format("email = \"%s\"", identifier);
        }
        else{
            clause = String.format("username = \"%s\"", identifier);
        }
        String query =
                String.format(
                        "SELECT email, username, password, type" +
                                " FROM users WHERE %s", clause);
        ResultSet result = dbManager.select(query);
        ArrayList<User> users = UserFactory.makeUsers(result);
        dbManager.close();
        if (users.isEmpty()){
            return null;
        }
        return users.get(0);
    }

    /**
     * Checks whether the given identifier is an email (as opposed to a username).
     * @param identifier the identifier
     * @return True if the given identifier is an email, otherwise false.
     */
    private boolean isEmail(String identifier){
        return EMAIL_REGEX.matcher(identifier).matches();
    }

    /**
     * Checks whether the given email is valid (from a formatting perspective).
     * @param email the email to check.
     * @return whether or not it is a valid email string.
     */
    private boolean verifyEmail(String email){
        return isEmail(email);
    }

    /**
     * Checks whether the give username is valid (from a formatting perspective).
     * @param username the username to check
     * @return whether or not it is a valid username string.
     */
    private boolean verifyUsername(String username){
        return USERNAME_REGEX.matcher(username).matches();
    }
}
