package viewmodel;

import at.favre.lib.crypto.bcrypt.BCrypt;
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
            LoginPackage newUser = model.loginUser(new LoginPackage(emailStringProperty.getValue(),BCrypt.withDefaults().hashToString(12,passwordStringProperty.getValue().toCharArray()) ));
            ViewState.getInstance().setUserID(newUser.getUuid());
            errorStringProperty.set("Successful login");
            return true;
        } catch(Exception e){
            return false;
        }



 
    }
}
