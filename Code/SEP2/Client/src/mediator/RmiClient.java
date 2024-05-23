package mediator;

import model.ClientModel;
import model.Event;
import model.User;
import model.UserEvent;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class RmiClient implements ClientModel, PropertyChangeListener, RemoteListener<Event, Event> {

    private RemoteModel server;
    private PropertyChangeSupport propertyChangeSupport;

    private UUID userId;

    public RmiClient() throws MalformedURLException, NotBoundException, RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        server = (RemoteModel) Naming.lookup("rmi://localhost:1099/TimeSchedule");
        server.addListener(this,null);
        this.propertyChangeSupport = new PropertyChangeSupport(this);

    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Received event" + evt);
    }

    public void propertyChange(Event oldValue, Event newValue) throws RemoteException{
        System.out.println("Received event: " + newValue);
    }

    @Override
    public void propertyChange(ObserverEvent<Event, Event> event) throws RemoteException {
        if(event.getPropertyName().equals("eventAdd")){
            propertyChangeSupport.firePropertyChange("eventReceived", null, event.getValue2());
        }else if(event.getPropertyName().equals("eventRemove")){
            propertyChangeSupport.firePropertyChange("eventRemove",null,event.getValue2());
        }
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
        setUserId(loginPackage.getUuid());
        System.out.println(this.userId + "here");
        return server.loginUser(loginPackage);
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

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {

    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {

    }


}
