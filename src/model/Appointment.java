package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DatabaseConnection;

/**
 *
 * @author Josh Shepherd
 */

/** This class defines the Appointment object.  It contains several methods that
 * that will be used by the controller classes.*/
public class Appointment {
    
    /*
        ----------VARIABLES----------
    */
    
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private int appointmentContactID;
    private String appointmentType;
    private LocalDateTime appointmentStartDateTime;
    private LocalDateTime appointmentEndDateTime;
    private LocalDateTime appointmentCreationDateTime;
    private String appointmentCreatedBy;
    private LocalDateTime appointmentLastUpdatedDateTime;
    private String appointmentLastUpdatedBy;
    private int appointmentCustomerID;
    private int appointmentUserID;
    private String appointmentContactName;
    private static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> searchedAppointmentList = FXCollections.observableArrayList();
    
    /*
        ----------CONSTRUCTORS----------
    */

    /** Primary constructor for the Appointment object.
     * @param appointmentID int
     * @param appointmentTitle String
     * @param appointmentDescription String
     * @param appointmentLocation String
     * @param appointmentContactID String
     * @param appointmentType String
     * @param appointmentStartDateTime LocalDateTime
     * @param appointmentEndDateTime LocalDateTime
     * @param appointmentCreationDateTime LocalDateTime
     * @param appointmentCreatedBy String
     * @param appointmentLastUpdatedDateTime LocalDateTime
     * @param appointmentLastUpdatedBy String
     * @param appointmentCustomerID int
     * @param appointmentUserID int
     * @param appointmentContactName String*/
    public Appointment(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, int appointmentContactID,
            String appointmentType, LocalDateTime appointmentStartDateTime, LocalDateTime appointmentEndDateTime, LocalDateTime appointmentCreationDateTime,
            String appointmentCreatedBy, LocalDateTime appointmentLastUpdatedDateTime, String appointmentLastUpdatedBy, int appointmentCustomerID, int appointmentUserID,
            String appointmentContactName) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactID = appointmentContactID;
        this.appointmentType = appointmentType;
        this.appointmentStartDateTime = appointmentStartDateTime;
        this.appointmentEndDateTime = appointmentEndDateTime;
        this.appointmentCreationDateTime = appointmentCreationDateTime;
        this.appointmentCreatedBy = appointmentCreatedBy;
        this.appointmentLastUpdatedDateTime = appointmentLastUpdatedDateTime;
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentUserID = appointmentUserID;
        this.appointmentContactName = appointmentContactName;
    }    

    /** Private constructor for the Appointment object. Used to create the appointmentList ObservableList object. 
     * @param appointmentID int
     * @param appointmentTitle String
     * @param appointmentDescription String
     * @param appointmentLocation String
     * @param appointmentContactName String
     * @param appointmentType String
     * @param appointmentStartDateTime LocalDateTime
     * @param appointmentEndDateTime LocalDateTime
     * @param appointmentCustomerID int*/
    private Appointment(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentContactName, 
            String appointmentType, LocalDateTime appointmentStartDateTime, LocalDateTime appointmentEndDateTime, int appointmentCustomerID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactName = appointmentContactName;
        this.appointmentType = appointmentType;
        this.appointmentStartDateTime = appointmentStartDateTime;
        this.appointmentEndDateTime = appointmentEndDateTime;
        this.appointmentCustomerID = appointmentCustomerID;
    }

    /** Additional constructor for the Appointment object.
     * @param appointmentID int
     * @param appointmentType String
     * @param appointmentStartDateTime LocalDateTime
     * @param appointmentEndDateTime LocalDateTime*/
    public Appointment(int appointmentID, String appointmentType, LocalDateTime appointmentStartDateTime, LocalDateTime appointmentEndDateTime) {
        this.appointmentID = appointmentID;
        this.appointmentType = appointmentType;
        this.appointmentStartDateTime = appointmentStartDateTime;
        this.appointmentEndDateTime = appointmentEndDateTime;
    }
    
    /*
        ----------SETTERS----------
    */

    /** Sets the appointmentID.
     * @param appointmentID int*/
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /** Sets the appointmentTitle.
     * @param appointmentTitle String*/
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /** Sets the appointmentDescription.
     * @param appointmentDescription String*/
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /** Sets the appointmentLocation.
     * @param appointmentLocation String*/
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /** Sets the appointmentContactID.
     * @param appointmentContactID int*/
    public void setAppointmentContactID(int appointmentContactID) {
        this.appointmentContactID = appointmentContactID;
    }

    /** Sets the appointmentType.
     * @param appointmentType String*/
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /** Sets the appointmentStartDateTime.
     * @param appointmentStartDateTime LocalDateTime*/
    public void setAppointmentStartDateTime(LocalDateTime appointmentStartDateTime) {
        this.appointmentStartDateTime = appointmentStartDateTime;
    }

    /** Sets the appointmentEndDateTime.
     * @param appointmentEndDateTime LocalDateTime*/
    public void setAppointmentEndDateTime(LocalDateTime appointmentEndDateTime) {
        this.appointmentEndDateTime = appointmentEndDateTime;
    }

    /** Sets the appointmentCreationDateTime.
     * @param appointmentCreationDateTime LocalDateTime*/
    public void setAppointmentCreationDateTime(LocalDateTime appointmentCreationDateTime) {
        this.appointmentCreationDateTime = appointmentCreationDateTime;
    }

    /** Sets the appointmentCreatedBy.
     * @param appointmentCreatedBy String*/
    public void setAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
    }

    /** Sets the appointmentUpdatedDateTime.
     * @param appointmentLastUpdatedDateTime LocalDateTime*/
    public void setAppointmentLastUpdatedDateTime(LocalDateTime appointmentLastUpdatedDateTime) {
        this.appointmentLastUpdatedDateTime = appointmentLastUpdatedDateTime;
    }

    /** Sets the appointmentLastUpdatedBy.
     * @param appointmentLastUpdatedBy String*/
    public void setAppointmentLastUpdatedBy(String appointmentLastUpdatedBy) {
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
    }

    /** Sets the appointmentCustomerID.
     * @param appointmentCustomerID int*/
    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID = appointmentCustomerID;
    }

    /** Sets the appointmentUserID.
     * @param appointmentUserID int*/
    public void setAppointmentUserID(int appointmentUserID) {
        this.appointmentUserID = appointmentUserID;
    }

    /** Sets the appointmentContactName.
     * @param appointmentContactName String*/
    public void setAppointmentContactName(String appointmentContactName) {
        this.appointmentContactName = appointmentContactName;
    }
    
    /*
        ----------GETTERS----------
    */

    /** Gets the appointmentID.
     * @return  appointmentID*/
    public int getAppointmentID() {
        return appointmentID;
    }

    /** Gets the appointmentTitle.
     * @return appointmentTitle*/
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /** Gets the appointmentDescription.
     * @return  appointmentDescription*/
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /** Gets the appointmentLocation.
     * @return  appointmentLocation*/
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /** Gets the appointmentContactID.
     * @return  appointmentContactID*/
    public int getAppointmentContactID() {
        return appointmentContactID;
    }

    /** Gets the appointmentType.
     * @return  appointmentType*/
    public String getAppointmentType() {
        return appointmentType;
    }

    /** Gets the appointmentStartDateTime.
     * @return  appointmentStartDateTime*/
    public LocalDateTime getAppointmentStartDateTime() {
        return appointmentStartDateTime;
    }

    /** Gets the appointmentEndDateTime.
     * @return  appointmentEndDateTime*/
    public LocalDateTime getAppointmentEndDateTime() {
        return appointmentEndDateTime;
    }

    /** Gets the appointmentCreationDateTime.
     * @return  appointmentCreationDateTime*/
    public LocalDateTime getAppointmentCreationDateTime() {
        return appointmentCreationDateTime;
    }

    /** Gets the appointmentCreatedBy.
     * @return  appointmentCreatedBy*/
    public String getAppointmentCreatedBy() {
        return appointmentCreatedBy;
    }

    /** Gets the appointmentLastUpdatedDateTime.
     * @return  appointmentLastUpdatedDateTime*/
    public LocalDateTime getAppointmentLastUpdatedDateTime() {
        return appointmentLastUpdatedDateTime;
    }

    /** Gets the appointmentLastUpdatedBy.
     * @return  appointmentLastUpdatedBy*/
    public String getAppointmentLastUpdatedBy() {
        return appointmentLastUpdatedBy;
    }

    /** Gets the appointmentCustomerID.
     * @return  appointmentCustomerID*/
    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    /** Gets the appointmentUserID.
     * @return  appointmentUserID*/
    public int getAppointmentUserID() {
        return appointmentUserID;
    }

    /** Gets the appointmentContactName.
     * @return  appointmentContactName*/
    public String getAppointmentContactName() {
        return appointmentContactName;
    }

    /** Gets a list of all appointments from the database. Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return  appointmentList*/
    public static ObservableList<Appointment> getAppointmentList() {
        
        clearAppointmentList();
        
        try{
            String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name, a.Type, a.Start, a.End, a.Customer_ID\n" +
                        "FROM appointments AS a\n" +
                        "INNER JOIN contacts AS c\n" +
                        "ON (a.Contact_ID = c.Contact_ID);";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentContactName = rs.getString("Contact_Name");
                String appointmentType = rs.getString("Type");
                ZoneId userLocal = ZoneId.of(TimeZone.getDefault().getID());
                LocalDateTime appointmentStartDateTime = rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(userLocal).toLocalDateTime();
                LocalDateTime appointmentEndDateTime = rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(userLocal).toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                Appointment appointment = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentContactName,
                                    appointmentType, appointmentStartDateTime, appointmentEndDateTime, appointmentCustomerID);
                appointmentList.add(appointment);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
                
        return appointmentList;
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Saves a new appointment to the database.  Uses a PreparedStatement to feed the parameter variables into a SQL INSERT query.
     * @param appointmentTitle String
     * @param appointmentDescription String
     * @param appointmentLocation String
     * @param appointmentType String
     * @param appointmentStartDateTime LocalDateTime
     * @param appointmentEndDateTime LocalDateTime
     * @param appointmentCustomerID String
     * @param appointmentUserID String
     * @param appointmentContactID String
     * @param appointmentUserName String*/
    public static void saveNewAppointment(String appointmentTitle, String appointmentDescription, String appointmentLocation,
                                        String appointmentType, LocalDateTime appointmentStartDateTime, LocalDateTime appointmentEndDateTime, 
                                        String appointmentCustomerID, String appointmentUserID, String appointmentContactID, String appointmentUserName) {
    
        try {
            
            String sql = "INSERT INTO  appointments(Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, appointmentTitle);
            ps.setString(2, appointmentDescription);
            ps.setString(3, appointmentLocation);
            ps.setString(4, appointmentType);
            ps.setTimestamp(5, Timestamp.valueOf(appointmentStartDateTime));
            ps.setTimestamp(6, Timestamp.valueOf(appointmentEndDateTime));
            ps.setString(7, appointmentUserName);
            ps.setString(8, appointmentUserName);
            ps.setString(9, appointmentCustomerID);
            ps.setString(10, appointmentUserID);
            ps.setString(11, appointmentContactID);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }

    }
    
    /** Updates an existing appointment in the database.  Uses a PreparedStatement to feed the parameter variables into a SQL UPDATE query.
     * @param appointmentID String
     * @param appointmentTitle String
     * @param appointmentDescription String
     * @param appointmentLocation String
     * @param appointmentType String
     * @param appointmentStartDateTime LocalDateTime
     * @param appointmentEndDateTime LocalDateTime
     * @param appointmentCustomerID String
     * @param appointmentUserID String
     * @param appointmentContactID String
     * @param appointmentUserName String*/
    public static void updateAppointment(String appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation,
                                        String appointmentType, LocalDateTime appointmentStartDateTime, LocalDateTime appointmentEndDateTime, 
                                        String appointmentCustomerID, String appointmentUserID, String appointmentContactID, String appointmentUserName) {
    
        try {
            
            String sql = "UPDATE appointments\n" + 
                        "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Created_By = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?\n" +
                        "WHERE Appointment_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, appointmentTitle);
            ps.setString(2, appointmentDescription);
            ps.setString(3, appointmentLocation);
            ps.setString(4, appointmentType);
            ps.setTimestamp(5, Timestamp.valueOf(appointmentStartDateTime));
            ps.setTimestamp(6, Timestamp.valueOf(appointmentEndDateTime));
            ps.setString(7, appointmentUserName);
            ps.setString(8, appointmentUserName);
            ps.setString(9, appointmentCustomerID);
            ps.setString(10, appointmentUserID);
            ps.setString(11, appointmentContactID);
            ps.setString(12, appointmentID);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
        
    }
    
    /** Delete an appointment from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL DELETE query.
     * @param appointmentID int*/
    public static void deleteAppointment(int appointmentID) {
    
        try {
            
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, String.valueOf(appointmentID));
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
    
    }
    
    /** Empties the appointmentList.
     * @return  appointmentList An empty list.*/
    public static ObservableList<Appointment> clearAppointmentList() {
    
        try{

            DatabaseConnection.getConnection();

            appointmentList.removeAll(appointmentList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return appointmentList;
        
    }
    
    /** Checks to see if a selected appointment overlaps with any appointments in the database.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @param start Timestamp
     * @param end Timestamp
     * @param customerID int
     * @return  boolean Returns true if there is an overlap and false if there isn't.*/
    public static boolean overlapsAnotherAppointment(Timestamp start, Timestamp end, int customerID) {
    
        try {
        
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ps.setString(1, String.valueOf(customerID));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timestamp startToCheckAgainst = rs.getTimestamp("Start");
                Timestamp endToCheckAgainst = rs.getTimestamp("End");
                
                if ((start.before(startToCheckAgainst) && end.after(endToCheckAgainst)) ||
                        (start.before(startToCheckAgainst) && end.after(startToCheckAgainst) && end.before(endToCheckAgainst)) ||
                        (start.after(startToCheckAgainst) && start.before(endToCheckAgainst) && end.after(endToCheckAgainst)) ||
                        (start.after(startToCheckAgainst) && end.before(endToCheckAgainst))) {
                    return true;
                }
            
            }
                
        } catch (SQLException e) {
            
            e.printStackTrace();
            
        }
        
        return false;
    
    }
    
    /** Checks to see if an appointment is scheduled outside of business operating hours, which are defined as 8:00 AMEST and 10:00 PM EST.
     * @param startDateTimeUTC ZonedDateTime
     * @param endDateTimeUTC ZonedDateTime
     * @return  boolean Returns true if the appointment is outside of operating hours and false if it isn't.*/
    public static boolean isOutsideBusinessHours(ZonedDateTime startDateTimeUTC, ZonedDateTime endDateTimeUTC) {
    
        if (startDateTimeUTC.withZoneSameInstant(ZoneId.of("US/Eastern")).toLocalTime().isBefore(LocalTime.of(8, 0)) ||
                startDateTimeUTC.withZoneSameInstant(ZoneId.of("US/Eastern")).toLocalTime().isAfter(LocalTime.of(22, 0)) ||
                endDateTimeUTC.withZoneSameInstant(ZoneId.of("US/Eastern")).toLocalTime().isBefore(LocalTime.of(8, 0)) ||
                endDateTimeUTC.withZoneSameInstant(ZoneId.of("US/Eastern")).toLocalTime().isAfter(LocalTime.of(22, 0))) {
            return true;
        }
        
        return false;
        
    }
    
    /** Creates a filteredAppointmentList that is filtered by contactID.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @param contactID int
     * @return  filterAppointmentList*/
    public static ObservableList<Appointment> filterAppointmentsByContact(int contactID) {
        
        clearFilteredAppointmentList();
        
        try{
            String sql = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location, c.Contact_Name, a.Type, a.Start, a.End, a.Customer_ID\n" +
                        "FROM appointments AS a\n" +
                        "INNER JOIN contacts AS c\n" +
                        "ON (a.Contact_ID = c.Contact_ID)" +
                        "WHERE a.Contact_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ps.setString(1, String.valueOf(contactID));
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDescription = rs.getString("Description");
                String appointmentLocation = rs.getString("Location");
                String appointmentContactName = rs.getString("Contact_Name");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStartDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEndDateTime = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCustomerID = rs.getInt("Customer_ID");
                Appointment appointment = new Appointment(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentContactName,
                                    appointmentType, appointmentStartDateTime, appointmentEndDateTime, appointmentCustomerID);
                filteredAppointmentList.add(appointment);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        
        return filteredAppointmentList;
    }
    
    /** Searches through all existing appointments based on a user entered search criteria and stores the results in an ObservableList.
     * @param criteriaToSearch
     * @return 
     */
    public static ObservableList<Appointment> searchAppointments(String criteriaToSearch) {
        clearSearchedAppointmentList();
        
        for (Appointment a : getAppointmentList()) {
            if (a.getAppointmentTitle().contains(criteriaToSearch) || 
                    a.getAppointmentDescription().contains(criteriaToSearch) ||
                    a.getAppointmentLocation().contains(criteriaToSearch) ||
                    a.getAppointmentContactName().contains(criteriaToSearch) || 
                    a.getAppointmentType().contains(criteriaToSearch)) {
                searchedAppointmentList.add(a);
            }
        }
        
        if (searchedAppointmentList.isEmpty()) {
            return appointmentList;
        } else {
            return searchedAppointmentList;
        }
    }
    
    /** Empties the searchedAppointmentList.
     * @return 
     */
    public static ObservableList<Appointment> clearSearchedAppointmentList() {
        searchedAppointmentList.removeAll(searchedAppointmentList);
        
        return searchedAppointmentList;
    }
    
    /** Empties the filteredAppointmentList.
     * @return  filteredAppointmentList An empty list.*/
    public static ObservableList<Appointment> clearFilteredAppointmentList() {
    
        try{

            DatabaseConnection.getConnection();

            filteredAppointmentList.removeAll(filteredAppointmentList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return filteredAppointmentList;
        
    }
    
    
    
}
