package viewmodel;

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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ProfileOverviewViewModel {
    private ClientModel clientModel;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty gender;
    private StringProperty dateOfBirth;
    private StringProperty fullName;
    private ObservableList<Event> list;
    private StringProperty oldPassword;
    private StringProperty newPassword;
    private StringProperty checkPassword;
    private StringProperty phoneNumber2;
    private StringProperty eventTitle;
    private StringProperty eventDate;
    private StringProperty eventTime;
    private StringProperty eventDescription;
    private StringProperty eventLocation;

    private StringProperty errorLabel2;

    private SimpleObjectProperty<Image> imageProperty;

    private User user;

    private boolean passwordVerified;



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
        this.phoneNumber2 = new SimpleStringProperty();
        this.list = FXCollections.observableArrayList();
        this.eventTitle = new SimpleStringProperty();
        this.eventDate = new SimpleStringProperty();
        this.eventTime = new SimpleStringProperty();
        this.eventDescription = new SimpleStringProperty();
        this.eventLocation = new SimpleStringProperty();
        this.imageProperty = new SimpleObjectProperty<>();
        getUser(ViewState.getInstance());
        this.passwordVerified = false;
        this.errorLabel2 = new SimpleStringProperty();
    }

    public boolean editEmail() {
        if (email.getValue() == null || !email.getValue().contains("@"))
            return false;
        return true;
    }

    public void getUser(ViewState viewState) throws RemoteException {
        user = clientModel.getUserById(viewState.getUserID());

        fullName.set(user.getFirstname() + " " + user.getLastname());
        gender.set(user.getSex());
        dateOfBirth.set(user.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        email.set(user.getEmail());
        String phone = user.getPhoneNumber();
        String[] parts = phone.split(" ", 2);
        phoneNumber.set(parts[0]);
        if (parts.length > 1) {
            phoneNumber2.set(parts[1]);
        }
        oldPassword.set(user.getPassword());
        if(getEvent() != null){


        this.eventTitle.set(getEvent().get(0).getTitle());
        LocalDateTime dateTimeStart = LocalDateTime.parse(getEvent().get(0).getStartTime().toString());
        LocalDateTime dateTimeEnd = LocalDateTime.parse(getEvent().get(0).getEndTime().toString());
        this.eventDate.set(dateTimeStart.toLocalDate().toString());
        this.eventTime.set(dateTimeStart.toLocalTime() + " to " + dateTimeEnd.toLocalTime());
        this.eventDescription.set(getEvent().get(0).getDescription());
        this.eventLocation.set(getEvent().get(0).getLocation());
        }
        this.imageProperty.set(new Image(new ByteArrayInputStream(user.getProfilePicture())));

    }

    public String getErrorLabel2()
    {
        return errorLabel2.get();
    }

    public StringProperty errorLabel2Property()
    {
        return errorLabel2;
    }

    public boolean editPhoneNumber() {
        if (!getPhoneNumberProperty2().get().matches("\\d*"))
            return false;
        return true;
    }

    public boolean editPhoneCode(){
        return true;
    }

    public void saveUser() throws RemoteException {
        String phoneNumber = getPhoneNumberProperty().get() + " " + getPhoneNumberProperty2().get();
        user.setPhoneNumber(phoneNumber);
        user.setEmail(getEmailTextFieldProperty().get());
        clientModel.updateUser(user);
    }

    public ObservableList<Event> getEvent() throws RemoteException {
        list.clear();
        List<Event> events = clientModel.getEventsByOwner(UUID.fromString(ViewState.getInstance().getUserID().toString()));
        LocalDateTime now = LocalDateTime.now();

        events.stream().filter(event -> event.getStartTime().isAfter(now)).forEach(list::add);
        list.addAll(events);
        if(list.isEmpty())
            return null;
        return list;
    }

    public boolean verifyPassword(String password) throws RemoteException{
         return clientModel.verifyPassword(ViewState.getInstance().getUserID(), password);
    }

    public void onTextFieldLostFocus(){
        String text = oldPassword.get();

            new Thread(() ->{
                try
                {
                    System.out.println("here");
                    passwordVerified = verifyPassword(text);
                }
                catch (RemoteException e)
                {
                    throw new RuntimeException(e);
                }
                System.out.println("here1");
                Platform.runLater(() ->{
                    if(!passwordVerified){
                        System.out.println("here2");
                        errorLabel2.setValue("Current password is incorrect");
                    }else{
                        errorLabel2.setValue("");
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
                    if(passwordVerified) errorLabel2.setValue("Enter your new password");
                } else if (checkPasswordText == null || checkPasswordText.isEmpty()) {
                    if(passwordVerified) errorLabel2.setValue("Confirm your new password");
                } else if (!newPasswordText.equals(checkPasswordText)) {
                    if(passwordVerified) errorLabel2.setValue("New passwords do not match");
                } else {
                    errorLabel2.setValue("");
                }
            });
        }).start();
    }

    public boolean resetPassword() throws RemoteException {


        if(getOldPasswordProperty().get() == null || getOldPasswordProperty().get().isEmpty()){

            errorLabel2.setValue("Current password not filled in");
            return false;
        }

        if(getNewPasswordProperty().get() == null || getNewPasswordProperty().get().isEmpty()){
            errorLabel2.setValue("Enter your new password");
            return false;
        }

        if(getCheckPasswordProperty().get() == null || getCheckPasswordProperty().get().isEmpty()){
            errorLabel2.setValue("Confirm your new password");
            return false;
        }

        if(passwordVerified){
            if(getCheckPasswordProperty().get().equals(getNewPasswordProperty().get())){
                clientModel.updatePassword(getNewPasswordProperty().get(),user.getId());
                return true;
            }else {
                errorLabel2.setValue("New passwords do not match");
                return false;
            }

        }


        return false;
    }

    public SimpleObjectProperty<Image> getImageProperty(){return imageProperty;}

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty getPhoneNumberProperty2(){
        return phoneNumber2;
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
    public StringProperty getOldPasswordProperty(){
        return oldPassword;
    }
    public StringProperty getCheckPasswordProperty(){
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
