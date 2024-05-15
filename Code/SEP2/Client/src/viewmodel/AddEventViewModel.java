package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.ClientModel;
import model.Event;
import model.User;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddEventViewModel {
    private User user;
    private ClientModel clientModel;
    private StringProperty eventTitle;
    private StringProperty eventDescription;
    private StringProperty location;
    private DatePicker startDate;
    private DatePicker endDate;
    private StringProperty errorLabel;
    private StringProperty participantsTextFieldProperty;
    private VBox listView;
    private AnchorPane anchorPane;
    public AddEventViewModel(ClientModel clientModel){
        this.clientModel = clientModel;
        eventTitle = new SimpleStringProperty();
        eventDescription = new SimpleStringProperty();
        location = new SimpleStringProperty();
        startDate = new DatePicker();
        endDate = new DatePicker();
        errorLabel = new SimpleStringProperty();
        participantsTextFieldProperty = new SimpleStringProperty();
    }

    public void setListView(VBox listView, AnchorPane anchorPane){
        this.listView = listView;
        this.anchorPane = anchorPane;
    }

    public void addEvent() throws RemoteException {
        user = new User("testemail", "testpass");
        if(getEventTitleProperty().getValue() == null)
            errorLabel.setValue("Invalid event name");
        else if (getStartDate().getValue() != null && getEndDate().getValue() != null && getStartDate().getValue().compareTo(getEndDate().getValue()) > 0)
            errorLabel.setValue("Invalid dates");
        else if(getEndDate().getValue() == null || getStartDate().getValue() == null)
            errorLabel.setValue("Invalid dates");
        else if(location.getValue() == null){
            errorLabel.setValue("Location cannot be empty");
        }
        else{
            System.out.println("Event Created");
            clientModel.createEvent(new Event(user.getId(),eventTitle.getValue(), eventDescription.getValue(),
                LocalDateTime.of(startDate.getValue(), LocalTime.of(0, 0, 0)),
                LocalDateTime.of(endDate.getValue(), LocalTime.of(0, 0, 0)), location.getValue()));
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
                            listView.getChildren().add(button);
                        }
                    }
                    else
                        for (int i = 0; i < clientModel.searchUsersByName(newValue).size(); i++) {
                            listView.setPrefHeight(listView.getPrefHeight() + 10);
                            anchorPane.setPrefHeight(anchorPane.getPrefHeight() + 10);
                            Button button = new Button(clientModel.searchUsersByName(newValue).get(i).getFirstname()
                                    + " " + clientModel.searchUsersByName(newValue).get(i).getLastname());
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
