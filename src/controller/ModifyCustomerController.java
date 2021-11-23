package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;
import utils.DatabaseConnection;
import utils.EmptyFieldInterface;

/**
 * FXML Controller class
 *
 * @author Josh Shepherd
 */

/** This FXML Controller class handles modifying Customer objects in the database. */
public class ModifyCustomerController implements Initializable {
    
    /*
        ----------VARIABLES----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(5000));

    @FXML
    private TextArea modifyCustomerIDTxt;

    @FXML
    private TextArea modifyCustomerNameTxt;

    @FXML
    private TextArea modifyCustomerAddressTxt;

    @FXML
    private TextArea modifyCustomerPostalCodeTxt;

    @FXML
    private TextArea modifyCustomerPhoneTxt;

    @FXML
    private ComboBox<Country> modifyCustomerCountryComboBox;

    @FXML
    private ComboBox<FirstLevelDivision> modifyCustomerDivisionComboBox;
    
    @FXML
    private Label modifyCustomerErrorLabel;
    
    /*
        ----------ACTION EVENTS----------
    */
    
    /** Filters the Divisions Combo Box by the selected Country once a Country is selected. */
    @FXML
    void modifyCustomerCountrySelected(ActionEvent event) {

        FirstLevelDivision.clearFilteredDivisionList();
        
        int countryIDToFilter = modifyCustomerCountryComboBox.getSelectionModel().getSelectedItem().getCountryID();
        
        modifyCustomerDivisionComboBox.setItems(FirstLevelDivision.getFilteredDivisionList(countryIDToFilter));
        
    }
    
    /** Loads the Main Menu without updating a Customer. */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Updates an existing Customer in the database and then loads the Main Menu. */
    @FXML
    void onActionModifyCustomer(ActionEvent event) {
        
        System.out.println("Customer modify button smashed.");
        
        if(emptyFields.aFieldIsEmpty()) {
            
            modifyCustomerErrorLabel.setText("One or more fields are empty!  Fill 'em up, Buttercup!");
            FADE_OUT.playFromStart();
            
        } else {
            
            System.out.println("Customer modified!");
            
            String id = modifyCustomerIDTxt.getText();
            String name = modifyCustomerNameTxt.getText();
            String address = modifyCustomerAddressTxt.getText();
            String postal = modifyCustomerPostalCodeTxt.getText();
            String phone = modifyCustomerPhoneTxt.getText();
            String divisionID = String.valueOf(modifyCustomerDivisionComboBox.getSelectionModel().getSelectedItem().getDivisionID());

            Customer.updateCustomer(id, name, address, postal, phone, divisionID);
            
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
    
    /*
        ----------INITIALIZER----------
    */
    
    /** Initializes the Modify Customer Menu by populating the Country and FirstLevelDivision combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FADE_OUT.setNode(modifyCustomerErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        Country.clearCountryList();
        
        ObservableList<Country> countryList = Country.getCountryList();
        modifyCustomerCountryComboBox.setItems(countryList);
        
        ObservableList<FirstLevelDivision> divisionList = FirstLevelDivision.getDivisionList();
        modifyCustomerDivisionComboBox.setItems(divisionList);
        
    }    
    
    /*
        ----------METHODS----------
    */
    
    /** Sends a Customer object from the Main Menu Customer Table to the Modify Customer Menu. Uses a PreparedStatement to feed the parameter variable into a SQL SELECT query.
     * @param customer Customer*/
    public void sendCustomer(Customer customer) {
        
        modifyCustomerIDTxt.setText(String.valueOf(customer.getCustomerID()));
        modifyCustomerNameTxt.setText(customer.getCustomerName());
        modifyCustomerAddressTxt.setText(customer.getCustomerAddress());
        modifyCustomerPostalCodeTxt.setText(customer.getCustomerPostalCode());
        modifyCustomerPhoneTxt.setText(customer.getCustomerPhone());
        
        String sql = "SELECT c.Customer_ID, d.Division, d.Division_ID, co.Country, co.Country_ID\n" +
                    "FROM customers AS c \n" +
                    "INNER JOIN first_level_divisions AS d\n" +
                    "    ON (c.Division_ID = d.Division_ID)\n" +
                    "INNER JOIN countries AS co\n" +
                    "	ON (d.Country_ID = co.Country_ID)\n" +
                    "WHERE Customer_ID = ?;";

        try {
        
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
        
            ps.setString(1, String.valueOf(customer.getCustomerID()));

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
        
                String countryName = rs.getString("Country");
                int countryID = rs.getInt("Country_ID");
                String divisionName = rs.getString("Division");
                int divisionID = rs.getInt("Division_ID");

                Country c = new Country(countryID, countryName);
                modifyCustomerCountryComboBox.setValue(c);
                
                modifyCustomerDivisionComboBox.setItems(FirstLevelDivision.getFilteredDivisionList(countryID));

                FirstLevelDivision d = new FirstLevelDivision(divisionID, divisionName, countryID);
                modifyCustomerDivisionComboBox.setValue(d);

            }
        
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
                        
    }
        
    /** A lambda expression that checks if any required Modify Customer fields are missing data and returns true if they are empty. */
    EmptyFieldInterface emptyFields = () -> {
        if (modifyCustomerNameTxt.getText().isEmpty() || modifyCustomerAddressTxt.getText().isEmpty() || modifyCustomerCountryComboBox.getValue() == null ||
                modifyCustomerPhoneTxt.getText().isEmpty() || modifyCustomerPostalCodeTxt.getText().isEmpty() || modifyCustomerDivisionComboBox.getValue() == null ) {
            return true;
        } else {
            return false;
        }
    };
    
}
