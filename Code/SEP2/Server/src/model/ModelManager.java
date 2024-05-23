package model;


import mediator.LoginPackage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.time.LocalDateTime;
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
    public synchronized void createEvent(Event event) throws RemoteException {

        eventRepository.createEvent(event);
    }

    @Override synchronized public void createUser(User user) throws RemoteException
    {
        userRepository.createUser(user);
    }

    @Override
    public synchronized void updateUser(User user) throws RemoteException {
        userRepository.updateUser(user);
        user.updateUser(user);
    }

    @Override
    public synchronized void updatePassword(String password, UUID uuid) throws RemoteException {
        userRepository.updatePassword(password, uuid);
    }

    @Override
    public synchronized void createUserEvent(Event event) throws RemoteException {
        userRepository.createUserEvent(event);
    }

    @Override public synchronized User getUserByEmail(String email) throws RemoteException
    {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public synchronized User getUserById(UUID userId) throws RemoteException {
        return userRepository.getUserById(userId);
    }

    @Override public synchronized List<Event> getEventsByOwner(UUID userId)
        throws RemoteException
    {
        return eventRepository.getEventsByOwner(userId);
    }

    @Override
    public synchronized List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException {
        return eventRepository.getEventsByOwner(userId, startDate, endDate);
    }

    @Override
    public synchronized Event getEvent(UUID eventId) throws RemoteException {
        return eventRepository.getEventById(eventId);
    }

    @Override public synchronized boolean isEmailFree(String email) throws RemoteException
    {
        return userRepository.isEmailFree(email);
    }

    @Override public synchronized List<User> searchUsersByName(String search)
        throws RemoteException
    {
        return userRepository.searchUsersByName(search);
    }

    @Override
    public synchronized LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        return userRepository.loginUser(loginPackage);
    }



    @Override public synchronized boolean verifyPassword(UUID userId,String password)
        throws RemoteException
    {
        return userRepository.verifyPassword(userId,password);
    }

    @Override public synchronized boolean doesEmailExist(String email) throws RemoteException
    {
        return EmailValidator.isEmailValid(email);
    }

    @Override public synchronized void removeEvent(Event event) throws RemoteException
    {
        eventRepository.removeEvent(event.getEventId());
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
