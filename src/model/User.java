package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DatabaseConnection;

/**
 *
 * @author Josh Shepherd
 */

/** This class defines the User object.  It contains several 
 * methods that will be used by the controller classes.
 */
public class User {
    
    /*
        ----------VARIABLES----------
    */
    
    private int userID;
    private String userName;
    private String password;
    private static ObservableList<User> userList = FXCollections.observableArrayList();
    
    /*
        ----------CONSTRUCTORS----------
    */
    
    /** Primary constructor for the User object.
     * @param userID int
     * @param userName String
     * @param password String*/
    public User(int userID, String userName, String password) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
    }

    /** Additional constructor for the User object.
     * @param userID int
     * @param userName String*/
    public User(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }
        
    /*
        ----------SETTERS----------
    */

    /** Sets the userID. 
     * @param userID int*/
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** Sets the userName.
     * @param userName String*/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** Sets the password.
     * @param password String*/
    public void setPassword(String password) {
        this.password = password;
    }
    
    /*
        ----------GETTERS----------
    */

    /** Gets the userID.
     * @return userID*/
    public int getUserID() {
        return userID;
    }

    /** Gets the userName.
     * @return userName*/
    public String getUserName() {
        return userName;
    }

    /** Gets the password.
     * @return  password*/
    public String getPassword() {
        return password;
    }

    /** Gets a list of all Users from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return userList List of all the users from the database.*/
    public static ObservableList<User> getUserList() {
        
        clearUserList();
        
        try{
            String sql = "SELECT * FROM users";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User user = new User(userID, userName, password);
                userList.add(user);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        
        return userList;
    }
    
    
    /*
        ----------METHODS----------
    */
    
    /** Empties the userList.
     * @return  userList An empty list.*/
    private static ObservableList<User> clearUserList() {
    
        try{

            DatabaseConnection.getConnection();

            userList.removeAll(userList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return userList;
        
    }
    
    @Override
    public String toString() {
        return (userName);
    }
    
}
