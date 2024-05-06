package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

public class ModelManager implements ServerModel{

    private PropertyChangeSupport propertyChangeSupport;
    private EventRepository eventRepository;
    private UserRepository userRepository;


    public ModelManager(){
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.eventRepository = new EventRepository(DatabaseSingleton.getInstance());
        this.userRepository = new UserRepository(DatabaseSingleton.getInstance());

    }
    @Override
    public void createEvent(Event event) throws RemoteException {
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        db.addEvent(event);
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
