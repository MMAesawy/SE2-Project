package project;

public class Privilege {
    private String username;
    private AccessType accessType;

    public Privilege(String username, AccessType accessType) {
        this.username = username;
        this.accessType = accessType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }
}
