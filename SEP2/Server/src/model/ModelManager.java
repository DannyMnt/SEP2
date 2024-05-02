package model;

public class ModelManager {

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

public class ModelManager implements ServerModel{

    private PropertyChangeSupport propertyChangeSupport;


    public ModelManager(){
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }
    @Override
    public void createEvent(Event event) throws RemoteException {

    }

    @Override
    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
