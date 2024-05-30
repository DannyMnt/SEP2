package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import model.ClientModel;
import model.Event;
import model.User;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * ViewModel for the profile overview, handling user data and events.
 */
public class ProfileOverviewViewModel {
    private ClientModel clientModel;
    private StringProperty email;
    private StringProperty gender;
    private StringProperty dateOfBirth;
    private StringProperty fullName;
    private ObservableList<Event> list;
    private StringProperty oldPassword;
    private StringProperty newPassword;
    private StringProperty checkPassword;
    private StringProperty eventTitle;
    private StringProperty eventDate;
    private StringProperty eventTime;
    private StringProperty eventDescription;
    private StringProperty eventLocation;

    private StringProperty errorPassword;
    private StringProperty errorUserEdit;
    private StringProperty phoneNumber;


    private SimpleObjectProperty<Image> imageProperty;

    private User user;

    private boolean passwordVerified;

    private Event upcomingEvent;


    /**
     * Constructs a ProfileOverviewViewModel with the given client model.
     *
     * @param clientModel the client model to interact with
     * @throws RemoteException if an RMI error occurs
     */
    public ProfileOverviewViewModel(ClientModel clientModel) throws RemoteException {
        this.clientModel = clientModel;
        this.email = new SimpleStringProperty();
        this.phoneNumber = new SimpleStringProperty();
        this.gender = new SimpleStringProperty();
        this.dateOfBirth = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.oldPassword = new SimpleStringProperty();
        this.newPassword = new SimpleStringProperty();
        this.checkPassword = new SimpleStringProperty();
        this.list = FXCollections.observableArrayList();
        this.eventTitle = new SimpleStringProperty();
        this.eventDate = new SimpleStringProperty();
        this.eventTime = new SimpleStringProperty();
        this.eventDescription = new SimpleStringProperty();
        this.eventLocation = new SimpleStringProperty();
        this.imageProperty = new SimpleObjectProperty<>();
        this.passwordVerified = false;

        this.errorPassword = new SimpleStringProperty();
        this.errorUserEdit = new SimpleStringProperty();
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Gets the upcoming event.
     *
     * @return the upcoming event
     */
    public Event getUpcomingEvent() {
        return upcomingEvent;
    }

    /**
     * Updates the user profile information from the client model.
     *
     * @throws RemoteException if an RMI error occurs
     */
    public void updateProfile() throws RemoteException {
        user = clientModel.getUser();
        upcomingEvent = clientModel.getUpcomingEvent();

        fullName.set(user.getFirstname() + " " + user.getLastname());
        gender.set(user.getSex());
        dateOfBirth.set(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        email.set(user.getEmail());
        phoneNumber.set(user.getPhoneNumber());

        oldPassword.set(user.getPassword());

        this.imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));

    }

    /**
     * Resets the user profile by re-fetching the data from the client model.
     */
    public void reset() {
        try {

            updateProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets the list of attendees for a given list of UUIDs.
     *
     * @param list the list of UUIDs of the attendees
     * @return the list of User objects representing the attendees
     * @throws RemoteException if an RMI error occurs
     */
    public List<User> getAttendees(List<UUID> list) throws RemoteException {
        List<model.User> users = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            users.add(clientModel.getUserById(list.get(i)));

        return users;
    }

    /**
     * Edits the user profile with the updated information.
     *
     * @return true if the edit was successful, false otherwise
     */
    public boolean editUser() {

        try {
            // Check whether user did any change if not it stops editing and does not send any request to server.
            if (phoneNumber.getValue().equals(user.getPhoneNumber()) && email.getValue().equals(user.getEmail())) {
                errorUserEdit.set("");
                return true;
            } else if (isNullOrEmpty(phoneNumber.getValue())) {
                throw new Exception("Phone number cannot be empty");
            } else if (phoneNumber.getValue().length() <= 1) {
                throw new Exception("Phone number is too short");
            } else if (phoneNumber.getValue().length() >= 30) {
                throw new Exception("Phone number is too long");
            } else if (isNullOrEmpty(email.getValue())) {
                throw new Exception("Email cannot be empty");
            } else if (email.getValue().length() >= 320) {
                throw new Exception("Email is not valid");
            } else if (!clientModel.isEmailFree(email.getValue())) {
                throw new Exception("Email is already taken");
            } else if (!clientModel.doesEmailExist(email.getValue())) {
                throw new Exception("Email is not valid");
            }
            saveUser();
            errorUserEdit.set("");
            return true;
        } catch (Exception e) {
            errorUserEdit.set(e.getMessage());
            return false;
        }

    }

    /**
     * Saves the user profile information to the client model.
     *
     * @throws RemoteException if an RMI error occurs
     */
    public void saveUser() throws RemoteException {
        user.setPhoneNumber(phoneNumber.getValue());
        user.setEmail(getEmailTextFieldProperty().get());
        System.out.println("Saving User: " + user.getPhoneNumber() + ", " + user.getEmail());
        clientModel.updateUser(user);
    }


    /**
     * Verifies the user's password.
     *
     * @param password the password to verify
     * @return true if the password is correct, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    public boolean verifyPassword(String password) throws RemoteException {
        return clientModel.verifyPassword(ViewState.getInstance().getUserID(), password);
    }

    /**
     * Handles the event when the old password text field loses focus.
     */
    public void onTextFieldLostFocus() {
        String text = oldPassword.get();

        new Thread(() -> {
            try {
                System.out.println("here");
                passwordVerified = verifyPassword(text);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            System.out.println("here1");
            Platform.runLater(() -> {
                if (!passwordVerified) {
                    System.out.println("here2");
                    errorPassword.setValue("Current password is incorrect");
                } else {
                    errorPassword.setValue("");
                }
            });
        }).start();
    }


    /**
     * Handles the event when the new password text field loses focus.
     */
    public void onNewPasswordFieldLostFocus() {
        String newPasswordText = newPassword.get();
        String checkPasswordText = checkPassword.get();

        new Thread(() -> {
            Platform.runLater(() -> {
                if (newPasswordText == null || newPasswordText.isEmpty()) {
                    if (passwordVerified) errorPassword.setValue("Enter your new password");
                } else if (checkPasswordText == null || checkPasswordText.isEmpty()) {
                    if (passwordVerified) errorPassword.setValue("Confirm your new password");
                } else if (!newPasswordText.equals(checkPasswordText)) {
                    if (passwordVerified) errorPassword.setValue("New passwords do not match");
                } else {
                    errorPassword.setValue("");
                }
            });
        }).start();
    }

    /**
     * Resets the user's password.
     *
     * @return true if the password reset was successful, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    public boolean resetPassword() throws RemoteException {


        if (getOldPasswordProperty().get() == null || getOldPasswordProperty().get().isEmpty()) {

            errorPassword.setValue("Current password not filled in");
            return false;
        }

        if (getNewPasswordProperty().get() == null || getNewPasswordProperty().get().isEmpty()) {
            errorPassword.setValue("Enter your new password");
            return false;
        }

        if (getCheckPasswordProperty().get() == null || getCheckPasswordProperty().get().isEmpty()) {
            errorPassword.setValue("Confirm your new password");
            return false;
        }

        if (passwordVerified) {
            if (getCheckPasswordProperty().get().equals(getNewPasswordProperty().get())) {
                clientModel.updatePassword(getNewPasswordProperty().get(), user.getId());
                return true;
            } else {
                errorPassword.setValue("New passwords do not match");
                return false;
            }

        }


        return false;
    }

    /**
     * Gets the error property for password-related errors.
     *
     * @return the error password property
     */
    public StringProperty errorPasswordProperty() {
        return errorPassword;
    }

    /**
     * Gets the error property for user edit-related errors.
     *
     * @return the error user edit property
     */
    public StringProperty errorUserEditProperty() {
        return errorUserEdit;
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
     * Gets the email text field property.
     *
     * @return the email text field property
     */
    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    /**
     * Gets the phone number property.
     *
     * @return the phone number property
     */
    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    /**
     * Gets the full name property.
     *
     * @return the full name property
     */
    public StringProperty getFullNameProperty() {
        return fullName;
    }

    /**
     * Gets the gender property.
     *
     * @return the gender property
     */
    public StringProperty getGenderProperty() {
        return gender;
    }

    /**
     * Gets the date of birth property.
     *
     * @return the date of birth property
     */
    public StringProperty getDateOfBirthProperty() {
        return dateOfBirth;
    }


    /**
     * Gets the new password property.
     *
     * @return the new password property
     */
    public StringProperty getNewPasswordProperty() {
        return newPassword;
    }

    /**
     * Gets the old password property.
     *
     * @return the old password property
     */
    public StringProperty getOldPasswordProperty() {
        return oldPassword;
    }

    /**
     * Gets the check password property.
     *
     * @return the check password property
     */
    public StringProperty getCheckPasswordProperty() {
        return checkPassword;
    }

    /**
     * Gets the event title property.
     *
     * @return the event title property
     */
    public StringProperty getEventTitleProperty() {
        return eventTitle;
    }

    /**
     * Gets the event date property.
     *
     * @return the event date property
     */
    public StringProperty getEventDateProperty() {
        return eventDate;
    }

    /**
     * Gets the event time property.
     *
     * @return the event time property
     */
    public StringProperty getEventTimeProperty() {
        return eventTime;
    }

    /**
     * Gets the event description property.
     *
     * @return the event description property
     */
    public StringProperty getEventDescriptionProperty() {
        return eventDescription;
    }

    /**
     * Gets the event location property.
     *
     * @return the event location property
     */
    public StringProperty getEventLocationProperty() {
        return eventLocation;
    }
}
