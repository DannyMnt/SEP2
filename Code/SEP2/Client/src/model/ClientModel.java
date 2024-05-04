package model;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;


public interface ClientModel extends NamedPropertyChangeSubject {


    public void createEvent(Event event) throws RemoteException;
    //public UserList getParticipants();
}
