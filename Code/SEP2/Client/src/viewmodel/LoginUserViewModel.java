package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mediator.LoginPackage;
import model.ClientModel;

import java.rmi.RemoteException;

/**
 * ViewModel for handling user login.
 */
public class LoginUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;

    private StringProperty errorStringProperty;

    /**
     * Constructs a LoginUserViewModel with the given client model.
     *
     * @param model the client model to interact with
     */
    public LoginUserViewModel(ClientModel model){
        this.model = model;
        emailStringProperty = new SimpleStringProperty("user1@example.com");
        passwordStringProperty = new SimpleStringProperty("password1");
        errorStringProperty = new SimpleStringProperty();
    }

    /**
     * Checks if the given email is free to use.
     *
     * @param email the email to check
     * @return true if the email is free, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
    }

    /**
     * Gets the email string property.
     *
     * @return the email string property
     */
    public StringProperty getEmailStringProperty() {
        return emailStringProperty;
    }

    /**
     * Gets the password string property.
     *
     * @return the password string property
     */
    public StringProperty getPasswordStringProperty() {
        return passwordStringProperty;
    }

    /**
     * Gets the error string property.
     *
     * @return the error string property
     */
    public StringProperty getErrorStringProperty() {
        return errorStringProperty;
    }

    /**
     * Attempts to log in the user with the provided email and password.
     *
     * @return true if the login was successful, false otherwise
     * @throws RemoteException if an RMI error occurs
     */
    public boolean loginUser() throws RemoteException {
        try{
            LoginPackage newUser = model.loginUser(new LoginPackage(emailStringProperty.getValue(), passwordStringProperty.getValue()));

            ViewState.getInstance().setUserID(newUser.getUuid());
            errorStringProperty.set("Successful login");
            return true;
        } catch(Exception e){

            errorStringProperty.set(e.getMessage());
            return false;
        }




    }
}
