package model;

import mediator.LoginPackage;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The ClientModel interface defines methods for client-side operations related to event scheduling and user management.
 * This interface extends NamedPropertyChangeSubject to support observer pattern for property change notifications.
 */
public interface ClientModel extends NamedPropertyChangeSubject {


    /**
     * Creates a new event.
     * @param event The event to create.
     * @throws RemoteException if a remote communication error occurs.
     */
    void createEvent(Event event) throws RemoteException;

    /**
     * Creates a new user.
     * @param user The user to create.
     * @throws RemoteException if a remote communication error occurs.
     */
    void createUser(User user) throws RemoteException;

    /**
     * Creates associations between users and events.
     * @param event The event for which associations are to be created.
     * @throws RemoteException if a remote communication error occurs.
     */
    void createUserEvent(Event event) throws RemoteException;

    /**
     * Updates user information.
     * @param user The user object containing updated information.
     * @throws RemoteException if a remote communication error occurs.
     */
    void updateUser(User user) throws RemoteException;

    /**
     * Updates the password for a user.
     * @param password The new password.
     * @param uuid The UUID of the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    void updatePassword(String password, UUID uuid) throws RemoteException;

    /**
     * Retrieves a user by email.
     * @param email The email of the user.
     * @return The user corresponding to the email.
     * @throws RemoteException if a remote communication error occurs.
     */
    User getUserByEmail(String email) throws RemoteException;


    /**
     * Retrieves a user by ID.
     * @param userId The UUID of the user.
     * @return The user corresponding to the ID.
     * @throws RemoteException if a remote communication error occurs.
     */
    User getUserById(UUID userId) throws RemoteException;

    /**
     * Retrieves events owned by a user.
     * @param userId The UUID of the user.
     * @return A list of events owned by the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId) throws RemoteException;

    /**
     * Retrieves events owned by a user within a specified date range.
     * @param userId The UUID of the user.
     * @param startDate The start date of the range.
     * @param endDate The end date of the range.
     * @return A list of events owned by the user within the specified date range.
     * @throws RemoteException if a remote communication error occurs.
     */
    List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException;

    /**
     * Retrieves an event by its ID.
     * @param eventId The UUID of the event.
     * @return The event corresponding to the ID.
     * @throws RemoteException if a remote communication error occurs.
     */
    Event getEvent(UUID eventId) throws RemoteException;

    /**
     * Searches users by name.
     * @param search The search string.
     * @return A list of users matching the search criteria.
     * @throws RemoteException if a remote communication error occurs.
     */
     List<User> searchUsersByName(String search) throws RemoteException;

    /**
     * Checks if an email is available (not already registered).
     * @param email The email to check.
     * @return True if the email is available; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
     boolean isEmailFree(String email) throws RemoteException;

    /**
     * Logs in a user.
     * @param loginPackage The login package containing login credentials.
     * @return The logged-in user's information.
     * @throws Exception if an error occurs during login process.
     */
    LoginPackage loginUser(LoginPackage loginPackage) throws Exception;


    /**
     * Disconnects the user.
     * @param userId The UUID of the user to disconnect.
     * @throws RemoteException if a remote communication error occurs.
     */
    void disconnect(UUID userId)throws RemoteException;


    /**
     * Verifies the password for a user.
     * @param userId The UUID of the user.
     * @param password The password to verify.
     * @return True if the password is verified; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    boolean verifyPassword(UUID userId, String password) throws RemoteException;

    /**
     * Checks if the given email address exists and is valid.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address exists and is valid, {@code false} otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    boolean doesEmailExist(String email) throws RemoteException;

    /**
     * Removes an event.
     * @param event The event to remove.
     * @throws RemoteException if a remote communication error occurs.
     */
    void removeEvent(Event event) throws RemoteException;

    /**
     * Retrieves events associated with a user.
     * @param userId The UUID of the user.
     * @return A list of events associated with the user.
     * @throws RemoteException if a remote communication error occurs.
     */
    List<Event> getUsersEvents(UUID userId) throws RemoteException;

    /**
     * Adds a listener to the client model.
     * @param object The listener to add.
     */
    void addListener(Object object);


    /**
     * Gets the current user.
     * @return The current user.
     */
  User getUser();

    /**
     * Sets the current user.
     * @param user The user to set.
     */
  void setUser(User user);

    /**
     * Retrieves the upcoming event.
     * @return The upcoming event.
     */
  Event getUpcomingEvent();
}
