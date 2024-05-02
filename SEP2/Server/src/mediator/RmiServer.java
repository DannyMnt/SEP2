package mediator;

import model.Event;
import model.ServerModel;
import utility.observer.listener.GeneralListener;
import utility.observer.subject.PropertyChangeHandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer implements RemoteModel{

    private ServerModel model;

    private PropertyChangeHandler<Event, Event> property;

    public RmiServer(ServerModel model) throws MalformedURLException, RemoteException {
        this.model = model;
        this.property = new PropertyChangeHandler<>(this, true);
        startRegistry();
        startServer();
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
        Naming.rebind("Case", this);
        System.out.println("Server started...");
    }

    @Override
    public void createEvent(Event event) {
        System.out.println(event.toString());
    }

    @Override
    public boolean addListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.addListener(listener, propertyNames);
    }

    @Override
    public boolean removeListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return property.removeListener(listener, propertyNames);
    }
}
