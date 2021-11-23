package appointmentscheduler;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DatabaseConnection;

/**
 *
 * @author Josh Shepherd
 */

/** This class loads the Login Menu and contains the Main method. The Main method establishes the database connection. */
public class AppointmentScheduler extends Application {
    
    /** Loads the Login Menu.
     * @param stage Stage
     * @throws java.lang.Exception No try-catch block needed. */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginMenu.fxml"));
        
        Scene scene = new Scene(root);
        
        Locale locale = Locale.getDefault();
        if(locale.getLanguage().equals("fr")) {
                    
            stage.setTitle("Planificateur de rendez-vous");
                    
        } else {
            stage.setTitle("APPOINTMENT SCHEDULER");
        }
        
        stage.setScene(scene);
        stage.show();
    }

    /*
        ----------MAIN----------
    */
    
    /** Opens the initial connection to the database and terminates it when the application is closed.
     * @param args String[]*/
    public static void main(String[] args) {
        
        DatabaseConnection.connectToDatabase();
        launch(args);
        DatabaseConnection.killConnection();
        
    }
    
}
