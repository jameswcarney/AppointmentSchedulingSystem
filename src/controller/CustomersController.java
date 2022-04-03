package controller;

import DAO.CustomerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Customer;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the Customers screen.
 * @author James Carney
 */
public class CustomersController implements Initializable {
    @FXML private TableView<Customer> CustomersTable;
    @FXML private TableColumn<Customer, Integer> CustomersTableIdColumn;
    @FXML private TableColumn<Customer, String> CustomersTableNameColumn;
    @FXML private TableColumn<Customer, String> CustomersTableAddressColumn;
    @FXML private TableColumn<Customer, String> CustomersTablePostalCodeColumn;
    @FXML private TableColumn<Customer, String> CustomersTablePhoneColumn;
    @FXML private TableColumn<Customer, Integer> CustomersTableDivisionIdColumn;
    @FXML private Button AddCustomerButton;
    @FXML private Button EditCustomerButton;
    @FXML private Button DeleteCustomerButton;
    @FXML private Button CancelButton;

    /**
     * Handler called when the "Add" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     */
    public void onAddCustomerButtonClicked(ActionEvent event) {
        try {
            Parent AddCustomerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddCustomerView.fxml")));
            Scene AddCustomerScene = new Scene(AddCustomerParent);
            Stage AddCustomerWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            AddCustomerWindow.setScene(AddCustomerScene);
            AddCustomerWindow.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler called when the "Edit" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     */
    public void onEditCustomerButtonClicked(ActionEvent event) {
        try {
            if (CustomersTable.getSelectionModel().getSelectedItem() == null) {
                Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.CONFIRMATION);
                noCustomerSelectedAlert.setTitle("Warning");
                noCustomerSelectedAlert.setContentText("You must select a customer in order to modify their record.");
                noCustomerSelectedAlert.showAndWait();
            }
            else {
                EditCustomerController.customerToEdit = CustomersTable.getSelectionModel().getSelectedItem();

                Parent EditCustomerParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/EditCustomerView.fxml")));
                Scene EditCustomerScene = new Scene(EditCustomerParent);
                Stage EditCustomerWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

                EditCustomerWindow.setScene(EditCustomerScene);
                EditCustomerWindow.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler called when the "Delete" button is clicked.
     */
    public void onDeleteCustomerButtonClicked() {
        if (CustomersTable.getSelectionModel().getSelectedItem() == null) {
            Alert noCustomerSelectedAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noCustomerSelectedAlert.setTitle("Warning");
            noCustomerSelectedAlert.setContentText("You must select a customer in order to delete their record.");
            noCustomerSelectedAlert.showAndWait();
        }
        else {
            Customer customerToDelete = CustomersTable.getSelectionModel().getSelectedItem();
            boolean customerDeleted = CustomerDAO.deleteCustomer(customerToDelete);
            if (customerDeleted) {
                Alert exitAlert = new Alert(Alert.AlertType.WARNING);
                exitAlert.setTitle("Success!");
                exitAlert.setContentText("Customer " + customerToDelete.getCustomerName().getValue() + " successfully deleted");
                exitAlert.showAndWait();
            }
            CustomersTable.setItems(CustomerDAO.getAllCustomers());
        }
    }

    /**
     * Handler called when the "Cancel" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     */
    public void onCancelButtonClicked(ActionEvent event) {
        try {
            Parent MainParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainView.fxml")));
            Scene MainScene = new Scene(MainParent);
            Stage MainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            MainWindow.setScene(MainScene);
            MainWindow.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the Customers screen.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populate CustomersTable with Customers
        CustomersTable.setItems(CustomerDAO.getAllCustomers());
        CustomersTableIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerId();
        });
        CustomersTableNameColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getCustomerName();
        });
        CustomersTableAddressColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getAddress();
        });
        CustomersTablePostalCodeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getPostalCode();
        });
        CustomersTablePhoneColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getPhone();
        });
        CustomersTableDivisionIdColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getDivisionId();
        });
    }

}

