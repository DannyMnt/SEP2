package viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import mediator.LoginPackage;
import model.ClientModel;
import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;
    private StringProperty firstNameStringProperty;
    private StringProperty lastNameStringProperty;
    private StringProperty phoneNumberStringProperty;
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
                    imageProperty.getValue() == null) {
                throw new Exception("Fields cannot be empty");
            }
            else if(!isEmailFree(emailStringProperty.get())){
                throw new Exception("An account with this email already exists!");
            }else if (!doesEmailExist(emailStringProperty.get())){
                throw new Exception("The domain of the email does not exist");
            }

            else if(!passwordStringProperty.get().equals(confirmStringProperty.get())){
                throw new Exception("Passwords need to be same!");
            }

            byte[] imageData = imageToByteArray(imageProperty.getValue());
            UUID id = UUID.randomUUID();
            User user = new User(id, getEmailStringProperty().get(), getPasswordStringProperty().get(),
                getFirstNameStringProperty().get(), getLastNameStringProperty().get(),
                getGenderStringProperty().get(), getPhoneNumberStringProperty().get(),
                LocalDateTime.now(), birthDate.get(), imageData);
        model.createUser(user);
        model.loginUser(new LoginPackage(user.getEmail(), user.getPassword()));

return true;
        } catch (Exception e) {
            errorStringProperty.set(e.getMessage());
        return false;
        }
    }

    public static byte[] imageToByteArray(Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public StringProperty getConfirmTextStringProperty() {
        return confirmStringProperty;
    }



    public boolean doesEmailExist(String email){
        try
        {
            return model.doesEmailExist(email);

        }catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }
}
