package model;

import mediator.LoginPackage;
import mediator.RmiClient;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

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

    @Override public void createUser(User user) throws RemoteException
    {
        client.createUser(user);
    }

    @Override
    public void createUserEvent(UserEvent userEvent) throws RemoteException {
        client.createUserEvent(userEvent);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
        client.updateUser(user) ;
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

    @Override
    public byte[] getImage() throws RemoteException {
        return client.getImage();
    }

    @Override
    public void sendImage(byte[] imageData) throws RemoteException {
client.sendImage(imageData);
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
