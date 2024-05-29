package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Region;
import mediator.LoginPackage;
import model.ClientModel;
import model.User;
import view.ViewHandler;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

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
            errorStringProperty.set(e.getMessage());
            return false;
        }




    }
}
