package controller;

import java.io.IOException;
import java.net.URL;
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
import utils.EmptyFieldInterface;

/**
 * FXML Controller class
 *
 * @author Josh Shepherd
 */

/** This FXML Controller class handles adding new appointments to the database. */
public class AddAppointmentController implements Initializable {
    
    /*
        ----------VARIABLES----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(5000));

    @FXML
    private TextField addAppointmentTitleTxt;

    @FXML
    private TextField addAppointmentDescriptionTxt;

    @FXML
    private TextField addAppointmentLocationTxt;

    @FXML
    private TextField addAppointmentTypeTxt;

    @FXML
    private ComboBox<Contact> addAppointmentContactComboBox;

    @FXML
    private ComboBox<User> addAppointmentUserIDComboBox;

    @FXML
    private ComboBox<Customer> addAppointmentCustomerIDComboBox;

    @FXML
    private DatePicker addAppointmentStartDatePicker;

    @FXML
    private DatePicker addAppointmentEndDatePicker;

    @FXML
    private ComboBox<String> addAppointmentStartHourComboBox;

    @FXML
    private ComboBox<String> addAppointmentStartMinuteComboBox;

    @FXML
    private ComboBox<String> addAppointmentStartAMPMComboBox;

    @FXML
    private ComboBox<String> addAppointmentEndHourComboBox;

    @FXML
    private ComboBox<String> addAppointmentEndMinuteComboBox;

    @FXML
    private ComboBox<String> addAppointmentEndAMPMComboBox;
    
    @FXML
    private Label addAppointmentErrorLabel;
    
    /*
        ----------ACTION EVENTS----------
    */
    
    /** Loads the Main Menu without adding a new appointment to the database. */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Saves a new appointment to the database when clicked after checking for any blank fields. */
    @FXML
    void onActionSaveAppointment(ActionEvent event) {
        
        System.out.println("Save appointment button clicked!");
        
        if(emptyFields.aFieldIsEmpty()) {
            
            addAppointmentErrorLabel.setText("One or more fields are empty!  Fill 'em up, Buttercup!");
            FADE_OUT.playFromStart();
            
        } else {
            
            String title = addAppointmentTitleTxt.getText();
            String description = addAppointmentDescriptionTxt.getText();
            String location = addAppointmentLocationTxt.getText();
            String type = addAppointmentTypeTxt.getText();
            
            // Format for LocalTime object
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            
            // Building a LocalDate and LocalTime to combine into a LocalDateTime object for startDateTime
            LocalDate startDate = addAppointmentStartDatePicker.getValue();
            
            String startHour = addAppointmentStartHourComboBox.getValue();
            String startMinute = addAppointmentStartMinuteComboBox.getValue();
            String startAMPM = addAppointmentStartAMPMComboBox.getValue();
            String startConcat = startHour + ":" + startMinute + " " + startAMPM;
            LocalTime startTime = LocalTime.parse(startConcat, formatter);
            
            ZoneId userLocal = ZoneId.of(TimeZone.getDefault().getID());
            
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            ZonedDateTime startDateTimeZDT = startDateTime.atZone(userLocal);
            ZonedDateTime startTimeToStoreInDB = startDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));
            
            // Building a LocalDate and LocalTime to combine into a LocalDateTime object for endDateTime
            LocalDate endDate = addAppointmentEndDatePicker.getValue();
            
            String endHour = addAppointmentEndHourComboBox.getValue();
            String endMinute = addAppointmentEndMinuteComboBox.getValue();
            String endAMPM = addAppointmentEndAMPMComboBox.getValue();
            String endConcat = endHour + ":" + endMinute + " " + endAMPM;
            LocalTime endTime = LocalTime.parse(endConcat, formatter);
            
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
            ZonedDateTime endDateTimeZDT = endDateTime.atZone(userLocal);
            ZonedDateTime endTimeToStoreInDB = endDateTimeZDT.withZoneSameInstant(ZoneId.of("UTC"));
                        
            // Customer, User, and Contact information
            int customerIDint = addAppointmentCustomerIDComboBox.getSelectionModel().getSelectedItem().getCustomerID();
            String customerID = String.valueOf(addAppointmentCustomerIDComboBox.getSelectionModel().getSelectedItem().getCustomerID());
            String userID = String.valueOf(addAppointmentUserIDComboBox.getSelectionModel().getSelectedItem().getUserID());
            String contactID = String.valueOf(addAppointmentContactComboBox.getSelectionModel().getSelectedItem().getContactID());
            String userName = String.valueOf(addAppointmentUserIDComboBox.getSelectionModel().getSelectedItem().getUserName());
            
            // Checking to see if the appointment falls outside of business hours, then checking for overlapping appointments, and then saving the new appointment if both return false.
            boolean outOfBusinessHours = false;
            boolean overlappingAppointment = false;
            if (Appointment.isOutsideBusinessHours(startDateTimeZDT, endDateTimeZDT)) {
                
                addAppointmentErrorLabel.setText("Operating business hours are 8:00 a.m. EST until 10:00 p.m. EST!");
                FADE_OUT.playFromStart();
                outOfBusinessHours = true;
                
            }
            
            if (Appointment.overlapsAnotherAppointment(Timestamp.valueOf(startDateTimeZDT.toLocalDateTime()), Timestamp.valueOf(endDateTimeZDT.toLocalDateTime()), customerIDint)) {

                addAppointmentErrorLabel.setText("Appointment overlaps with an existing appointment!");
                FADE_OUT.playFromStart();
                overlappingAppointment = true;
                
            } 
                            
            if (outOfBusinessHours == false && overlappingAppointment == false) {

                Appointment.saveNewAppointment(title, description, location, type, startTimeToStoreInDB.toLocalDateTime(), endTimeToStoreInDB.toLocalDateTime(), customerID, userID, contactID, userName);
                System.out.println("Appointment saved!");

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
        ----------INTIALIZER----------
    */
    
    /** Initializes the Add Appointment Menu by populating the User, Contact, Customer, and Time combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FADE_OUT.setNode(addAppointmentErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        ObservableList<User> userList = User.getUserList();
        addAppointmentUserIDComboBox.setItems(userList);
        
        ObservableList<Contact> contactList = Contact.getContactList();
        addAppointmentContactComboBox.setItems(contactList);
        
        ObservableList<Customer> customerList = Customer.getCustomerList();
        addAppointmentCustomerIDComboBox.setItems(customerList);
        
        setupTimeComboBoxes();
        
    }
    
    /*
        ----------METHODS----------
    */
    
    /** A lambda expression that checks if any required Add Appointment fields are missing data and returns true if they are empty. */
    EmptyFieldInterface emptyFields = () -> {
        if (addAppointmentTitleTxt.getText().isEmpty() || addAppointmentDescriptionTxt.getText().isEmpty() || addAppointmentLocationTxt.getText().isEmpty() || 
                addAppointmentTypeTxt.getText().isEmpty() || addAppointmentContactComboBox.getValue() == null || addAppointmentUserIDComboBox.getValue() == null ||
                addAppointmentCustomerIDComboBox.getValue() == null || addAppointmentStartDatePicker.getValue() == null || addAppointmentEndDatePicker.getValue() == null ||
                addAppointmentStartHourComboBox.getValue() == null || addAppointmentStartMinuteComboBox.getValue() == null || addAppointmentStartAMPMComboBox.getValue() == null ||
                addAppointmentEndHourComboBox.getValue() == null || addAppointmentEndMinuteComboBox.getValue() == null || addAppointmentEndAMPMComboBox.getValue() == null) {
            return true;
        } else {
            return false;
        }
    };
    
    /** Populates the startTime and endTime combo boxes. */
    private void setupTimeComboBoxes() {
    
        String[] hoursArray = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] minutesArray = {"00", "15", "30", "45"};
        String[] AMPMArray = {"AM", "PM"};
        
        addAppointmentStartHourComboBox.getItems().addAll(hoursArray);
        addAppointmentStartMinuteComboBox.getItems().addAll(minutesArray);
        addAppointmentStartAMPMComboBox.getItems().addAll(AMPMArray);
        
        addAppointmentEndHourComboBox.getItems().addAll(hoursArray);
        addAppointmentEndMinuteComboBox.getItems().addAll(minutesArray);
        addAppointmentEndAMPMComboBox.getItems().addAll(AMPMArray);
                
    } 
    
}
