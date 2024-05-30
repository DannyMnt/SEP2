package utill;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A utility class for formatting time and dates.
 */
public class TimeFormatter {

    /**
     * Formats a LocalDateTime object to a string representation of time in "HH:mm" format.
     *
     * @param localDateTime The LocalDateTime object to be formatted.
     * @return A string representation of the time in "HH:mm" format.
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    /**
     * Rounds down a LocalDateTime object to the nearest hour and formats it to "HH:mm" format.
     *
     * @param dateTime The LocalDateTime object to be rounded down and formatted.
     * @return A string representation of the rounded down time in "HH:mm" format.
     */
    public static String roundDownToHourAndFormat(LocalDateTime dateTime) {
        LocalDateTime roundedDateTime = dateTime.withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return roundedDateTime.format(formatter);
    }

    /**
     * Formats event start and end dates to a human-readable string.
     *
     * @param startDate The start date of the event.
     * @param endDate   The end date of the event.
     * @return A string representation of the event dates.
     * @throws IllegalArgumentException If either startDate or endDate is null.
     */
    public static String formatEventDates(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date cant be null");
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d. M. yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            String date = startDate.format(dateFormatter);
            String startTime = startDate.format(timeFormatter);
            String endTime = endDate.format(timeFormatter);
            return String.format("%s %s to %s", date, startTime, endTime);
        } else {
            String start = startDate.format(dateFormatter);
            String end = endDate.format(dateFormatter);
            return String.format("%s to %s", start, end);
        }
    }
}
