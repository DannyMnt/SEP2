package utill;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    public static String roundDownToHourAndFormat(LocalDateTime dateTime) {
        LocalDateTime roundedDateTime = dateTime.withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return roundedDateTime.format(formatter);
    }

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
