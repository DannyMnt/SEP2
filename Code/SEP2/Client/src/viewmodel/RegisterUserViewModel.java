package viewmodel;

import javafx.scene.layout.Region;
import model.ClientModel;
import view.ViewHandler;

import java.rmi.RemoteException;

public class RegisterUserViewModel {
    private ClientModel model;
    public RegisterUserViewModel(ClientModel model){
        this.model = model;
    }

    public boolean isEmailValid(String email) throws RemoteException {
        return model.isEmailValid(email);
    }
}
