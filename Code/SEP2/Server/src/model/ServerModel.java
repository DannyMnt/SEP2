package model;

import mediator.LoginPackage;
import utility.observer.javaobserver.UnnamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface ServerModel extends UnnamedPropertyChangeSubject {

    void createEvent(Event event) throws RemoteException;

       void createUser(User user) throws RemoteException;

    void updateUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws RemoteException;

    User getUserById(UUID userId) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;
    boolean isEmailFree(String email) throws RemoteException;

    List<User> searchUsersByName(String search) throws RemoteException;

    LoginPackage loginUser(LoginPackage loginPackage) throws Exception;

    byte[] getImage() throws RemoteException;
    void sendImage(byte[] imageData) throws RemoteException;

}
