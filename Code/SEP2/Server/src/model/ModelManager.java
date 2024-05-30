package model;

import mediator.LoginPackage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Manages the interactions between the server's data model and the application logic.
 */
public class ModelManager implements ServerModel{

    private PropertyChangeSupport propertyChangeSupport;
    private EventRepository eventRepository;
    private UserRepository userRepository;


    /**
     * Constructs a new ModelManager instance.
     */
    public ModelManager(){
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.eventRepository = new EventRepository(DatabaseSingleton.getInstance());
        this.userRepository = new UserRepository(DatabaseSingleton.getInstance());

    }

    /**
     * Creates a new event and stores it in the repository.
     *
     * @param event The event to be created.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized void createEvent(Event event) throws RemoteException {

        eventRepository.createEvent(event);
    }

    /**
     * Creates a new user and stores it in the repository.
     *
     * @param user The user to be created.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override synchronized public void createUser(User user) throws RemoteException
    {
        userRepository.createUser(user);
    }

    /**
     * Updates the information of an existing user in the repository.
     *
     * @param user The user with updated information.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized void updateUser(User user) throws RemoteException {
        userRepository.updateUser(user);
        user.updateUser(user);
    }

    /**
     * Updates the password of a user with the specified UUID.
     *
     * @param password The new password.
     * @param uuid     The UUID of the user whose password is to be updated.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized void updatePassword(String password, UUID uuid) throws RemoteException {
        userRepository.updatePassword(password, uuid);
    }

    /**
     * Creates a new user event in the repository.
     *
     * @param event The event to be associated with the user.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized void createUserEvent(Event event) throws RemoteException {
        userRepository.createUserEvent(event);
    }

    /**
     * Retrieves a user from the repository based on their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The user with the specified email address.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized User getUserByEmail(String email) throws RemoteException
    {
        return userRepository.getUserByEmail(email);
    }

    /**
     * Retrieves a user from the repository based on their UUID.
     *
     * @param userId The UUID of the user to retrieve.
     * @return The user with the specified UUID.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized User getUserById(UUID userId) throws RemoteException {
        return userRepository.getUserById(userId);
    }

    /**
     * Retrieves a list of events owned by the user with the specified UUID.
     *
     * @param userId The UUID of the user whose events are to be retrieved.
     * @return A list of events owned by the user.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized List<Event> getEventsByOwner(UUID userId)
        throws RemoteException
    {
        return eventRepository.getEventsByOwner(userId);
    }

    /**
     * Retrieves a list of events owned by the user with the specified UUID within the specified time range.
     *
     * @param userId     The UUID of the user whose events are to be retrieved.
     * @param startDate  The start date of the time range.
     * @param endDate    The end date of the time range.
     * @return A list of events owned by the user within the specified time range.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return eventRepository.getEventsByOwner(userId, startDate, endDate);
    }

    /**
     * Retrieves the event with the specified UUID from the repository.
     *
     * @param eventId The UUID of the event to retrieve.
     * @return The event with the specified UUID.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override
    public synchronized Event getEvent(UUID eventId) throws RemoteException {
        return eventRepository.getEventById(eventId);
    }

    /**
     * Checks if the provided email address is available (not associated with any existing user).
     *
     * @param email The email address to check.
     * @return {@code true} if the email address is available, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized boolean isEmailFree(String email) throws RemoteException
    {
        return userRepository.isEmailFree(email);
    }

    /**
     * Searches for users by name in the repository.
     *
     * @param search The name or part of the name to search for.
     * @return A list of users matching the search criteria.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return userRepository.searchUsersByName(search);
    }

    /**
     * Attempts to log in a user using the provided login package.
     *
     * @param loginPackage The login package containing user credentials.
     * @return The updated login package.
     * @throws Exception If an error occurs during the login process.
     */
    @Override
    public synchronized LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        return userRepository.loginUser(loginPackage);
    }


    /**
     * Verifies the password for the user with the specified UUID.
     *
     * @param userId The UUID of the user whose password is to be verified.
     * @param password The password to be verified.
     * @return {@code true} if the password is correct, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
     @Override public synchronized boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
        return userRepository.verifyPassword(userId,password);
    }

    /**
     * Checks if the given email address exists and is valid.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address exists and is valid, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized boolean doesEmailExist(String email) throws RemoteException
    {
        return EmailValidator.isEmailValid(email);
    }

    /**
     * Removes the specified event from the repository.
     *
     * @param event The event to be removed.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized void removeEvent(Event event) throws RemoteException
    {
        eventRepository.removeEvent(event.getEventId());
    }

    /**
     * Retrieves a list of events associated with the specified user from the repository.
     *
     * @param userId The UUID of the user whose events are to be retrieved.
     * @return A list of events associated with the user.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    @Override public synchronized List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        return eventRepository.getUsersEvents(userId);
    }

    /**
     * Adds a property change listener to the model.
     *
     * @param listener The property change listener to be added.
     */
    @Override
    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener from the model.
     *
     * @param listener The property change listener to be removed.
     */
    @Override
    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
