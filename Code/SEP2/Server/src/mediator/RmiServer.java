package mediator;

import model.Event;
import model.ServerModel;
import model.User;
import utility.observer.listener.GeneralListener;
import utility.observer.subject.PropertyChangeHandler;
import utility.observer.subject.RemoteSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class RmiServer implements RemoteModel, RemoteSubject<Event, Event>, PropertyChangeListener {

    private ServerModel model;

    private PropertyChangeHandler<Event, Event> property;

    public RmiServer(ServerModel model) throws MalformedURLException, RemoteException {
        this.model = model;
        this.property = new PropertyChangeHandler<>(this, true);
        startRegistry();
        startServer();
        model.addListener(this);
    }

    private void startRegistry() {
        try {
            Registry reg = LocateRegistry.createRegistry(1099);
            System.out.println("Registry.started...");
        } catch (RemoteException e) {
            System.out.println("Registry already started? " + e.getMessage());
        }
    }

    private void startServer() throws RemoteException, MalformedURLException {

        UnicastRemoteObject.exportObject(this, 0);
        Naming.rebind("TimeSchedule", this);
        System.out.println("Server started...");
    }

    @Override
    public void createEvent(Event event) throws RemoteException {
        System.out.println(event.toString());
        model.createEvent(event);
    }

    @Override
    public void createUser(User user) throws RemoteException {
        model.createUser(user);
    }

    @Override
    public User getUserByEmail(String email) throws RemoteException {
        return model.getUserByEmail(email);
    }

    @Override
    public List<Event> getEventsByOwner(UUID userId) throws RemoteException {
        return model.getEventsByOwner(userId);
    }

    @Override
    public boolean isEmailValid(String email) throws RemoteException {
        return false;
    }

    @Override
    public boolean addListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.addListener(listener, propertyNames);
    }

    @Override
    public boolean removeListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.removeListener(listener, propertyNames);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }
}
