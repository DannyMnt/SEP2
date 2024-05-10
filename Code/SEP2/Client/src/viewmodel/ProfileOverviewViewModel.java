package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ClientModel;
import model.User;

import java.rmi.RemoteException;

public class ProfileOverviewViewModel {
    private ClientModel clientModel;
    private StringProperty email;
    private StringProperty phoneNumber;

    private StringProperty firstName;
    private StringProperty lastName;

    private StringProperty sex;
    private StringProperty age;


    public ProfileOverviewViewModel(ClientModel clientModel) throws RemoteException {
        this.clientModel = clientModel;

        User user = clientModel.getUserById(ViewState.getInstance().getUserID());

        email = new SimpleStringProperty();
        phoneNumber = new SimpleStringProperty();
    }

    public boolean editEmail(){
        if(email.getValue() == null || !email.getValue().contains("@"))
            return false;
        return true;
    }

    public boolean editPhoneNumber(){
        if(!phoneNumber.get().matches("\\d*"))
            return false;
        return true;
    }

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }
}
