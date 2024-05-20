package model;


import mediator.LoginPackage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        eventRepository.createEvent(event);
    }

    @Override public void createUser(User user) throws RemoteException
    {
        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user) throws RemoteException {
//        userRepository.updateUser(user);
    }

    @Override
    public void createUserEvent(Event event) throws RemoteException {
        userRepository.createUserEvent(event);
    }

    @Override public User getUserByEmail(String email) throws RemoteException
    {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(UUID userId) throws RemoteException {
        return userRepository.getUserById(userId);
    }

    @Override public List<Event> getEventsByOwner(UUID userId)
        throws RemoteException
    {
        return eventRepository.getEventsByOwner(userId);
    }

    @Override public boolean isEmailFree(String email) throws RemoteException
    {
        return userRepository.isEmailFree(email);
    }

    @Override public List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return userRepository.searchUsersByName(search);
    }

    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {

        return userRepository.loginUser(loginPackage);
    }

    @Override
    public byte[] getImage() throws RemoteException {
        return userRepository.getImage();
    }

    @Override
    public void sendImage(byte[] imageData) throws RemoteException {
            userRepository.sendImage(imageData);
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
