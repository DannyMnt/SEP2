
package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Event;
import utill.TimeFormatter;
import viewmodel.CalendarViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Controller class for the Calendar view.
 */
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

    /**
     * Initializes the controller with the ViewHandler, CalendarViewModel, and root region.
     *
     * @param viewHandler      The ViewHandler instance.
     * @param calendarViewModel The CalendarViewModel instance.
     * @param root             The root region of the view.
     */
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

    /**
     * Loads the month view for the specified start date and events.
     *
     * @param startDate The start date of the month.
     * @param events    The list of events to display.
     */
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
                hBox.getChildren().add(dateField);
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
                                                System.out.println((((row + j) * (rowStep))+1));
                                                if((((row + j) * (rowStep))+1)<20){
                                                    newController.setEventTimeLabel("");
                                                }
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
                            e.printStackTrace();
                        }
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
        }
    }

    /**
     * Removes a node from the GridPane by its row and column indices.
     *
     * @param gridPane The GridPane from which to remove the node.
     * @param row      The row index of the node.
     * @param column   The column index of the node.
     */
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



    /**
     * Checks if a cell in the GridPane is empty.
     *
     * @param gridPane The GridPane to check.
     * @param col      The column index of the cell.
     * @param row      The row index of the cell.
     * @return True if the cell is empty, false otherwise.
     */
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


    /**
     * Navigates to the previous month and updates the view.
     */
    @FXML
    private void goToPreviousMonth() {
        this.calendarStartDate = calendarStartDate.minusMonths(1);
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));

        reset();
    }

    /**
     * Navigates to the next month and updates the view.
     */
    @FXML
    private void goToNextMonth() {

        this.calendarStartDate = calendarStartDate.plusMonths(1);
        this.events = calendarViewModel.getEvents(calendarStartDate.minusMonths(1), calendarStartDate.plusMonths(1));
        reset();

    }

    /**
     * Displays an overlay with event details.
     *
     * @param ownerStage The stage owning the overlay.
     * @param event      The event to display.
     * @return The overlay stage.
     * @throws IOException If an error occurs while loading the FXML file.
     */
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

    /**
     * Retrieves the root region of the view.
     *
     * @return The root region.
     */
    public Region getRoot() {
        return root;
    }


    /**
     * Resets the view by clearing the grid and reloading the month view.
     */
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

    /**
     * Navigates to the calendar view.
     */
    @FXML
    private void gotoCalendar() {
        viewHandler.openView("calendar");
    }

    /**
     * Navigates to the chat view.
     */
    @FXML
    private void gotoChat() {
        viewHandler.openView("chat");
    }

    /**
     * Opens the profile view.
     */
    public void openProfileView() {
        viewHandler.openView("profile");
    }

    /**
     * Opens the create event view.
     */
    public void openCreateEventView() {
        viewHandler.openView("addEvent");
    }

    /**
     * Handles property change events.
     *
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("viewmodelEventAdd".equals(evt.getPropertyName())) {

            reset();
        } else if ("viewmodelEventRemove".equals(evt.getPropertyName())) {

            reset();
        }

    }
}

