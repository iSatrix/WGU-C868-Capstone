package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Josh Shepherd
 */

/** This class is used throughout the application to open and close a connection
 * with the database.  It also provides a convenient getConnection() method
 * to avoid constantly creating a new connection and allow the program to run faster.
 */
public class DatabaseConnection {
    
    /*
        ----------VARIABLES----------
    */
    
    // JDBC URL parts
    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR_NAME = ":mysql:";
    private static final String DATABASE_LOCATION = "//localhost:3306/client_schedule?useLegacyDatetimeCode=false&serverTimeZone=UTC";
    
    // JDBC URL concatenated
    private static final String DATABASE_URL = PROTOCOL + VENDOR_NAME + DATABASE_LOCATION;
    
    // Driver and Connection Interface Reference
    //private static final String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;
    
    // Database User Name and Password
    private static final String USER_NAME = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";
    
    /*
        ----------METHODS----------
    */
    
    /** Opens the initial database connection.  This method is only called in the Main method.
     * @return conn A connection object.*/
    public static Connection connectToDatabase() {
        
        try {
            //Class.forName(DATABASE_DRIVER);
            conn = (Connection) DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
            System.out.println("Connection successful!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        }
        
        return conn;
        
    }
    
    /** Used to get the connection after the initial connection has been made. 
     * @return conn A connection object.*/
    public static Connection getConnection() {
    
        return conn;
        
    }
    
    /** Ends the connection with the database.  Is only called when the application is terminated.*/
    public static void killConnection() {
        
        try {
            conn.close();
            System.out.println("Connection terminated.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            //System.out.println(ex.getMessage());
        }
        
    }
}
