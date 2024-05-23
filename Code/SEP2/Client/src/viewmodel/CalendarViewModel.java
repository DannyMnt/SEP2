package viewmodel;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import model.ClientModel;
import model.Event;
import model.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.UUID;

public class CalendarViewModel implements PropertyChangeListener
{
    private ClientModel model;


    private ListProperty<Event> events;

    private SimpleObjectProperty<GridPane> gridPane = new SimpleObjectProperty<>();

    private SimpleStringProperty monthLabel = new SimpleStringProperty();
    private SimpleObjectProperty<Image> imageProperty;

    private PropertyChangeSupport propertyChangeSupport;


    public SimpleObjectProperty<GridPane> getGridPaneProperty() {
        return gridPane;
    }


    public SimpleStringProperty getMonthLabelProperty() {
        return monthLabel;
    }

    public CalendarViewModel(ClientModel model) {

        this.model = model;
        this.imageProperty = new SimpleObjectProperty<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);



//        try {
//
////            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));
//
//
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public ObservableList<Event> getEvents(LocalDate startDate, LocalDate endDate) {
        return events.get();
    }


    public void reset() {
        try {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getEventsByOwner(ViewState.getInstance().getUserID())));
            User user = model.getUserById(ViewState.getInstance().getUserID());
            imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public Event getEvent(UUID eventId) throws RemoteException {
        return model.getEvent(eventId);
    }

    public SimpleObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }


    public ListProperty<Event> eventsProperty() {
        return events;
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        if("eventReceived".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            propertyChangeSupport.firePropertyChange("eventReceived",null,receivedEvent);
        }else if ("eventRemove".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            propertyChangeSupport.firePropertyChange("eventRemove",null,receivedEvent);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
