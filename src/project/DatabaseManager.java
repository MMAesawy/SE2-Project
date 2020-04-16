package project;

import java.sql.*;

/**
 * Class for handling connecting and querying from the database.
 */
public class DatabaseManager {
    final private static String CONNECTION_STRING = "jdbc:sqlite:/db/database.db";
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
        if (isConnected()) return; // already connected, don't reconnect.
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(CONNECTION_STRING);
            //connection.
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public void close(){
        if (isConnected()){
            try {
                connection.close();
            }
            catch ( SQLException e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                e.printStackTrace();
                System.exit(0);
            }
            connection = null;
        }
    }

    public void update(String query){
        update(query, true);
    }

    public void update(String query, boolean autoConnect){
        if (autoConnect) connect();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
        catch ( SQLException e ){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }
        //if (autoConnect) close();
        System.out.println("Executed " + query);
    }

    public ResultSet select(String query){
        return select(query, true);
    }

    public ResultSet select(String query, boolean autoConnect){
        if (autoConnect) connect();
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            stmt.closeOnCompletion();
        }
        catch ( SQLException e ){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
            System.exit(0);
        }
        //if (autoConnect) close();
        System.out.println("Executed " + query);
        return rs;
    }
}
