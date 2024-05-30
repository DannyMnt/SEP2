package mediator;

import model.Event;
import model.ServerModel;
import model.User;
import utility.observer.listener.GeneralListener;
import utility.observer.subject.PropertyChangeHandler;
import utility.observer.subject.RemoteSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The RmiServer class represents a server implementation for the Remote Method Invocation (RMI) framework.
 * It implements the RemoteModel interface and serves as a subject for RemoteSubject.
 * It also implements PropertyChangeListener to handle property change events.
 */
public class RmiServer implements RemoteModel, RemoteSubject<Event, Event>, PropertyChangeListener {

    private ServerModel model;

    private PropertyChangeHandler<Event, Event> property;

    private List<UUID> connectedUsers;

    private Map<UUID,ClientCallback> clients;

    /**
     * Constructs a new RmiServer object with the specified server model.
     * @param model The server model to associate with the server.
     * @throws MalformedURLException If the URL is malformed.
     * @throws RemoteException If a remote communication error occurs.
     */
    public RmiServer(ServerModel model) throws MalformedURLException, RemoteException {
        this.model = model;
        this.property = new PropertyChangeHandler<>(this, true);
        startRegistry();
        startServer();
        model.addListener(this);
        this.connectedUsers = new CopyOnWriteArrayList<>();
        this.clients = new ConcurrentHashMap<>();
    }

    /**
     * Starts the RMI registry.
     */
    private void startRegistry() {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            System.out.println("Registry.started...");
        } catch (RemoteException e) {
            System.out.println("Registry already started? " + e.getMessage());
        }
    }

    /**
     * Starts the RMI server.
     * @throws RemoteException If a remote communication error occurs.
     * @throws MalformedURLException If the URL is malformed.
     */
    private void startServer() throws RemoteException, MalformedURLException {

        UnicastRemoteObject.exportObject(this, 0);
        Naming.rebind("TimeSchedule", this);
        System.out.println("Server started...");
    }

    /**
     * Creates a new event and notifies clients about it.
     * @param event The event to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public synchronized void createEvent(Event event) throws RemoteException {
        model.createEvent(event);

        for (UUID client: clients.keySet()){
            if(event.getAttendeeIDs().contains(client)){

                clients.get(client).notify("addEvent",event);
            }
        }

    }

    /**
     * Creates a new user.
     * @param user The user to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void createUser(User user) throws RemoteException {
        model.createUser(user);
    }

    /**
     * Associates a user with an event.
     * @param event The event to associate the user with.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void createUserEvent(Event event) throws RemoteException {
        model.createUserEvent(event);
    }

    /**
     * Updates user information.
     * @param user The updated user information.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void updateUser(User user) throws RemoteException {
        model.updateUser(user);
    }

    /**
     * Updates the password for a user with the specified UUID.
     * @param password The new password.
     * @param uuid The UUID of the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {
        model.updatePassword(password, uuid);
    }

    /**
     * Retrieves a user by email address.
     * @param email The email address of the user.
     * @return The user associated with the specified email.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public User getUserByEmail(String email) throws RemoteException {
        return model.getUserByEmail(email);
    }

    /**
     * Retrieves a user by their UUID.
     * @param userId The UUID of the user.
     * @return The user associated with the specified UUID.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public User getUserById(UUID userId) throws RemoteException {
        return model.getUserById(userId);
    }

    /**
     * Retrieves events owned by a user.
     * @param userId The UUID of the user.
     * @return A list of events owned by the specified user.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public List<Event> getEventsByOwner(UUID userId) throws RemoteException {
        return model.getEventsByOwner(userId);
    }

    /**
     * Retrieves events owned by a user within a specified date range.
     * @param userId The UUID of the user.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events owned by the specified user within the specified date range.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return model.getEventsByOwner(userId,startDate,endDate);
    }

    /**
     * Retrieves an event by its UUID.
     * @param eventId The UUID of the event.
     * @return The event associated with the specified UUID.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public Event getEvent(UUID eventId) throws RemoteException {
        return model.getEvent(eventId);
    }

    /**
     * Searches for users by name.
     * @param search The search string.
     * @return A list of users matching the search criteria.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return model.searchUsersByName(search);
    }

    /**
     * Checks if an email address is available.
     * @param email The email address to check.
     * @return True if the email address is available; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
    }

    /**
     * Logs in a user with the provided login package.
     * @param loginPackage The login package containing user credentials.
     * @return The login package with UUID assigned.
     * @throws Exception If login fails.
     */
    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = model.loginUser(loginPackage);
        connectedUsers.add(loginPackage.getUuid());

        return userLoggedIn;
    }



    /**
     * Disconnects a user from the server.
     * @param userId The UUID of the user to disconnect.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public synchronized void disconnect(UUID userId) throws RemoteException
    {
        connectedUsers.remove(userId);
        clients.remove(userId);
    }

    /**
     * Verifies the password for a user.
     * @param userId The UUID of the user.
     * @param password The password to verify.
     * @return True if the password is correct; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {

        return model.verifyPassword(userId,password);
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
        return model.doesEmailExist(email);
    }

    /**
     * Removes an event from the system.
     * @param event The event to be removed.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public synchronized void removeEvent(Event event) throws RemoteException
    {
        for (UUID client: clients.keySet()){
            if(event.getAttendeeIDs().contains(client)){
                clients.get(client).notify("removeEvent",event);
            }
        }
        model.removeEvent(event);
    }

    /**
     * Registers a client with the server.
     * @param userId The UUID of the client.
     * @param client The callback interface for the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public synchronized void registerClient(UUID userId,ClientCallback client)
        throws RemoteException
    {
        clients.put(userId,client);
    }

    /**
     * Retrieves events associated with a user.
     * @param userId The UUID of the user.
     * @return A list of events associated with the specified user.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        return model.getUsersEvents(userId);
    }

    /**
     * Adds a listener to the server's property change support.
     * @param listener The listener to add.
     * @param propertyNames The names of the properties to listen for changes on.
     * @return True if the listener was successfully added; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public synchronized boolean addListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.addListener(listener, propertyNames);
    }

    /**
     * Removes a listener from the server's property change support.
     * @param listener The listener to remove.
     * @param propertyNames The names of the properties to stop listening for changes on.
     * @return True if the listener was successfully removed; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public synchronized boolean removeListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.removeListener(listener, propertyNames);
    }

    /**
     * Invoked when a property change event is received.
     * This method is not used in the server implementation.
     * @param propertyChangeEvent The property change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }







}
