package mediator;

import model.Event;
import model.ServerModel;
import model.User;
import model.UserEvent;
import utility.observer.event.ObserverEvent;
import utility.observer.listener.GeneralListener;
import utility.observer.listener.RemoteListener;
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
import java.time.LocalDateTime;
import java.util.*;

public class RmiServer implements RemoteModel, RemoteSubject<Event, Event>, PropertyChangeListener {

    private ServerModel model;

    private PropertyChangeHandler<Event, Event> property;

    private List<UUID> connectedUsers;

    private Map<UUID,RemoteListener<Event,Event>> userListeners;
    private List<RemoteListener<Event,Event>> listeners;

    public RmiServer(ServerModel model) throws MalformedURLException, RemoteException {
        this.model = model;
        this.property = new PropertyChangeHandler<>(this, true);
        this.userListeners = Collections.synchronizedMap(new HashMap<>());
        startRegistry();
        startServer();
        model.addListener(this);
        this.connectedUsers = Collections.synchronizedList(new ArrayList<>());
        this.listeners = Collections.synchronizedList(new ArrayList<>());
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
        for (UUID userId: userListeners.keySet()){
            if(event.getAttendeeIDs().contains(userId)){
                RemoteListener<Event,Event> listener = userListeners.get(userId);
                if(listener!= null){
                    ObserverEvent<Event,Event> observerEvent = new ObserverEvent<>(null,"eventAdd",null,event);
                    listener.propertyChange(observerEvent);
                }
            }
        }

        model.createEvent(event);
    }

    @Override
    public void createUser(User user) throws RemoteException {
        model.createUser(user);
    }

    @Override
    public void createUserEvent(Event event) throws RemoteException {
        model.createUserEvent(event);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        model.updateUser(user);
    }

    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {
        model.updatePassword(password, uuid);
    }

    @Override
    public User getUserByEmail(String email) throws RemoteException {
        return model.getUserByEmail(email);
    }

    @Override
    public User getUserById(UUID userId) throws RemoteException {
        return model.getUserById(userId);
    }

    @Override
    public List<Event> getEventsByOwner(UUID userId) throws RemoteException {
        return model.getEventsByOwner(userId);
    }

    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return model.getEventsByOwner(userId,startDate,endDate);
    }

    @Override
    public Event getEvent(UUID eventId) throws RemoteException {
        return model.getEvent(eventId);
    }

    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return model.searchUsersByName(search);
    }

    @Override
    public boolean isEmailFree(String email) throws RemoteException {
        return model.isEmailFree(email);
    }

    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = model.loginUser(loginPackage);
        connectedUsers.add(loginPackage.getUuid());
        addListener(new RemoteListener<Event, Event>()
        {
            @Override public void propertyChange(
                ObserverEvent<Event, Event> event) throws RemoteException
            {

            }
        }, loginPackage.getUuid().toString());
        return userLoggedIn;
    }



    @Override public void disconnect(UUID userId) throws RemoteException
    {
        connectedUsers.remove(userId);
    }

    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {

        return model.verifyPassword(userId,password);
    }

    @Override public boolean doesEmailExist(String email) throws RemoteException
    {
        return model.doesEmailExist(email);
    }

    @Override public void removeEvent(Event event) throws RemoteException
    {
        for (UUID userId: userListeners.keySet()){
            if(event.getAttendeeIDs().contains(userId)){
                RemoteListener<Event,Event> listener = userListeners.get(userId);
                if(listener!= null){
                    ObserverEvent<Event,Event> observerEvent = new ObserverEvent<>(null,"eventRemove",null,event);
                    listener.propertyChange(observerEvent);
                }
            }
        }
        model.removeEvent(event);
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


    public void addListener(RemoteListener<Event,Event> listener, String propertyName){
        UUID userId = UUID.fromString(propertyName);
        userListeners.put(userId,listener);
    }

    public void removeListener(RemoteListener<Event,Event> listener,String propertyName){
        UUID userId = UUID.fromString(propertyName);
        userListeners.remove(userId);
    }




}
