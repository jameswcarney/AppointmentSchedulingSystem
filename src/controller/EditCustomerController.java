package controller;

import DAO.CustomerDAO;
import DAO.DBConnection;
import DAO.Query;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;
import util.FormValidator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Controller for the Edit Customer screen.
 * @author James Carney
 */
public class EditCustomerController implements Initializable {
    @FXML private TextField idBox;
    @FXML private TextField nameBox;
    @FXML private TextField addressBox;
    @FXML private TextField postalCodeBox;
    @FXML private TextField phoneBox;
    @FXML private ComboBox<FirstLevelDivision> stateComboBox;
    @FXML private ComboBox<Country> countryComboBox;
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    private ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private ObservableList<FirstLevelDivision> firstLevelDivisionsUS = FXCollections.observableArrayList();
    private ObservableList<FirstLevelDivision> firstLevelDivisionsCanada = FXCollections.observableArrayList();
    private ObservableList<FirstLevelDivision> firstLevelDivisionsUK = FXCollections.observableArrayList();

    /**
     * The Customer object passed to this screen for editing.
     */
    public static Customer customerToEdit;

    /**
     * The FirstLevelDivision of the customer passed to this screen for editing.
     */
    public FirstLevelDivision customerFLD = CustomerDAO.getFLDbyId(customerToEdit.getDivisionId().getValue());

    /**
     * Handler called when the "Save" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onSaveButtonClicked(ActionEvent event) throws Exception {
        try {
            Set<Integer> validationSet = new HashSet<>();
            ObservableValue<Integer> customerId = new ReadOnlyObjectWrapper<>(Integer.parseInt(idBox.getText()));
            validationSet.add(FormValidator.ValidateTextField(nameBox));
            ObservableValue<String> name = new ReadOnlyObjectWrapper<>(nameBox.getText());
            validationSet.add(FormValidator.ValidateTextField(addressBox));
            ObservableValue<String> address = new ReadOnlyObjectWrapper<>(addressBox.getText());
            validationSet.add(FormValidator.ValidateTextField(postalCodeBox));
            ObservableValue<String> postalCode = new ReadOnlyObjectWrapper<>(postalCodeBox.getText());
            validationSet.add(FormValidator.ValidateTextField(phoneBox));
            ObservableValue<String> phone = new ReadOnlyObjectWrapper<>(phoneBox.getText());
            validationSet.add(FormValidator.ComboBoxValidator(stateComboBox));
            FirstLevelDivision fld = stateComboBox.getValue();


            if (validationSet.contains(0)) {
                Alert missingDataAlert = new Alert(Alert.AlertType.WARNING);
                missingDataAlert.setTitle("Warning");
                missingDataAlert.setHeaderText("One or more fields are missing data.");
                missingDataAlert.setContentText("Please complete all data fields to continue.");
                missingDataAlert.showAndWait();
            }
            else{
                Customer editedCustomer = new Customer(customerId, name, address, postalCode, phone, new ReadOnlyObjectWrapper<>(fld.getDivisionId()));
                CustomerDAO.updateCustomer(customerToEdit.getCustomerId().getValue(), editedCustomer);

                Parent CustomerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomersView.fxml")));
                Scene CustomerScene = new Scene(CustomerParent);
                Stage CustomerWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

                CustomerWindow.setScene(CustomerScene);
                CustomerWindow.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler called when the "Cancel" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onCancelButtonClicked(ActionEvent event) throws Exception {
        Parent CustomerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/CustomersView.fxml")));
        Scene CustomerScene = new Scene(CustomerParent);
        Stage CustomerWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        CustomerWindow.setScene(CustomerScene);
        CustomerWindow.show();
    }

    /**
     * Populates the list of countries for the Edit Customer screen.
     */
    public void populateAllCountries() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM countries";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ResultSet rs = ps.executeQuery(sqlStatement);

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country country = new Country(countryId, countryName);
                allCountries.add(country);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Handler called when the user selects a country.
     */
    public void onCountrySelected() {
        int selectedIndex = countryComboBox.getSelectionModel().getSelectedIndex();

        if (selectedIndex == 0) {
            stateComboBox.setItems(firstLevelDivisionsUS);
        }
        else if (selectedIndex == 1) {
            stateComboBox.setItems(firstLevelDivisionsCanada);
        }
        else if (selectedIndex == 2) {
            stateComboBox.setItems(firstLevelDivisionsUK);
        }
    }

    /**
     * Populates the list of first-level divisions for the Edit Customer screen.
     */
    public void populateFLDs() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM first_level_divisions WHERE COUNTRY_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            //Populate US FLD list
            ps.setInt(1, 1);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                FirstLevelDivision fld = new FirstLevelDivision(divisionId, divisionName, countryId);
                firstLevelDivisionsUS.add(fld);
            }

            //Populate UK FLD list
            ps.setInt(1, 2);
            rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                FirstLevelDivision fld = new FirstLevelDivision(divisionId, divisionName, countryId);
                firstLevelDivisionsCanada.add(fld);
            }

            //Populate Canada FLD list
            ps.setInt(1, 3);
            rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                FirstLevelDivision fld = new FirstLevelDivision(divisionId, divisionName, countryId);
                firstLevelDivisionsUK.add(fld);
            }

        }
        catch (SQLException e) {
            e.getStackTrace();
        }
    }

    /**
     * Initializes the Edit Customer screen.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateAllCountries();
        countryComboBox.setItems(allCountries);
        populateFLDs();

        idBox.setText(Integer.toString(customerToEdit.getCustomerId().getValue()));
        nameBox.setText(customerToEdit.getCustomerName().getValue());
        addressBox.setText(customerToEdit.getAddress().getValue());
        postalCodeBox.setText(customerToEdit.getPostalCode().getValue());
        phoneBox.setText(customerToEdit.getPhone().getValue());
        countryComboBox.getSelectionModel().select(customerFLD.getCountryId() - 1);
        stateComboBox.getSelectionModel().select(customerFLD);
    }
}
