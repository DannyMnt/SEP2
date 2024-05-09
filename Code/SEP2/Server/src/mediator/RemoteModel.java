package mediator;

import model.Event;
import model.User;
import utility.observer.subject.RemoteSubject;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RemoteModel extends RemoteSubject<Event, Event> {

    void createEvent(Event event) throws RemoteException;

    void createUser(User user) throws RemoteException;

    User getUserByEmail(String email) throws  RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;

}