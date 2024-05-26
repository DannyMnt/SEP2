package model;

import mediator.LoginPackage;
import mediator.RmiClient;
import utility.observer.event.ObserverEvent;
import viewmodel.CalendarViewModel;
import viewmodel.LoginUserViewModel;
import viewmodel.ViewState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ModelManager implements ClientModel,PropertyChangeListener{



    private RmiClient client;
    private PropertyChangeSupport propertyChangeSupport;

    private User user;

    private List<Event> eventList;

    private List<Event> ownedEvents;

    private List<PropertyChangeListener> listeners;


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

        this.user = null;
        this.eventList = null;
        this.ownedEvents = null;
        this.listeners = new ArrayList<>();
        client.addListener((Object) this);
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
        if(!(this.user == null)){
            if(email.equals(this.user.getEmail())){
                return this.user;
            }
        }

        return client.getUserByEmail(email);
    }

    @Override
    public User getUserById(UUID userId) throws RemoteException {
        if(!(this.user == null)){
            if(this.user.getId().equals(userId)){
                return this.user;
            }
        }
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

        Optional<Event> eventWithId = eventList.stream().filter(event -> event.getEventId().equals(eventId)).findFirst();
        if(eventWithId.isPresent()){
            return eventWithId.get();
        } else
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

    @Override public User getUser()
    {
        return user;
    }
    @Override public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public Event getUpcomingEvent() {

        LocalDateTime now = LocalDateTime.now();

        // Filter and sort the events to find the earliest upcoming event
        if(eventList == null) return null ;
        List<Event> upcomingEvents = eventList.stream()
                .filter(event -> event.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(Event::getStartTime))
                .toList();

        if (upcomingEvents.isEmpty()) {
            return null;
        }

        return upcomingEvents.get(0);
    }

    public List<Event> getEventList()
    {
        return eventList;
    }

    public void setEventList(List<Event> eventList)
    {
        System.out.println("here in set event list");

        this.eventList = eventList;

    }

    public List<Event> getOwnedEvents()
    {
        return ownedEvents;
    }

    public void setOwnedEvents(List<Event> ownedEvents)
    {
        this.ownedEvents = ownedEvents;
    }

    @Override
    public LoginPackage loginUser(LoginPackage loginPackage) throws Exception {
        LoginPackage userLoggedIn = client.loginUser(loginPackage);
        UUID userId = userLoggedIn.getUuid();
        setUser(getUserById(userId));
        setEventList(getUsersEvents(userId));
        setOwnedEvents(getEventsByOwner(userId));
        return userLoggedIn;
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
        eventList.remove(event);
        ownedEvents.remove(event);
        client.removeEvent(event);
    }

    @Override public List<Event> getUsersEvents(UUID userId)
        throws RemoteException
    {
        if(this.eventList != null)
        {
            if(this.user.getId().equals(userId)){
                return this.eventList;
            }
        }
        return client.getUsersEvents(userId);
    }

    @Override public void addListener(Object object)
    {
        listeners.add((PropertyChangeListener) object);
    }

    @Override
    public void addListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removeListener(String propertyName, PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        System.out.println("we here in the model");
        if("clientEventAdd".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            this.eventList.add(receivedEvent);
            System.out.println(this.eventList);
            firePropertyChange("modelEventAdd",null,receivedEvent);
        }else if ("clientEventRemove".equals(evt.getPropertyName())){
            Event receivedEvent = (Event) evt.getNewValue();
            this.eventList.removeIf(event -> event.getEventId().equals(receivedEvent.getEventId()));

            System.out.println("here" + eventList.stream().filter(event -> event.getEventId().equals(receivedEvent.getEventId())).toList());
            firePropertyChange("modelEventRemove",null,receivedEvent);
        }
    }

    public void firePropertyChange(String propertyName, Event oldValue, Event newValue) {

        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(event);
        }
    }
    @Override
    public boolean isUserOwner(Event event){
      return ownedEvents.contains(event);
    }
}
