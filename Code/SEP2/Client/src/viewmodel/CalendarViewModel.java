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

/**
 * ViewModel for managing calendar events.
 */
public class CalendarViewModel implements PropertyChangeListener
{
    private ClientModel model;


    private ListProperty<Event> events;

    private SimpleObjectProperty<GridPane> gridPane = new SimpleObjectProperty<>();

    private SimpleStringProperty monthLabel = new SimpleStringProperty();
    private SimpleObjectProperty<Image> imageProperty;

    private PropertyChangeSupport propertyChangeSupport;




    private List<PropertyChangeListener> listeners;

    /**
     * Constructs a CalendarViewModel with the given client model.
     *
     * @param model the client model to interact with
     */
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

    /**
     * Gets the grid pane property.
     *
     * @return the grid pane property
     */
    public SimpleObjectProperty<GridPane> getGridPaneProperty() {
        return gridPane;
    }


    /**
     * Gets the month label property.
     *
     * @return the month label property
     */
    public SimpleStringProperty getMonthLabelProperty() {
        return monthLabel;
    }

    /**
     * Gets the list of events within the specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return the list of events within the specified date range
     */
    public ObservableList<Event> getEvents(LocalDate startDate, LocalDate endDate) {
        return events.get();
    }


    /**
     * Resets the ViewModel to its initial state.
     */
    public void reset() {
        try {
            this.events = new SimpleListProperty<>(FXCollections.observableArrayList(model.getUsersEvents(ViewState.getInstance().getUserID())));
            User user = model.getUserById(ViewState.getInstance().getUserID());
            imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the specified event.
     *
     * @param event the event to remove
     * @throws RemoteException if an RMI error occurs
     */
    public void removeEvent(Event event) throws RemoteException {
        model.removeEvent(event);
    }


    /**
     * Gets the event with the specified ID.
     *
     * @param eventId the ID of the event to retrieve
     * @return the event with the specified ID
     * @throws RemoteException if an RMI error occurs
     */
    public Event getEvent(UUID eventId) throws RemoteException {
        return model.getEvent(eventId);
    }

    /**
     * Gets the image property.
     *
     * @return the image property
     */
    public SimpleObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }


    /**
     * Gets the events property.
     *
     * @return the events property
     */
    public ListProperty<Event> eventsProperty() {
        return events;
    }


    /**
     * Handles property change events from the model.
     *
     * @param evt the property change event
     */
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

    /**
     * Fires a property change event.
     *
     * @param propertyName the name of the property that changed
     * @param oldValue     the old value of the property
     * @param newValue     the new value of the property
     */
    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    /**
     * Adds a property change listener.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Gets the list of events for the specified user.
     *
     * @param userId the ID of the user
     * @return the list of events for the specified user
     * @throws RemoteException if an RMI error occurs
     */
    public List<Event> getUsersEvents(UUID userId) throws RemoteException{
        return model.getUsersEvents(userId);
    }

    /**
     * Adds a listener to the ViewModel.
     *
     * @param object the listener to add
     */
    public void addListener(Object object){
        listeners.add((PropertyChangeListener) object);
    }


    /**
     * Gets the list of property change listeners.
     *
     * @return the list of property change listeners
     */
    public List<PropertyChangeListener> getListeners()
    {
        return listeners;
    }

    /**
     * Checks if the specified event belongs to the user.
     *
     * @param event the event to check
     * @return true if the event belongs to the user, false otherwise
     */
    public boolean isUsersEvent(Event event){
        return model.isUserOwner(event);
    }

    /**
     * Gets the list of attendees for the specified event.
     *
     * @param list the list of attendee IDs
     * @return the list of attendees
     * @throws RemoteException if an RMI error occurs
     */
    public List<User> getAttendees(List<UUID> list) throws RemoteException {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            users.add(model.getUserById(list.get(i)));

        return users;
    }
}
