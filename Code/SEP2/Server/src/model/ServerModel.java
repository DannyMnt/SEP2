package model;

import mediator.LoginPackage;
import utility.observer.javaobserver.UnnamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents the server-side model interface for managing users and events.
 * This interface extends UnnamedPropertyChangeSubject to allow observation of changes.
 */
public interface ServerModel extends UnnamedPropertyChangeSubject {

    /**
     * Creates a new event.
     *
     * @param event The event to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createEvent(Event event) throws RemoteException;

    /**
     * Creates a new user.
     *
     * @param user The user to be created.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createUser(User user) throws RemoteException;

    /**
     * Updates an existing user.
     *
     * @param user The updated user information.
     * @throws RemoteException If a remote communication error occurs.
     */
    void updateUser(User user) throws RemoteException;

    /**
     * Updates the password of a user.
     *
     * @param password The new password.
     * @param uuid     The unique identifier of the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    void updatePassword(String password, UUID uuid) throws RemoteException;

    /**
     * Creates a user event.
     *
     * @param event The event to be associated with the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    void createUserEvent(Event event) throws RemoteException;

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user.
     * @return The user with the specified email address.
     * @throws RemoteException If a remote communication error occurs.
     */
    User getUserByEmail(String email) throws RemoteException;

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user.
     * @return The user with the specified unique identifier.
     * @throws RemoteException If a remote communication error occurs.
     */
    User getUserById(UUID userId) throws RemoteException;

    /**
     * Retrieves a list of events owned by a user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of events owned by the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId) throws RemoteException;

    /**
     * Retrieves a list of events owned by a user within a specified date range.
     *
     * @param userId     The unique identifier of the user.
     * @param startDate  The start date of the date range.
     * @param endDate    The end date of the date range.
     * @return A list of events owned by the user within the specified date range.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException;

    /**
     * Retrieves an event by its unique identifier.
     *
     * @param eventId The unique identifier of the event.
     * @return The event with the specified unique identifier.
     * @throws RemoteException If a remote communication error occurs.
     */
    Event getEvent(UUID eventId) throws RemoteException;

    /**
     * Checks if an email address is available.
     *
     * @param email The email address to check.
     * @return True if the email address is available, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean isEmailFree(String email) throws RemoteException;

    /**
     * Searches for users by name.
     *
     * @param search The search query.
     * @return A list of users matching the search query.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<User> searchUsersByName(String search) throws RemoteException;

    /**
     * Logs in a user with the provided login credentials.
     *
     * @param loginPackage The login package containing the user's credentials.
     * @return The login package with authentication status and user information.
     * @throws Exception If an error occurs during login.
     */
    LoginPackage loginUser(LoginPackage loginPackage) throws Exception;


    /**
     * Verifies a user's password.
     *
     * @param userId   The unique identifier of the user.
     * @param password The password to verify.
     * @return True if the password is correct, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean verifyPassword(UUID userId,String password) throws RemoteException;

    /**
     * Checks if the domain of an email has is valid and if it has mail exchange records.
     *
     * @param email The email address to check.
     * @return True if the email address exists, false otherwise.
     * @throws RemoteException If a remote communication error occurs.
     */
    boolean doesEmailExist(String email) throws RemoteException;

    /**
     * Removes an event from the system.
     *
     * @param event The event to be removed.
     * @throws RemoteException If a remote communication error occurs.
     */
    void removeEvent(Event event) throws RemoteException;

    /**
     * Retrieves a list of events associated with a user.
     *
     * @param userId The unique identifier of the user.
     * @return A list of events associated with the user.
     * @throws RemoteException If a remote communication error occurs.
     */
    List<Event> getUsersEvents(UUID userId) throws RemoteException;

}
