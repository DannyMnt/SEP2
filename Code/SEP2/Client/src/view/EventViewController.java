package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import model.User;
import viewmodel.CalendarViewModel;
import viewmodel.LoginUserViewModel;
import viewmodel.ViewState;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventViewController {

    @FXML
    private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private VBox attendeesVBox;
    @FXML private AnchorPane attendeesAnchorPane;
    private ViewHandler viewHandler;
    private Region root;

    private CalendarViewModel viewModel;

    private Event event;

    private List<UUID> attendees = new ArrayList<>();

    private Stage eventStage;

    private CalendarViewController calendarViewController;

    public void init(Stage eventStage, CalendarViewController calendarViewController, CalendarViewModel viewModel,  Event event) throws RemoteException {


        this.eventStage = eventStage;
        titleLabel.setText(event.getTitle());
        dateLabel.setText(formatEventDates(event.getStartTime(), event.getEndTime()));
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText("Location: " + event.getLocation());
        this.event = event;
        this.calendarViewController = calendarViewController;
        //        System.out.println(event.toString());
        this.viewModel = viewModel;
        if(event.getEventId() != null) {
            attendees.addAll(event.getAttendeeIDs());
            for (int i = 0; i < event.getAttendeeIDs().size(); i++) {
                Label label =
                        new Label(viewModel.getAttendees(attendees).get(i).getFirstname() + " " + viewModel.getAttendees(attendees).get(i).getLastname());
                attendeesAnchorPane.setPrefHeight(attendeesAnchorPane.getPrefHeight() + 17);
                attendeesVBox.setPrefHeight(attendeesVBox.getPrefHeight() + 17);
                attendeesVBox.getChildren().add(label);
            }
        }
    }

    public static String formatEventDates(LocalDateTime startDate, LocalDateTime endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d. M. yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (startDate.toLocalDate().equals(endDate.toLocalDate())) {
            // Same day
            String date = startDate.format(dateFormatter);
            String startTime = startDate.format(timeFormatter);
            String endTime = endDate.format(timeFormatter);
            return String.format("%s %s to %s", date, startTime, endTime);
        } else {
            // Different days
            String start = startDate.format(dateFormatter);
            String end = endDate.format(dateFormatter);
            return String.format("%s to %s", start, end);
        }
    }

    public void removeEvent() throws RemoteException {

            viewModel.removeEvent(event);
            eventStage.close();
            calendarViewController.reset();
    }
}