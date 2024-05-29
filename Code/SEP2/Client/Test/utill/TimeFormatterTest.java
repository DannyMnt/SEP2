package utill;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeFormatterTest
{
  @Test
  void testFormatEventDates() {
    assertThrows(IllegalArgumentException.class, () -> TimeFormatter.formatEventDates(null, null));

    LocalDateTime startDate = LocalDateTime.of(2024, 5, 29, 12, 0);
    assertThrows(IllegalArgumentException.class, () -> TimeFormatter.formatEventDates(startDate, null));
    assertThrows(IllegalArgumentException.class, () -> TimeFormatter.formatEventDates(null, startDate));

    LocalDateTime endDate = LocalDateTime.of(2024, 5, 29, 14, 0);
    assertEquals("29. 5. 2024 12:00 to 14:00", TimeFormatter.formatEventDates(startDate, endDate));

    LocalDateTime diffDaySameTimeStart = LocalDateTime.of(2024, 5, 29, 12, 0);
    LocalDateTime diffDaySameTimeEnd = LocalDateTime.of(2024, 5, 30, 12, 0);
    assertEquals("29. 5. 2024 to 30. 5. 2024", TimeFormatter.formatEventDates(diffDaySameTimeStart, diffDaySameTimeEnd));

    LocalDateTime sameDayDiffTimeStart = LocalDateTime.of(2024, 5, 29, 10, 0);
    LocalDateTime sameDayDiffTimeEnd = LocalDateTime.of(2024, 5, 29, 14, 30);
    assertEquals("29. 5. 2024 10:00 to 14:30", TimeFormatter.formatEventDates(sameDayDiffTimeStart, sameDayDiffTimeEnd));

     diffDaySameTimeStart = LocalDateTime.of(2024, 5, 29, 10, 0);
     diffDaySameTimeEnd = LocalDateTime.of(2024, 5, 30, 10, 0);
    assertEquals("29. 5. 2024 to 30. 5. 2024", TimeFormatter.formatEventDates(diffDaySameTimeStart, diffDaySameTimeEnd));
  }

}