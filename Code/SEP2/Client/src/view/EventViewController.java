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
import utill.TimeFormatter;
import viewmodel.CalendarViewModel;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
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
        dateLabel.setText(TimeFormatter.formatEventDates(event.getStartTime(), event.getEndTime()));
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText(event.getLocation());
        this.event = event;
        this.calendarViewController = calendarViewController;
        this.viewModel = viewModel;
        if(event.getEventId() != null) {
            List<User> attendees = viewModel.getAttendees(event.getAttendeeIDs());
            for (int i = 0; i < attendees.size(); i++) {
                User attendee = attendees.get(i);
                Label label =
                        new Label(attendee.getFirstname() + " " + attendee.getLastname());
                label.setMaxHeight(Double.MAX_VALUE);
                Image image = new Image(new ByteArrayInputStream(attendee.getProfilePicture()));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(30);
                imageView.setFitHeight(30);

                Circle clip = new Circle();
                clip.setCenterX(imageView.getFitWidth() / 2); // Center X of the circle
                clip.setCenterY(imageView.getFitHeight() / 2); // Center Y of the circle
                clip.setRadius(Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2);
                imageView.setClip(clip);

                HBox hBox = new HBox();
                hBox.setSpacing(5);
                hBox.getChildren().addAll(imageView, label);
                attendeesVBox.getChildren().add(hBox);
                attendeesVBox.setSpacing(5);
            }
        }
    }


    public void removeEvent() throws RemoteException {

            viewModel.removeEvent(event);
            eventStage.close();
            calendarViewController.reset();
    }
}