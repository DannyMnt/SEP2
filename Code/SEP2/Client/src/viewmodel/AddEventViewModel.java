package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.ClientModel;
import model.Event;
import model.User;
import model.UserEvent;
import utill.TimeFormatter;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddEventViewModel {
    private User user;
    private ClientModel clientModel;
    private StringProperty eventTitle;
    private StringProperty eventDescription;
    private StringProperty location;
    private DatePicker startDate;
    private DatePicker endDate;
    private StringProperty startTime;
    private StringProperty endTime;
    private StringProperty errorLabel;
    private StringProperty participantsTextFieldProperty;
    private VBox listView;
    private AnchorPane anchorPane;
    private AnchorPane attendeesAnchorPane;
    private VBox attendeesVBox;
    private List<User> attendees;

    public AddEventViewModel(ClientModel clientModel) {
        this.clientModel = clientModel;
        eventTitle = new SimpleStringProperty();
        eventDescription = new SimpleStringProperty();
        location = new SimpleStringProperty();
        startDate = new DatePicker(LocalDate.now());
        endDate = new DatePicker(LocalDate.now().plusDays(1));
        errorLabel = new SimpleStringProperty();
        participantsTextFieldProperty = new SimpleStringProperty();
        attendees = FXCollections.observableArrayList();
        startTime = new SimpleStringProperty(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now()));
        endTime = new SimpleStringProperty(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now().plusHours(1)));


    }

    public void reset() {
        attendees.clear();
        attendeesVBox.getChildren().clear();
        attendeesVBox.setPrefHeight(0);
        attendeesAnchorPane.setPrefHeight(0);

        eventTitle.setValue("");
        eventDescription.setValue("");
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now().plusDays(1));
        startTime.setValue(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now()));
        endTime.setValue(TimeFormatter.roundDownToHourAndFormat(LocalDateTime.now().plusHours(1)));
        errorLabel.set("");
        location.setValue("");
    }

    public void setListView(VBox listView, AnchorPane anchorPane, AnchorPane attendeesAnchorPane, VBox attendeesVBox) {
        this.listView = listView;
        this.anchorPane = anchorPane;
        this.attendeesAnchorPane = attendeesAnchorPane;
        this.attendeesVBox = attendeesVBox;
    }



    public boolean addEvent() throws RemoteException {

        try {
            if (getEventTitleProperty().getValue() == null)
                throw new IllegalArgumentException("Invalid title");
            else if (eventTitle.getValue().length() >= 255)
                throw new IllegalArgumentException("Title is too long");
            else if (eventDescription.getValue() == null)
                throw new IllegalArgumentException("Invalid description");
            else if (eventDescription.getValue().length() >= 255)
                throw new IllegalArgumentException("Description is too long");
            else if (getStartDate().getValue() != null && getEndDate().getValue() != null && getStartDate().getValue().compareTo(getEndDate().getValue()) > 0)
                throw new IllegalArgumentException("Invalid dates");
            else if (getEndDate().getValue() == null || getStartDate().getValue() == null)
                throw new IllegalArgumentException("Invalid dates");
            else if (location.getValue() == null)
                throw new IllegalArgumentException("Invalid location");
            else if (location.getValue().length() >= 255)
                throw new IllegalArgumentException("Location is too long");
            else if (startTime.getValue().isEmpty()) {

                throw new IllegalArgumentException("Invalid times");
            } else if(endTime.getValue().isEmpty()){
                throw new IllegalArgumentException("Invalid times");
            }
            System.out.println("Creating event");
            List<UUID> attendeeIDs = new ArrayList<>(attendees.stream().map(User::getId).toList());
            attendeeIDs.add(ViewState.getInstance().getUserID());
            Event event = new Event(ViewState.getInstance().getUserID(), eventTitle.getValue(), eventDescription.getValue(),
                    LocalDateTime.of(startDate.getValue(), LocalTime.parse(startTime.getValue())),
                    LocalDateTime.of(endDate.getValue(), LocalTime.parse(endTime.getValue())),
                    location.getValue(),
                    attendeeIDs);
            clientModel.createEvent(event);
            event.getAttendeeIDs().removeIf(uuid -> uuid.equals(ViewState.getInstance().getUserID()));

            if (!attendees.isEmpty()) {
                clientModel.createUserEvent(event);
            }
            errorLabel.setValue("");
            return true;
        } catch (
                IllegalArgumentException e) {
            errorLabel.setValue(e.getMessage());
            return false;
        }

    }

    public StringProperty getLocationProperty() {
        return location;
    }

    public StringProperty getEventTitleProperty() {
        return eventTitle;
    }

    public StringProperty getEventDescriptionProperty() {
        return eventDescription;
    }

    public DatePicker getStartDate() {
        return startDate;
    }

    public DatePicker getEndDate() {
        return endDate;
    }

    public StringProperty getErrorLabelProperty() {
        return errorLabel;
    }

    public StringProperty getParticipantsTextFieldProperty() {
        return participantsTextFieldProperty;
    }

    public StringProperty getStartTimeProperty() {
        return startTime;
    }

    public StringProperty getEndTimeProperty() {
        return endTime;
    }

    public void addListener() {
        participantsTextFieldProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    List<User> users = clientModel.searchUsersByName(newValue);
                    clearList();
                    for (User user : users) {
                        listView.setPrefHeight(listView.getPrefHeight() + 10);
                        anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 10);
                        Button button = new Button(user.getFirstname() + " " + user.getLastname());
                        button.setId(user.getEmail());
                        button.setOnAction(event -> {
                            Button clickedButton = (Button) event.getSource();
                            try {
                                User selectedUser = clientModel.getUserByEmail(clickedButton.getId());
                                if (!attendees.contains(selectedUser)) {
                                    attendees.add(selectedUser);
                                    attendeesVBox.setPrefHeight(attendeesVBox.getPrefHeight() + 17);
                                    attendeesAnchorPane.setPrefHeight(attendeesAnchorPane.getPrefHeight() + 17);
                                    attendeesVBox.getChildren().add(new Label(selectedUser.getFirstname() + " " + selectedUser.getLastname()));
                                } else
                                    errorLabel.setValue("User already added");
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                            clearList();
                            participantsTextFieldProperty.setValue("");
                        });
                        listView.getChildren().add(button);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clearList() {
        for (int i = 0; i < listView.getChildren().size(); i++) {
            listView.getChildren().clear();
        }
    }
}
