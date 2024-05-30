package mediator;

import model.Event;
import model.User;
import utility.observer.subject.RemoteSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The RemoteModel interface defines the remote methods available for interacting with the model
 * in a distributed system.
 */
public interface RemoteModel extends RemoteSubject<Event, Event> {

    /**
     * Creates a new event.
     * @param event The event to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createEvent(Event event) throws RemoteException;

    /**
     * Creates a new user.
     * @param user The user to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createUser(User user) throws RemoteException;

    /**
     * Associates a user with an event.
     * @param event The event to associate the user with.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createUserEvent(Event event) throws RemoteException;
    /**
     * Updates user information.
     * @param user The updated user information.
     * @throws RemoteException If a remote communication error occurs.
     */
    void updateUser(User user) throws RemoteException;
    /**
     * Updates the password for a user with the specified UUID.
     * @param password The new password.
     * @param uuid The UUID of the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    void updatePassword(String password, UUID uuid) throws RemoteException;

    /**
     * Retrieves a user by email address.
     * @param email The email address of the user.
     * @return The user associated with the specified email.
     * @throws RemoteException If a remote communication error occurs.
     */
    User getUserByEmail(String email) throws  RemoteException;

    /**
     * Retrieves a user by their UUID.
     * @param userId The UUID of the user.
     * @return The user associated with the specified UUID.
     * @throws RemoteException If a remote communication error occurs.
     */
    User getUserById(UUID userId) throws RemoteException;

    /**
     * Retrieves an event by its UUID.
     * @param eventId The UUID of the event.
     * @return The event associated with the specified UUID.
     * @throws RemoteException If a remote communication error occurs.
     */
    Event getEvent(UUID eventId) throws RemoteException;

    /**
     * Retrieves events owned by a user.
     * @param userId The UUID of the user.
     * @return A list of events owned by the specified user.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId) throws RemoteException;

    /**
     * Retrieves events owned by a user within a specified date range.
     * @param userId The UUID of the user.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events owned by the specified user within the specified date range.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException;

    /**
     * Searches for users by name.
     * @param search The search string.
     * @return A list of users matching the search criteria.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<User> searchUsersByName(String search) throws RemoteException;

    /**
     * Checks if an email address is available.
     * @param email The email address to check.
     * @return True if the email address is available; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean isEmailFree(String email) throws RemoteException;

    /**
     * Logs in a user with the provided login package.
     * @param loginPackage The login package containing user credentials.
     * @return The login package with UUID assigned.
     * @throws Exception If login fails.
     */
    LoginPackage loginUser(LoginPackage loginPackage) throws Exception;

    /**
     * Disconnects a user with the specified UUID.
     * @param userId The UUID of the user to disconnect.
     * @throws RemoteException If a remote communication error occurs.
     */
    void disconnect(UUID userId) throws RemoteException;

    /**
     * Verifies the password for a user with the specified UUID.
     * @param userId The UUID of the user.
     * @param password The password to verify.
     * @return True if the password is correct; false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean verifyPassword(UUID userId,String password) throws RemoteException;

    /**
     * Checks if the given email address exists and is valid.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address exists and is valid, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    boolean doesEmailExist(String email) throws RemoteException;

    /**
     * Removes an event from the system.
     * @param event The event to be removed.
     * @throws RemoteException If a remote communication error occurs.
     */
    void removeEvent(Event event) throws RemoteException;

    /**
     * Registers a client with the specified UUID and callback interface.
     * @param userId The UUID of the client.
     * @param client The callback interface for the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    void registerClient(UUID userId,ClientCallback client) throws RemoteException;

    /**
     * Retrieves events associated with a user.
     * @param userId The UUID of the user.
     * @return A list of events associated with the specified user.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getUsersEvents(UUID userId) throws RemoteException;

}
