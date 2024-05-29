package model;
import mediator.LoginPackage;
import utility.observer.javaobserver.NamedPropertyChangeSubject;
import viewmodel.CalendarViewModel;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public interface ClientModel extends NamedPropertyChangeSubject {


    void createEvent(Event event) throws RemoteException;
    void createUser(User user) throws RemoteException;

    void createUserEvent(Event event) throws RemoteException;

    void updateUser(User user) throws RemoteException;
    void updatePassword(String password, UUID uuid) throws RemoteException;
    User getUserByEmail(String email) throws RemoteException;

    User getUserById(UUID userId) throws RemoteException;

    List<Event> getEventsByOwner(UUID userId) throws RemoteException;
    List<Event> getEventsByOwner(UUID userId, LocalDateTime startDate, LocalDateTime endDate) throws RemoteException;

    Event getEvent(UUID eventId) throws RemoteException;

  List<User> searchUsersByName(String search) throws RemoteException;
    boolean isEmailFree(String email) throws RemoteException;
  LoginPackage loginUser(LoginPackage loginPackage) throws Exception;



    void disconnect(UUID userId)throws RemoteException;

    boolean verifyPassword(UUID userId, String password) throws RemoteException;

    boolean doesEmailExist(String email) throws RemoteException;

    void removeEvent(Event event) throws RemoteException;

    List<Event> getUsersEvents(UUID userId) throws RemoteException;

  void addListener(Object object);
  boolean isUserOwner(Event event);

  User getUser();
  void setUser(User user);
  Event getUpcomingEvent();
}
