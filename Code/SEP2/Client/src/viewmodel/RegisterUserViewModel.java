package viewmodel;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import model.ClientModel;
import model.User;
import view.ViewHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class RegisterUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;
    private StringProperty firstNameStringProperty;
    private StringProperty lastNameStringProperty;
    private StringProperty phoneNumberStringProperty;
    private DatePicker birthDate;
    private StringProperty genderStringProperty;
    private StringProperty confirmStringProperty;
    public RegisterUserViewModel(ClientModel model){
        this.model = model;
        emailStringProperty = new SimpleStringProperty();
        passwordStringProperty = new SimpleStringProperty();
        firstNameStringProperty = new SimpleStringProperty();
        lastNameStringProperty = new SimpleStringProperty();
        phoneNumberStringProperty = new SimpleStringProperty();
        birthDate = new DatePicker();
        genderStringProperty = new SimpleStringProperty();
        confirmStringProperty = new SimpleStringProperty();
    }

    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
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
        UUID id = UUID.randomUUID();
        User user = new User(id, getEmailStringProperty().get(), getPasswordStringProperty().get(),
                getFirstNameStringProperty().get(), getLastNameStringProperty().get(),
                getGenderStringProperty().get(), getPhoneNumberStringProperty().get(),
                LocalDateTime.now(), getBirthDate().getValue());
        model.createUser(user);
    }

    public StringProperty getConfirmTextStringProperty() {
        return confirmStringProperty;
    }
}
