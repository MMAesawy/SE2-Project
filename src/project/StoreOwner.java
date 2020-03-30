package project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoreOwner extends project.User {
    public StoreOwner(String email, String username, String password) {
        super(email, username, password);
    }

    public StoreOwner(){
        super();
    }
}
