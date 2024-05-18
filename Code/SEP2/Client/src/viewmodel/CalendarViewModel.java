package viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mediator.LoginPackage;
import model.ClientModel;
import model.Event;

import java.rmi.RemoteException;

public class CalendarViewModel {
    private ClientModel model;

    private ListProperty<Event> events;
    public CalendarViewModel(ClientModel model){

        this.model = model;
        try
        {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));


        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public ObservableList<Event> getEvents()
    {
        return events.get();
    }

    public ListProperty<Event> eventsProperty()
    {
        return events;
    }
}
