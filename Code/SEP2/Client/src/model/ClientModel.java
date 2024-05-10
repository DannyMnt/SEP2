package model;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ClientModel extends NamedPropertyChangeSubject {


    void createEvent(Event event) throws RemoteException;
    void createUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws RemoteException;

    User getUserById(UUID userId) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;
    //public UserList getParticipants();

    boolean isEmailValid(String email) throws RemoteException;


}
