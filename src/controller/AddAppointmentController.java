package controller;

import DAO.AppointmentDAO;
import DAO.DBConnection;
import DAO.Query;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import util.FormValidator;
import util.TimeZone;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;


/**
 * Controller class for the Add Appointment screen.
 *
 * @author James Carney
 */
public class AddAppointmentController implements Initializable {
    @FXML private TextField appointmentIdBox;
    @FXML private TextField appointmentTitleBox;
    @FXML private TextField appointmentDescriptionBox;
    @FXML private TextField appointmentLocationBox;
    @FXML private ComboBox<Contact> appointmentContactBox;
    @FXML private ComboBox<String> appointmentTypeBox;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ComboBox<LocalTime> appointmentStartBox;
    @FXML private ComboBox<LocalTime> appointmentEndBox;
    @FXML private ComboBox<Customer> appointmentCustomerBox;
    @FXML private ComboBox<User> appointmentUserBox;

    private ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private ObservableList<String> allTypes = FXCollections.observableArrayList();
    private ObservableList<User> allUsers = FXCollections.observableArrayList();
    private ObservableList<LocalTime> allTimes = FXCollections.observableArrayList();

    /**
     * Handler called when the "Add" button is clicked.
     *
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onAddButtonClicked(ActionEvent event) throws Exception {
        try {
            Set<Integer> validationSet =  new HashSet<>();

            validationSet.add(FormValidator.ValidateTextField(appointmentTitleBox));
            validationSet.add(FormValidator.ValidateTextField(appointmentDescriptionBox));
            validationSet.add(FormValidator.ValidateTextField(appointmentLocationBox));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentContactBox));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentTypeBox));
            validationSet.add(FormValidator.ValidateDatePicker(appointmentDatePicker));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentStartBox));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentEndBox));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentCustomerBox));
            validationSet.add(FormValidator.ComboBoxValidator(appointmentUserBox));

            if (validationSet.contains(0)) {
                Alert missingDataAlert = new Alert(Alert.AlertType.WARNING);
                missingDataAlert.setTitle("Warning");
                missingDataAlert.setHeaderText("One or more fields are missing data.");
                missingDataAlert.setContentText("Please complete all data fields to continue.");
                missingDataAlert.showAndWait();
            }
            else {
                ObservableValue<String> title = new ReadOnlyObjectWrapper<>(appointmentTitleBox.getText());
                ObservableValue<String> description = new ReadOnlyObjectWrapper<>(appointmentDescriptionBox.getText());
                ObservableValue<String> location = new ReadOnlyObjectWrapper<>(appointmentLocationBox.getText());
                ObservableValue<Integer> contactId = new ReadOnlyObjectWrapper<>(appointmentContactBox.getSelectionModel().getSelectedItem().getContactId());
                ObservableValue<String> type = new ReadOnlyObjectWrapper<>(appointmentTypeBox.getSelectionModel().getSelectedItem());
                LocalDate appointmentDate = appointmentDatePicker.getValue();
                LocalTime startTime = appointmentStartBox.getSelectionModel().getSelectedItem();
                LocalTime endTime = appointmentEndBox.getSelectionModel().getSelectedItem();
                ObservableValue<Integer> customer = new ReadOnlyObjectWrapper<>(appointmentCustomerBox.getSelectionModel().getSelectedItem().getCustomerId().getValue());
                ObservableValue<Integer> user = new ReadOnlyObjectWrapper<>(appointmentUserBox.getSelectionModel().getSelectedItem().getUserId());
                LocalDateTime start = LocalDateTime.of(appointmentDate, startTime);
                LocalDateTime end = LocalDateTime.of(appointmentDate, endTime);
                ObservableValue<ZonedDateTime> zonedStart = new ReadOnlyObjectWrapper<>(ZonedDateTime.of(start, TimeZone.userZoneId));
                ObservableValue<ZonedDateTime> zonedEnd = new ReadOnlyObjectWrapper<>(ZonedDateTime.of(end, TimeZone.userZoneId));

                boolean overlappingAppointment = isOverlappingAppointment(zonedStart, zonedEnd);
                if (!overlappingAppointment) {
                    if (TimeZone.validateAppointmentAgainstEST(zonedStart, zonedEnd)) {

                        Appointment newAppointment = new Appointment(title, description, location, type, zonedStart, zonedEnd, customer, user, contactId);
                        AppointmentDAO.createAppointment(newAppointment);

                        Parent AppointmentParent = FXMLLoader.load(getClass().getResource("/view/AppointmentsView.fxml"));
                        Scene AppointmentScene = new Scene(AppointmentParent);
                        Stage AppointmentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        AppointmentWindow.setScene(AppointmentScene);
                        AppointmentWindow.show();
                    } else {
                        Alert businessHoursAlert = new Alert(Alert.AlertType.WARNING);
                        businessHoursAlert.setTitle("Warning");
                        businessHoursAlert.setHeaderText("Appointment start/end time are outside business hours.");
                        businessHoursAlert.setContentText("Please change start/end time to continue.");
                        businessHoursAlert.showAndWait();
                    }
                } else {
                    Alert appointmentOverlapAlert = new Alert(Alert.AlertType.WARNING);
                    appointmentOverlapAlert.setTitle("Warning");
                    appointmentOverlapAlert.setHeaderText("Appointment overlaps with a previously-scheduled appointment.");
                    appointmentOverlapAlert.setContentText("Please change start/end time to continue.");
                    appointmentOverlapAlert.showAndWait();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The handler called when the "Cancel" button is clicked.
     *
     * The lambda in this function simplifies the code required to change scenes by removing the step where
     * a value is stored in "response". The data is passed straight to the lambda and the lambda handles
     * the loading of the next scene.
     *
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onCancelButtonClicked(ActionEvent event) throws Exception {

        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Confirm Cancellation");
        cancelAlert.setHeaderText("Are you sure you want to cancel?");
        cancelAlert.setContentText("All unsaved data will be lost.");

        cancelAlert.showAndWait().ifPresent((response) -> {
            if(response == ButtonType.OK) {
                try {
                    Parent AppointmentParent = FXMLLoader.load(getClass().getResource("/view/AppointmentsView.fxml"));
                    Scene AppointmentScene = new Scene(AppointmentParent);
                    Stage AppointmentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    AppointmentWindow.setScene(AppointmentScene);
                    AppointmentWindow.show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Populates the list of contacts for the Add Appointment screen.
     */
    public void populateContacts() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM contacts";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ResultSet rs = ps.executeQuery(sqlStatement);

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contact contact = new Contact(contactId, contactName, email);
                allContacts.add(contact);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Populates the list of customers for the Add Appointment screen.
     */
    public void populateCustomers() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM customers";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ResultSet rs = ps.executeQuery(sqlStatement);

            while (rs.next()) {
                ObservableValue<Integer> customerId = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
                ObservableValue<String> customerName = new ReadOnlyObjectWrapper<>(rs.getString("Customer_Name"));
                ObservableValue<String> address = new ReadOnlyObjectWrapper<>(rs.getString("Address"));
                ObservableValue<String> postalCode = new ReadOnlyObjectWrapper<>(rs.getString("Postal_Code"));
                ObservableValue<String> phone = new ReadOnlyObjectWrapper<>(rs.getString("Phone"));
                ObservableValue<Integer> divisionId = new ReadOnlyObjectWrapper<>(rs.getInt("Division_ID"));
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId);
                allCustomers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Populates the list of times for the Add Appointment screen.
     */
    public void populateTimes() {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);

        while(start.isBefore(end.plusSeconds(1))) {
            allTimes.add(start);
            start = start.plusMinutes(15);
        }
    }

    /**
     * Populates the list of appointment types for the Add Appointment screen.
     */
    public void populateTypes() {
        allTypes.add("Planning Session");
        allTypes.add("De-Briefing");
        allTypes.add("Financial");
        allTypes.add("Performance Review");
        allTypes.add("Board Meeting");
    }

    /**
     * Populates the list of users for the Add Appointment screen.
     */
    public void populateUsers() {

        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM users";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ResultSet rs = ps.executeQuery(sqlStatement);

            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                User user = new User(userId, userName);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks to see if the appointment a user has attempted to create overlaps with an appointment already scheduled.
     *
     * @param start Start time of the user appointment.
     * @param end End time of the user appointment.
     * @return Boolean, with true representing an appointment that overlaps, and false representing an appointment
     *  that does not overlap.
     */
    public boolean isOverlappingAppointment(ObservableValue<ZonedDateTime> start, ObservableValue<ZonedDateTime> end) {
        for (Appointment appointment : AppointmentDAO.getAllAppointments()) {
            // Yikes. Clean this up
            if (start.getValue().toLocalDateTime().isAfter(appointment.getStart().getValue().toLocalDateTime()) &&
                    (start.getValue().toLocalDateTime().isBefore(appointment.getEnd().getValue().toLocalDateTime()))) {
                return true;
            }
            else   if (end.getValue().toLocalDateTime().isAfter(appointment.getStart().getValue().toLocalDateTime()) &&
                    end.getValue().toLocalDateTime().isBefore(appointment.getEnd().getValue().toLocalDateTime())) {
                    return true;
            }
        }
        return false;
    }

    /**
     * Initializes the Add Appointment view of the program.
     *
     * @param url Path of the object root.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateContacts();
        appointmentContactBox.setItems(allContacts);
        populateCustomers();
        appointmentCustomerBox.setItems(allCustomers);
        populateTimes();
        appointmentStartBox.setItems(allTimes);
        appointmentEndBox.setItems(allTimes);
        populateTypes();
        appointmentTypeBox.setItems(allTypes);
        populateUsers();
        appointmentUserBox.setItems(allUsers);
    }
}
