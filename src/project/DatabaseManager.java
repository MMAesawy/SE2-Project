package project;

import java.sql.*;

/**
 * Class for handling connecting and querying from the database.
 */
public class DatabaseManager {
    private Connection connection = null;
    private static DatabaseManager instance = null;

    private DatabaseManager(){

    }

    public static DatabaseManager getInstance(){
        if (instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public boolean isConnected(){
        return connection != null;
    }

    public void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }
}
