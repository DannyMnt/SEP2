package viewmodel;

import java.util.UUID;

public class ViewState {

    private static ViewState instance;
    private UUID userID;

    private ViewState() {
        this.userID = null;
    }

    public static ViewState getInstance() {
        if (instance == null) {
            instance = new ViewState();
        }
        return instance;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }
}