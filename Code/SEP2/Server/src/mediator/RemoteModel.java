package mediator;

import model.Event;
import model.User;
import utility.observer.subject.RemoteSubject;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;

public interface RemoteModel extends RemoteSubject<Event, Event> {

    void createEvent(Event event) throws RemoteException;

}
