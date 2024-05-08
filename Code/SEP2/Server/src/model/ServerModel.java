package model;

import utility.observer.javaobserver.UnnamedPropertyChangeSubject;

import java.rmi.RemoteException;

public interface ServerModel extends UnnamedPropertyChangeSubject {

    void createEvent(Event event) throws RemoteException;

    void creaseUser(User user) throws RemoteException;
}
