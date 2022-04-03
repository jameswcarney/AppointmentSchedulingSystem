package controller;

import DAO.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import util.Logger;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the Login screen.
 * @author James Carney
 */
public class LoginController implements Initializable {

    @FXML private TextField userNameField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label zoneHeaderLabel;
    @FXML private Label zoneIdLabel;

    /**
     * Handler called when the "Login" button is clicked.
     * @param event The click event used to trigger a change to the new scene.
     * @throws Exception A generic exception.
     */
    public void onLoginButtonClicked(ActionEvent event) throws Exception {
        String username = userNameField.getText();
        String password = passwordField.getText();
        User returnedUser = UserDAO.getUser(username);
        boolean success = false;
        String fileName = "login_activity.txt";
        Logger logger = new Logger(fileName);

        if ((returnedUser != null) && (Objects.equals(returnedUser.getUserName(), username)) && (Objects.equals(returnedUser.getPassword(), password))) {
            success = true;
            logger.recordLoginAttempt(LocalDateTime.now(), username, success);

            Parent MainParent = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Scene MainScene = new Scene(MainParent);
            Stage MainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();

            MainWindow.setScene(MainScene);
            MainWindow.show();
        }
        else if ((returnedUser == null) && (Locale.getDefault() == Locale.FRANCE)) {
            logger.recordLoginAttempt(LocalDateTime.now(), username, false);
            Alert exitAlert = new Alert(Alert.AlertType.WARNING);
            exitAlert.setTitle("Avertissement");
            exitAlert.setContentText("Veuillez saisir des identifiants de connexion valides.");
            exitAlert.showAndWait();
        }
        else {
            logger.recordLoginAttempt(LocalDateTime.now(), username, false);
            Alert exitAlert = new Alert(Alert.AlertType.WARNING);
            exitAlert.setTitle("Warning");
            exitAlert.setContentText("Please enter valid login credentials.");
            exitAlert.showAndWait();
        }

    }
    /**
     * Handler called when the "Exit" button is clicked.
     */
    public void onExitButtonClicked() {
        Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        exitAlert.setTitle("Exit");
        exitAlert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = exitAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    /**
     * Initializes the Login screen.
     *
     * @param url The path of the root object.
     * @param resourceBundle The ResourceBundle of the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String userZone = ZoneId.systemDefault().toString();
        zoneIdLabel.setText(userZone);
    }
}
