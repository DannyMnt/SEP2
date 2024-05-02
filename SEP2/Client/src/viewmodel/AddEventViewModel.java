package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import model.ClientModel;
import model.Event;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddEventViewModel {
    private ClientModel clientModel;
    private StringProperty eventTitle;
    private StringProperty eventDescription;
    private DatePicker startDate;
    private DatePicker endDate;
    public AddEventViewModel(ClientModel clientModel){
        this.clientModel = clientModel;
        eventTitle = new SimpleStringProperty();
        eventDescription = new SimpleStringProperty();
        startDate = new DatePicker();
        endDate = new DatePicker();
    }

    public void addEvent(){
        System.out.println("Event Created");
        System.out.println(endDate.getValue());
        Event event = new Event(eventTitle.getValue(), eventDescription.getValue(),
                LocalDateTime.of(startDate.getValue(), LocalTime.of(0, 0, 0)),
                LocalDateTime.of(endDate.getValue(), LocalTime.of(0, 0, 0)));
        System.out.println(event.toString());
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
}
