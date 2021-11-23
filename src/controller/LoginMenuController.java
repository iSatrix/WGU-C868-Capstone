package controller;

import java.sql.Connection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import utils.DatabaseConnection;
import utils.DatabaseQuery;
import utils.MessageInterface;

/**
 *
 * @author Josh Shepherd
 */

/** This class is responsible for validating attempted logins and logging whether the attempts
 * were successful or not into a file located in the applications root folder using two lambda expressions.  
 * If a login is valid then the Main Menu is loaded. */
public class LoginMenuController implements Initializable {
    
    /*
        ----------VARIABLES----------
    */
    
    Stage stage;
    Parent scene;
    private final FadeTransition FADE_OUT = new FadeTransition(Duration.millis(5000));
    
     @FXML
    private Label logoLabel;
    
    @FXML
    private Label loginUserLocationLabel;

    @FXML
    private Label loginDetectedLocationLabel;

    @FXML
    private Label loginUserNameLabel;

    @FXML
    private Label loginPasswordLabel;
    
    @FXML
    private TextField loginUserNameTxt;

    @FXML
    private TextField loginPasswordTxt;

    @FXML
    private Button loginBtn;
    
    @FXML
    private Label loginErrorLabel;
    
    /*
        ----------ACTION EVENTS----------
    */
    
    /** Validates user login credentials and opens the Main Menu or displays incorrect login text*/
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        
        System.out.println("Login button pushed.");
        
        String enteredUserName = loginUserNameTxt.getText();
        String enteredPassword = loginPasswordTxt.getText();
        
        boolean valid = validateUser(enteredUserName, enteredPassword);
        
        if (valid) {
        
            logger(enteredUserName, valid);
            
            System.out.println("Opening Main Menu.");
                
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setTitle("APPOINTMENT SCHEDULER - Main Menu");
            stage.setScene(new Scene(scene));
            stage.show();
            
        } else {
            
            logger(enteredUserName, valid);
        
            loginErrorLabel.setVisible(true);
            FADE_OUT.playFromStart();
            
        }
        
    }
    
    /*
        ----------INITIALIZER----------
    */
    
    /** Initializes the Login Menu by getting the users ZoneID and displaying it. Then gets the users Locale to create a ResourceBundle and then calls translate(). */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FADE_OUT.setNode(loginErrorLabel);
        FADE_OUT.setFromValue(1.0);
        FADE_OUT.setToValue(0.0);
        FADE_OUT.setCycleCount(1);
        FADE_OUT.setAutoReverse(false);
        
        ZoneId zone = ZoneId.systemDefault();
        loginDetectedLocationLabel.setText(zone.toString());
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("properties/LoginMenu", locale);
        
        if(locale.getLanguage().equals("fr")) {
        
            translate(bundle);
        
        }
        
    }    

    /*
        ----------METHODS----------
    */
    
    /** Accepts a ResourceBundle storing the users locale to translate the Login Menu into French. */
    private void translate(ResourceBundle bundle) {
        
        loginUserLocationLabel.setText(bundle.getString("userLocation"));
        loginUserNameLabel.setText(bundle.getString("userName"));
        loginPasswordLabel.setText(bundle.getString("password"));
        loginErrorLabel.setText(bundle.getString("loginError"));
        loginBtn.setText(bundle.getString("login"));
        logoLabel.setText(bundle.getString("logo"));
        
    }
    
    /** Accepts two Strings, userName and password, and uses them to search the Users table in the database for a match and returns true if a match is found. */
    private boolean validateUser(String userName, String password) {
    
        try {
            Connection conn = DatabaseConnection.getConnection();
            String selectStatement = "SELECT User_Name, Password FROM users WHERE User_Name = ?";

            DatabaseQuery.setPreparedStatement(conn, selectStatement); // create PreparedStatement

            PreparedStatement ps = DatabaseQuery.getPreparedStatement();  // PreparedStatement reference

            ps.setString(1, userName);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while(rs.next()) {

                if(userName.equalsIgnoreCase(rs.getString("User_Name")) && password.equalsIgnoreCase(rs.getString("Password"))) {

                    return true;

                }
            }

            return false;
        
        } catch(SQLException e) {
        
            System.out.println(e.getMessage());
            return false;
            
        }
        
    }
    
    /** This method utilizes two lambda expressions to create messages to print to the logFile.  
     * The method uses a boolean to determine whether to log a successful or failed login attempt and uses a String userName to log what name was attempted.
     * The file is stored in the applications root folder. */
    private static void logger(String userName, boolean valid) {
    
        try {
            
            String logFile = "login_activity.txt";
            File file = new File(logFile);
            FileWriter fw = new FileWriter(file, true);
            PrintWriter results = new PrintWriter(fw);
            LocalDateTime attemptDateTime = LocalDateTime.now();
            
            if (valid) {
                
                MessageInterface message = (s, t) -> "User Name: " + s + " SUCCESSFUL login at: " + t;
                results.println(message.getMessage(userName, Timestamp.valueOf(attemptDateTime)));
                results.close();
                
            } else {
            
                MessageInterface message = (s, t) -> "User Name: " + s + " FAILED login at: " + t;
                results.println(message.getMessage(userName, Timestamp.valueOf(attemptDateTime)));
                results.close();
                
            }
        
        } catch (IOException e) {
        
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
    
    }
    
}
