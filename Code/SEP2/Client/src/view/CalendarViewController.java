
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


public class CalendarViewController implements PropertyChangeListener {
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

        monthLabel.textProperty().bindBidirectional(calendarViewModel.getMonthLabelProperty());
        smallProfilePictureView.imageProperty().bindBidirectional(calendarViewModel.getImageProperty());

        LocalDate today = LocalDate.now();

        LocalDate firstDayOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());

        this.calendarStartDate = firstDayOfMonth;
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));
        reset();

        Circle clip = new Circle();
        clip.setCenterX(smallProfilePictureView.getFitWidth() / 2); // Center X of the circle
        clip.setCenterY(smallProfilePictureView.getFitHeight() / 2); // Center Y of the circle
        clip.setRadius(Math.min(smallProfilePictureView.getFitWidth(), smallProfilePictureView.getFitHeight()) / 2); // Radius of the circle
        smallProfilePictureView.setClip(clip);
        calendarViewModel.addListener(this);


    }
    private void loadMonth(LocalDate startDate, List<Event> events) {
        int rowStep = 5;
        LocalDate currentDate = startDate;
        for (int row = 0; row < rowStep; row++) {
            for (int col = 0; col < 7; col++) {
                int dayOfMonth = currentDate.getDayOfMonth();
                int moreEventCount = 0;

                HBox hBox = new HBox();
                hBox.setPrefHeight(25);
                hBox.setMaxHeight(25);
                hBox.setAlignment(Pos.CENTER);

                Label dateField = new Label(String.valueOf(dayOfMonth));
                dateField.setMaxHeight(25);
                dateField.setAlignment(Pos.CENTER);

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

                    if (currentDate.compareTo(eventStartDate) >= 0 && currentDate.compareTo(eventEndDate) <= 0) {
                        iterator.remove();

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("monthEventEntryView.fxml"));
                            HBox eventEntry = loader.load();

                            MonthEventEntryViewController controller = loader.getController();
                            controller.setEventTitleLabel(event.getTitle());
                            controller.setEventTimeLabel(TimeFormatter.formatLocalDateTime(eventStartDateTime));

                            eventEntry.setOnMouseClicked((MouseEvent e) -> {
                                mouseX = e.getScreenX();
                                mouseY = e.getScreenY();
                                try {
                                    viewHandler.loadEventView(event);
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });

                            for (int i = 0; i < 4; i++) {
                                int daysBetween = (int) Duration.between(currentDate.atStartOfDay(), event.getEndTime()).toDays();
                                int rowNum = (row * rowStep) + i + 1;
                                if(i == 3) {
                                moreEventCount++;
                                    String labelText = moreEventCount + " more " + (moreEventCount== 1 ? "event" : "events");
                                    Label countText = new Label(labelText);
                                    HBox moreCountHBox = new HBox();
                                    moreCountHBox.getChildren().add(countText);
                                    GridPane.setConstraints(moreCountHBox, col, rowNum, 1, 1);
                                    removeNodeByRowColumn(gridPane, rowNum, col);
                                    gridPane.getChildren().add(moreCountHBox);
                                    break;
                                }
                                if (isCellEmpty(gridPane, col, rowNum)) {
                                    GridPane.setConstraints(eventEntry, col, rowNum, daysBetween + 1, 1);

                                    gridPane.getChildren().add(eventEntry);



                                    if (currentDate.isAfter(eventStartDate)) {
                                        eventEntry.getStyleClass().add("right");
                                        controller.setEventTitleLabel("");
                                    }
                                    if (col + daysBetween > 7) {
                                        eventEntry.getStyleClass().add("left");
                                        controller.setEventTimeLabel("");
                                        int leftDays = daysBetween - (7 - col);
                                        int countLeftDays = leftDays;
                                        for (int j = 1; j < leftDays / 7 + 2; j++) {
                                            if ((row + j) * (rowStep) > 25) break;


                                            FXMLLoader newLoader = new FXMLLoader(getClass().getResource("monthEventEntryView.fxml"));
                                            HBox newRowEventEntry = newLoader.load();

                                            MonthEventEntryViewController newController = newLoader.getController();

                                            for (int k = 0; k < 4; k++) {
                                                int rowShort = ((row + j) * (rowStep)) + k + 1;
                                                if (rowShort > 25) break;


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
                                                mouseX = e.getScreenX();
                                                mouseY = e.getScreenY();
                                                try {
                                                    viewHandler.loadEventView(event);
                                                } catch (IOException ex) {
                                                    throw new RuntimeException(ex);
                                                }
                                            });



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

    }

    public void removeNodeByRowColumn(GridPane gridPane, int row, int column) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeColumn = GridPane.getColumnIndex(node);

            nodeRow = (nodeRow == null) ? 0 : nodeRow;
            nodeColumn = (nodeColumn == null) ? 0 : nodeColumn;

            if (nodeRow == row && nodeColumn == column) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
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


        Stage overlayStage = new Stage(StageStyle.TRANSPARENT);
        overlayStage.initOwner(ownerStage);

        eventViewController.init(overlayStage, this, calendarViewModel, eventData);
        Scene overlayScene = new Scene(overlayContent);
        overlayScene.setFill(null);

        overlayStage.setScene(overlayScene);

        overlayStage.setOnShown(e -> {
            double overlayHeight = overlayStage.getHeight();
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
            gridPane.getChildren().clear();
            gridPane.setGridLinesVisible(true);
            calendarViewModel.reset();
            DayOfWeek dayOfWeek = calendarStartDate.getDayOfWeek();
            int daysAfterMonday = dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
            this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));

            LocalDate firstMondayDate = calendarStartDate.minusDays(daysAfterMonday);
            monthLabel.setText(calendarStartDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + calendarStartDate.getYear());
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("viewmodelEventAdd".equals(evt.getPropertyName())) {

            reset();
        } else if ("viewmodelEventRemove".equals(evt.getPropertyName())) {

            reset();
        }

    }
}

