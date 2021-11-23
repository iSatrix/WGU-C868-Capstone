package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Josh Shepherd
 */

/** Used by the Login Menu to create PreparedStatement Objects. */
public class DatabaseQuery {
    
    /*
        ----------VARIABLES----------
    */
    
    private static PreparedStatement preparedStatement;
    
    /*
        ----------SETTERS----------
    */
            
    /** Create PreparedStatement Object.
     * @param conn Connection to be used.
     * @param sqlStatement String
     * @throws java.sql.SQLException SQLExceptions are caught in try-catch blocks in the rest of the application and will print a Stack Trace. */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
    
        preparedStatement = conn.prepareStatement(sqlStatement);
    
    }
    
    /*
        ----------GETTERS----------
    */
        
    /** Gets the PreparedStatement Object
     * @return  preparedStatement*/
    public static PreparedStatement getPreparedStatement() {
    
        return preparedStatement;
        
    }
    
}
