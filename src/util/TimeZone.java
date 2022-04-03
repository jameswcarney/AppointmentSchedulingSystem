package util;

import javafx.beans.value.ObservableValue;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Simple utility class for handling storing some time zone conversion and validation information in a handy format.
 * @author James Carney
 */
public class TimeZone {
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final ZoneId EST = ZoneId.of("America/New_York");
    public static final ZoneId userZoneId = ZoneOffset.systemDefault();
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Validates times the user enters for an appointment against the company's business hours (EST).
     *
     * @param start The start time.
     * @param end The end time.
     * @return boolean representing whether the appointment is during business hours.
     */
    public static boolean validateAppointmentAgainstEST(ObservableValue<ZonedDateTime> start, ObservableValue<ZonedDateTime> end) {
        ZonedDateTime estStartTime = start.getValue().withZoneSameInstant(EST);
        ZonedDateTime estEndTime = end.getValue().withZoneSameInstant(EST);
        LocalDate meetingDate = estStartTime.toLocalDate();
        LocalTime businessStart = LocalTime.of(8, 0);
        LocalTime businessEnd = LocalTime.of(22, 0);
        ZonedDateTime validMeetingStart = ZonedDateTime.of(meetingDate, businessStart, EST);
        ZonedDateTime validMeetingEnd = ZonedDateTime.of(meetingDate, businessEnd, EST);


        if (estStartTime.isBefore(validMeetingStart) || estStartTime.isAfter(validMeetingEnd)) {
            return false;
        }
        else if (estEndTime.isBefore(validMeetingStart) || estEndTime.isAfter(validMeetingEnd)) {
            return false;
        }
        else return true;
    }
}
