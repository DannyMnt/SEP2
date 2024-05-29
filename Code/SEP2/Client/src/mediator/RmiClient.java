package mediator;

import model.ClientModel;
import model.Event;
import model.User;
import model.UserEvent;
import utility.observer.event.ObserverEvent;
import utility.observer.listener.GeneralListener;
import utility.observer.listener.RemoteListener;
import viewmodel.CalendarViewModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RmiClient implements RemoteModel, PropertyChangeListener, RemoteListener<Event, Event>,ClientCallback{

    private RemoteModel server;
    private PropertyChangeSupport propertyChangeSupport;

    private UUID userId;

    private List<PropertyChangeListener> listeners;


    public RmiClient() throws MalformedURLException, NotBoundException, RemoteException {
        super();
        UnicastRemoteObject.exportObject(this, 0);
        server = (RemoteModel) Naming.lookup("rmi://localhost:1099/TimeSchedule");
        server.addListener(this,null);
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.listeners = new ArrayList<>();


    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }



    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


    @Override
    public void propertyChange(ObserverEvent<Event, Event> event) throws RemoteException {

        if(event.getPropertyName().equals("addEvent")){
            firePropertyChange("clientEventAdd", null, event.getValue2());
        }else if(event.getPropertyName().equals("removeEvent")){
            firePropertyChange("clientEventRemove",null,event.getValue2());
        }
    }

    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }


    public void addListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void createEvent(Event event) throws RemoteException {
        server.createEvent(event);
    }

    public void createUser(User user) throws RemoteException {
        server.createUser(user);
    }

    public void createUserEvent(Event event) throws RemoteException {
        server.createUserEvent(event);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        server.updateUser(user);
    }

    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {
        server.updatePassword(password, uuid);
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
        return server.getEventsByOwner(userId);
    }

    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return server.getEventsByOwner(userId, startDate, endDate);
    }

    @Override
    public Event getEvent(UUID eventId) throws RemoteException {
        return server.getEvent(eventId);
    }

    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return server.searchUsersByName(search);
    }

    @Override
    public boolean isEmailFree(String email) throws RemoteException {

        return server.isEmailFree(email);
    }

    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = server.loginUser(loginPackage);

        registerClient(userLoggedIn.getUuid(),this);
        return userLoggedIn;

    }



    @Override public void disconnect(UUID userId) throws RemoteException
    {
        server.disconnect(userId);
    }

    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
         return server.verifyPassword(userId,password);
    }

    @Override public boolean doesEmailExist(String email) throws RemoteException
    {
        return server.doesEmailExist(email);
    }

    @Override public void removeEvent(Event event) throws RemoteException
    {
        server.removeEvent(event);
    }

    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        return server.getUsersEvents(userId);
    }

    public void addListener(Object object)
    {
        listeners.add((PropertyChangeListener)object);
    }


    public void notify(String change,Event event) throws RemoteException
    {
        propertyChange(new ObserverEvent<>(null,change,null,event));
    }

    public void registerClient(UUID userId,ClientCallback client) throws RemoteException
    {
        server.registerClient(userId,client);
    }

    public UUID getUserId()
    {
        return userId;
    }

    @Override
    public boolean addListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return false;
    }

    @Override
    public boolean removeListener(GeneralListener<Event, Event> listener, String... propertyNames) throws RemoteException {
        return false;
    }
}
