package controller;

import java.io.IOException;
import java.net.URL;
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
import utils.EmptyFieldInterface;

/**
 * FXML Controller class
 *
 * @author Josh Shepherd
 */

/** This FXML Controller class handles adding a new customer to the database. */
public class AddCustomerController implements Initializable {
    
    /*
        ----------VARIABLESS----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(5000));

    @FXML
    private TextArea addCustomerNameTxt;

    @FXML
    private TextArea addCustomerAddressTxt;

    @FXML
    private TextArea addCustomerPostalCodeTxt;

    @FXML
    private TextArea addCustomerPhoneTxt;

    @FXML
    private ComboBox<Country> addCustomerCountryComboBox;

    @FXML
    private ComboBox<FirstLevelDivision> addCustomerDivisionComboBox;
    
    @FXML
    private Label addCustomerErrorLabel;
    
    /*
        ----------ACTION EVENTS----------
    */
    
    /** Clears the filteredDivisionList and then sets it's items to contain Divisions based on the selected Country. */
    @FXML
    void addCustomerCountrySelected(ActionEvent event) {
        
        FirstLevelDivision.clearFilteredDivisionList();
        
        int countryIDToFilter = addCustomerCountryComboBox.getSelectionModel().getSelectedItem().getCountryID();
        
        addCustomerDivisionComboBox.setItems(FirstLevelDivision.getFilteredDivisionList(countryIDToFilter));
        
    }
    
    /** Loads the Main Menu without saving a new Customer. */
    @FXML
    void onActionDisplayMainMenu(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** Saves a new customer to the database when clicked after checking for any blank text fields. */
    @FXML
    void onActionSaveCustomer(ActionEvent event) {
        
        System.out.println("Customer save button smashed!");
        
        if(emptyFields.aFieldIsEmpty()) {
            
            addCustomerErrorLabel.setText("One or more fields are empty!  Fill 'em up, Buttercup!");
            FADE_OUT.playFromStart();
            
        } else {
            
            System.out.println("Customer saved!");
            
            String name = addCustomerNameTxt.getText();
            String address = addCustomerAddressTxt.getText();
            String postal = addCustomerPostalCodeTxt.getText();
            String phone = addCustomerPhoneTxt.getText();
            String divisionID = String.valueOf(addCustomerDivisionComboBox.getSelectionModel().getSelectedItem().getDivisionID());

            Customer.saveNewCustomer(name, address, postal, phone, divisionID);
            
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
    
    /** Initializes the Add Customer Menu by populating the Country and FirstLevelDivision combo boxes. */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FADE_OUT.setNode(addCustomerErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        Country.clearCountryList();
        
        ObservableList<Country> countryList = Country.getCountryList();
        addCustomerCountryComboBox.setItems(countryList);
        
        ObservableList<FirstLevelDivision> divisionList = FirstLevelDivision.getDivisionList();
        addCustomerDivisionComboBox.setItems(divisionList);
        
    }
    
    /*
        ----------METHODS----------
    */
        
    /** A lambda expression that checks if any required Add Customer fields are missing data and returns true if they are empty. */
    EmptyFieldInterface emptyFields = () -> {
        if (addCustomerNameTxt.getText().isEmpty() || addCustomerAddressTxt.getText().isEmpty() || addCustomerCountryComboBox.getValue() == null ||
                addCustomerPhoneTxt.getText().isEmpty() || addCustomerPostalCodeTxt.getText().isEmpty() || addCustomerDivisionComboBox.getValue() == null ) {
            return true;
        } else {
            return false;
        }
    };
    
}
