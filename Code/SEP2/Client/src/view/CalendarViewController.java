package view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import viewmodel.CalendarViewModel;



import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class CalendarViewController {
  private ViewHandler viewHandler;
  private CalendarViewModel calendarViewModel;
  private Region root;

  @FXML private Label monthLabel;



  @FXML
  private GridPane gridPane;
  private LocalDate calendarStartDate;

  public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root) {
    this.viewHandler = viewHandler;
    this.calendarViewModel = calendarViewModel;
    this.root = root;

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

    loadMonth(startDate);
  }

  private void loadMonth(LocalDate startDate) {
    for (int row = 0; row < gridPane.getRowCount(); row++) {
      for (int col = 0; col < gridPane.getColumnCount(); col++) {
        try {
          // Load the sub-FXML file
          FXMLLoader loader = new FXMLLoader(getClass().getResource("dayEntryView.fxml"));
          Pane cellContent = loader.load();

          // Get the controller and set the date
          DayEntryViewController controller = loader.getController();
          controller.setDate(startDate);

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

    loadMonth(firstMondayDate);
  }

  @FXML
  private void goToNextMonth() {

    this.calendarStartDate = calendarStartDate.plusMonths(1);
    DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
    int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
    LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);

    reset();
    monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));

    loadMonth(firstMondayDate);

  }


  public Region getRoot() {
    return root;
  }

  public void reset(){
    gridPane.getChildren().clear();
  }


}
