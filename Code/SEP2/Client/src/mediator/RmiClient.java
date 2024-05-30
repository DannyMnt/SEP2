package mediator;

import model.Event;
import model.User;
import utility.observer.event.ObserverEvent;
import utility.observer.listener.GeneralListener;
import utility.observer.listener.RemoteListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The RmiClient class implements the RemoteModel interface and serves as a client for interacting with the remote server via RMI.
 * It also implements PropertyChangeListener and RemoteListener interfaces to handle property changes and remote events respectively.
 */
public class RmiClient implements RemoteModel, PropertyChangeListener, RemoteListener<Event, Event>,ClientCallback{

    private RemoteModel server;
    private PropertyChangeSupport propertyChangeSupport;

    private UUID userId;

    private List<PropertyChangeListener> listeners;

    /**
     * Constructs a new RmiClient.
     * @throws MalformedURLException if the URL for the RMI registry is malformed.
     * @throws NotBoundException if the RMI registry is not bound.
     * @throws RemoteException if a remote communication error occurs.
     */
    public RmiClient() throws MalformedURLException, NotBoundException, RemoteException {
        super();
        UnicastRemoteObject.exportObject(this, 0);
        server = (RemoteModel) Naming.lookup("rmi://localhost:1099/TimeSchedule");
        server.addListener(this,null);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.listeners = new ArrayList<>();


    }

    /**
     * Sets the user ID for this client.
     * @param userId The UUID of the user.
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }


    /**
     * Property change event handler.
     * @param evt The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    /**
     * Property change event handler.
     * @param event The observer event.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void propertyChange(ObserverEvent<Event, Event> event) throws RemoteException {

        if(event.getPropertyName().equals("addEvent")){
            firePropertyChange("clientEventAdd", null, event.getValue2());
        }else if(event.getPropertyName().equals("removeEvent")){
            firePropertyChange("clientEventRemove",null,event.getValue2());
        }
    }

    /**
     * Fires a property change event.
     * @param propertyName The name of the property that changed.
     * @param oldValue The old value of the property.
     * @param newValue The new value of the property.
     */
    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }

    /**
     * Adds a property change listener.
     * @param listener The property change listener to add.
     */
    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     * @param listener The property change listener to remove.
     */
    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Creates an event.
     * @param event The event to create.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void createEvent(Event event) throws RemoteException {
        server.createEvent(event);
    }

    /**
     * Creates a user.
     * @param user The user to create.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void createUser(User user) throws RemoteException {
        server.createUser(user);
    }

    /**
     * Creates user-event associations.
     * @param event The event for which associations are to be created.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void createUserEvent(Event event) throws RemoteException {
        server.createUserEvent(event);
    }

    /**
     * Updates user information.
     * @param user The user object containing updated information.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void updateUser(User user) throws RemoteException {
        server.updateUser(user);
    }

    /**
     * Updates the password for a user.
     * @param password The new password.
     * @param uuid The UUID of the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {
        server.updatePassword(password, uuid);
    }

    /**
     * Retrieves a user by email.
     * @param email The email of the user.
     * @return The user corresponding to the email.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public User getUserByEmail(String email) throws RemoteException {
        return server.getUserByEmail(email);
    }



    /**
     * Retrieves a user by ID.
     * @param userId The UUID of the user.
     * @return The user corresponding to the ID.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public User getUserById(UUID userId) throws RemoteException{
        return server.getUserById(userId);
    }

    /**
     * Retrieves events owned by a user.
     * @param userId The UUID of the user.
     * @return A list of events owned by the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public List<Event> getEventsByOwner(UUID userId) throws RemoteException {
        return server.getEventsByOwner(userId);
    }

    /**
     * Retrieves events owned by a user within a specified date range.
     * @param userId The UUID of the user.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events owned by the user within the specified date range.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return server.getEventsByOwner(userId, startDate, endDate);
    }

    /**
     * Retrieves an event by its ID.
     * @param eventId The UUID of the event.
     * @return The event corresponding to the ID.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public Event getEvent(UUID eventId) throws RemoteException {
        return server.getEvent(eventId);
    }

    /**
     * Searches users by name.
     * @param search The search string.
     * @return A list of users matching the search criteria.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return server.searchUsersByName(search);
    }

    /**
     * Checks if an email is available (not already registered).
     * @param email The email to check.
     * @return True if the email is available; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public boolean isEmailFree(String email) throws RemoteException {

        return server.isEmailFree(email);
    }

    /**
     * Logs in a user.
     * @param loginPackage The login package containing login credentials.
     * @return The logged-in user's information.
     * @throws Exception if an error occurs during login process.
     */
    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = server.loginUser(loginPackage);

        registerClient(userLoggedIn.getUuid(),this);
        return userLoggedIn;

    }


    /**
     * Disconnects the client.
     * @param userId The UUID of the user to disconnect.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public void disconnect(UUID userId) throws RemoteException
    {
        server.disconnect(userId);
    }

    /**
     * Verifies the password for a user.
     * @param userId The UUID of the user.
     * @param password The password to verify.
     * @return True if the password is verified; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
         return server.verifyPassword(userId,password);
    }

    /**
     * Checks if an email exists.
     * @param email The email to check.
     * @return True if the email exists; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public boolean doesEmailExist(String email) throws RemoteException
    {
        return server.doesEmailExist(email);
    }

    /**
     * Removes an event.
     * @param event The event to remove.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public void removeEvent(Event event) throws RemoteException
    {
        server.removeEvent(event);
    }

    /**
     * Retrieves events for a specific user.
     * @param userId The UUID of the user.
     * @return A list of events for the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        return server.getUsersEvents(userId);
    }

    /**
     * Adds a property change listener.
     * @param object The property change listener to add.
     */
    public void addListener(Object object)
    {
        listeners.add((PropertyChangeListener)object);
    }

    /**
     * Notifies the client of a change.
     * @param change The type of change that occurred.
     * @param event The event associated with the change.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void notify(String change,Event event) throws RemoteException
    {
        propertyChange(new ObserverEvent<>(null,change,null,event));
    }

    /**
     * Registers the client with the server.
     * @param userId The UUID of the user.
     * @param client The client callback object.
     * @throws RemoteException if a remote communication error occurs.
     */
    public void registerClient(UUID userId,ClientCallback client) throws RemoteException
    {
        server.registerClient(userId,client);
    }

    /**
     * Gets the user ID.
     * @return The UUID of the user.
     */
    public UUID getUserId()
    {
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return false;
    }
}
