package controller;

import DAO.AppointmentDAO;
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
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import main.Main;
import model.Appointment;
import util.TimeZone;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Main screen.
 * @author James Carney
 */
public class MainController implements Initializable {
    @FXML private Button CustomersButton;
    @FXML private Button AppointmentsButton;
    @FXML private Button ReportsButton;
    @FXML private Button LogOutButton;

    /**
     * A list of all appointments currently scheduled.
     */
    public ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();

    /**
     * Handler called when the "Customers" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onCustomersButtonClicked(ActionEvent event) throws Exception {
        Parent CustomerParent = FXMLLoader.load(getClass().getResource("/view/CustomersView.fxml"), Main.rb);
        Scene CustomerScene = new Scene(CustomerParent);
        Stage CustomerWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        CustomerWindow.setScene(CustomerScene);
        CustomerWindow.show();
    }

    /**
     * Handler called when the "Appointments" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onAppointmentsButtonClicked(ActionEvent event) throws Exception {
        Parent AppointmentParent = FXMLLoader.load(getClass().getResource("/view/AppointmentsView.fxml"), Main.rb);
        Scene AppointmentScene = new Scene(AppointmentParent);
        Stage AppointmentWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AppointmentWindow.setScene(AppointmentScene);
        AppointmentWindow.show();
    }

    /**
     * Handler called when the "Reports" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onReportsButtonClicked(ActionEvent event) throws Exception {
        Parent ReportsParent = FXMLLoader.load(getClass().getResource("/view/ReportsView.fxml"), Main.rb);
        Scene ReportsScene = new Scene(ReportsParent);
        Stage ReportsWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

        ReportsWindow.setScene(ReportsScene);
        ReportsWindow.show();
    }

    /**
     * Handler called when the "Logout" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onLogOutButtonClicked(ActionEvent event) throws Exception {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Logout");
        exitAlert.setContentText("Are you sure you want to log out?");
        Optional<ButtonType> result = exitAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Parent LoginParent = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"), Main.rb);
            Scene LoginScene = new Scene(LoginParent);
            Stage LoginWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            LoginWindow.setScene(LoginScene);
            LoginWindow.show();
        }
    }

    /**
     * Queries the database for all scheduled appointments.
     * @return An appointment starting less than 15 minutes from now.
     */
    public Appointment checkForUpcomingAppointments() {
        LocalDateTime nowPlus15 = LocalDateTime.now().plusMinutes(15);

        for (Appointment appointment : allAppointments) {
            if ((appointment.getStart().getValue().toLocalDateTime().isBefore(nowPlus15)) && (appointment.getStart().getValue().toLocalDateTime().isAfter(LocalDateTime.now()))) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Initializes the Main screen.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        if (checkForUpcomingAppointments() != null) {
            Appointment appointmentToAlert = checkForUpcomingAppointments();
            Alert appointmentAlert = new Alert(Alert.AlertType.CONFIRMATION);
            appointmentAlert.setTitle("Upcoming Appointment");
            appointmentAlert.setContentText("Appointment soon: " + appointmentToAlert.getAppointmentId().getValue().toString() + " - " + appointmentToAlert.getStart().getValue().toLocalDateTime().format(TimeZone.dtf));
            appointmentAlert.showAndWait();
        }
        else {
            Alert noAppointmentAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noAppointmentAlert.setTitle("No Upcoming Appointment");
            noAppointmentAlert.setContentText("No appointments in the next 15 minutes.");
            noAppointmentAlert.showAndWait();
        }
    }
}
