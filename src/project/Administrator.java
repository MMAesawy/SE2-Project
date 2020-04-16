package project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Administrator extends project.User {
    public Administrator(String email, String username, String password) {
        super(email, username, password);
    }

    public Administrator(){
        super();
    }
}
