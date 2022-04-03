package util;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.Objects;

/**
 * Form validator class
 *
 * @author James Carney
 */
public class FormValidator {
    //TODO: Implement additional data sanitization
    public static int ValidateTextField(TextField tf) {
        if (tf.getText() != null && !Objects.equals(tf.getText(), "")) {
            return 1;
        }
        else return 0;
    }

    public static int ComboBoxValidator(ComboBox cb) {
        if (cb.getSelectionModel().getSelectedItem() != null) {
            return 1;
        }
        else return 0;
    }

    public static int ValidateDatePicker(DatePicker dp) {
        if (dp.getValue() != null) {
            return 1;
        }
        else return 0;
    }
}
