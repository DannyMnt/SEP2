package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ClientModel;
import model.Event;
import model.User;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class ProfileOverviewViewModel {
    private ClientModel clientModel;
    private StringProperty email;
    private StringProperty phoneNumber;
    private StringProperty firstName;
    private StringProperty lastName;

    private StringProperty sex;
    private StringProperty age;

    private ObservableList<Event> list;

    private User user;


    public ProfileOverviewViewModel(ClientModel clientModel) throws RemoteException {
        this.clientModel = clientModel;
        System.out.println(ViewState.getInstance().getUserID());
        user = clientModel.getUserById(ViewState.getInstance().getUserID());
        list = FXCollections.observableArrayList();

        firstName = new SimpleStringProperty(user.getFirstname());
        lastName = new SimpleStringProperty(user.getLastname());
        sex = new SimpleStringProperty(user.getSex());
        age = new SimpleStringProperty("69");
        email = new SimpleStringProperty(user.getEmail());
        phoneNumber = new SimpleStringProperty(user.getPhoneNumber());
    }

    public boolean editEmail() {
        if (email.getValue() == null || !email.getValue().contains("@"))
            return false;
        return true;
    }

    public boolean editPhoneNumber() {
        if (!phoneNumber.get().matches("\\d*"))
            return false;
        return true;
    }

    public boolean editPhoneCode(){
        return true;
    }

    public void saveUser() throws RemoteException {
        user.setPhoneNumber(phoneNumber.toString());
        user.setEmail(email.toString());
        clientModel.updateUser(user);
    }

    public ObservableList<Event> getEvents() throws RemoteException {
        list.clear();
        List<Event> events = clientModel.getEventsByOwner(UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7"));
        list.addAll(events);
        return list;
    }

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty getFirstNameProperty() {
        return firstName;
    }


    public StringProperty getLastNameProperty() {
        return lastName;
    }

    public StringProperty getSexProperty() {
        return sex;
    }


    public StringProperty getAgeProperty() {
        return age;
    }
}
