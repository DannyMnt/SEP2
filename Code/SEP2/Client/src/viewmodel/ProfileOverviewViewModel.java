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

    public void editEmail(){
        if(email.getValue() == null || !email.getValue().contains("@"))
            System.out.println("Error");
        else
            System.out.println("Success");
    }

    public void editPhoneNumber(){

    }

    public StringProperty getEmailTextFieldProperty() {
        return email;
    }

    public StringProperty getPhoneNumberProperty() {
        return phoneNumber;
    }
}
