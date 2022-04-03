package controller;

import DAO.AppointmentDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import util.TimeZone;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *Controller for the Appointments screen.
 * @author James Carney
 */
public class AppointmentsController implements Initializable {
    @FXML private TableView<Appointment> AppointmentsTable;
    @FXML private TableColumn<Appointment, Integer> AppointmentsTableIdColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableTitleColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableDescriptionColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableLocationColumn;
    @FXML private TableColumn<Appointment, Integer> AppointmentsTableContactColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableTypeColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableStartColumn;
    @FXML private TableColumn<Appointment, String> AppointmentsTableEndColumn;
    @FXML private TableColumn<Appointment, Integer> AppointmentsTableCustomerIdColumn;
    @FXML private TableColumn<Appointment, Integer> AppointmentsTableUserIdColumn;
    @FXML private ToggleGroup radioButtonGroup;
    @FXML private RadioButton weeklyRadioButton;
    @FXML private RadioButton monthlyRadioButton;
    @FXML private RadioButton allRadioButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button cancelButton;

    private ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();

    /**
     * Handler called when the "Weekly" radio button is selected.
     */
    public void onWeeklyRadioButtonSelected() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusSeven = now.plusDays(7);
        FilteredList<Appointment> weeklyAppointments = new FilteredList<>(allAppointments);
        weeklyAppointments.setPredicate(row -> {
            LocalDateTime rowStart = row.getStart().getValue().toLocalDateTime();

            return rowStart.isAfter(now) && rowStart.isBefore(nowPlusSeven);
        });
        AppointmentsTable.setItems(weeklyAppointments);
    }

    /**
     * Handler called when the "Monthly" radio button is selected.
     */
    public void onMonthlyRadioButtonSelected() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlusThirty = now.plusDays(30);
        FilteredList<Appointment> monthlyAppointments = new FilteredList<>(allAppointments);
        monthlyAppointments.setPredicate(row -> {
            LocalDateTime rowStart = row.getStart().getValue().toLocalDateTime();

            return rowStart.isAfter(now) && rowStart.isBefore(nowPlusThirty);
        });
        AppointmentsTable.setItems(monthlyAppointments);
    }

    /**
     * Handler called when the "All" radio button is selected.
     */
    public void onAllRadioButtonSelected() {
        AppointmentsTable.setItems(allAppointments);
    }

    /**
     * Handler called when the "Add" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onAddAppointmentButtonClicked(ActionEvent event) throws Exception {
        Parent AddAppointmentParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddAppointmentView.fxml")));
        Scene AddAppointmentScene = new Scene(AddAppointmentParent);
        Stage AddAppointmentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AddAppointmentWindow.setScene(AddAppointmentScene);
        AddAppointmentWindow.show();
    }

    /**
     * Handler called when the "Edit" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */

    public void onEditAppointmentButtonClicked(ActionEvent event) throws Exception {
        if (AppointmentsTable.getSelectionModel().getSelectedItem() == null) {
            Alert noAppointmentSelectedAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noAppointmentSelectedAlert.setTitle("Warning");
            noAppointmentSelectedAlert.setContentText("You must select an appointment in order to modify it.");
            noAppointmentSelectedAlert.showAndWait();
        }
        else {
            EditAppointmentController.appointmentToEdit = AppointmentsTable.getSelectionModel().getSelectedItem();

            Parent EditAppointmentParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/EditAppointmentView.fxml")));
            Scene EditAppointmentScene = new Scene(EditAppointmentParent);
            Stage EditAppointmentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            EditAppointmentWindow.setScene(EditAppointmentScene);
            EditAppointmentWindow.show();
        }
    }

    /**
     *Handler called when the "Delete" button is clicked.
     */
    public void onDeleteAppointmentButtonClicked() {
        if (AppointmentsTable.getSelectionModel().getSelectedItem() == null) {
            Alert noAppointmentSelectedAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noAppointmentSelectedAlert.setTitle("Warning");
            noAppointmentSelectedAlert.setContentText("You must select an appointment in order to delete it.");
            noAppointmentSelectedAlert.showAndWait();
        }
        else {
            Appointment appointmentToDelete = AppointmentsTable.getSelectionModel().getSelectedItem();
            boolean appointmentDeleted = AppointmentDAO.deleteAppointment(appointmentToDelete.getAppointmentId().getValue());
            if (appointmentDeleted) {
                Alert deleteAlert = new Alert(Alert.AlertType.WARNING);
                deleteAlert.setTitle("Success!");
                deleteAlert.setContentText("Appointment " + appointmentToDelete.getAppointmentId().getValue() + " - " + appointmentToDelete.getType().getValue() + " successfully deleted");
                deleteAlert.showAndWait();
            }
            AppointmentsTable.setItems(AppointmentDAO.getAllAppointments());
        }
    }

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
     * Initializes the Appointments screen.
     *
     * The lambda expressions used to populate the cells allow for more readable code. Additionally, they avoid the
     * use of PropertyValueFactory, which has a number of drawbacks related to how class getters/setters need to be
     * formatted. However, one of the biggest drawbacks of the PropertyValueFactory is that the table needs to be
     * re-sorted or redrawn in some way before any changes to the cell data will be visible. The lambda expression does
     * not have this same drawback.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populate AppointmentsTable with Appointments
        AppointmentsTable.setItems(allAppointments);
        AppointmentsTableIdColumn.setCellValueFactory(cellData -> cellData.getValue().getAppointmentId());
        AppointmentsTableTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        AppointmentsTableDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());
        AppointmentsTableLocationColumn.setCellValueFactory(cellData -> cellData.getValue().getLocation());
        AppointmentsTableContactColumn.setCellValueFactory(cellData ->  cellData.getValue().getContactId());
        AppointmentsTableTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getType());
        AppointmentsTableStartColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStart().getValue().toLocalDateTime().format(TimeZone.dtf)));
        AppointmentsTableEndColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getEnd().getValue().toLocalDateTime().format(TimeZone.dtf)));
        AppointmentsTableCustomerIdColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerId());
        AppointmentsTableUserIdColumn.setCellValueFactory(cellData -> cellData.getValue().getUserId());
    }
}
