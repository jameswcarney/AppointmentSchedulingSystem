package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.ReportGenerator;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the Reports screen.
 * @author James Carney
 */
public class ReportsController implements Initializable {
    private ReportGenerator rg = new ReportGenerator();

    @FXML private Label ByTypeLabel;
    @FXML private TableView<ReportGenerator.AppointmentsByType> ByTypeTable;
    @FXML private TableColumn<ReportGenerator.AppointmentsByType, String> ByTypeMonthColumn;
    @FXML private TableColumn<ReportGenerator.AppointmentsByType, String> ByTypeTypeColumn;
    @FXML private TableColumn<ReportGenerator.AppointmentsByType, Integer> ByTypeCountColumn;

    @FXML private Label CustomersByCountry;
    @FXML private TableView<ReportGenerator.CustomersByCountry> customersByCountryTable;
    @FXML private TableColumn<ReportGenerator.CustomersByCountry, String> cbcCountryColumn;
    @FXML private TableColumn<ReportGenerator.CustomersByCountry, Integer> cbcCustomersColumn;

    @FXML private Label ScheduleByContact;
    @FXML private TableView<ReportGenerator.ScheduleByContact> ScheduleTable;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, Integer> ScheduleContactIdColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, Integer> ScheduleAppointmentIdColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, String> ScheduleTitleColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, String> ScheduleTypeColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, String> ScheduleDescriptionColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, ZonedDateTime> ScheduleStartColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, ZonedDateTime> ScheduleEndColumn;
    @FXML private TableColumn<ReportGenerator.ScheduleByContact, Integer> ScheduleCustomerIdColumn;

    /**
     * Handler called when the "Cancel" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onCancelButtonClicked(ActionEvent event) throws Exception {
        Parent MainParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainView.fxml")));
        Scene MainScene = new Scene(MainParent);
        Stage MainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MainWindow.setScene(MainScene);
        MainWindow.show();
    }

    /**
     * Initializes the Reports screen.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rg.processAppointmentsByType();
        ByTypeTable.setItems(rg.getAppointmentsByType());
        ByTypeMonthColumn.setCellValueFactory(new PropertyValueFactory<>("monthString"));
        ByTypeTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ByTypeCountColumn.setCellValueFactory(new PropertyValueFactory<>("count"));

        rg.processCustomersByCountry();
        customersByCountryTable.setItems(rg.getCustomersByCountry());
        cbcCountryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        cbcCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));

        rg.processScheduleByContact();
        ScheduleTable.setItems(rg.getSchedulesByContact());
        ScheduleContactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        ScheduleAppointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        ScheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ScheduleDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ScheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        ScheduleStartColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        ScheduleEndColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        ScheduleCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
    }
}
