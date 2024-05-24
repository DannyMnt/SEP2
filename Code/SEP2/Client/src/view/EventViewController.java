package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Event;
import viewmodel.CalendarViewModel;
import viewmodel.LoginUserViewModel;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class EventViewController {

    @FXML
    private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label descriptionLabel;
    @FXML private
    HBox attendeesHBox;
    private ViewHandler viewHandler;
    private Region root;

    private CalendarViewModel viewModel;

    private Event event;

    private Stage eventStage;

    private CalendarViewController calendarViewController;

    public void init(Stage eventStage, CalendarViewController calendarViewController, CalendarViewModel viewModel,  Event event){


        this.eventStage = eventStage;
        titleLabel.setText(event.getTitle());
        dateLabel.setText(formatEventDates(event.getStartTime(), event.getEndTime()));
        descriptionLabel.setText(event.getDescription());
        this.event = event;
        this.calendarViewController = calendarViewController;
        //        System.out.println(event.toString());
        this.viewModel = viewModel;


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