package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.LocalDateStringConverter;
import model.ClientModel;
import model.Event;
import model.User;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private User user;


    public ProfileOverviewViewModel(ClientModel clientModel) throws RemoteException {
        this.clientModel = clientModel;
        System.out.println(ViewState.getInstance().getUserID());
        user = clientModel.getUserById(ViewState.getInstance().getUserID());
        list = FXCollections.observableArrayList();


        fullName = new SimpleStringProperty(user.getFirstname() + " " + user.getLastname());
        gender = new SimpleStringProperty(user.getSex());
        dateOfBirth = new SimpleStringProperty(user.getDateOfBirth().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        email = new SimpleStringProperty(user.getEmail());
        phoneNumber = new SimpleStringProperty(user.getPhoneNumber());
        phoneNumber2 = new SimpleStringProperty();
        String[] parts = phoneNumber.get().split(" ", 2);
        phoneNumber2.set(parts[1]);
        oldPassword = new SimpleStringProperty(user.getPassword());
        newPassword = new SimpleStringProperty();
        checkPassword = new SimpleStringProperty();
    }

    public boolean editEmail() {
        if (email.getValue() == null || !email.getValue().contains("@"))
            return false;
        return true;
    }

    public boolean editPhoneNumber() {
        if (!phoneNumber2.get().matches("\\d*"))
            return false;
        return true;
    }

    public boolean editPhoneCode(){
        return true;
    }

    public void saveUser() throws RemoteException {
        user.setPhoneNumber(getPhoneNumberProperty().get());
        user.setEmail(getEmailTextFieldProperty().get());
        clientModel.updateUser(user);
    }

    public ObservableList<Event> getEvents() throws RemoteException {
        list.clear();
        List<Event> events = clientModel.getEventsByOwner(UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"));
        list.addAll(events);
        return list;
    }

    public boolean resetPassword() throws RemoteException {
        if(getOldPasswordProperty().get().equals(user.getPassword()) && getNewPasswordProperty().get().equals(getCheckPasswordProperty().get())) {
            clientModel.updatePassword(getNewPasswordProperty().get(), user.getId());
            return true;
        }
        return false;
    }

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber2;
    }

    public StringProperty getPhoneNumber(){
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
    public StringProperty getOldPasswordProperty(){
        return oldPassword;
    }
    public StringProperty getCheckPasswordProperty(){
        return checkPassword;
    }
}
