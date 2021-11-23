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

/** This class defines the FirstLevelDivision object.  This is used to set the State or Province
 * for a User object.*/
public class FirstLevelDivision {
    
    /*
        ----------VARIABLES----------
    */
    
    private int divisionID;
    private String divisionName;
    private int countryID;
    private static ObservableList<FirstLevelDivision> divisionList = FXCollections.observableArrayList();
    private static ObservableList<FirstLevelDivision> filteredDivisionList = FXCollections.observableArrayList();
    
    /*
        ----------CONSTRUCTORS----------
    */
    
    /** Constructor for FirstLevelDivision object.
     * @param divisionID int
     * @param divisionName String
     * @param countryID int*/
    public FirstLevelDivision(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }
    
    /*
        ----------SETTERS----------
    */
    
    /** Sets the divisionID.
     * @param divisionID int*/
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /** Sets the divisionName.
     * @param divisionName String*/
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /** Sets the countryID for the FirstLevelDivision object. This links the FirstLevelDivision and Country tables.
     * @param countryID int*/
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /** Sets the divisionList.
     * @param divisionList ObservableList*/
    public static void setDivisionList(ObservableList<FirstLevelDivision> divisionList) {
        FirstLevelDivision.divisionList = divisionList;
    }
    
    /** Sets the filteredDivisionList.
     * @param filteredDivisionList ObservableList*/
    public static void setFilteredDivisionList(ObservableList<FirstLevelDivision> filteredDivisionList) {
        FirstLevelDivision.filteredDivisionList = filteredDivisionList;
    }
    
    /*
        ----------GETTERS----------
    */

    /** Gets the divisionID.
     * @return  divisionID*/
    public int getDivisionID() {
        return divisionID;
    }

    /** Gets the divisionName.
     * @return  divisionName*/
    public String getDivisionName() {
        return divisionName;
    }

    /** Gets the countryID.
     * @return  countryID*/
    public int getCountryID() {
        return countryID;
    }
    
    /** Gets a list of all FirstLevelDivisions from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return  divisionList List of all divisions.*/
    public static ObservableList<FirstLevelDivision> getDivisionList() {
        
        try{
            String sql = "SELECT * from first_level_divisions";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                FirstLevelDivision division = new FirstLevelDivision(divisionID, divisionName, countryID);
                divisionList.add(division);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        
        return divisionList;
    }
    
    /** Gets a filtered list of FirstLevelDivisions from the database using a countryID.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @param filterCountryID int
     * @return filteredDivisionList A list only containing divisions matching the associated country.*/
    public static ObservableList<FirstLevelDivision> getFilteredDivisionList(int filterCountryID) {
        
        try{
            String sql = "SELECT * from first_level_divisions WHERE COUNTRY_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ps.setString(1, String.valueOf(filterCountryID));
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("COUNTRY_ID");
                FirstLevelDivision division = new FirstLevelDivision(divisionID, divisionName, countryID);
                filteredDivisionList.add(division);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        
        return filteredDivisionList;
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Empties the filteredDivisionList.
     * @return  filteredDivisionList An empty list.*/
    public static ObservableList<FirstLevelDivision> clearFilteredDivisionList() {
    
        try{

            DatabaseConnection.getConnection();

            filteredDivisionList.removeAll(filteredDivisionList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return filteredDivisionList;
        
    }
    
    /** Overrides the toSting() method so Combo Boxes will display the divisionName of each Division object instead of just Division object hash locations. */
    @Override
    public String toString() {
        return (divisionName);
    }
    
}
