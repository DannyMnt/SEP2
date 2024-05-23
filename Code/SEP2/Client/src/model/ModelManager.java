package model;

import mediator.LoginPackage;
import mediator.RmiClient;
import utility.observer.event.ObserverEvent;
import viewmodel.CalendarViewModel;
import viewmodel.ViewState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ModelManager implements ClientModel{



    private RmiClient client;
    private PropertyChangeSupport propertyChangeSupport;
    public ModelManager() throws MalformedURLException, NotBoundException, RemoteException {
        this.client = new RmiClient();
        propertyChangeSupport = new PropertyChangeSupport(this);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->{
            try
            {
                client.disconnect(ViewState.getInstance().getUserID());
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void createEvent(Event event) throws RemoteException {
        client.createEvent(event);
    }



    @Override public void createUser(User user) throws RemoteException
    {
        client.createUser(user);
    }

    @Override
    public void createUserEvent(Event event) throws RemoteException {
        client.createUserEvent(event);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        client.updateUser(user) ;
        user.updateUser(user);
    }

    @Override
    public void updatePassword(String password, UUID uuid) throws RemoteException {
        client.updatePassword(password, uuid);
    }

    @Override public User getUserByEmail(String email) throws RemoteException
    {
        return client.getUserByEmail(email);
    }

    @Override
    public User getUserById(UUID userId) throws RemoteException {
        return client.getUserById(userId);
    }

    @Override public List<Event> getEventsByOwner(UUID userId)
        throws RemoteException
    {
        return client.getEventsByOwner(userId);
    }

    @Override
    public List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return client.getEventsByOwner(userId, startDate, endDate);
    }

    @Override
    public Event getEvent(UUID eventId) throws RemoteException {
        return client.getEvent(eventId);
    }

    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return client.searchUsersByName(search);
    }

    @Override
    public boolean isEmailFree(String email) throws RemoteException {

        return client.isEmailFree(email);
    }

    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        return client.loginUser(loginPackage);
    }


    @Override public void disconnect(UUID userId) throws RemoteException
    {
        client.disconnect(userId);
    }

    @Override public boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
        return client.verifyPassword(userId,password);
    }

    @Override public boolean doesEmailExist(String email) throws RemoteException
    {
        return client.doesEmailExist(email);
    }

    @Override public void removeEvent(Event event) throws RemoteException
    {
        client.removeEvent(event);
    }

    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        return client.getUsersEvents(userId);
    }

    @Override public void addListener(Object object)
    {
        client.addListener(object);
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
