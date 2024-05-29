package viewmodel;

import com.google.i18n.phonenumbers.Phonenumber;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public Event getUpcomingEvent() {
        return upcomingEvent;
    }

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

    public void reset() {
        try {

            updateProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAttendees(List<UUID> list) throws RemoteException {
        List<model.User> users = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            users.add(clientModel.getUserById(list.get(i)));

        return users;
    }

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


    public void saveUser() throws RemoteException {
        user.setPhoneNumber(phoneNumber.getValue());
        user.setEmail(getEmailTextFieldProperty().get());
        System.out.println("Saving User: " + user.getPhoneNumber() + ", " + user.getEmail());
        clientModel.updateUser(user);
    }


    public boolean verifyPassword(String password) throws RemoteException {
        return clientModel.verifyPassword(ViewState.getInstance().getUserID(), password);
    }

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

    public StringProperty errorPasswordProperty() {
        return errorPassword;
    }

    public StringProperty errorUserEditProperty() {
        return errorUserEdit;
    }

    public SimpleObjectProperty<Image> getImageProperty() {
        return imageProperty;
    }

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty getFullNameProperty() {
        return fullName;
    }

    public StringProperty getGenderProperty() {
        return gender;
    }

    public StringProperty getDateOfBirthProperty() {
        return dateOfBirth;
    }

    public StringProperty getNewPasswordProperty() {
        return newPassword;
    }

    public StringProperty getOldPasswordProperty() {
        return oldPassword;
    }

    public StringProperty getCheckPasswordProperty() {
        return checkPassword;
    }

    public StringProperty getEventTitleProperty() {
        return eventTitle;
    }

    public StringProperty getEventDateProperty() {
        return eventDate;
    }

    public StringProperty getEventTimeProperty() {
        return eventTime;
    }

    public StringProperty getEventDescriptionProperty() {
        return eventDescription;
    }

    public StringProperty getEventLocationProperty() {
        return eventLocation;
    }
}
