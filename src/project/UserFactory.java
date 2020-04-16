package project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserFactory {
    final public static String BUYER_TYPE = "buyer";
    final public static String OWNER_TYPE = "storeowner";
    final public static String ADMIN_TYPE = "admin";

    private UserFactory(){}

    public static User makeUser(String email, String username, String password, String type){
        switch (type) {
            case BUYER_TYPE:
                return new Buyer(email, username, password);
            case OWNER_TYPE:
                return new StoreOwner(email, username, password);
            case ADMIN_TYPE:
                return new Administrator(email, username, password);
            default:
                return null;
        }
    }

    public static ArrayList<User> makeUsers(ResultSet resultSet){
        ArrayList<User> users = new ArrayList<>();
        try {
            while(resultSet.next()){
                String email = resultSet.getString(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                String type = resultSet.getString(4);
                users.add(makeUser(email, username, password, type));
            }


            resultSet.close();
        }
        catch( SQLException e ){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }
        return users;
    }

    public static String getType(User user){
        if (user instanceof Buyer)
            return BUYER_TYPE;
        else if (user instanceof StoreOwner)
            return OWNER_TYPE;
        else if (user instanceof Administrator)
            return ADMIN_TYPE;
        else return null;
    }
}
