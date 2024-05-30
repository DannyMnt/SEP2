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

            System.out.println("Phone length: "+ phoneNumberStringProperty.getValue().length());
            System.out.println("Phone length: "+ phoneNumberStringProperty.get());
            System.out.println(firstNameStringProperty.getValue());
            if(isNullOrEmpty(emailStringProperty.get())){
                throw new Exception("Email cannot be empty");
            }
            else if(isNullOrEmpty(passwordStringProperty.get())){
                throw new Exception("Password cannot be empty");
            }
            else if(isNullOrEmpty(confirmStringProperty.get())){
                throw new Exception("Passwords do not match");
            }
            else if(isNullOrEmpty(firstNameStringProperty.get())){
                throw new Exception("First name cannot be empty");
            }
            else if(isNullOrEmpty(lastNameStringProperty.get())){
                throw new Exception("Last name cannot be empty");
            }
            else if(isNullOrEmpty(genderStringProperty.get())){
                throw new Exception("Gender cannot be empty");
            }
            else if(isNullOrEmpty(phoneNumberStringProperty.get())){
                throw new Exception("Phone number cannot be empty");
            }
            else if(isNullOrEmpty(birthDate.getValue().toString())){
                throw new Exception("Birthday cannot bet empty");
            }
            else if(imageProperty.getValue() == null){
                throw new Exception("Profile picture cannot be empty");
            }

            else if(phoneNumberStringProperty.getValue().length() >= 30){
                throw new Exception("Phone number is too long");
            }
            else if(firstNameStringProperty.getValue().length() >= 255){
                throw new Exception("First name is too long");
            }
            else if(lastNameStringProperty.getValue().length() >= 255){
                throw new Exception("Last name is too long");
            }
            else if(emailStringProperty.getValue().length() >= 355){
                throw new Exception("Email is not valid");
            }
            else if(passwordStringProperty.getValue().length() >= 255){
                throw new Exception("Password is too long");
            }

            else if(!isEmailFree(emailStringProperty.get())){
                throw new Exception("Email is already taken");
            }else if (!doesEmailExist(emailStringProperty.get())){
                throw new Exception("Email is not valid");
            }

            else if(!passwordStringProperty.get().equals(confirmStringProperty.get())){
                throw new Exception("Passwords do not match");
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
