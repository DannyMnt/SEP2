package model;

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



}
