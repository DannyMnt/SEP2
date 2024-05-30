package model;

import mediator.LoginPackage;
import mediator.RmiClient;
import viewmodel.ViewState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The ModelManager class implements the ClientModel interface and serves as the central component for managing client-side data and interactions.
 * It communicates with the server through RMI to perform various operations such as event creation, user management, and authentication.
 */
public class ModelManager implements ClientModel,PropertyChangeListener{



    private RmiClient client;
    private PropertyChangeSupport propertyChangeSupport;

    private User user;

    private List<Event> eventList;

    private List<Event> ownedEvents;

    private List<PropertyChangeListener> listeners;


    /**
     * Constructs a new ModelManager instance.
     *
     * @throws MalformedURLException if a URL is malformed.
     * @throws NotBoundException if the remote object is not bound.
     * @throws RemoteException if a remote communication error occurs.
     */
    public ModelManager() throws MalformedURLException, NotBoundException, RemoteException {
        this.client = new RmiClient();
        propertyChangeSupport = new PropertyChangeSupport(this);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try
            {
                client.disconnect(ViewState.getInstance().getUserID());
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }));

        this.user = null;
        this.eventList = null;
        this.ownedEvents = null;
        this.listeners = new ArrayList<>();
        client.addListener((Object) this);
    }

    /**
     * Creates a new event by delegating the operation to the RmiClient.
     *
     * @param event The event to be created.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void createEvent(Event event) throws RemoteException {

        client.createEvent(event);

    }


    /**
     * Creates a new user by delegating the operation to the RmiClient.
     *
     * @param user The user to be created.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public void createUser(User user) throws RemoteException
    {
        client.createUser(user);
    }

    /**
     * Creates a new user event by delegating the operation to the RmiClient.
     *
     * @param event The event to be associated with the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void createUserEvent(Event event) throws RemoteException {
        client.createUserEvent(event);
    }

    /**
     * Updates an existing user by delegating the operation to the RmiClient.
     *
     * @param user The user to be updated.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void updateUser(User user) throws RemoteException {
        client.updateUser(user) ;
        user.updateUser(user);
    }

    /**
     * Updates the password of a user identified by the provided UUID by delegating the operation to the RmiClient.
     *
     * @param password The new password.
     * @param uuid     The UUID of the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {

        client.updatePassword(password, uuid);
    }

    /**
     * Retrieves a user by email by delegating the operation to the RmiClient.
     *
     * @param email The email address of the user.
     * @return The user associated with the provided email address.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public User getUserByEmail(String email) throws RemoteException
    {
        if(!(this.user == null)){
            if(email.equals(this.user.getEmail())){
                return this.user;
            }
        }

        return client.getUserByEmail(email);
    }

    /**
     * Retrieves a user by ID by delegating the operation to the RmiClient.
     *
     * @param userId The UUID of the user.
     * @return The user associated with the provided UUID.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public User getUserById(UUID userId) throws RemoteException {
        if(!(this.user == null)){
            if(this.user.getId().equals(userId)){
                return this.user;
            }
        }
        return client.getUserById(userId);

    }

    /**
     * Retrieves a list of events owned by a user identified by the provided UUID by delegating the operation to the RmiClient.
     *
     * @param userId The UUID of the user.
     * @return A list of events owned by the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public List<Event> getEventsByOwner(UUID userId)
        throws RemoteException
    {
        return client.getEventsByOwner(userId);
    }

    /**
     * Retrieves a list of events owned by a user within a specified time range by delegating the operation to the RmiClient.
     *
     * @param userId     The UUID of the user.
     * @param startDate  The start date of the time range.
     * @param endDate    The end date of the time range.
     * @return A list of events owned by the user within the specified time range.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return client.getEventsByOwner(userId, startDate, endDate);
    }

    /**
     * Retrieves an event by its UUID by delegating the operation to the RmiClient.
     *
     * @param eventId The UUID of the event.
     * @return The event associated with the provided UUID.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public Event getEvent(UUID eventId) throws RemoteException {

        Optional<Event> eventWithId = eventList.stream().filter(event -> event.getEventId().equals(eventId)).findFirst();
        if(eventWithId.isPresent()){
            return eventWithId.get();
        } else
            return client.getEvent(eventId);
    }

    /**
     * Searches for users by name by delegating the operation to the RmiClient.
     *
     * @param search The search query for user names.
     * @return A list of users matching the search query.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return client.searchUsersByName(search);
    }

    /**
     * Checks if the provided email address is available for registration by delegating the operation to the RmiClient.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address is available, {@code false} otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public boolean isEmailFree(String email) throws RemoteException {

        return client.isEmailFree(email);
    }

    /**
     * Retrieves the current user.
     *
     * @return The current user.
     */
    @Override public User getUser()
    {
        return user;
    }

    /**
     * Sets the current user.
     *
     * @param user The user to be set as the current user.
     */
    @Override public void setUser(User user)
    {
        this.user = user;
    }


    /**
     * Retrieves the upcoming event based on the current date and time.
     *
     * @return The upcoming event, or null if there are no upcoming events.
     */
    @Override
    public Event getUpcomingEvent() {

        LocalDateTime now = LocalDateTime.now();

        if(eventList == null) return null ;
        List<Event> upcomingEvents = eventList.stream()
                .filter(event -> event.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(Event::getStartTime))
                .toList();

        if (upcomingEvents.isEmpty()) {
            return null;
        }

        return upcomingEvents.get(0);
    }

    /**
     * Retrieves the list of events.
     *
     * @return The list of events.
     */
    public List<Event> getEventList()
    {
        return eventList;
    }

    /**
     * Sets the list of events.
     *
     * @param eventList The list of events to be set.
     */
    public void setEventList(List<Event> eventList)
    {
        this.eventList = eventList;
    }


    /**
     * Retrieves the list of owned events.
     *
     * @return The list of owned events.
     */
    public List<Event> getOwnedEvents()
    {
        return ownedEvents;
    }

    /**
     * Sets the list of owned events.
     *
     * @param ownedEvents The list of owned events to be set.
     */
    public void setOwnedEvents(List<Event> ownedEvents)
    {
        this.ownedEvents = ownedEvents;
    }

    /**
     * Logs in a user with the provided login package by delegating the operation to the RmiClient.
     *
     * @param loginPackage The login credentials.
     * @return The login package containing user information.
     * @throws Exception if an error occurs during the login process.
     */
    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = client.loginUser(loginPackage);
        UUID userId = userLoggedIn.getUuid();
        setUser(getUserById(userId));
        setEventList(getUsersEvents(userId));
        setOwnedEvents(getEventsByOwner(userId));
        ViewState.getInstance().setUserID(userId);
        return userLoggedIn;
    }

    /**
     * Disconnects the user identified by the provided UUID by delegating the operation to the RmiClient.
     *
     * @param userId The UUID of the user to disconnect.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public void disconnect(UUID userId) throws RemoteException
    {
        client.disconnect(userId);
    }

    /**
     * Verifies the password for the user identified by the provided UUID by delegating the operation to the RmiClient.
     *
     * @param userId   The UUID of the user.
     * @param password The password to verify.
     * @return {@code true} if the password is verified, {@code false} otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
        return client.verifyPassword(userId,password);
    }

    /**
     * Checks if the given email address exists and is valid.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address exists and is valid, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public boolean doesEmailExist(String email) throws RemoteException
    {
        return client.doesEmailExist(email);
    }

    /**
     * Removes the specified event by delegating the operation to the RmiClient.
     *
     * @param event The event to be removed.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public void removeEvent(Event event) throws RemoteException
    {
        eventList.remove(event);
        ownedEvents.remove(event);
        client.removeEvent(event);
    }
    /**
     * Retrieves the list of events associated with the user identified by the provided UUID by delegating the operation to the RmiClient.
     *
     * @param userId The UUID of the user.
     * @return A list of events associated with the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        if(this.eventList != null)
        {
            if(this.user.getId().equals(userId)){
                return this.eventList;
            }
        }
        return client.getUsersEvents(userId);
    }

    /**
     * Adds a property change listener to the model manager.
     *
     * @param object The property change listener to be added.
     */
    @Override public void addListener(Object object)
    {
        listeners.add((PropertyChangeListener) object);
    }

    /**
     * Adds a property change listener for the specified property name.
     *
     * @param propertyName The name of the property to listen for changes.
     * @param listener     The property change listener to be added.
     */
    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a property change listener for the specified property name.
     *
     * @param propertyName The name of the property for which the listener was registered.
     * @param listener     The property change listener to be removed.
     */
    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    /**
     * Property change event handler method.
     *
     * @param evt The property change event.
     */
    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        if("clientEventAdd".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            this.eventList.add(receivedEvent);
            firePropertyChange("modelEventAdd",null,receivedEvent);
        }else if ("clientEventRemove".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            this.eventList.removeIf(event -> event.getEventId().equals(receivedEvent.getEventId()));

            firePropertyChange("modelEventRemove",null,receivedEvent);
        }
    }

    /**
     * Fires a property change event.
     *
     * @param propertyName The name of the property that has changed.
     * @param oldValue     The old value of the property.
     * @param newValue     The new value of the property.
     */
    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {

        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    /**
     * Checks if the user is the owner of the specified event.
     *
     * @param event The event to check ownership.
     * @return {@code true} if the user is the owner of the event, {@code false} otherwise.
     */
    @Override
    public boolean isUserOwner(Event event){
      return ownedEvents.contains(event);
    }
}
