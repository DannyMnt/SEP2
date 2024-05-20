package mediator;

import model.Event;
import model.User;
import model.UserEvent;
import utility.observer.subject.RemoteSubject;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface RemoteModel extends RemoteSubject<Event, Event> {

    void createEvent(Event event) throws RemoteException;

    void createUser(User user) throws RemoteException;

    void createUserEvent(Event event) throws RemoteException;

    void updateUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws  RemoteException;

    User getUserById(UUID userId) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;

   List<User> searchUsersByName(String search) throws RemoteException;

    boolean isEmailFree(String email) throws RemoteException;
  LoginPackage loginUser(LoginPackage loginPackage) throws Exception;


    byte[] getImage() throws RemoteException;
    void sendImage(byte[] imageData) throws RemoteException;

    void disconnect(UUID userId) throws RemoteException;


}
