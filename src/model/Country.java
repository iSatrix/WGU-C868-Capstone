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

/** This class defines the Country object.  This is used to filter FirstLevelDivision combo boxes. */
public class Country {
    
    /*
        ----------VARIABLES----------
    */
    
    private static ObservableList<Country> countryList = FXCollections.observableArrayList();
    private int countryID;
    private String countryName;
    
    /*
        ----------CONSTRUCTORS----------
    */
    
    /** Primary constructor for the Country object.
     * @param countryID int
     * @param countryName String*/
    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }
    
    /*
        ----------SETTERS----------
    */
    
    /** Sets the countryID.
     * @param countryID int*/
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Sets the countryName.
     * @param countryName String*/
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    /** Sets the countryList.
     * @param countryList ObservableList*/
    public static void setCountryList(ObservableList<Country> countryList) {
        Country.countryList = countryList;
    }
    
    /*
        ----------GETTERS----------
    */

    /** Gets a list of all the countries from the database. Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return  countryList*/
    public static ObservableList<Country> getCountryList() {
        
        try{
            String sql = "SELECT * from countries WHERE (Country_ID = 1 OR country_ID = 2 OR country_ID = 3)";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryID, countryName);
                countryList.add(country);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        
        return countryList;
    }

    /** Gets the countryID.
     * @return  countryID*/
    public int getCountryID() {
        return countryID;
    }

    /** Gets the countryName.
     * @return  countryName*/
    public String getCountryName() {
        return countryName;
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Empties the countryList.
     * @return  countryList An empty list.*/
    public static ObservableList<Country> clearCountryList() {
    
        try{

            DatabaseConnection.getConnection();

            countryList.removeAll(countryList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return countryList;
        
    }
    
    /** Overrides the toSting() method so Combo Boxes will display the countryName of each Country object instead of just Country object hash locations. */
    @Override
    public String toString() {
        return (countryName);
    }
       
}
