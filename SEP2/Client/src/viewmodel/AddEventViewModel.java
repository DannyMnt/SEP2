package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
    private DatePicker startDate;
    private DatePicker endDate;
    private StringProperty errorLabel;
    public AddEventViewModel(ClientModel clientModel){
        this.clientModel = clientModel;
        eventTitle = new SimpleStringProperty();
        eventDescription = new SimpleStringProperty();
        startDate = new DatePicker();
        endDate = new DatePicker();
        errorLabel = new SimpleStringProperty();
    }

    public void addEvent() throws RemoteException {
        if(getEventTitleProperty().getValue() == null)
            errorLabel.setValue("Invalid event name");
        else if (getStartDate().getValue() != null && getEndDate().getValue() != null && getStartDate().getValue().compareTo(getEndDate().getValue()) > 0)
            errorLabel.setValue("Invalid dates");
        else if(getEndDate().getValue() == null || getStartDate().getValue() == null)
            errorLabel.setValue("Invalid dates");
        else{
            System.out.println("Event Created");
            clientModel.createEvent(new Event(user.getId(),eventTitle.getValue(), eventDescription.getValue(),
                LocalDateTime.of(startDate.getValue(), LocalTime.of(0, 0, 0)),
                LocalDateTime.of(endDate.getValue(), LocalTime.of(0, 0, 0))));
            System.out.println(event.toString());
            errorLabel.setValue("");
        }
        user = new User("testemail", "testpass");
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
}
