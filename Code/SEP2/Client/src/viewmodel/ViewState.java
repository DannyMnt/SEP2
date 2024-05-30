package viewmodel;

import java.util.UUID;


/**
 * A singleton class to manage the state of the view.
 * Holds information about the current user's ID.
 */
public class ViewState {

    private static ViewState instance;
    private UUID userID;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the userID to null.
     */
    private ViewState() {
        this.userID = null;
    }

    /**
     * Returns the single instance of ViewState.
     * If the instance is null, it creates a new instance.
     *
     * @return The single instance of ViewState.
     */
    public static ViewState getInstance() {
        if (instance == null) {
            instance = new ViewState();
        }
        return instance;
    }

    /**
     * Gets the current user's ID.
     *
     * @return The UUID of the current user.
     */
    public UUID getUserID() {
        return userID;
    }

    /**
     * Sets the current user's ID.
     *
     * @param userID The UUID to set as the current user's ID.
     */
    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    /**
     * Returns a string representation of the ViewState.
     *
     * @return A string representation of the ViewState.
     */
    @Override
    public String toString() {
        return "ViewState{" +
                "userID=" + userID +
                '}';
    }
}