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

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
    public AddEventViewModel(ClientModel clientModel){
        this.clientModel = clientModel;
        eventTitle = new SimpleStringProperty();
        eventDescription = new SimpleStringProperty();
        location = new SimpleStringProperty();
        startDate = new DatePicker();
        endDate = new DatePicker();
        errorLabel = new SimpleStringProperty();
        participantsTextFieldProperty = new SimpleStringProperty();
        attendees = FXCollections.observableArrayList();
        startTime = new SimpleStringProperty();
        endTime = new SimpleStringProperty();
    }

    public void reset(){
        attendees.clear();
        attendeesVBox.getChildren().clear();
        attendeesVBox.setPrefHeight(0);
        attendeesAnchorPane.setPrefHeight(0);
    }

    public void setListView(VBox listView, AnchorPane anchorPane, AnchorPane attendeesAnchorPane, VBox attendeesVBox){
        this.listView = listView;
        this.anchorPane = anchorPane;
        this.attendeesAnchorPane = attendeesAnchorPane;
        this.attendeesVBox = attendeesVBox;
    }

    public void addEvent() throws RemoteException {

        LocalTime startLocalTime;
        LocalTime endLocalTime;
        try{
            startLocalTime = LocalTime.parse(startTime.getValue());
            endLocalTime = LocalTime.parse(endTime.getValue());
        }
        catch (DateTimeParseException e){
            errorLabel.setValue("Invalid times");
        }
        if(getEventTitleProperty().getValue() == null)
            errorLabel.setValue("Invalid event name");
        else if (getStartDate().getValue() != null && getEndDate().getValue() != null && getStartDate().getValue().compareTo(getEndDate().getValue()) > 0)
            errorLabel.setValue("Invalid dates");
        else if(getEndDate().getValue() == null || getStartDate().getValue() == null)
            errorLabel.setValue("Invalid dates");
        else if(location.getValue() == null){
            errorLabel.setValue("Location cannot be empty");
        }
        else if(startTime.getValue() == null || endTime.getValue() == null)
            errorLabel.setValue("Invalid times");
        else if(startTime.getValue().compareTo(endTime.getValue()) > 0)
            errorLabel.setValue("Invalid times");
        else{
            System.out.println("Event Created");
            List<UUID> attendeeIDs = attendees.stream().map(User::getId).toList();
            Event event = new Event(ViewState.getInstance().getUserID(),eventTitle.getValue(), eventDescription.getValue(),
                    LocalDateTime.of(startDate.getValue(), LocalTime.parse(startTime.getValue())),
                    LocalDateTime.of(endDate.getValue(), LocalTime.parse(endTime.getValue())),
                    location.getValue(),
                    attendeeIDs);
            clientModel.createEvent(event);
            if(!attendees.isEmpty()){
                clientModel.createUserEvent(event);
            }

            errorLabel.setValue("");
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
    public StringProperty getStartTimeProperty(){
        return startTime;
    }
    public StringProperty getEndTimeProperty(){
        return endTime;
    }

    public void addListener(){
        participantsTextFieldProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (listView.getChildren().size() > 0) {
                        clearList();
                        for (int i = 0; i < clientModel.searchUsersByName(newValue).size(); i++) {
                            listView.setPrefHeight(listView.getPrefHeight() + 10);
                            anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 10);
                            Button button = new Button(clientModel.searchUsersByName(newValue).get(i).getFirstname()
                                    + " " + clientModel.searchUsersByName(newValue).get(i).getLastname());
                            button.setId(clientModel.searchUsersByName(newValue).get(i).getEmail());
                            button.setOnAction(event ->{
                                Button clickedButton = (Button) event.getSource();
                                try {
                                    attendees.add(clientModel.getUserByEmail(clickedButton.getId()));
                                    attendeesVBox.setPrefHeight(attendeesVBox.getPrefHeight() + 17);
                                    attendeesAnchorPane.setPrefHeight(attendeesAnchorPane.getPrefHeight() + 17);
                                    attendeesVBox.getChildren().add(new Label(clientModel.getUserByEmail(clickedButton.getId()).getFirstname() + " "
                                    + clientModel.getUserByEmail(clickedButton.getId()).getLastname()));
                                    System.out.println(Arrays.toString(attendees.toArray()));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                clearList();
                                participantsTextFieldProperty.setValue("");
                            });
                            listView.getChildren().add(button);
                        }
                    }
                    else
                        for (int i = 0; i < clientModel.searchUsersByName(newValue).size(); i++) {
                            listView.setPrefHeight(listView.getPrefHeight() + 10);
                            anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 10);
                            Button button = new Button(clientModel.searchUsersByName(newValue).get(i).getFirstname()
                                    + " " + clientModel.searchUsersByName(newValue).get(i).getLastname());
                            button.setId(clientModel.searchUsersByName(newValue).get(i).getEmail());
                            button.setOnAction(event ->{
                                Button clickedButton = (Button) event.getSource();
                                try {
                                    attendees.add(clientModel.getUserByEmail(clickedButton.getId()));
                                    attendeesVBox.setPrefHeight(attendeesVBox.getPrefHeight() + 17);
                                    attendeesAnchorPane.setPrefHeight(attendeesAnchorPane.getPrefHeight() + 17);
                                    attendeesVBox.getChildren().add(new Label(clientModel.getUserByEmail(clickedButton.getId()).getFirstname() + " "
                                            + clientModel.getUserByEmail(clickedButton.getId()).getLastname()));
                                    System.out.println(Arrays.toString(attendees.toArray()));
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                }
                                clearList();
                                participantsTextFieldProperty.setValue("");
                            });
                            listView.getChildren().add(button);
                        }

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void clearList(){
        for (int i = 0; i < listView.getChildren().size(); i++) {
            listView.getChildren().clear();
        }
    }
}
