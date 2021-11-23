package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import utils.DatabaseConnection;

/**
 *
 * @author Josh Shepherd
 */

/** This class defines the Customer object. It contains several methods that will be used
  *by the controller classes. */
public class Customer {
    
    /*
        ----------VARIABLES----------
    */
    
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private String customerDivisionName;
    private int customerDivisionID;
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<Customer> searchedCustomerList = FXCollections.observableArrayList();
    
    
    /*
        ----------CONSTRUCTORS----------
    */

    /** Primary constructor for the Customer object.
     * @param customerID int
     * @param customerName String
     * @param customerAddress String
     * @param customerPostalCode String
     * @param customerPhone String
     * @param customerDivisionName String
     * @param customerDivisionID int*/
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone, String customerDivisionName, int customerDivisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivisionName = customerDivisionName;
        this.customerDivisionID = customerDivisionID;
    }

    /** Private constructor for the Customer object. Used to create the customerList ObservableList object. 
     * @param customerID int
     * @param customerName String
     * @param customerAddress String
     * @param customerPostalCode String
     * @param customerPhone String
     * @param customerDivisionName String*/
    private Customer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone, String customerDivisionName) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivisionName = customerDivisionName;
    }

    /** Additional constructor for the Customer object.
     * @param customerID int
     * @param customerName String*/
    public Customer(int customerID, String customerName) {
        this.customerID = customerID;
        this.customerName = customerName;
    }
            
    /*
        ----------SETTERS----------
    */

    /** Sets the customerID.
     * @param customerID int*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Sets the customerName.
     * @param customerName String*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /** Sets the customerAddress.
     * @param customerAddress String*/
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /** Sets the customerPostalCode.
     * @param customerPostalCode String*/
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /** Sets the customerPhone.
     * @param customerPhone String*/
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /** Sets the customerDivisionName.
     * @param customerDivisionName String*/
    public void setCustomerDivisionName(String customerDivisionName) {
        this.customerDivisionName = customerDivisionName;
    }

    /** Sets the customerDivisionID. This links the Customer and FirstLevelDivision tables.
     * @param customerDivisionID int*/
    public void setCustomerDivisionID(int customerDivisionID) {
        this.customerDivisionID = customerDivisionID;
    }

    
    /*
        ----------GETTERS----------
    */

    /** Gets the customerID.
     * @return  customerID*/
    public int getCustomerID() {
        return customerID;
    }

    /** Gets the customerName.
     * @return  customerName*/
    public String getCustomerName() {
        return customerName;
    }

    /** Gets the customerAddress.
     * @return  customerAddress*/
    public String getCustomerAddress() {
        return customerAddress;
    }

    /** Gets the customerPostalCode.
     * @return  customerPostalCode*/
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /** Gets the customerPhone.
     * @return  customerPhone*/
    public String getCustomerPhone() {
        return customerPhone;
    }

    /** Gets the customerDivisionName.
     * @return  customerDivisionName*/
    public String getCustomerDivisionName() {
        return customerDivisionName;
    }

    /** Gets the customerDivisionID.
     * @return  customerDivisionID*/
    public int getCustomerDivisionID() {
        return customerDivisionID;
    }
    
    /** Gets a list of all the customers from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL SELECT query.
     * @return  customerList List of all customers.*/
    public static ObservableList<Customer> getCustomerList() {
        
        clearCustomerList();
        
        try{
            String sql = "SELECT c.Customer_ID, c.Customer_Name, c.Address, c.Postal_Code, c.Phone, d.Division\n" +
                        "FROM customers AS c \n" +
                        "INNER JOIN first_level_divisions AS d\n" +
                        "    ON (c.Division_ID = d.Division_ID);";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhone = rs.getString("Phone");
                String customerDivisionName = rs.getString("Division");
                Customer customer = new Customer(customerID, customerName, customerAddress, customerPostalCode, customerPhone, customerDivisionName);
                customerList.add(customer);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        
        return customerList;
    }
    
    /*
        ----------METHODS----------
    */
    
    /** Saves a new customer to the database. Uses a PreparedStatement to feed the parameter variables into a SQL INSERT query.
     * @param customerName String
     * @param customerAddress String
     * @param customerPostalCode String
     * @param customerPhone String
     * @param customerDivisionID String*/
    public static void saveNewCustomer(String customerName, String customerAddress, String customerPostalCode, String customerPhone, String customerDivisionID) {
    
        try {
            
            String sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?) ";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setString(5, customerDivisionID);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }

    }
    
    /** Updates an existing customer in the database. Uses a PreparedStatement to feed the parameter variables into a SQL UPDATE query.
     * @param customerID String
     * @param customerName String
     * @param customerAddress String
     * @param customerPostalCode String
     * @param customerPhone String
     * @param customerDivisionID String*/
    public static void updateCustomer(String customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhone, String customerDivisionID) {
    
        try {
            
            String sql = "UPDATE customers\n" +
                        "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?\n" +
                        "WHERE Customer_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, customerName);
            ps.setString(2, customerAddress);
            ps.setString(3, customerPostalCode);
            ps.setString(4, customerPhone);
            ps.setString(5, customerDivisionID);
            ps.setString(6, customerID);
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
        }
    
    }
    
    /** Empties the customerList.
     * @return  customerList An empty list.*/
    public static ObservableList<Customer> clearCustomerList() {
    
        try{

            DatabaseConnection.getConnection();

            customerList.removeAll(customerList);

        } catch (Exception e) {
        
            e.printStackTrace();
            
        }
        
        return customerList;
        
    }
    
    /** Deletes a customer from the database.  Uses a PreparedStatement to feed the parameter variables into a SQL DELETE query.
     * @param customerID int*/
    public static void deleteCustomer(int customerID) {
    
        try {
            
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";

            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);
            
            ps.setString(1, String.valueOf(customerID));
            
            ps.executeUpdate();
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            System.out.println(e.getMessage());
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setContentText("This customer is associated with one or more appointments.  All associated apointments will also be deleted.");
            alert.showAndWait();
            
            
            ObservableList<Appointment> appointmentsToCheck = Appointment.getAppointmentList();
            
            for (Appointment appointmentToDelete : appointmentsToCheck) {
            
                if (appointmentToDelete.getAppointmentCustomerID() == customerID) {
                
                    Appointment.deleteAppointment(appointmentToDelete.getAppointmentID());
                    
                }
            
            }
            
            try {

                String sql = "DELETE FROM customers WHERE Customer_ID = ?";

                PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql);

                ps.setString(1, String.valueOf(customerID));

                ps.executeUpdate();
            
            } catch (SQLException ex) {
            
                ex.printStackTrace();
                System.out.println(ex.getMessage());
            
            }
            
        }

    }
    
    /** Searches through all existing customers based on a user entered search criteria and stores the results in an ObservableList.
     * @param criteriaToSearch
     * @return 
     */
    public static ObservableList<Customer> searchCustomers(String criteriaToSearch) {
        clearSearchedCustomerList();
        
        for (Customer c : getCustomerList()) {
            if (c.getCustomerName().contains(criteriaToSearch) ||
                    c.getCustomerAddress().contains(criteriaToSearch) ||
                    c.getCustomerPostalCode().contains(criteriaToSearch) || 
                    c.getCustomerPhone().contains(criteriaToSearch) || 
                    c.getCustomerDivisionName().contains(criteriaToSearch)) {
                searchedCustomerList.add(c);
            }
        }
        
        if (searchedCustomerList.isEmpty()) {
            return customerList;
        } else {
            return searchedCustomerList;
        }
    }
    
    /** Empties the searchedCustomerList.
     * @return 
     */
    public static ObservableList<Customer> clearSearchedCustomerList() {
        searchedCustomerList.removeAll(searchedCustomerList);
        
        return searchedCustomerList;
    }
    
    /** Overrides the toString() method to display the customerID and customerName in the combo boxes instead of customer hash locations. */
    @Override
    public String toString() {
        return ("[" + customerID + "] " + customerName);
    }
    
    
}
