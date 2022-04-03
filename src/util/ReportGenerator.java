package util;

import DAO.DBConnection;
import DAO.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.DateFormatSymbols;

import java.time.ZonedDateTime;

/**
 * Generates reports for the Report screen.
 *
 * @author James Carney
 */
public class ReportGenerator {
    private ObservableList<AppointmentsByType> allByType = FXCollections.observableArrayList();
    private ObservableList<ScheduleByContact> allSchedules = FXCollections.observableArrayList();
    private ObservableList<CustomersByCountry> allCountries = FXCollections.observableArrayList();

    /**
     * A subclass that stores information for the "Appointments by Type" report.
     */
    public class AppointmentsByType {
        private int month;
        private String type;
        private int count;
        private String monthString;

        /**
         * Class constructor.
         *
         * @param month The month of an appointment.
         * @param type The type of appointment.
         * @param count The number of a given type of appointment in the given month.
         */
        public AppointmentsByType(int month, String type, int count) {
            this.month = month;
            this.type = type;
            this.count = count;
            this.monthString = new DateFormatSymbols().getMonths()[month - 1];
        }

        /**
         * Gets the month of an appointment.
         *
         * @return The month.
         */
        public int getMonth() {
            return month;
        }

        /**
         * Gets the type of an appointment.
         *
         * @return The type,
         */
        public String getType() {
            return type;
        }

        /**
         * Gets the number of an appointment of a certain type in a given month.
         *
         * @return The number of appointments.
         */
        public int getCount() {
            return count;
        }

        /**
         * Converts the month integer representation from the database query into a string of the month name.
         *
         * @return The month.
         */
        public String getMonthString() {
            return monthString;
        }
    }

    /**
     * A subclass that stores information for the "Schedule by Contact" report.
     */
    public class ScheduleByContact {

        private int contactId;
        private int appointmentId;
        private String title;
        private String description;
        private String type;
        private ZonedDateTime start;
        private ZonedDateTime end;
        private int customerId;

        /**
         * Class constructor.
         *
         * @param contactId The contact's ID.
         * @param appointmentId The appointment ID.
         * @param title The appointment title.
         * @param description The appointment description.
         * @param type The appointment type.
         * @param start The appointment start time.
         * @param end The appointment end time.
         * @param customerId The customer's ID.
         */
        public ScheduleByContact(int contactId, int appointmentId, String title, String description, String type, ZonedDateTime start, ZonedDateTime end, int customerId) {
            this.contactId = contactId;
            this.appointmentId = appointmentId;
            this.title = title;
            this.description = description;
            this.type = type;
            this.start = start;
            this.end = end;
            this.customerId = customerId;
        }

        /**
         * Gets the ID of a contact.
         *
         * @return The contact ID.
         */
        public int getContactId() {
            return contactId;
        }

        /**
         * Gets the ID of an appointment.
         *
         * @return The appointment ID.
         */
        public int getAppointmentId() {
            return appointmentId;
        }

        /**
         * Gets the title of an appointment.
         *
         * @return The title.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets the description of an appointment.
         *
         * @return The description.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Gets the type of an appointment.
         *
         * @return The type.
         */
        public String getType() {
            return type;
        }

        /**
         * Gets the start time of an appointment.
         *
         * @return The start time.
         */
        public ZonedDateTime getStart() {
            return start;
        }

        /**
         * Gets the end time of an appointment.
         *
         * @return The end time.
         */
        public ZonedDateTime getEnd() {
            return end;
        }

        /**
         * Gets the ID of a customer associated with an appointment.
         *
         * @return The customer ID.
         */
        public int getCustomerId() {
            return customerId;
        }
    }

    /**
     * A subclass that stores information for the "Customers by Country" report.
     */
    public class CustomersByCountry {
        private int countryId;
        private String countryName;
        private int customerCount;

        /**
         * Class constructor.
         *
         * @param countryId The ID number of a country.
         * @param customerCount The number of customers from a given country.
         */
        public CustomersByCountry(int countryId, int customerCount) {
            this.countryId = countryId;
            this.customerCount = customerCount;

            if (countryId == 1)
                this.countryName = "U.S.";
            else if (countryId == 2)
                this.countryName = "U.K.";
            else if (countryId == 3)
                this.countryName = "Canada";
        }

        /**
         * Gets the ID of a country.
         *
         * @return The country ID.
         */
        public int getCountryId() {
            return countryId;
        }

        /**
         * Gets the customer count for a country.
         *
         * @return The customer count.
         */
        public int getCustomerCount() {
            return customerCount;
        }

        /**
         * Gets the name of a country.
         *
         * @return The name.
         */
        public String getCountryName() {
            return countryName;
        }
    }

    /**
     * Queries the database to populate the "Appointments by Type" report.
     */
    public void processAppointmentsByType() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT month(Start), Type, COUNT(Start) FROM appointments GROUP BY Type ORDER BY Start";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int month = rs.getInt(1);
                String type = rs.getString(2);
                int count = rs.getInt(3);

                AppointmentsByType result = new AppointmentsByType(month, type, count);
                allByType.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *Gets a list of appointments sorted by type.
     *
     * @return The list.
     */
    public ObservableList<AppointmentsByType> getAppointmentsByType() {
        return allByType;
    }

    /**
     * Queries the database to populate the "Schedule by Contact" report.
     */
    public void processScheduleByContact() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT Contact_ID, Appointment_Id, Title, Type, Description,  Start, End, Customer_ID FROM appointments  ORDER BY Contact_ID, Start";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int contactId = rs.getInt(1);
                int appointmentId = rs.getInt(2);
                String title = rs.getString(3);
                String description = rs.getString(4);
                String type = rs.getString(5);
                Timestamp startTs = rs.getTimestamp(6);
                Timestamp endTs = rs.getTimestamp(7);
                int customerId = rs.getInt(8);

                ZonedDateTime startUtc = startTs.toLocalDateTime().atZone(TimeZone.UTC);
                ZonedDateTime endUtc = endTs.toLocalDateTime().atZone(TimeZone.UTC);
                ZonedDateTime startUser = startUtc.withZoneSameInstant(TimeZone.userZoneId);
                ZonedDateTime endUser = endUtc.withZoneSameInstant(TimeZone.userZoneId);

                ScheduleByContact schedule = new ScheduleByContact(contactId, appointmentId, title, description, type, startUser, endUser, customerId);
                allSchedules.add(schedule);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of all schedules sorted by contact.
     *
     * @return The list.
     */
    public ObservableList<ScheduleByContact> getSchedulesByContact() { return allSchedules; }

    /**
     * Queries the database to populate the "Customers by Country" report.
     */
    public void processCustomersByCountry() {
        try {
            Connection conn = DBConnection.getConnection();
            String sqlStatement = "SELECT first_level_divisions.COUNTRY_ID, COUNT(*) FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID GROUP BY COUNTRY_ID ORDER BY COUNTRY_ID";
            Query.setPreparedStatement(conn, sqlStatement);
            PreparedStatement ps = Query.getPreparedStatement();

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                int countryId = rs.getInt(1);
                int count = rs.getInt(2);

                CustomersByCountry cbc = new CustomersByCountry(countryId, count);
                allCountries.add(cbc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of the number of customers in each country.
     *
     * @return The list.
     */
    public ObservableList<CustomersByCountry> getCustomersByCountry() { return allCountries; }
}

