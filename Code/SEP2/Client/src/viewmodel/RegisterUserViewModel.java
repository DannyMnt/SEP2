package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import model.ClientModel;
import model.User;
import view.ViewHandler;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;

public class RegisterUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;
    private StringProperty firstNameStringProperty;
    private StringProperty lastNameStringProperty;
    private StringProperty phoneNumberStringProperty;
    private DatePicker birthDate;
    private StringProperty genderStringProperty;
    public RegisterUserViewModel(ClientModel model){
        this.model = model;
        emailStringProperty = new SimpleStringProperty();
        passwordStringProperty = new SimpleStringProperty();
        firstNameStringProperty = new SimpleStringProperty();
        lastNameStringProperty = new SimpleStringProperty();
        phoneNumberStringProperty = new SimpleStringProperty();
        birthDate = new DatePicker();
        genderStringProperty = new SimpleStringProperty();
    }

    public boolean isEmailValid(String email) throws RemoteException {
        return model.isEmailValid(email);
    }

    public StringProperty getEmailStringProperty() {
        return emailStringProperty;
    }

    public StringProperty getPasswordStringProperty() {
        return passwordStringProperty;
    }

    public StringProperty getFirstNameStringProperty() {
        return firstNameStringProperty;
    }

    public StringProperty getLastNameStringProperty() {
        return lastNameStringProperty;
    }

    public DatePicker getBirthDate() {
        return birthDate;
    }

    public StringProperty getGenderStringProperty() {
        return genderStringProperty;
    }

    public StringProperty getPhoneNumberStringProperty() {
        return phoneNumberStringProperty;
    }

    public void createUser() throws RemoteException {
        User user = new User(getEmailStringProperty().get(), getPasswordStringProperty().get(),
                getFirstNameStringProperty().get(), getLastNameStringProperty().get(),
                getGenderStringProperty().get(), getPhoneNumberStringProperty().get(),
                getBirthDate().getValue());
        model.createUser(user);
    }
}