package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Event;
import viewmodel.CalendarViewModel;



import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

public class CalendarViewController {
  private ViewHandler viewHandler;
  private CalendarViewModel calendarViewModel;
  private Region root;

  @FXML private Label monthLabel;

  List<Event> events;

  @FXML
  private GridPane gridPane;
  private LocalDate calendarStartDate;

  public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root) {
    this.viewHandler = viewHandler;
    this.calendarViewModel = calendarViewModel;
    this.root = root;
    this.events = calendarViewModel.getEvents();
    System.out.println(events);

    LocalDate today = LocalDate.now();

    LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

    int startingDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
    this.calendarStartDate = firstDayOfMonth;
    System.out.println(calendarStartDate);

    // Determine the number of days to show from the previous month
    int daysFromPreviousMonth = startingDayOfWeek - 1;

    monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

    // Load cells for the previous month
    LocalDate startDate = firstDayOfMonth.minusDays(daysFromPreviousMonth);

    loadMonth(startDate,events);
  }

  private void loadMonth(LocalDate startDate, List<Event> events) {
    for (int row = 0; row < gridPane.getRowCount(); row++) {
      for (int col = 0; col < gridPane.getColumnCount(); col++) {
        try {
          // Load the sub-FXML file
          FXMLLoader loader = new FXMLLoader(getClass().getResource("monthDayEntryView.fxml"));
          VBox cellContent = loader.load();
          GridPane eventCont = (GridPane) cellContent.lookup("#eventContainer");
          // Get the controller and set the date
          MonthDayEntryViewController controller = loader.getController();
          controller.setDate(startDate);

          // Add events to the cell
          for (Event event : events) {
            LocalDateTime eventStartDateTime = event.getStartTime();
            LocalDateTime eventEndDateTime = event.getEndTime();
            LocalDate eventStartDate = eventStartDateTime.toLocalDate();
            LocalDate eventEndDate = eventEndDateTime.toLocalDate();
            LocalTime eventStartTime = eventStartDateTime.toLocalTime();
            LocalTime eventEndTime = eventEndDateTime.toLocalTime();
            String style = "";
            if (startDate.compareTo(eventStartDate) >= 0 && startDate.compareTo(eventEndDate) <= 0) {


              if (eventStartDate.equals(startDate) && eventEndDate.equals(startDate)) {
                // Single-day event
                style = "classic";
              } else if (eventStartDate.equals(startDate)) {
                // Event starts on this day
                style = "left";
              } else if (eventEndDate.equals(startDate)) {
                // Event ends on this day
                style = "right";
              } else {
                // Event is in between start and end dates
                style = "full";
              }
              String text = (style.equals("left") || style.equals("classic")) ? event.getTitle() : "";
              System.out.println(style);
              Pane eventPane = controller.createEventPane(style,text);
              eventCont.add(eventPane,0,0);
            }
          }

          // Add the loaded content to the corresponding cell in the GridPane
          gridPane.add(cellContent, col, row);

          // Update the date for the next cell
          startDate = startDate.plusDays(1);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }





  @FXML
  private void goToPreviousMonth() {
    this.calendarStartDate = calendarStartDate.minusMonths(1);
    DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
    int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
    LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);

    reset();
    monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

    loadMonth(firstMondayDate,events);
  }

  @FXML
  private void goToNextMonth() {

    this.calendarStartDate = calendarStartDate.plusMonths(1);
    DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
    int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
    LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);

    reset();
    monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

    loadMonth(firstMondayDate,events);

  }


  public Region getRoot() {
    return root;
  }

  public void reset(){
    gridPane.getChildren().clear();
  }


}
