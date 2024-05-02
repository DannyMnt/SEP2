package mediator;

import model.ClientModel;
import model.Event;
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

public class RmiClient implements PropertyChangeListener, RemoteListener<Event,Event> {

    private RemoteModel server;
    private PropertyChangeSupport propertyChangeSupport;

    public RmiClient() throws MalformedURLException, NotBoundException, RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        server=(RemoteModel) Naming.lookup("rmi://localhost:1099/Message");
        server.addListener(this);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void propertyChange(ObserverEvent<Event, Event> event) throws RemoteException {
        propertyChangeSupport.firePropertyChange(event.getPropertyName(),event.getValue1(), event.getValue2());
    }


    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
}
