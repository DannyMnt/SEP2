package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mediator.LoginPackage;
import model.ClientModel;

import java.rmi.RemoteException;

public class LoginUserViewModel {
    private ClientModel model;
    private StringProperty emailStringProperty;
    private StringProperty passwordStringProperty;

    private StringProperty errorStringProperty;
    public LoginUserViewModel(ClientModel model){
        this.model = model;
        emailStringProperty = new SimpleStringProperty("user1@example.com");
        passwordStringProperty = new SimpleStringProperty("password1");
        errorStringProperty = new SimpleStringProperty();
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
    public StringProperty getErrorStringProperty() {
        return errorStringProperty;
    }

    public boolean loginUser() throws RemoteException {
        try{
            LoginPackage newUser = model.loginUser(new LoginPackage(emailStringProperty.getValue(), passwordStringProperty.getValue()));

            ViewState.getInstance().setUserID(newUser.getUuid());
            errorStringProperty.set("Successful login");
            return true;
        } catch(Exception e){
            e.printStackTrace();
            errorStringProperty.set("Login Failed");
            return false;
        }




    }
}
