package mediator;

import model.ClientModel;
import model.Event;
import model.User;
import utility.observer.event.ObserverEvent;
import utility.observer.listener.RemoteListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class RmiClient implements ClientModel, PropertyChangeListener, RemoteListener<Event, Event> {

    private RemoteModel server;
    private PropertyChangeSupport propertyChangeSupport;

    public RmiClient() throws MalformedURLException, NotBoundException, RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        server = (RemoteModel) Naming.lookup("rmi://localhost:1099/TimeSchedule");
//        server.addListener(this);
//        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void propertyChange(ObserverEvent<Event, Event> event) throws RemoteException {
        propertyChangeSupport.firePropertyChange(event.getPropertyName(), event.getValue1(), event.getValue2());
    }


    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void createEvent(Event event) throws RemoteException {
        server.createEvent(event);
    }

    @Override
    public void createUser(User user) throws RemoteException {
        server.createUser(user);
    }

    @Override
    public User getUserByEmail(String email) throws RemoteException {
        return server.getUserByEmail(email);
    }


    @Override
    public User getUserById(UUID userId) throws RemoteException{
        return server.getUserById(userId);
    }
    @Override
    public List<Event> getEventsByOwner(UUID userId) throws RemoteException {
        return getEventsByOwner(userId);
    }

    @Override
    public boolean isEmailValid(String email) throws RemoteException {
        return server.isEmailValid(email);
    }


    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {

    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {

    }
}
