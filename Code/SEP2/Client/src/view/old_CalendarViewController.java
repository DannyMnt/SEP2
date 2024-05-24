
package view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Event;
import viewmodel.CalendarViewModel;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static utill.TimeFormatter.formatLocalDateTime;

public class old_CalendarViewController
{
  private ViewHandler viewHandler;
  private CalendarViewModel calendarViewModel;
  private Region root;

  @FXML
  private Label monthLabel;


  List<Event> events;

  @FXML
  private GridPane gridPane;
  private LocalDate calendarStartDate;

  private double mouseX;
  private double mouseY;

  private Stage eventStage;

  @FXML private ImageView smallProfilePictureView;

  public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root) {
    this.viewHandler = viewHandler;
    this.calendarViewModel = calendarViewModel;
    this.root = root;
   // this.events = calendarViewModel.getEvents(); TO BE SOLVED
    System.out.println(events);

    monthLabel.textProperty().bindBidirectional(calendarViewModel.getMonthLabelProperty());
    smallProfilePictureView.imageProperty().bindBidirectional(calendarViewModel.getImageProperty());

    LocalDate today = LocalDate.now();

    LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

    this.calendarStartDate = firstDayOfMonth;

    reset();

    Circle clip = new Circle();
    clip.setCenterX(smallProfilePictureView.getFitWidth() / 2); // Center X of the circle
    clip.setCenterY(smallProfilePictureView.getFitHeight() / 2); // Center Y of the circle
    clip.setRadius(Math.min(smallProfilePictureView.getFitWidth(), smallProfilePictureView.getFitHeight()) / 2); // Radius of the circle
    smallProfilePictureView.setClip(clip);


  }

  private ObjectProperty<GridPane> gridPaneProperty() {
    return new SimpleObjectProperty<>(gridPane);
  }


//  private void loadMonth(LocalDate startDate, List<Event> events) {
//    for (int row = 0; row < gridPane.getRowCount(); row++) {
//      int eventPaneCountLast = 0;
//      for (int col = 0; col < gridPane.getColumnCount(); col++) {
//        try {
//          // Load the sub-FXML file
//          FXMLLoader loader = new FXMLLoader(getClass().getResource("old_monthDayEntryView.fxml"));
//          VBox cellContent = loader.load();
//          VBox eventContent = (VBox) cellContent.lookup("#eventContainer");
//
//          // Get the controller and set the date
//          MonthDayEntryViewController controller = loader.getController();
//          controller.setDate(startDate);
//          HBox eventPane;
//          // Add events to the cell
//          int eventPaneCountCurrent = 0;
//          for (Event event : events) {
//            LocalDateTime eventStartDateTime = event.getStartTime();
//            LocalDateTime eventEndDateTime = event.getEndTime();
//            LocalDate eventStartDate = eventStartDateTime.toLocalDate();
//            LocalDate eventEndDate = eventEndDateTime.toLocalDate();
//            LocalTime eventStartTime = eventStartDateTime.toLocalTime();
//            LocalTime eventEndTime = eventEndDateTime.toLocalTime();
//            String style = "";
//            if (startDate.compareTo(eventStartDate) >= 0 && startDate.compareTo(eventEndDate) <= 0) {
//              eventPaneCountCurrent++;
//              if (eventStartDate.equals(startDate) && eventEndDate.equals(
//                      startDate)) {
//                // Single-day event
//                style = "classic";
//              } else if (eventStartDate.equals(startDate)) {
//                // Event starts on this day
//                style = "left";
//              } else if (eventEndDate.equals(startDate)) {
//                // Event ends on this day
//                style = "right";
//              } else {
//                // Event is in between start and end dates
//                style = "full";
//              }
//              String text = (style.equals("left") || style.equals("classic")) ?
//                      event.getTitle() :
//                      "";
//              if(style.equals("right") || style.equals("full")){
//                System.out.println(eventPaneCountLast);
//                for (int i = 0; i < (eventPaneCountLast-eventPaneCountCurrent); i++) {
//                  eventContent.getChildren().add(controller.createEmptyPane());
//                  eventPaneCountCurrent++;
//
//                }
//              }
//              eventPane = controller.createEventPane(style, text, formatLocalDateTime(event.getStartTime()));
//
//              eventPane.setOnMouseClicked((MouseEvent e) -> {
//                System.out.println(event.getEventId());
//                mouseX = e.getScreenX();
//                mouseY = e.getScreenY();
//                try {
//                  viewHandler.loadEventView(event.getEventId());
//                } catch (IOException ex) {
//                  throw new RuntimeException(ex);
//                }
//              });
//
//              eventContent.getChildren().add(eventPane);
//
//            }
//
//          }
//          eventPaneCountLast = eventPaneCountCurrent;
//          // Add the loaded content to the corresponding cell in the GridPane
//          gridPane.add(cellContent, col, row);
//
//          // Update the date for the next cell
//          startDate = startDate.plusDays(1);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }

  private void loadMonth(LocalDate startDate, List<Event> events) {
    Map<LocalDate, List<Event>> eventMap = new HashMap<>();
    for (Event event : events) {
      LocalDate eventStartDate = event.getStartTime().toLocalDate();
      LocalDate eventEndDate = event.getEndTime().toLocalDate();
      LocalDate date = eventStartDate;

      while (!date.isAfter(eventEndDate)) {
        eventMap.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
        date = date.plusDays(1);
      }
    }

    LocalDate currentDate = startDate;
    for (int row = 0; row < gridPane.getRowCount(); row++) {
      for (int col = 0; col < gridPane.getColumnCount(); col++) {
        try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("old_monthDayEntryView.fxml"));
          VBox cellContent = loader.load();
          VBox eventContent = (VBox) cellContent.lookup("#eventContainer");

          old_MonthDayEntryViewController controller = loader.getController();
          controller.setDate(currentDate);

          List<Event> dayEvents = eventMap.getOrDefault(currentDate, Collections.emptyList());
          int eventPaneCount = 0;

          final LocalDate loopCurrentDate = currentDate;
          List<Event> sortedEvents = dayEvents.stream()
                  .sorted(Comparator.comparing(event -> {
                    LocalDate startDateLoop = event.getStartTime().toLocalDate();
                    LocalDate endDateLoop = event.getEndTime().toLocalDate();
                    String style = getStyle(loopCurrentDate, startDateLoop, endDateLoop);
                    return getOrder(style);
                  }))
                  .collect(Collectors.toList());

//          System.out.println(sortedEvents.t);
          System.out.println(sortedEvents.size());
          for (Event event : sortedEvents) {
            LocalDateTime eventStartDateTime = event.getStartTime();
            LocalDateTime eventEndDateTime = event.getEndTime();
            LocalDate eventStartDate = eventStartDateTime.toLocalDate();
            LocalDate eventEndDate = eventEndDateTime.toLocalDate();
            String style = "";

            if (currentDate.isEqual(eventStartDate) && currentDate.isEqual(eventEndDate)) {
              style = "classic";
            } else if (currentDate.isEqual(eventStartDate)) {
              style = "left";
            } else if (currentDate.isEqual(eventEndDate)) {
              style = "right";
            } else {
              style = "full";
            }

            String text = (style.equals("left") || style.equals("classic")) ? event.getTitle() : "";
            HBox eventPane = controller.createEventPane(style, text, formatLocalDateTime(event.getStartTime()));
            eventPane.setOnMouseClicked((MouseEvent e) -> {
              System.out.println(event.getEventId());
              mouseX = e.getScreenX();
              mouseY = e.getScreenY();
//              try {
////                viewHandler.loadEventView(event.getEventId());
//              } catch (IOException ex) {
//                throw new RuntimeException(ex);
//              }
            });

//            while (eventPaneCount < dayEvents.indexOf(event)) {
//              eventContent.getChildren().add(controller.createEmptyPane());
//              eventPaneCount++;
//            }

            eventContent.getChildren().add(eventPane);
            eventPaneCount++;
          }

          gridPane.add(cellContent, col, row);
          currentDate = currentDate.plusDays(1);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // Helper method to determine the style of an event based on its start and end dates
  private String getStyle(LocalDate currentDate, LocalDate eventStartDate, LocalDate eventEndDate) {
    if (currentDate.isEqual(eventStartDate) && currentDate.isEqual(eventEndDate)) {
      return "classic";
    } else if (currentDate.isEqual(eventStartDate)) {
      return "left";
    } else if (currentDate.isEqual(eventEndDate)) {
      return "right";
    } else {
      return "full";
    }
  }

  // Helper method to assign numerical order to styles
  private int getOrder(String style) {
    switch (style) {
      case "full":
        return 0;
      case "right":
        return 1;
      case "left":
        return 2;
      case "classic":
        return 3;
      default:
        return Integer.MAX_VALUE; // Fallback to avoid comparison issues
    }
  }

  @FXML
  private void goToPreviousMonth() {
    this.calendarStartDate = calendarStartDate.minusMonths(1);
    reset();
  }

  @FXML
  private void goToNextMonth() {

    this.calendarStartDate = calendarStartDate.plusMonths(1);

    reset();

  }

  public Stage showOverlay(Stage ownerStage, UUID eventId) throws IOException {
    // Load the FXML file
    FXMLLoader loader = new FXMLLoader(getClass().getResource("eventView.fxml"));
    Event eventData = calendarViewModel.getEvent(eventId);

    Parent overlayContent = loader.load();
    EventViewController eventViewController = loader.getController();
    //eventViewController.init(calendarViewModel, eventData);


    // Create overlay stage
    Stage overlayStage = new Stage(StageStyle.TRANSPARENT);
    overlayStage.initOwner(ownerStage);
//    overlayStage.initModality(Modality.APPLICATION_MODAL);
//    overlayStage.setAlwaysOnTop(true);

    // Set up the scene
    Scene overlayScene = new Scene(overlayContent);
    overlayScene.setFill(null); // Make the scene transparent

    overlayStage.setScene(overlayScene);

    // Position the overlay relative to the owner stage
    overlayStage.setOnShown(e -> {
      double overlayHeight = overlayStage.getHeight(); // Get the height of the overlay stage
      overlayStage.setX(mouseX);
      overlayStage.setY(mouseY - (overlayHeight / 2));
    });

    overlayStage.show();
    return overlayStage;
  }



  public Region getRoot() {
    return root;
  }

  public void reset() {
    gridPane.getChildren().clear();
    calendarViewModel.reset();
//profilePictureView.setImage(new Image());
    this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));
    DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
    int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
    LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);
    monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + calendarStartDate.getYear());

    loadMonth(firstMondayDate, events);
  }

  @FXML
  private void gotoCalendar(){
    viewHandler.openView("calendar");
  }
  @FXML
  private void gotoChat(){
    viewHandler.openView("chat");
  }

  public void openProfileView() {
    viewHandler.openView("profile");
  }

  public void openCreateEventView() {
    viewHandler.openView("addEvent");
  }

}

