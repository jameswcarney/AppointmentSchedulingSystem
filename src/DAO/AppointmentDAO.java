package DAO;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import util.TimeZone;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Handles database queries related to scheduling appointments.
 *
 * @author James Carney
 */
public class AppointmentDAO {
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    /**
     * Creates a new appointment in the database.
     *
     * @param appointment The appointment to be created.
     * @return boolean indicating success/failure of the create operation.
     */
    public static boolean createAppointment(Appointment appointment) {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "INSERT INTO appointments(title, description, location, type, start, end, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setString(1, appointment.getTitle().getValue());
            ps.setString(2, appointment.getDescription().getValue());
            ps.setString(3, appointment.getLocation().getValue());
            ps.setString(4, appointment.getType().getValue());
            //Store appointment times in database as a Timestamp value
            ps.setTimestamp(5,Timestamp.valueOf(appointment.getStart().getValue().toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd().getValue().toLocalDateTime()));
            ps.setInt(7, appointment.getCustomerId().getValue());
            ps.setInt(8, appointment.getUserId().getValue());
            ps.setInt(9, appointment.getContactId().getValue());

            ps.execute();

            if (ps.getUpdateCount() > 0) {
                System.out.println(ps.getUpdateCount() + " row(s) affected.");
                DBConnection.closeConnection();
                return true;
            }
            else {
                System.out.println("No change");
                DBConnection.closeConnection();
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * Gets a single appointment from the database.
     *
     * @param customerId The ID of the customer with whom the appointment is scheduled.
     * @param startTime The start time of the appointment.
     * @return An appointment object representing the requested appointment.
     * @throws SQLException An exception related to the database query.
     */
    public static Appointment getAppointment(int customerId, LocalDateTime startTime) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "SELECT * FROM appointments WHERE Customer_ID = ? and Start = ?";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setInt(1, customerId);
        ps.setString(2, startTime.toString());

        ResultSet rs = ps.getResultSet();

        while (rs.next()) {
            ObservableValue<Integer> appointmentId = new ReadOnlyObjectWrapper<>(rs.getInt("Appointment_ID"));
            ObservableValue<String> title = new ReadOnlyObjectWrapper<>(rs.getString("Title"));
            ObservableValue<String> description = new ReadOnlyObjectWrapper<>(rs.getString("Description"));
            ObservableValue<String> location = new ReadOnlyObjectWrapper<>(rs.getString("Location"));
            ObservableValue<String> type = new ReadOnlyObjectWrapper<>(rs.getString("Type"));
            Timestamp startTimestamp = rs.getTimestamp("Start");
            Timestamp endTimestamp = rs.getTimestamp("End");
            ObservableValue<Integer> custId = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
            ObservableValue<Integer> userId = new ReadOnlyObjectWrapper<>(rs.getInt("User_ID"));
            ObservableValue<Integer> contactId = new ReadOnlyObjectWrapper<>(rs.getInt("Contact_ID"));
            //Convert UTC times from database to user time
            LocalDateTime startUnzoned = startTimestamp.toLocalDateTime();
            LocalDateTime endUnzoned = endTimestamp.toLocalDateTime();
            ZonedDateTime startUTC = ZonedDateTime.of(startUnzoned, TimeZone.UTC);
            ZonedDateTime endUTC = ZonedDateTime.of(endUnzoned, TimeZone.EST);
            ZonedDateTime startUserTime = startUTC.withZoneSameInstant(TimeZone.userZoneId);
            ZonedDateTime endUserTime = endUTC.withZoneSameInstant(TimeZone.userZoneId);
            Appointment appointment = new Appointment(appointmentId, title, description, location, type, new ReadOnlyObjectWrapper<>(startUserTime), new ReadOnlyObjectWrapper<>(endUserTime), custId, userId, contactId);
            return appointment;
        }
        DBConnection.closeConnection();
        return null;
    }

    /**
     * Gets a list of all appointments from the database.
     *
     * @return A list of all appointments.
     */
    public static ObservableList<Appointment> getAllAppointments() {
        allAppointments.clear();

        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT * FROM appointments";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ObservableValue<Integer> appointmentId = new ReadOnlyObjectWrapper<>(rs.getInt("Appointment_ID"));
                ObservableValue<String> title = new ReadOnlyObjectWrapper<>(rs.getString("Title"));
                ObservableValue<String> description = new ReadOnlyObjectWrapper<>(rs.getString("Description"));
                ObservableValue<String> location = new ReadOnlyObjectWrapper<>(rs.getString("Location"));
                ObservableValue<String> type = new ReadOnlyObjectWrapper<>(rs.getString("Type"));
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");
                ObservableValue<Integer> custId = new ReadOnlyObjectWrapper<>(rs.getInt("Customer_ID"));
                ObservableValue<Integer> userId = new ReadOnlyObjectWrapper<>(rs.getInt("User_ID"));
                ObservableValue<Integer> contactId = new ReadOnlyObjectWrapper<>(rs.getInt("Contact_ID"));

                // Convert the timestamp to a ZonedDateTime
                ZonedDateTime userStart = startTime.toLocalDateTime().atZone(ZoneId.of(ZoneId.systemDefault().getId()));
                ZonedDateTime userEnd = endTime.toLocalDateTime().atZone(ZoneId.of(ZoneId.systemDefault().getId()));

                Appointment appointment = new Appointment(appointmentId, title, description, location, type, new ReadOnlyObjectWrapper<>(userStart), new ReadOnlyObjectWrapper<>(userEnd), custId, userId, contactId);
                allAppointments.add(appointment);
            }
            return allAppointments;
        }
        catch (SQLException e) {
            System.out.println(e.getStackTrace());
            return null;
        }
    }

    /**
     * Updates an appointment already in the database.
     *
     * @param appointment The appointment to be updated.
     * @throws SQLException An exception related to the database query.
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();


        ps.setString(1, appointment.getTitle().getValue());
        ps.setString(2,appointment.getDescription().getValue());
        ps.setString(3, appointment.getLocation().getValue());
        ps.setString(4, appointment.getType().getValue());
        ps.setTimestamp(5, Timestamp.valueOf(appointment.getStart().getValue().toLocalDateTime()));
        ps.setTimestamp(6, Timestamp.valueOf(appointment.getEnd().getValue().toLocalDateTime()));
        ps.setInt(7, appointment.getCustomerId().getValue());
        ps.setInt(8, appointment.getUserId().getValue());
        ps.setInt(9, appointment.getContactId().getValue());
        ps.setInt(10, appointment.getAppointmentId().getValue());

        ps.execute();
        //return ps.getUpdateCount() > 0;
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentId The ID of the appointment to be deleted.
     * @return Boolean representing success or failure of the delete operation.
     */
    public static boolean deleteAppointment(int appointmentId)  {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setInt(1, appointmentId);
            ps.execute();

            if (ps.getUpdateCount() > 0) {
                return true;
            }
            else return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes all appointments associated with a specific customer.
     *
     * @param customerId The ID of the customer whose appointments will be deleted.
     */
    public static void deleteAllAppointmentsByCustomer(int customerId) {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "DELETE FROM appointments WHERE Customer_ID = ?";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.setInt(1, customerId);
            ps.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
