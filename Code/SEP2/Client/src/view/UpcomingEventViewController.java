package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Event;
import model.User;
import utill.ImageFormatter;
import utill.TimeFormatter;
import viewmodel.ProfileOverviewViewModel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for the UpcomingEventView.
 * This controller is responsible for managing the UI elements of the upcoming event view,
 * displaying details about the event and its attendees.
 */
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

    /**
     * Initializes the view with the provided event and view model.
     * @param event The upcoming event to be displayed
     * @param viewModel The view model for managing profile overview
     * @throws RemoteException If a remote exception occurs during data retrieval
     */
    public void init(Event event, ProfileOverviewViewModel viewModel) throws RemoteException {

        this.viewModel = viewModel;
        this.event = event;
        titleLabel.setText(event.getTitle());
        dateLabel.setText(TimeFormatter.formatEventDates(event.getStartTime(), event.getEndTime()));
        descriptionLabel.setText(event.getDescription());
        locationLabel.setText(event.getLocation());

        if(event.getEventId() != null) {
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