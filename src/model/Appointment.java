package model;

import javafx.beans.value.ObservableValue;
import java.time.ZonedDateTime;

/**
 * A class representing an appointment
 *
 * @author James Carney
 */
public class Appointment {
    private ObservableValue<Integer> appointmentId;
    private ObservableValue<String> title;
    private ObservableValue<String> description;
    private ObservableValue<String> location;
    private ObservableValue<String> type;
    private ObservableValue<ZonedDateTime> start;
    private ObservableValue<ZonedDateTime> end;
    private ObservableValue<Integer> customerId;
    private ObservableValue<Integer> userId;
    private ObservableValue<Integer> contactId;

    /**
     * Class constructor with associated appointmentId. Used when editing an existing appointment.
     *
     * @param appointmentId The ID of the appointment.
     * @param title The title of the appoingment.
     * @param description A description of the appointment.
     * @param location The location of the appointment.
     * @param type The type of appointment.
     * @param start The appointment start time.
     * @param end The appointment end time.
     * @param customerId The ID of the customer with whom the appointment is scheduled.
     * @param userId The ID of the user making the appointment.
     * @param contactId The contact info of a person related to the appointment.
     */
    public Appointment(ObservableValue<Integer> appointmentId, ObservableValue<String> title, ObservableValue<String> description,
                       ObservableValue<String> location, ObservableValue<String> type, ObservableValue<ZonedDateTime> start, ObservableValue<ZonedDateTime> end,
                       ObservableValue<Integer> customerId, ObservableValue<Integer> userId, ObservableValue<Integer> contactId) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * Class constructor without appointment ID. Used during creating of a new appointment.
     *
     * @param title The title of the appointment.
     * @param description A description of the appointment.
     * @param location The location of the appointment.
     * @param type The type of appointment.
     * @param start The appointment start time.
     * @param end The appointment end time.
     * @param customerId The ID of the customer with whom the appointment is scheduled.
     * @param userId The ID of the user making the appointment.
     * @param contactId The contact info of a person related to the appointment.
     */
    public Appointment(ObservableValue<String> title, ObservableValue<String> description,
                       ObservableValue<String> location, ObservableValue<String> type, ObservableValue<ZonedDateTime> start, ObservableValue<ZonedDateTime> end,
                       ObservableValue<Integer> customerId, ObservableValue<Integer> userId, ObservableValue<Integer> contactId) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public Appointment() {

    }

    /**
     * Gets the ID of an appointment.
     *
     * @return the appointment ID.
     */
    public ObservableValue<Integer> getAppointmentId() {
        return appointmentId;
    }

    /**
     * Set the ID of an appointment.
     *
     * @param appointmentId The appointment ID.
     */
    public void setAppointmentId(ObservableValue<Integer> appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the title of an appointment.
     *
     * @return the title.
     */
    public ObservableValue<String> getTitle() {
        return title;
    }

    /**
     * Sets the title of an appointment.
     *
     * @param title The title to be set.
     */
    public void setTitle(ObservableValue<String> title) {
        this.title = title;
    }

    /**
     * Gets a description of the appointment.
     *
     * @return the description.
     */
    public ObservableValue<String> getDescription() {
        return description;
    }

    /**
     * Sets the description of an appointment.
     *
     * @param description The appointment description.
     */
    public void setDescription(ObservableValue<String> description) {
        this.description = description;
    }

    /**
     * Gets the location of an appointment.
     *
     * @return the location.
     */
    public ObservableValue<String> getLocation() {
        return location;
    }

    /**
     * Sets the location of an appointment.
     *
     * @param location The location.
     */
    public void setLocation(ObservableValue<String> location) {
        this.location = location;
    }

    /**
     * Gets the type of appointment.
     *
     * @return The type.
     */
    public ObservableValue<String> getType() {
        return type;
    }

    /**
     * Sets the appointment type.
     *
     * @param type The appointment type.
     */
    public void setType(ObservableValue<String> type) {
        this.type = type;
    }

    /**
     * Gets the start time of an appointment.
     *
     * @return The start time.
     */
    public ObservableValue<ZonedDateTime> getStart() {
        return start;
    }

    /**
     * Sets the start time of an appointment.
     *
     * @param start The start time.
     */
    public void setStart(ObservableValue<ZonedDateTime> start) {
        this.start = start;
    }

    /**
     * Gets the end time of an appointment.
     *
     * @return The end time.
     */
    public ObservableValue<ZonedDateTime> getEnd() {
        return end;
    }

    /**
     * Sets the end time of an appointment.
     *
     * @param end The end time.
     */
    public void setEnd(ObservableValue<ZonedDateTime> end) {
        this.end = end;
    }

    /**
     * Gets the ID of the customer associated with the appointment.
     *
     * @return The customer ID.
     */
    public ObservableValue<Integer> getCustomerId() {
        return customerId;
    }

    /**
     * Sets the ID of the customer associated with the appointment.
     *
     * @param customerId The customer ID.
     */
    public void setCustomerId(ObservableValue<Integer> customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the ID of the user who created the appointment.
     *
     * @return The user ID.
     */
    public ObservableValue<Integer> getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the user who created the appointment.
     *
     * @param userId The user ID.
     */
    public void setUserId(ObservableValue<Integer> userId) {
        this.userId = userId;
    }

    /**
     * Gets the ID of a contact associated with the appointment.
     *
     * @return The contact ID.
     */
    public ObservableValue<Integer> getContactId() {
        return contactId;
    }

    /**
     * Sets the contact ID of a contact associated with the appointment.
     *
     * @param contactId The contact ID.
     */
    public void setContactId(ObservableValue<Integer> contactId) {
        this.contactId = contactId;
    }
}
