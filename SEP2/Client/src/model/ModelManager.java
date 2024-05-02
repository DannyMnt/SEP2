package model;

import mediator.RemoteModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ModelManager implements ClientModel{

    private RemoteModel remoteModel;
    private PropertyChangeSupport propertyChangeSupport;
    public ModelManager(RemoteModel remoteModel) {
        this.remoteModel = remoteModel;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    @Override
    public void createEvent(Event event) throws RemoteException {
        remoteModel.createEvent(event);
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
