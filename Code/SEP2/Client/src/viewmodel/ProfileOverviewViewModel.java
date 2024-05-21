package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ClientModel;
import model.Event;
import model.User;

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
    private User user;


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
        getUser(ViewState.getInstance());
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
        this.eventTitle.set(getEvent().get(0).getTitle());
        LocalDateTime dateTimeStart = LocalDateTime.parse(getEvent().get(0).getStartTime().toString());
        LocalDateTime dateTimeEnd = LocalDateTime.parse(getEvent().get(0).getEndTime().toString());
        this.eventDate.set(dateTimeStart.toLocalDate().toString());
        this.eventTime.set(dateTimeStart.toLocalTime() + " to " + dateTimeEnd.toLocalTime());
        this.eventDescription.set(getEvent().get(0).getDescription());
        this.eventLocation.set(getEvent().get(0).getLocation());
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
         return clientModel.verifyPassword(ViewState.getInstance().getUserID(), getNewPasswordProperty().get());
    }

    public boolean resetPassword() throws RemoteException {
        if(verifyPassword(getOldPasswordProperty().get())){
          return getNewPasswordProperty().get()
              .equals(getCheckPasswordProperty().get());
        }
        return false;
    }

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
