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

/**
 * ViewModel class for user registration.
 */
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


    /**
     * Constructor for RegisterUserViewModel.
     * Initializes the properties used in the registration process.
     *
     * @param model The ClientModel instance to be used by the ViewModel.
     */
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

    /**
     * Checks if the given email is free.
     *
     * @param email The email to check.
     * @return True if the email is free, false otherwise.
     * @throws RemoteException If an error occurs during the remote method invocation.
     */
    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
    }


    /**
     * Registers a new user with the provided properties.
     *
     * @return true if the registration was successful, false otherwise
     */
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

    /**
     * Converts an Image object to a byte array.
     *
     * @param image the image to convert
     * @return the byte array representing the image
     * @throws IOException if an I/O error occurs
     */
    public static byte[] imageToByteArray(Image image) throws IOException {
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        return baos.toByteArray();
    }


    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Gets the confirm text string property.
     *
     * @return the confirm text string property
     */
    public StringProperty getConfirmTextStringProperty() {
        return confirmStringProperty;
    }


    /**
     * Checks if the given email address exists and is valid.
     *
     * @param email The email address to check.
     * @return {@code true} if the email address exists and is valid, {@code false} otherwise.
     */
    public boolean doesEmailExist(String email){
        try
        {
            return model.doesEmailExist(email);

        }catch (RemoteException e){
            e.printStackTrace();
        }
        return false;
    }

    public StringProperty getEmailStringProperty()
    {
        return emailStringProperty;
    }



    public void setEmailStringProperty(String emailStringProperty)
    {
        this.emailStringProperty.set(emailStringProperty);
    }

    public StringProperty getPasswordStringProperty()
    {
        return passwordStringProperty;
    }


    public void setPasswordStringProperty(String passwordStringProperty)
    {
        this.passwordStringProperty.set(passwordStringProperty);
    }

    public StringProperty getFirstNameStringProperty()
    {
        return firstNameStringProperty;
    }


    public void setFirstNameStringProperty(String firstNameStringProperty)
    {
        this.firstNameStringProperty.set(firstNameStringProperty);
    }

    public StringProperty getLastNameStringProperty()
    {
        return lastNameStringProperty;
    }


    public void setLastNameStringProperty(String lastNameStringProperty)
    {
        this.lastNameStringProperty.set(lastNameStringProperty);
    }

    public StringProperty getPhoneNumberStringProperty()
    {
        return phoneNumberStringProperty;
    }


    public void setPhoneNumberStringProperty(String phoneNumberStringProperty)
    {
        this.phoneNumberStringProperty.set(phoneNumberStringProperty);
    }

    public SimpleObjectProperty<LocalDate> getBirthProperty()
    {
        return birthDate;
    }



    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate.set(birthDate);
    }

    public StringProperty getGenderStringProperty()
    {
        return genderStringProperty;
    }



    public void setGenderStringProperty(String genderStringProperty)
    {
        this.genderStringProperty.set(genderStringProperty);
    }

    public StringProperty getConfirmStringProperty()
    {
        return confirmStringProperty;
    }



    public void setConfirmStringProperty(String confirmStringProperty)
    {
        this.confirmStringProperty.set(confirmStringProperty);
    }

    public StringProperty getErrorStringProperty()
    {
        return errorStringProperty;
    }



    public void setErrorStringProperty(String errorStringProperty)
    {
        this.errorStringProperty.set(errorStringProperty);
    }

    public SimpleObjectProperty<Image> getImagePropertyProperty()
    {
        return imageProperty;
    }


    public void setImageProperty(Image imageProperty)
    {
        this.imageProperty.set(imageProperty);
    }
}
