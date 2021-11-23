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

/** This class defines the Contact object.  This object is used to populate appointment
 * combo boxes and for some list filtering. */
public class Contact {
    
    /*
        ----------VARIABLES----------
    */
    
    private int contactID;
    private String contactName;
    private String contactEmail;
    private static ObservableList<Contact> contactList = FXCollections.observableArrayList();
    
    /*
        ----------CONSTRUCTORS----------
    */

    /** Primary constructor for the Contact object.
     * @param contactID int
     * @param contactName String
     * @param contactEmail String*/
    public Contact(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /** Additional constructor for the Contact object.
     * @param contactID int
     * @param contactName String  */
    public Contact(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }
        
    /*
        ----------SETTERS----------
    */

    /** Sets the contactID.
     * @param contactID int*/
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Sets the contactName.
     * @param contactName String*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /** Sets the contactEmail.
     * @param contactEmail String*/
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    /*
        ----------GETTERS----------
    */
    
    /** Gets the contactID.
     * @return  contactID*/
    public int getContactID() {
        return contactID;
    }

    /** Gets the contactName.
     * @return  contactName*/
    public String getContactName() {
        return contactName;
    }

    /** Gets the contactEmail.
     * @return  contactEmail*/
    public String getContactEmail() {
        return contactEmail;
    }

    /** Gets a list of all contacts from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return  contactList */
    public static ObservableList<Contact> getContactList() {
        
        clearContactList();
        
        try{
            String sql = "SELECT * FROM contacts";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contact contact = new Contact(contactID, contactName, contactEmail);
                contactList.add(contact);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        
        return contactList;
    }
        
    /*
        ----------METHODS----------
    */
    
    /** Empties the contactList.
     * @return  contactList An empty list. */
    public static ObservableList<Contact> clearContactList() {
    
        try{

            DatabaseConnection.getConnection();

            contactList.removeAll(contactList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return contactList;
        
    }
    
    /** Overrides the toString() method to print the contactName in the combo boxes instead of a hash location of the contact object.*/
    @Override
    public String toString() {
        return (contactName);
    }
    
}
