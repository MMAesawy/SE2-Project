package project;

public abstract class User {
    protected String email;
    protected String username;
    protected String password;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Checks if the given password matches this user's actual password.
     * @param password the password to check
     * @return whether or not the given password matches this user's actual password.
     */
    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
