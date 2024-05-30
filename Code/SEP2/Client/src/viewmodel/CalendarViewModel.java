package viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CalendarViewModel implements PropertyChangeListener
{
    private ClientModel model;


    private ListProperty<Event> events;

    private SimpleObjectProperty<GridPane> gridPane = new SimpleObjectProperty<>();

    private SimpleStringProperty monthLabel = new SimpleStringProperty();
    private SimpleObjectProperty<Image> imageProperty;

    private PropertyChangeSupport propertyChangeSupport;




    private List<PropertyChangeListener> listeners;

    public CalendarViewModel(ClientModel model) {

        this.model = model;
        this.imageProperty = new SimpleObjectProperty<>();
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.listeners = new ArrayList<>();
        model.addListener(this);
        try
        {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getUsersEvents(ViewState.getInstance().getUserID())));
        }catch (RemoteException e){
            e.printStackTrace();
        }


    }

    public SimpleObjectProperty<GridPane> getGridPaneProperty() {
        return gridPane;
    }


    public SimpleStringProperty getMonthLabelProperty() {
        return monthLabel;
    }

    public ObservableList<Event> getEvents(LocalDate startDate, LocalDate endDate) {
        return events.get();
    }


    public void reset() {
        try {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getUsersEvents(ViewState.getInstance().getUserID())));
            User user = model.getUserById(ViewState.getInstance().getUserID());
            imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeEvent(Event event) throws RemoteException {
        model.removeEvent(event);
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
        if("modelEventAdd".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            firePropertyChange("viewmodelEventAdd",null,receivedEvent);
        }else if ("modelEventRemove".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            firePropertyChange("viewmodelEventRemove",null,receivedEvent);
        }
    }

    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public List<Event> getUsersEvents(UUID userId) throws RemoteException{
        return model.getUsersEvents(userId);
    }

    public void addListener(Object object){
        listeners.add((PropertyChangeListener) object);
    }


    public List<PropertyChangeListener> getListeners()
    {
        return listeners;
    }

    public boolean isUsersEvent(Event event){
        return model.isUserOwner(event);
    }

    public List<User> getAttendees(List<UUID> list) throws RemoteException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            users.add(model.getUserById(list.get(i)));

        return users;
    }
}
