package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Contains the entry point for the program.
 *
 * @author James Carney
 */
public class Main extends Application {
    /**
     * The resource bundle handles localization/language translation
     */
    public static ResourceBundle rb = ResourceBundle.getBundle("resources/Nat", Locale.getDefault());

    /**
     * This method loads the first window of the program, the Login view.
     *
     * @param stage The stage object on which JavaFX scenes can be loaded.
     * @throws Exception A generic exception.
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"), rb);
        stage.setTitle("Appointment Scheduling System");
        stage.setScene(new Scene(root, 400, 250));
        stage.show();
    }

    /**
     * Main method, the entry point for the program.
     *
     * @param args User-provided arguments, if applicable.
     */
    public static void main(String[] args) {
        launch(args);
        }
}