package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.ClientModel;

public class ProfileOverviewViewModel {
    private ClientModel clientModel;
    private StringProperty email;
    private StringProperty phoneNumber;

    public ProfileOverviewViewModel(ClientModel clientModel){
        this.clientModel = clientModel;

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
