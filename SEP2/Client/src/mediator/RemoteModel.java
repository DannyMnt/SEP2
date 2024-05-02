package mediator;

import model.Event;
import model.User;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;

public interface RemoteModel {

    public void createEvent(Event event) throws RemoteException;

    void addListener(RmiClient rmiClient);
}
