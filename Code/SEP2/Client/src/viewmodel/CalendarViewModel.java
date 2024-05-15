package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mediator.LoginPackage;
import model.ClientModel;

import java.rmi.RemoteException;

public class CalendarViewModel {
    private ClientModel model;
    public CalendarViewModel(ClientModel model){
        this.model = model;
    }


}
