package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Event;
import model.User;
import utill.ImageFormatter;
import utill.TimeFormatter;
import viewmodel.CalendarViewModel;
import viewmodel.LoginUserViewModel;
import viewmodel.ProfileOverviewViewModel;
import viewmodel.ViewState;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpcomingEventViewController {

    @FXML
    private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label locationLabel;
    @FXML private VBox attendeesVBox;
    private ViewHandler viewHandler;
    private Region root;

    private ProfileOverviewViewModel viewModel;

    private Event event;

    private List<UUID> attendees = new ArrayList<>();

    private Stage eventStage;

    private CalendarViewController calendarViewController;

    public void init(Event event, ProfileOverviewViewModel viewModel) throws RemoteException {

        this.viewModel = viewModel;
        this.event = event;
        titleLabel.setText(event.getTitle());
        dateLabel.setText(TimeFormatter.formatEventDates(event.getStartTime(), event.getEndTime()));
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText(event.getLocation());

        if(event.getEventId() != null) {
//            attendees.addAll(event.getAttendeeIDs());
            List<User> attendees = viewModel.getAttendees(event.getAttendeeIDs());
            for (int i = 0; i < attendees.size(); i++) {
                User attendee = attendees.get(i);
                Label label =
                        new Label(attendee.getFirstname() + " " + attendee.getLastname());
                label.setMaxHeight(Double.MAX_VALUE);
                ImageView imageView = ImageFormatter.getImageView(attendee, 30);

                HBox hBox = new HBox();
                hBox.setSpacing(5);
                hBox.getChildren().addAll(imageView, label);
                attendeesVBox.getChildren().add(hBox);
                attendeesVBox.setSpacing(5);
            }
        }
    }




}