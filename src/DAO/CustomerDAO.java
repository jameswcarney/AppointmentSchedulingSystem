package DAO;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles database queries related to customers.
 * @author James Carney
 */
public class CustomerDAO {
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Creates a new customer in the database.
     *
     * @param customer The customer to be created.
     * @return boolean indicating success/failure of the create operation.
     * @throws SQLException An exception related to the database query.
     */
    public static boolean createCustomer(Customer customer) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "INSERT INTO customers(Customer_Name, address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setString(1, customer.getCustomerName().getValue());
        ps.setString(2, customer.getAddress().getValue());
        ps.setString(3, customer.getPostalCode().getValue());
        ps.setString(4, customer.getPhone().getValue());
        ps.setInt(5, customer.getDivisionId().getValue());

        ps.execute();

        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) affected.");
            DBConnection.closeConnection();
            return true;
        }
        else {
            System.out.println("No change.");
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * Gets a single customer from the database.
     *
     * @param customerId The ID of the customer to be retrieved.
     * @return A customer object representing the requested customer.
     * @throws SQLException An exception related to the database query.
     */
    public static Customer getCustomer(int customerId) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "SELECT * FROM customers WHERE Customer_ID = ?";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setInt(1, customerId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ObservableValue<Integer> custId = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
            ObservableValue<String> customerName = new ReadOnlyObjectWrapper<>(rs.getString("Customer_Name"));
            ObservableValue<String> address = new ReadOnlyObjectWrapper<>(rs.getString("Address"));
            ObservableValue<String> postalCode = new ReadOnlyObjectWrapper<>(rs.getString("Postal_Code"));
            ObservableValue<String> phone = new ReadOnlyObjectWrapper<>(rs.getString("Phone"));
            ObservableValue<Integer> divisionId = new ReadOnlyObjectWrapper<>(rs.getInt("Division_ID"));
            Customer customer = new Customer(custId, customerName, address, postalCode, phone, divisionId);
            DBConnection.closeConnection();
            return customer;
        }
        DBConnection.closeConnection();
        return null;
    }

    /**
     * Gets a list of all customers from the database.
     *
     * @return A list of all customers.
     */
    public static ObservableList<Customer> getAllCustomers()  {
            allCustomers.clear();

            try {
                Connection conn = DBConnection.getConnection();
                String sqlStatement = "SELECT * FROM customers";
                Query.setPreparedStatement(conn, sqlStatement);
                PreparedStatement ps = Query.getPreparedStatement();

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ObservableValue<Integer> custId = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
                    ObservableValue<String> customerName = new ReadOnlyObjectWrapper<>(rs.getString("Customer_Name"));
                    ObservableValue<String> address = new ReadOnlyObjectWrapper<>(rs.getString("Address"));
                    ObservableValue<String> postalCode = new ReadOnlyObjectWrapper<>(rs.getString("Postal_Code"));
                    ObservableValue<String> phone = new ReadOnlyObjectWrapper<>(rs.getString("Phone"));
                    ObservableValue<Integer> divisionId = new ReadOnlyObjectWrapper<>(rs.getInt("Division_ID"));
                    Customer customer = new Customer(custId, customerName, address, postalCode, phone, divisionId);
                    allCustomers.add(customer);
                }
                return allCustomers;
            }
            catch (SQLException e) {
                System.out.println(e.getStackTrace());
                return null;
            }
    }

    /**
     * Updates a customer already in the database.
     *
     * @param customerId The ID of the customer to be updated.
     * @param customer The customer to be updated.
     * @return boolean representing whether the customer was updated.
     */
    public static boolean updateCustomer(int customerId, Customer customer) {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setInt(1, customer.getCustomerId().getValue());
            ps.setString(2, customer.getCustomerName().getValue());
            ps.setString(3, customer.getAddress().getValue());
            ps.setString(4, customer.getPostalCode().getValue());
            ps.setString(5, customer.getPhone().getValue());
            ps.setInt(6, customer.getDivisionId().getValue());
            ps.setInt(7, customerId);

            ps.execute();

            return ps.getUpdateCount() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a customer from the database.
     *
     * @param customer The customer to be deleted.
     * @return Boolean representing success or failure of the delete operation.
     */
    public static boolean deleteCustomer(Customer customer)  {
        try {
            Connection conn = DBConnection.getConnection();
            // We must delete all the customer's appointments before deleting the customer
            AppointmentDAO.deleteAllAppointmentsByCustomer(customer.getCustomerId().getValue());
            //Now delete the customer
            String sqlStatement = "DELETE FROM customers WHERE Customer_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setInt(1, customer.getCustomerId().getValue());
            ps.execute();

            return ps.getUpdateCount() > 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets a first-level division by ID.
     *
     * @param divId The ID of the first-level division to be retrieved.
     * @return FirstLevelDivision object representing the FLD requested by ID.
     */
    public static FirstLevelDivision getFLDbyId(int divId) {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setInt(1, divId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");

                FirstLevelDivision result = new FirstLevelDivision(divisionId, division, countryId);
                return result;
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
