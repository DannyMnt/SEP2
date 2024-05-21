package viewmodel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.beans.property.*;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import model.ClientModel;
import model.Country;
import model.User;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Type;
import view.ViewHandler;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class RegisterUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;
    private StringProperty firstNameStringProperty;
    private StringProperty lastNameStringProperty;
    private StringProperty phoneNumberStringProperty;
    private ObjectProperty<Country> prefixPhoneStringProperty;
    private SimpleObjectProperty<LocalDate> birthDate;
    private StringProperty genderStringProperty;
    private StringProperty confirmStringProperty;

    private StringProperty errorStringProperty;

    private SimpleObjectProperty<Image> imageProperty;


    public RegisterUserViewModel(ClientModel model) {
        this.model = model;
        emailStringProperty = new SimpleStringProperty();
        passwordStringProperty = new SimpleStringProperty();
        firstNameStringProperty = new SimpleStringProperty();
        lastNameStringProperty = new SimpleStringProperty();
        prefixPhoneStringProperty = new SimpleObjectProperty();
        phoneNumberStringProperty = new SimpleStringProperty();
        birthDate = new SimpleObjectProperty<LocalDate>();
        genderStringProperty = new SimpleStringProperty();
        confirmStringProperty = new SimpleStringProperty();
        errorStringProperty = new SimpleStringProperty();
        imageProperty = new SimpleObjectProperty<>();

    }

    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
    }

    public StringProperty getEmailStringProperty() {
        return emailStringProperty;
    }


    public SimpleObjectProperty<Image> getImagePropertyProperty() {
        return imageProperty;
    }

    public ObjectProperty<Country> getPrefixPhoneStringProperty() {
        return prefixPhoneStringProperty;
    }

    public StringProperty getErrorStringProperty() {
        return errorStringProperty;
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

    public SimpleObjectProperty<LocalDate> getBirthProperty() {
        return birthDate;
    }

    public StringProperty getGenderStringProperty() {
        return genderStringProperty;
    }

    public StringProperty getPhoneNumberStringProperty() {
        return phoneNumberStringProperty;
    }

    private String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());

    }

    public boolean register() {
        try {
            if (isNullOrEmpty(emailStringProperty.get()) ||
                    isNullOrEmpty(passwordStringProperty.get()) ||
                    isNullOrEmpty(confirmStringProperty.get()) ||
                    isNullOrEmpty(firstNameStringProperty.get()) ||
                    isNullOrEmpty(lastNameStringProperty.get()) ||
                    isNullOrEmpty(genderStringProperty.get()) ||
                    isNullOrEmpty(phoneNumberStringProperty.get()) ||
                    isNullOrEmpty(birthDate.getValue().toString()) ||
                    prefixPhoneStringProperty.getValue() == null ||
                    imageProperty.getValue() == null) {
                throw new Exception("Fields cannot be empty");
            }
            else if(!isEmailFree(emailStringProperty.get())){
                throw new Exception("Email is already taken!");
            }
//              POSSIBLE EMAIL VALIDATION
            //            else if(!isValidEmail(emailStringProperty.get())){
//                throw new Exception("Email is not valid!");
//            }
            else if(!passwordStringProperty.get().equals(confirmStringProperty.get())){
                throw new Exception("Passwords need to be same!");
            }
//            else if()


            errorStringProperty.set("Successfully registered");
        } catch (Exception e) {
            errorStringProperty.set(e.getMessage());
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void createUser() throws RemoteException {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, getEmailStringProperty().get(), hashPassword(getPasswordStringProperty().get()),
//                getFirstNameStringProperty().get(), getLastNameStringProperty().get(),
//                getGenderStringProperty().get(), getPhoneNumberStringProperty().get(),
//                LocalDateTime.now(), getBirthDate().getValue());
//        model.createUser(user);
    }

    public StringProperty getConfirmTextStringProperty() {
        return confirmStringProperty;
    }

    public static boolean isValidEmail(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            // Address is not valid
        }

        if (isValid) {
            isValid = isValidDomain(email);
        }

        return isValid;
    }

    public static boolean isValidDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1);
        try {
            Lookup lookup = new Lookup(domain, Type.MX);
            lookup.run();
            if (lookup.getResult() == Lookup.SUCCESSFUL) {
                return true;
            }
        } catch (Exception e) {
            // DNS lookup failed
        }
        return false;
    }
}
