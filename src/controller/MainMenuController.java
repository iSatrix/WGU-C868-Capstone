package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Appointment;
import static model.Appointment.searchAppointments;
import model.Contact;
import model.Customer;
import static model.Customer.searchCustomers;
import utils.DatabaseConnection;

/**
 * FXML Controller class
 *
 * @author Josh Shepherd
 */

/** This FXML Controller class functions as the central hub for the application by
 * providing tables to display Customer and Appointment information as well as various
 * controls for generating reports.  */
public class MainMenuController implements Initializable {
    
    /*
        ----------VARIABLES----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(10000));
    private final FadeTransition FADE_OUT_A = new FadeTransition(Duration.millis(10000));
    private static ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList();
    
    
     @FXML
    private Button mainCustomerSearchBtn;

    @FXML
    private TextField mainCustomerSearchText;
    
    @FXML
    private Button mainAppointmentSearchBtn;

    @FXML
    private TextField mainAppointmentSearchText;
    
    @FXML
    private TableView<Customer> mainCustomerTableView;

    @FXML
    private TableColumn<Customer, Integer> mainCustomerIDCol;

    @FXML
    private TableColumn<Customer, String> mainCustomerNameCol;

    @FXML
    private TableColumn<Customer, String> mainCustomerAddressCol;

    @FXML
    private TableColumn<Customer, String> mainCustomerPostalCodeCol;

    @FXML
    private TableColumn<Customer, String> mainCustomerDivisionCol;

    @FXML
    private TableColumn<Customer, String> mainCustomerPhoneCol;
    
    @FXML
    private Label mainCustomerTableErrorLabel;

    @FXML
    private TableView<Appointment> mainAppointmentTableView;

    @FXML
    private TableColumn<Appointment, Integer> mainAppointmentIDCol;

    @FXML
    private TableColumn<Appointment, String> mainAppointmentTitleCol;

    @FXML
    private TableColumn<Appointment, String> mainAppointmentDescriptionCol;

    @FXML
    private TableColumn<Appointment, String> mainAppointmentLocationCol;

    @FXML
    private TableColumn<Appointment, String> mainAppointmentContactCol;

    @FXML
    private TableColumn<Appointment, String> mainAppointmentTypeCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> mainAppointmentStartTimeCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> mainAppointmentEndTimeCol;

    @FXML
    private TableColumn<Appointment, Integer> mainAppointmentCustomerIDCol;
    
    @FXML
    private Label mainAppointmentTableErrorLabel;
    
    @FXML
    private Label upcomingAppointmentsLabel;
    
    @FXML
    private ComboBox<Contact> contactsScheduleComboBox;
    
    @FXML
    private ComboBox<String> appointmentTypeComboBox;

    @FXML
    private ComboBox<String> appointmentMonthComboBox;
    
    @FXML
    private Label numberOfAppointmentsLabel;
    
    @FXML
    private Label numberOfTypeMonthAppointsLabel;
        
    /*
        ----------ACTION EVENTS----------
    */
    
    /**  Searches through appointments based on user entered text.
     */
    @FXML
    void onActionSearchAppointments(ActionEvent event) {
        String searchCriteria = mainAppointmentSearchText.getText();
        mainAppointmentTableView.setItems(searchAppointments(searchCriteria));
    }

    /**  Searches through customers based on user entered text.
     */
    @FXML
    void onActionSearchCustomers(ActionEvent event) {
        String searchCriteria = mainCustomerSearchText.getText();
        mainCustomerTableView.setItems(searchCustomers(searchCriteria));
    }
    
    /** Deletes an appointment from the database when clicked by calling the deleteAppointment() method from the Appointment class 
     * after checking to see if an Appointment has been selected from the table. */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) {
        
        Appointment selectedAppointment = mainAppointmentTableView.getSelectionModel().getSelectedItem();
        
        if (selectedAppointment == null) {
            
            mainAppointmentTableErrorLabel.setText("You must select an appointment to delete.");
            FADE_OUT_A.playFromStart();
            
        } else {
        
            System.out.println("Appointment deleted!");

            Appointment appointmentToDelete = mainAppointmentTableView.getSelectionModel().getSelectedItem();
            int appointmentID = appointmentToDelete.getAppointmentID();

            Appointment.deleteAppointment(appointmentID);
            
            String id = String.valueOf(appointmentID);
            String type = appointmentToDelete.getAppointmentType();

            mainAppointmentTableView.setItems(Appointment.getAppointmentList());
            mainAppointmentTableErrorLabel.setText("Appointment ID: " + id + ", of Type: " + type + ", has been canceled.");
            FADE_OUT_A.playFromStart();
            
        }

    }
    
    /** Deletes a customer from the database when clicked by calling the deleteCustomer() method from the Customer class 
     * after checking to see if a Customer has been selected from the table. */
    @FXML
    void onActionDeleteCustomer(ActionEvent event) {
        
        Customer selectedCustomer = mainCustomerTableView.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) {
            
            mainCustomerTableErrorLabel.setText("You must select a customer to delete.");
            FADE_OUT.playFromStart();
            
        } else {
        
            System.out.println("Customer deleted!");

            Customer customerToDelete = mainCustomerTableView.getSelectionModel().getSelectedItem();
            int customerID = customerToDelete.getCustomerID();

            Customer.deleteCustomer(customerID);

            mainCustomerTableView.setItems(Customer.getCustomerList());
            mainAppointmentTableView.setItems(Appointment.getAppointmentList());
            mainCustomerTableErrorLabel.setText("Customer has been deleted.  There's no turning back.");
            FADE_OUT.playFromStart();
            
        }

    }

    /** Loads the Add Appointment Menu when clicked. */
    @FXML
    void onActionDisplayAddAppointmentMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Add Appointment");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Loads the Add Customer Menu when clicked. */
    @FXML
    void onActionDisplayAddCustomerMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Add Customer");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Loads the Modify Appointment Menu when clicked after checking to see if an Appointment has been selected. */
    @FXML
    void onActionDisplayModifyAppointmentMenu(ActionEvent event) throws IOException {
        
        Appointment selectedAppointment = mainAppointmentTableView.getSelectionModel().getSelectedItem();
        
        if(selectedAppointment == null) {
        
            mainAppointmentTableErrorLabel.setText("You must select an appointment to modify.");
            FADE_OUT_A.playFromStart();
        
        } else {
        
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyAppointment.fxml"));
            loader.load();

            ModifyAppointmentController MAController = loader.getController();
            MAController.sendAppointment(selectedAppointment);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent loaderScene = loader.getRoot();
            stage.setTitle("APPOINTMENT SCHEDULER - Modify Appointment");
            stage.setScene(new Scene(loaderScene));
            stage.show();

        }
        
    }

    /** Loads the Modify Customer Menu when clicked after checking if a Customer has been selected. */
    @FXML
    void onActionDisplayModifyCustomerMenu(ActionEvent event) throws IOException {
        
        Customer selectedCustomer = mainCustomerTableView.getSelectionModel().getSelectedItem();
        
        if(selectedCustomer == null) {
        
            mainCustomerTableErrorLabel.setText("You must select a customer to modify.");
            FADE_OUT.playFromStart();
        
        } else {
        
            

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyCustomer.fxml"));
            loader.load();

            ModifyCustomerController MCController = loader.getController();
            MCController.sendCustomer(selectedCustomer);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent loaderScene = loader.getRoot();
            stage.setTitle("APPOINTMENT SCHEDULER - Modify Customer");
            stage.setScene(new Scene(loaderScene));
            stage.show();

        }
        
    }

    /** Terminates the connection with the database and closes the application when clicked. */
    @FXML
    void onActionExit(ActionEvent event) {
        
        DatabaseConnection.killConnection();
        System.exit(0);
        
    }
    
    /** Displays all scheduled appointments in the Appointments Table when selected. */
    @FXML
    void showAllApppointments(ActionEvent event) {
        
        mainAppointmentTableView.setItems(Appointment.getAppointmentList());

    }

    /** Displays all appointments scheduled for the current month in the Appointments Table when selected. */
    @FXML
    void showAppointmentsThisMonth(ActionEvent event) {

        ObservableList<Appointment> appointments = Appointment.getAppointmentList();
        filterAppointmentsByMonth(appointments);
        mainAppointmentTableView.setItems(filteredAppointmentList);
        
    }

    /** Displays all appointments scheduled for the current week in the Appointments Table when selected. */
    @FXML
    void showAppointmentsThisWeek(ActionEvent event) {

        ObservableList<Appointment> appointments = Appointment.getAppointmentList();
        filterAppointmentsByWeek(appointments);
        mainAppointmentTableView.setItems(filteredAppointmentList);
        
    }
    
    /** Runs the report to count how many appointments exist for a given appointmentType AND month when clicked. */
    @FXML
    void showNumberOfTypeMonthAppointments(ActionEvent event) {
        
        numberOfTypeMonthAppointsLabel.setText(String.valueOf(getNumberOfAppointmentsByTypeAndMonth()));

    }
    
    /** Runs the report to count how many appointments are currently scheduled in total when clicked. */
    @FXML
    void showNumberOfAppointments(ActionEvent event) {
        
        numberOfAppointmentsLabel.setText(String.valueOf(getNumberOfAppointments()));

    }
    
    /** Runs the report to filter the Appointments Table View to show the schedule for a given Contact selected from the combo box. */
    @FXML
    void showContactSchedule(ActionEvent event) {
        
        int contactID = contactsScheduleComboBox.getSelectionModel().getSelectedItem().getContactID();
        mainAppointmentTableView.setItems(Appointment.filterAppointmentsByContact(contactID));

    }

    /*
        ----------INITIALIZER----------
    */
    
    /** Initializes the Main Menu by populating all table views and combo boxes.  Also checks to see if any appointments are scheduled within 15 minutes of the User's login. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ObservableList<Appointment> appointments = Appointment.getAppointmentList();
        
        for (Appointment appointment : appointments) {
            
            long minutesUntilAppointment = ChronoUnit.MINUTES.between(LocalDateTime.now(), appointment.getAppointmentStartDateTime());
            LocalTime startTime = appointment.getAppointmentStartDateTime().toLocalTime();
            LocalDate startDate = appointment.getAppointmentStartDateTime().toLocalDate();
            String appointmentID = String.valueOf(appointment.getAppointmentID());
            
            if (minutesUntilAppointment >= 0 && minutesUntilAppointment <= 15) {
                
                upcomingAppointmentsLabel.setText("Appointment ID: " + appointmentID + " begins at " + startTime + " on " + startDate + ".");
                
            }
            
        }
        
        FADE_OUT.setNode(mainCustomerTableErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        FADE_OUT_A.setNode(mainAppointmentTableErrorLabel);
        FADE_OUT_A.setFromValue(1.0);
        FADE_OUT_A.setToValue(0.0);
        FADE_OUT_A.setCycleCount(1);
        FADE_OUT_A.setAutoReverse(false);
        
        mainCustomerTableView.setItems(Customer.getCustomerList());
        
        mainCustomerIDCol.setCellValueFactory(new PropertyValueFactory("customerID"));
        mainCustomerNameCol.setCellValueFactory(new PropertyValueFactory("customerName"));
        mainCustomerAddressCol.setCellValueFactory(new PropertyValueFactory("customerAddress"));
        mainCustomerPostalCodeCol.setCellValueFactory(new PropertyValueFactory("customerPostalCode"));
        mainCustomerDivisionCol.setCellValueFactory(new PropertyValueFactory("customerDivisionName"));
        mainCustomerPhoneCol.setCellValueFactory(new PropertyValueFactory("customerPhone"));
        
        mainAppointmentTableView.setItems(Appointment.getAppointmentList());
        
        mainAppointmentIDCol.setCellValueFactory(new PropertyValueFactory("appointmentID"));
        mainAppointmentTitleCol.setCellValueFactory(new PropertyValueFactory("appointmentTitle"));
        mainAppointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory("appointmentDescription"));
        mainAppointmentLocationCol.setCellValueFactory(new PropertyValueFactory("appointmentLocation"));
        mainAppointmentContactCol.setCellValueFactory(new PropertyValueFactory("appointmentContactName"));
        mainAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory("appointmentType"));
        mainAppointmentStartTimeCol.setCellValueFactory(new PropertyValueFactory("appointmentStartDateTime"));
        mainAppointmentEndTimeCol.setCellValueFactory(new PropertyValueFactory("appointmentEndDateTime"));
        mainAppointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory("appointmentCustomerID"));
        
        ObservableList<Contact> contactList = Contact.getContactList();
        contactsScheduleComboBox.setItems(contactList);
        
        appointmentTypeComboBox.setItems(removeDuplicates(getAppointmentTypes()));
        
        ObservableList<String> monthList = FXCollections.observableArrayList("January", "March", "February", "April", "May", "June",
                                                                                "July", "August", "September", "October", "November", "December");
        appointmentMonthComboBox.setItems(monthList);
        
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Creates a list of appointments from the database that are filtered by the current week of the year. 
     * @return filteredAppointmentList List of appointments scheduled for the current week. */
    private static ObservableList<Appointment> filterAppointmentsByWeek(ObservableList<Appointment> appointmentsToFilterFrom) {
    
        clearFilteredAppointmentList();
        
        WeekFields week = WeekFields.of(Locale.getDefault());
        for (Appointment appointment : appointmentsToFilterFrom) {
            int startDateWeek = appointment.getAppointmentStartDateTime().get(week.weekOfWeekBasedYear());
            int endDateWeek = appointment.getAppointmentEndDateTime().get(week.weekOfWeekBasedYear());
            int currentDateWeek = LocalDate.now().get(week.weekOfWeekBasedYear());
            if (startDateWeek == currentDateWeek || currentDateWeek == endDateWeek) {
                filteredAppointmentList.add(appointment);
            }
        }


        return filteredAppointmentList;
    
    }
    
    /** Creates a list of appointments from the database that are filtered by the current month of the year. 
     * @return filteredAppointmentList List of appointments scheduled for the month. */
    private static ObservableList<Appointment> filterAppointmentsByMonth(ObservableList<Appointment> appointmentsToFilterFrom) {
    
        clearFilteredAppointmentList();
            
        for (Appointment appointment : appointmentsToFilterFrom) {
            int startDateMonth = appointment.getAppointmentStartDateTime().getMonthValue();
            int endDateMonth = appointment.getAppointmentEndDateTime().getMonthValue();
            int currentDateMonth = LocalDate.now().getMonthValue();
            if (startDateMonth == currentDateMonth || currentDateMonth == endDateMonth) {
                filteredAppointmentList.add(appointment);
            }
        }

        return filteredAppointmentList;
    
    }
    
    /** Empties the filteredAppointmentList. */
    private static void clearFilteredAppointmentList() {
    
        filteredAppointmentList.removeAll(filteredAppointmentList);
    
    }
    
    /** Extracts all the appointmentTypes from all existing appointments and puts them in an ObservableList. 
     * @return appointmentTypesList List of appointment types that contains duplicates. */
    private ObservableList<String> getAppointmentTypes() {
    
        ObservableList<Appointment> appointments = Appointment.getAppointmentList();
        ObservableList<String> appointmentTypesList = FXCollections.observableArrayList();
        
        for (Appointment appointment : appointments) {
        
            appointmentTypesList.add(appointment.getAppointmentType());
        
        }
        
        return appointmentTypesList;
    
    }
    
    /** Removes duplicates from an ObservableList. 
     * @return cleanedList An ObservableList with no duplicates. */
    private ObservableList<String> removeDuplicates(ObservableList<String> listToClean) {
    
        ObservableList<String> cleanedList = FXCollections.observableArrayList();
        
        for (String type: listToClean) {
            
            if (!cleanedList.contains(type)) {
                cleanedList.add(type);
            }
            
        }
        
        return cleanedList;
    }
    
    /** Counts the number of existing appointments.
     * @return  total An int count of all scheduled appointments.*/
    public int getNumberOfAppointments() {
    
        int total = 0;
        
        try {
            String sql = "SELECT * FROM appointments";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                total++;
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }

        return total;
        
    }
    
    /** Counts all the scheduled appointments of a specified appointmentType AND month.
     * @return  total An int count of appointments for the given type and month. */
    public int getNumberOfAppointmentsByTypeAndMonth() {
    
        int total = 0;
        String type = appointmentTypeComboBox.getValue();
        String month = appointmentMonthComboBox.getValue();
        ObservableList<Appointment> listToFilterThenCount = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT Appointment_ID, Type, Start, End FROM appointments WHERE Type = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, type);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentType = rs.getString("Type");
                LocalDateTime appointmentStartDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime appointmentEndDateTime = rs.getTimestamp("End").toLocalDateTime();
                
                Appointment appointment = new Appointment(appointmentID, appointmentType, appointmentStartDateTime, appointmentEndDateTime);
                listToFilterThenCount.add(appointment);
                
            }
            
            for (Appointment appointment : listToFilterThenCount) {
                
                if (appointment.getAppointmentStartDateTime().getMonth().name().equalsIgnoreCase(month) || 
                        appointment.getAppointmentEndDateTime().getMonth().name().equalsIgnoreCase(month)) {
                    total++;
                }
                
            }
            
            listToFilterThenCount.removeAll(listToFilterThenCount);
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }

        return total;
        
    }
    
}
