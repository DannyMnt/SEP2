
package view;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Event;
import utill.TimeFormatter;
import viewmodel.CalendarViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static utill.TimeFormatter.formatLocalDateTime;

public class CalendarViewController implements PropertyChangeListener
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

    @FXML
    private ImageView smallProfilePictureView;

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
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1),calendarStartDate.plusMonths(1));
        System.out.println(events);
        reset();

        Circle clip = new Circle();
        clip.setCenterX(smallProfilePictureView.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(smallProfilePictureView.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(smallProfilePictureView.getFitWidth(), smallProfilePictureView.getFitHeight()) / 2); // Radius of the circle
        smallProfilePictureView.setClip(clip);
        calendarViewModel.addListener(this);
//    loadMonth(firstDayOfMonth, events);
//    gridPane.setGridLinesVisible(true);


    }

    private ObjectProperty<GridPane> gridPaneProperty() {
        return new SimpleObjectProperty<>(gridPane);
    }




    private void oldLoadMonth(LocalDate startDate, List<Event> events) {
//        for (int row = 0; row < gridPane.getRowCount(); row++) {
//            int eventPaneCountLast = 0;
//            for (int col = 0; col < gridPane.getColumnCount(); col++) {
//                try {
//                    // Load the sub-FXML file
//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("old_monthDayEntryView.fxml"));
//                    VBox cellContent = loader.load();
//                    VBox eventContent = (VBox) cellContent.lookup("#eventContainer");
//
//                    // Get the controller and set the date
//                    old_MonthDayEntryViewController controller = loader.getController();
//                    controller.setDate(startDate);
//                    HBox eventPane;
//                    // Add events to the cell
//                    int eventPaneCountCurrent = 0;
//                    for (Event event : events) {
//                        LocalDateTime eventStartDateTime = event.getStartTime();
//                        LocalDateTime eventEndDateTime = event.getEndTime();
//                        LocalDate eventStartDate = eventStartDateTime.toLocalDate();
//                        LocalDate eventEndDate = eventEndDateTime.toLocalDate();
//                        LocalTime eventStartTime = eventStartDateTime.toLocalTime();
//                        LocalTime eventEndTime = eventEndDateTime.toLocalTime();
//                        String style = "";
//                        if (startDate.compareTo(eventStartDate) >= 0 && startDate.compareTo(eventEndDate) <= 0) {
//                            eventPaneCountCurrent++;
//                            if (eventStartDate.equals(startDate) && eventEndDate.equals(
//                                    startDate)) {
//                                // Single-day event
//                                style = "classic";
//                            } else if (eventStartDate.equals(startDate)) {
//                                // Event starts on this day
//                                style = "left";
//                            } else if (eventEndDate.equals(startDate)) {
//                                // Event ends on this day
//                                style = "right";
//                            } else {
//                                // Event is in between start and end dates
//                                style = "full";
//                            }
//                            String text = (style.equals("left") || style.equals("classic")) ?
//                                    event.getTitle() :
//                                    "";
//                            if (style.equals("right") || style.equals("full")) {
//                                System.out.println(eventPaneCountLast);
//                                for (int i = 0; i < (eventPaneCountLast - eventPaneCountCurrent); i++) {
//                                    eventContent.getChildren().add(controller.createEmptyPane());
//                                    eventPaneCountCurrent++;
//
//                                }
//                            }
//                            eventPane = controller.createEventPane(style, text, formatLocalDateTime(event.getStartTime()));
//
//                            eventPane.setOnMouseClicked((MouseEvent e) -> {
//                                System.out.println(event.getEventId());
//                                mouseX = e.getScreenX();
//                                mouseY = e.getScreenY();
//                                try {
//                                    viewHandler.loadEventView(event.getEventId());
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            });
//
//                            eventContent.getChildren().add(eventPane);
//
//                        }
//
//                    }
//                    eventPaneCountLast = eventPaneCountCurrent;
//                    // Add the loaded content to the corresponding cell in the GridPane
//                    gridPane.add(cellContent, col, row);
//
//                    // Update the date for the next cell
//                    startDate = startDate.plusDays(1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    private void loadMonth(LocalDate startDate, List<Event> events) {
        System.out.println();
        System.out.println();
        System.out.println();
        int rowStep = 5;
        LocalDate currentDate = startDate;
        for (int row = 0; row < rowStep; row++) {
            for (int col = 0; col < 7; col++) {
                int dayOfMonth = currentDate.getDayOfMonth();

                // Create an HBox
                HBox hBox = new HBox();
                hBox.setPrefHeight(25); // Set preferred height
                hBox.setMaxHeight(25); // Set maximum height
                hBox.setAlignment(Pos.CENTER); // Center align the content

                // Create a Label for the day
                Label dateField = new Label(String.valueOf(dayOfMonth));
                dateField.setMaxHeight(25); // Set maximum height
                dateField.setAlignment(Pos.CENTER); // Center align the text

                // Add the Label to the HBox
                hBox.getChildren().add(dateField);

                // Add the HBox to the GridPane
                gridPane.add(hBox, col, row * rowStep);


                Iterator<Event> iterator = events.iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    LocalDateTime eventStartDateTime = event.getStartTime();
                    LocalDateTime eventEndDateTime = event.getEndTime();
                    LocalDate eventStartDate = eventStartDateTime.toLocalDate();
                    LocalDate eventEndDate = eventEndDateTime.toLocalDate();
                    LocalTime eventStartTime = eventStartDateTime.toLocalTime();
                    LocalTime eventEndTime = eventEndDateTime.toLocalTime();
                    String style = "";

                    if (currentDate.compareTo(eventStartDate) >= 0 && currentDate.compareTo(eventEndDate) <= 0) {// Check if event starts on the currentDate
                        iterator.remove();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("monthEventEntryView.fxml"));
                            HBox eventEntry = loader.load();

                            MonthEventEntryViewController controller = loader.getController();
                            controller.setEventTitleLabel(event.getTitle());
                            controller.setEventTimeLabel(TimeFormatter.formatLocalDateTime(eventStartDateTime));

                            eventEntry.setOnMouseClicked((MouseEvent e) -> {
                                System.out.println(event.getEventId());
                                mouseX = e.getScreenX();
                                mouseY = e.getScreenY();
                                try {
                                    viewHandler.loadEventView(event);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

//                            eventEntry.setStyle("-fx-background-color: #70BC47; -fx-background-radius: 5 5 5 5; -fx-background-insets: 3 5 3 5;");
//                            eventEntry.getStyleClass().add("event-pane");
//                            eventEntry.getStyleClass().add("classic");
                            for (int i = 0; i < 4; i++) {
                                if (isCellEmpty(gridPane, col, (row * rowStep) + i + 1)) {
                                    System.out.println("row: "+  ((row * rowStep) + i + 1));
                                    int daysBetween = (int) Duration.between(currentDate.atStartOfDay(), event.getEndTime()).toDays();
                                    GridPane.setConstraints(eventEntry, col, (row * rowStep) + i + 1, daysBetween + 1, 1);
                                    gridPane.getChildren().add(eventEntry);

                                    if (currentDate.isAfter(eventStartDate)) {
                                        eventEntry.getStyleClass().add("right");
                                        controller.setEventTitleLabel("");
                                    }
                                    if (col + daysBetween > 7) {
                                        System.out.println("overflow");
                                        eventEntry.getStyleClass().add("left");
                                        controller.setEventTimeLabel("");
//                                        System.out.println("current: " + currentDate.toString());
//                                        System.out.println("end: " + event.getEndTime().toString());
//                                        System.out.println(col);

//                                        System.out.println(daysBetween);
                                        int leftDays = daysBetween - (7 - col);
//                  System.out.println(leftDays);
                                        System.out.println(leftDays / 7 + 2);
                                        int countLeftDays = leftDays;
                                        for (int j = 1; j < leftDays / 7 + 2; j++) {
                                            if ((row + j) * (rowStep) > 25) {
                                                break;
                                            }
//                    System.out.println("count"+j);
//                    System.out.println("oldRow: "+ ((row * rowStep) + i)) ;
//                    System.out.println("row: " + (((row+j) * (rowStep)) + i) );
//                    System.out.println("colSpan:" + leftDays%7+1);

                                            FXMLLoader newLoader = new FXMLLoader(getClass().getResource("monthEventEntryView.fxml"));
                                            HBox newRowEventEntry = newLoader.load();

                                            MonthEventEntryViewController newController = newLoader.getController();
//                                            newController.setEventTitleLabel(event.getTitle());
//                                            newController.setEventTimeLabel(TimeFormatter.formatLocalDateTime(eventStartDateTime));

//                                            newRowEventEntry.setStyle("-fx-background-color: #70BC47; -fx-background-radius: 0 5 5 0; -fx-background-insets: 3 0 3 5;");
                                            for (int k = 0; k < 4; k++) {
                                                int rowShort = ((row + j) * (rowStep)) + k + 1;
                                                if (rowShort > 25) break;
//                                                System.out.println("row: " + rowShort);
                                                if (isCellEmpty(gridPane, col, rowShort)) {
                                                    GridPane.setConstraints(newRowEventEntry, 0, rowShort, countLeftDays + 1, 1);
                                                    gridPane.getChildren().add(newRowEventEntry);
                                                    break;
                                                }
                                            }
                                            countLeftDays -= 7;
                                            if (countLeftDays < 0) {
                                                newRowEventEntry.getStyleClass().add("right");
                                                newController.setEventTimeLabel(TimeFormatter.formatLocalDateTime(eventStartDateTime));
                                            } else {
                                                newRowEventEntry.getStyleClass().add("full");
                                            }

                                            newRowEventEntry.setOnMouseClicked((MouseEvent e) -> {
                                                System.out.println(event.getEventId());
                                                mouseX = e.getScreenX();
                                                mouseY = e.getScreenY();
                                                try {
                                                    viewHandler.loadEventView(event);
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            });


//                                            HBox newEventEntry = new HBox();
//                                            newEventEntry.setStyle("-fx-background-color: blue; -fx-background-radius: 0 5 5 0; -fx-background-insets: 3 0 3 5;");
//                                            for (int k = 0; k < 4; k++) {
//                                                if (isCellEmpty(gridPane, col, ((row + j) * (rowStep)) + k + 1)) {
//                                                    GridPane.setConstraints(newEventEntry, 0, ((row + j) * (rowStep)) + k + 1, countLeftDays + 1, 1);
//                                                    gridPane.getChildren().add(newEventEntry);
//                                                    break;
//                                                }
//                                            }
//                                            countLeftDays -= 7;

                                        }
                                    }

                                    break;
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }


                // Move to the next day
                currentDate = currentDate.plusDays(1);
            }
        }
//    for (Event event : events) {
//      long daysBetween = Duration.between(event.getStartTime(), event.getEndTime()).toDays();
//      System.out.println("Event: " + event.getTitle() + ", Duration: " + daysBetween + " days");
//    }

    }


    public void openEvent(Event event){

    }

    private boolean isCellEmpty(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeColIndex = GridPane.getColumnIndex(node);
            Integer nodeRowIndex = GridPane.getRowIndex(node);
            Integer nodeColSpan = GridPane.getColumnSpan(node);
            Integer nodeRowSpan = GridPane.getRowSpan(node);

            if (nodeColIndex == null) nodeColIndex = 0;
            if (nodeRowIndex == null) nodeRowIndex = 0;
            if (nodeColSpan == null) nodeColSpan = 1;
            if (nodeRowSpan == null) nodeRowSpan = 1;

            int nodeColEnd = nodeColIndex + nodeColSpan - 1;
            int nodeRowEnd = nodeRowIndex + nodeRowSpan - 1;

            if (col >= nodeColIndex && col <= nodeColEnd && row >= nodeRowIndex && row <= nodeRowEnd) {
                return false;
            }
        }
        return true;
    }

//  private void loadMonth(LocalDate startDate, List<Event> events) {
//    Map<LocalDate, List<Event>> eventMap = new HashMap<>();
//    for (Event event : events) {
//      LocalDate eventStartDate = event.getStartTime().toLocalDate();
//      LocalDate eventEndDate = event.getEndTime().toLocalDate();
//      LocalDate date = eventStartDate;
//
//      while (!date.isAfter(eventEndDate)) {
//        eventMap.computeIfAbsent(date, k -> new ArrayList<>()).add(event);
//        date = date.plusDays(1);
//      }
//    }
//
//    LocalDate currentDate = startDate;
//    for (int row = 0; row < gridPane.getRowCount(); row++) {
//      for (int col = 0; col < gridPane.getColumnCount(); col++) {
//        try {
//          FXMLLoader loader = new FXMLLoader(getClass().getResource("old_monthDayEntryView.fxml"));
//          VBox cellContent = loader.load();
//          VBox eventContent = (VBox) cellContent.lookup("#eventContainer");
//
//          MonthDayEntryViewController controller = loader.getController();
//          controller.setDate(currentDate);
//
//          List<Event> dayEvents = eventMap.getOrDefault(currentDate, Collections.emptyList());
//          int eventPaneCount = 0;
//
//          final LocalDate loopCurrentDate = currentDate;
//          List<Event> sortedEvents = dayEvents.stream()
//                  .sorted(Comparator.comparing(event -> {
//                    LocalDate startDateLoop = event.getStartTime().toLocalDate();
//                    LocalDate endDateLoop = event.getEndTime().toLocalDate();
//                    String style = getStyle(loopCurrentDate, startDateLoop, endDateLoop);
//                    return getOrder(style);
//                  }))
//                  .collect(Collectors.toList());
//
////          System.out.println(sortedEvents.t);
//          System.out.println(sortedEvents.size());
//          for (Event event : sortedEvents) {
//            LocalDateTime eventStartDateTime = event.getStartTime();
//            LocalDateTime eventEndDateTime = event.getEndTime();
//            LocalDate eventStartDate = eventStartDateTime.toLocalDate();
//            LocalDate eventEndDate = eventEndDateTime.toLocalDate();
//            String style = "";
//
//            if (currentDate.isEqual(eventStartDate) && currentDate.isEqual(eventEndDate)) {
//              style = "classic";
//            } else if (currentDate.isEqual(eventStartDate)) {
//              style = "left";
//            } else if (currentDate.isEqual(eventEndDate)) {
//              style = "right";
//            } else {
//              style = "full";
//            }
//
//            String text = (style.equals("left") || style.equals("classic")) ? event.getTitle() : "";
//            HBox eventPane = controller.createEventPane(style, text, formatLocalDateTime(event.getStartTime()));
//            eventPane.setOnMouseClicked((MouseEvent e) -> {
//              System.out.println(event.getEventId());
//              mouseX = e.getScreenX();
//              mouseY = e.getScreenY();
//              try {
//                viewHandler.loadEventView(event.getEventId());
//              } catch (IOException ex) {
//                throw new RuntimeException(ex);
//              }
//            });
//
////            while (eventPaneCount < dayEvents.indexOf(event)) {
////              eventContent.getChildren().add(controller.createEmptyPane());
////              eventPaneCount++;
////            }
//
//            eventContent.getChildren().add(eventPane);
//            eventPaneCount++;
//          }
//
//          gridPane.add(cellContent, col, row);
//          currentDate = currentDate.plusDays(1);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
//  }
//
//  // Helper method to determine the style of an event based on its start and end dates
//  private String getStyle(LocalDate currentDate, LocalDate eventStartDate, LocalDate eventEndDate) {
//    if (currentDate.isEqual(eventStartDate) && currentDate.isEqual(eventEndDate)) {
//      return "classic";
//    } else if (currentDate.isEqual(eventStartDate)) {
//      return "left";
//    } else if (currentDate.isEqual(eventEndDate)) {
//      return "right";
//    } else {
//      return "full";
//    }
//  }
//
//  // Helper method to assign numerical order to styles
//  private int getOrder(String style) {
//    switch (style) {
//      case "full":
//        return 0;
//      case "right":
//        return 1;
//      case "left":
//        return 2;
//      case "classic":
//        return 3;
//      default:
//        return Integer.MAX_VALUE; // Fallback to avoid comparison issues
//    }
//  }

    @FXML
    private void goToPreviousMonth() {
        this.calendarStartDate = calendarStartDate.minusMonths(1);
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));

        reset();
    }

    @FXML
    private void goToNextMonth() {

        this.calendarStartDate = calendarStartDate.plusMonths(1);
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));
        reset();

    }

    public Stage showOverlay(Stage ownerStage, Event event) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("eventView.fxml"));
        Event eventData = calendarViewModel.getEvent(event.getEventId());

        Parent overlayContent = loader.load();
        EventViewController eventViewController = loader.getController();


        // Create overlay stage
        Stage overlayStage = new Stage(StageStyle.TRANSPARENT);
        overlayStage.initOwner(ownerStage);

        eventViewController.init(overlayStage, this, calendarViewModel, eventData);
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
        Platform.runLater(() -> {
            //    gridPane.getChildren().clear();
            System.out.println("wehere reset here");
            gridPane.getChildren().clear();
            gridPane.setGridLinesVisible(true);
            //    gridPane.getChildren().removeIf(node -> !(node instanceof ColumnConstraints || node instanceof RowConstraints));
            calendarViewModel.reset();
            //profilePictureView.setImage(new Image());
            DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
            int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
            this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));

            LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);
            monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + calendarStartDate.getYear());
            System.out.println(events);
            loadMonth(firstMondayDate, events);
        });

    }

    @FXML
    private void gotoCalendar() {
        viewHandler.openView("calendar");
    }

    @FXML
    private void gotoChat() {
        viewHandler.openView("chat");
    }

    public void openProfileView() {
        viewHandler.openView("profile");
    }

    public void openCreateEventView() {
        viewHandler.openView("addEvent");
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        System.out.println("we here in the view");
        System.out.println(events);
        if("viewmodelEventAdd".equals(evt.getPropertyName())){
            System.out.println("here adding event");
            System.out.println(events);
            reset();
        }else if ("viewmodelEventRemove".equals(evt.getPropertyName())){
            System.out.println("here removing event");

            reset();
        }

    }
}

