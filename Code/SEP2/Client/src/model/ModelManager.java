package model;

import mediator.RmiClient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ModelManager implements ClientModel{



    private RmiClient client;
    private PropertyChangeSupport propertyChangeSupport;
    public ModelManager() throws MalformedURLException, NotBoundException, RemoteException {
        this.client = new RmiClient();
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public void createEvent(Event event) throws RemoteException {
        client.createEvent(event);
    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }
}
