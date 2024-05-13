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
        emailStringProperty = new SimpleStringProperty();
        passwordStringProperty = new SimpleStringProperty();
        errorStringProperty = new SimpleStringProperty();
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
    public StringProperty getErrorStringProperty() {
        return errorStringProperty;
    }

    public void loginUser() throws RemoteException {
        try{

        LoginPackage newUser = model.loginUser(new LoginPackage(emailStringProperty.getValue(), passwordStringProperty.getValue()));
        ViewState.getInstance().setUserID(newUser.getUuid());
        errorStringProperty.set("Successfully login");
        } catch(Exception e){
            errorStringProperty.set(e.getMessage());
        }




    }
}