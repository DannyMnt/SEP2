package model;
import mediator.LoginPackage;
import utility.observer.javaobserver.NamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface ClientModel extends NamedPropertyChangeSubject {


    void createEvent(Event event) throws RemoteException;
    void createUser(User user) throws RemoteException;

    void updateUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws RemoteException;

    User getUserById(UUID userId) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;
    //public UserList getParticipants();

  List<User> searchUsersByName(String search) throws RemoteException;
  boolean isEmailFree(String email) throws RemoteException;
  LoginPackage loginUser(LoginPackage loginPackage) throws Exception;

}
