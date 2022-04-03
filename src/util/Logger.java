package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Handles the logging of user login attempts.
 *
 * @author James Carney
 */
public class Logger {
    private String fileName;
    private String getFileName() {
        return fileName;
    }

    /**
     * Class constructor.
     *
     * @param fileName The name of the file to which login attempt information will be written.
     */
    public Logger(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Writes information about login attempts to the file stored in fileName.
     *
     * @param now The exact time of the login attempt.
     * @param userName The name entered by the person logging in.
     * @param success boolean returning true for successful login and false for failed login.
     * @throws IOException An exception related to improper input characters.
     */
    public void recordLoginAttempt(LocalDateTime now, String userName, boolean success) throws IOException {
        String successValue;
        String valueToWrite;

        if (success) { successValue = "Login Successful"; }
        else { successValue = "Login Failed"; }

        BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(), true));
        valueToWrite = "Login attempt - User: " + userName + " Time: " + now.format(TimeZone.dtf) + " " + successValue;
        writer.append('\n');
        writer.append(valueToWrite);
        writer.close();
    }
}
