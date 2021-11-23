package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import utils.DatabaseConnection;
import utils.EmptyFieldInterface;

/**
 * FXML Controller class
 *
 * @author Josh Shepherd
 */

/** This FXML Controller handles modifying Appointment objects in the database. */
public class ModifyAppointmentController implements Initializable {
    
    /*
        ----------VARIABLES----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(5000));

     @FXML
    private TextField modifyAppointmentIDTxt;

    @FXML
    private TextField modifyAppointmentTitleTxt;

    @FXML
    private TextField modifyAppointmentDescriptionTxt;

    @FXML
    private TextField modifyAppointmentLocationTxt;

    @FXML
    private TextField modifyAppointmentTypeTxt;

    @FXML
    private ComboBox<Contact> modifyAppointmentContactComboBox;

    @FXML
    private ComboBox<User> modifyAppointmentUserIDComboBox;

    @FXML
    private ComboBox<Customer> modifyAppointmentCustomerIDComboBox;

    @FXML
    private DatePicker modifyAppointmentStartDatePicker;

    @FXML
    private DatePicker modifyAppointmentEndDatePicker;

    @FXML
    private ComboBox<String> modifyAppointmentStartHourComboBox;

    @FXML
    private ComboBox<String> modifyAppointmentStartMinuteComboBox;

    @FXML
    private ComboBox<String> modifyAppointmentStartAMPMComboBox;

    @FXML
    private ComboBox<String> modifyAppointmentEndHourComboBox;

    @FXML
    private ComboBox<String> modifyAppointmentEndMinuteComboBox;

    @FXML
    private ComboBox<String> modifyAppointmentEndAMPMComboBox;
    
    @FXML
    private Label modifyAppointmentErrorLabel;
    
    /*
        ----------ACTION EVENTS----------
    */
    
    /** Displays the Main Menu without updating an appointment. */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Updates an existing Appointment in the database and then loads the Main Menu. */
    @FXML
    void onActionModifyAppointment(ActionEvent event) {
        
        System.out.println("Modify appointment now!!");
        
        if(emptyFields.aFieldIsEmpty()) {
            
            modifyAppointmentErrorLabel.setText("One or more fields are empty!  Fill 'em up, Buttercup!");
            FADE_OUT.playFromStart();
            
        } else {
            
            String id = modifyAppointmentIDTxt.getText();
            String title = modifyAppointmentTitleTxt.getText();
            String description = modifyAppointmentDescriptionTxt.getText();
            String location = modifyAppointmentLocationTxt.getText();
            String type = modifyAppointmentTypeTxt.getText();
            
            // Format for LocalTime object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            
            // Building a LocalDate and LocalTime to combine into a LocalDateTime object for startDateTime
            LocalDate startDate = modifyAppointmentStartDatePicker.getValue();
            
            String startHour = modifyAppointmentStartHourComboBox.getValue();
            String startMinute = modifyAppointmentStartMinuteComboBox.getValue();
            String startAMPM = modifyAppointmentStartAMPMComboBox.getValue();
            String startConcat = startHour + ":" + startMinute + " " + startAMPM;
            LocalTime startTime = LocalTime.parse(startConcat, formatter);
            
            ZoneId userLocal = ZoneId.of(TimeZone.getDefault().getID());
            
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            ZonedDateTime startDateTimeZDT = startDateTime.atZone(userLocal);
            ZonedDateTime startTimeToStoreInDB = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));

            // Building a LocalDate and LocalTime to combine into a LocalDateTime object for endDateTime
            LocalDate endDate = modifyAppointmentEndDatePicker.getValue();
            
            String endHour = modifyAppointmentEndHourComboBox.getValue();
            String endMinute = modifyAppointmentEndMinuteComboBox.getValue();
            String endAMPM = modifyAppointmentEndAMPMComboBox.getValue();
            String endConcat = endHour + ":" + endMinute + " " + endAMPM;
            LocalTime endTime = LocalTime.parse(endConcat, formatter);
            
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            ZonedDateTime endDateTimeZDT = endDateTime.atZone(userLocal);
            ZonedDateTime endTimeToStoreInDB = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));
            
            // Customer, User, and Contact information
            int customerIDint = modifyAppointmentCustomerIDComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            String customerID = String.valueOf(modifyAppointmentCustomerIDComboBox.getSelectionModel().getSelectedItem().getCustomerID());
            String userID = String.valueOf(modifyAppointmentUserIDComboBox.getSelectionModel().getSelectedItem().getUserID());
            String contactID = String.valueOf(modifyAppointmentContactComboBox.getSelectionModel().getSelectedItem().getContactID());
            String userName = String.valueOf(modifyAppointmentUserIDComboBox.getSelectionModel().getSelectedItem().getUserName());

            // Checking to see if the appointment falls outside of business hours, then checking for overlapping appointments, and then saving the new appointment if both return false.
            boolean outOfBusinessHours = false;
            boolean overlappingAppointment = false;
            if (Appointment.isOutsideBusinessHours(startDateTimeZDT, endDateTimeZDT)) {
                
                modifyAppointmentErrorLabel.setText("Operating business hours are 8:00 a.m. EST until 10:00 p.m. EST!");
                FADE_OUT.playFromStart();
                outOfBusinessHours = true;
                
            } else if (Appointment.overlapsAnotherAppointment(Timestamp.valueOf(startDateTimeZDT.toLocalDateTime()), Timestamp.valueOf(endDateTimeZDT.toLocalDateTime()), customerIDint)) {

                modifyAppointmentErrorLabel.setText("Appointment overlaps with an existing appointment!");
                FADE_OUT.playFromStart();
                overlappingAppointment = true;

            } else if (outOfBusinessHours == false && overlappingAppointment == false) {

                Appointment.updateAppointment(id, title, description, location, type, startTimeToStoreInDB.toLocalDateTime(), endTimeToStoreInDB.toLocalDateTime(), customerID, userID, contactID, userName);
                System.out.println("Appointment modified!");
            
                try {

                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
                    stage.setScene(new Scene(scene));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            
            
            
            
        
        }

    }
    
    /*
        ----------INITIALIZER----------
    */
    
    /** Initializes the Modify Appointment Menu by populating the User, Contact, and Customer combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FADE_OUT.setNode(modifyAppointmentErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        ObservableList<User> userList = User.getUserList();
        modifyAppointmentUserIDComboBox.setItems(userList);
        
        ObservableList<Contact> contactList = Contact.getContactList();
        modifyAppointmentContactComboBox.setItems(contactList);
        
        ObservableList<Customer> customerList = Customer.getCustomerList();
        modifyAppointmentCustomerIDComboBox.setItems(customerList);
        
        setupTimeComboBoxes();
        
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Sends an Appointment object from the Main Menu Appointment Table to the Modify Appointment Menu.  Uses a PreparedStatement to feed the parameter variable into a SQL SELECT query.
     * @param appointment Appointment*/
    public void sendAppointment(Appointment appointment) {
        
        modifyAppointmentIDTxt.setText(String.valueOf(appointment.getAppointmentID()));
        modifyAppointmentTitleTxt.setText(appointment.getAppointmentTitle());
        modifyAppointmentDescriptionTxt.setText(appointment.getAppointmentDescription());
        modifyAppointmentLocationTxt.setText(appointment.getAppointmentLocation());
        modifyAppointmentTypeTxt.setText(appointment.getAppointmentTitle());
        
        String sql = "SELECT a.Appointment_ID, c.Contact_ID, c.Contact_Name, a.Start, a.End, cu.Customer_ID, cu.Customer_Name, u.User_ID, u.User_Name\n" +
                    "FROM appointments AS a\n" +
                    "INNER JOIN contacts AS c\n" +
                    "ON (a.Contact_ID = c.Contact_ID)\n" +
                    "INNER JOIN customers AS cu\n" +
                    "ON (a.Customer_ID = cu.Customer_ID)\n" +
                    "INNER JOIN users AS u\n" +
                    "ON (a.User_ID = u.User_ID)\n" +
                    "WHERE Appointment_ID = ?;";

        try {
        
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        
            ps.setString(1, String.valueOf(appointment.getAppointmentID()));

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
        
                String contactName = rs.getString("Contact_Name");
                int contactID = rs.getInt("Contact_ID");
                Contact c = new Contact(contactID, contactName);
                modifyAppointmentContactComboBox.setValue(c);
                
                String userName = rs.getString("User_Name");
                int userID = rs.getInt("User_ID");
                User u = new User(userID, userName);
                modifyAppointmentUserIDComboBox.setValue(u);
                
                String customerName = rs.getString("Customer_Name");
                int customerID = rs.getInt("Customer_ID");
                Customer cu = new Customer(customerID, customerName);
                modifyAppointmentCustomerIDComboBox.setValue(cu);
                                                
                LocalDateTime startDateTime = appointment.getAppointmentStartDateTime();
                ZonedDateTime startDateTimeUTC = startDateTime.atZone(ZoneId.systemDefault());
                LocalDate startDate = startDateTimeUTC.toLocalDate();
                LocalTime startTime = startDateTimeUTC.toLocalTime();
                String startHour = null;
                String startMinute = String.valueOf(startTime.getMinute());
                String startAMPM;
                
                // Sets startHour as String 1-12 because getHour returns an int of 0-23 from a LocalTime.
                switch (startTime.getHour()) {
                    case 0:
                    case 12:
                        startHour = "12";
                        break;
                    case 1:
                    case 13:
                        startHour = "01";
                        break;
                    case 2:
                    case 14:
                        startHour = "02";
                        break;
                    case 3:
                    case 15:
                        startHour = "03";
                        break;
                    case 4:
                    case 16:
                        startHour = "04";
                        break;
                    case 5:
                    case 17:
                        startHour = "05";
                        break;
                    case 6:
                    case 18:
                        startHour = "06";
                        break;
                    case 7:
                    case 19:
                        startHour = "07";
                        break;
                    case 8:
                    case 20:
                        startHour = "08";
                        break;
                    case 9:
                    case 21:
                        startHour = "09";
                        break;
                    case 10:
                    case 22:
                        startHour = "10";
                        break;
                    case 11:
                    case 23:
                        startHour = "11";
                        break;
                    default:
                        break;
                }
                
                if (startTime.getMinute() == 0) {
                    startMinute = "00";
                }
                
                if (startTime.getHour() >= 0 && startTime.getHour() <= 11) {
                    startAMPM = "AM";
                } else {
                    startAMPM = "PM";
                }
                
                modifyAppointmentStartDatePicker.setValue(startDate);
                modifyAppointmentStartHourComboBox.setValue(startHour);
                modifyAppointmentStartMinuteComboBox.setValue(startMinute);
                modifyAppointmentStartAMPMComboBox.setValue(startAMPM);
                                
                LocalDateTime endDateTime = appointment.getAppointmentEndDateTime();
                ZonedDateTime endDateTimeUTC = endDateTime.atZone(ZoneId.systemDefault());
                LocalDate endDate = endDateTimeUTC.toLocalDate();
                LocalTime endTime = endDateTimeUTC.toLocalTime();
                String endHour = null;
                String endMinute = String.valueOf(endTime.getMinute());
                String endAMPM;
                
                // Sets endHour as String 1-12 because getHour returns an int of 0-23 from a LocalTime.
                switch (endTime.getHour()) {
                    case 0:
                    case 12:
                        endHour = "12";
                        break;
                    case 1:
                    case 13:
                        endHour = "01";
                        break;
                    case 2:
                    case 14:
                        endHour = "02";
                        break;
                    case 3:
                    case 15:
                        endHour = "03";
                        break;
                    case 4:
                    case 16:
                        endHour = "04";
                        break;
                    case 5:
                    case 17:
                        endHour = "05";
                        break;
                    case 6:
                    case 18:
                        endHour = "06";
                        break;
                    case 7:
                    case 19:
                        endHour = "07";
                        break;
                    case 8:
                    case 20:
                        endHour = "08";
                        break;
                    case 9:
                    case 21:
                        endHour = "09";
                        break;
                    case 10:
                    case 22:
                        endHour = "10";
                        break;
                    case 11:
                    case 23:
                        endHour = "11";
                        break;
                    default:
                        break;
                }
                
                if (endTime.getMinute() == 0) {
                    endMinute = "00";
                }
                
                if (endTime.getHour() >= 0 && endTime.getHour() <= 11) {
                    endAMPM = "AM";
                } else {
                    endAMPM = "PM";
                }
                
                modifyAppointmentEndDatePicker.setValue(endDate);
                modifyAppointmentEndHourComboBox.setValue(endHour);
                modifyAppointmentEndMinuteComboBox.setValue(endMinute);
                modifyAppointmentEndAMPMComboBox.setValue(endAMPM);

            }
        
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
                        
    }
    
    /** Populates the StartTime and EndTime combo boxes. */
    private void setupTimeComboBoxes() {
    
        String[] hoursArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] minutesArray = {"00", "15", "30", "45"};
        String[] AMPMArray = {"AM", "PM"};
        
        modifyAppointmentStartHourComboBox.getItems().addAll(hoursArray);
        modifyAppointmentStartMinuteComboBox.getItems().addAll(minutesArray);
        modifyAppointmentStartAMPMComboBox.getItems().addAll(AMPMArray);
        
        modifyAppointmentEndHourComboBox.getItems().addAll(hoursArray);
        modifyAppointmentEndMinuteComboBox.getItems().addAll(minutesArray);
        modifyAppointmentEndAMPMComboBox.getItems().addAll(AMPMArray);
                
    } 
    
    /** A lambda expression that checks if any required Modify Appointment fields are missing data and returns true if they are empty. */
    EmptyFieldInterface emptyFields = () -> {
        if (modifyAppointmentTitleTxt.getText().isEmpty() || modifyAppointmentDescriptionTxt.getText().isEmpty() || modifyAppointmentLocationTxt.getText().isEmpty() || 
                modifyAppointmentTypeTxt.getText().isEmpty() || modifyAppointmentContactComboBox.getValue() == null || modifyAppointmentUserIDComboBox.getValue() == null ||
                modifyAppointmentCustomerIDComboBox.getValue() == null || modifyAppointmentStartDatePicker.getValue() == null || modifyAppointmentEndDatePicker.getValue() == null ||
                modifyAppointmentStartHourComboBox.getValue() == null || modifyAppointmentStartMinuteComboBox.getValue() == null || modifyAppointmentStartAMPMComboBox.getValue() == null ||
                modifyAppointmentEndHourComboBox.getValue() == null || modifyAppointmentEndMinuteComboBox.getValue() == null || modifyAppointmentEndAMPMComboBox.getValue() == null) {
            return true;
        } else {
            return false;
        }
    };
    
}
