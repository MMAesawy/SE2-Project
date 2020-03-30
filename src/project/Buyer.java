package project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Buyer extends project.User {
    public Buyer(String email, String username, String password) {
        super(email, username, password);
    }

    public Buyer(){
        super();
    }
}
