package model;

import utility.observer.javaobserver.UnnamedPropertyChangeSubject;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface ServerModel extends UnnamedPropertyChangeSubject {

    void createEvent(Event event) throws RemoteException;

    void creaseUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;
}
