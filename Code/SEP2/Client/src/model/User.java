package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;

    private List<Event> eventList;

    private String firstname;
    private String lastname;
    private String sex;

    private String phoneNumber;

    private LocalDateTime creationDate;

    private String salt;

    public User(String email, String password,String firstname, String lastname, String sex, String phoneNumber) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.eventList = new ArrayList<>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.creationDate = LocalDateTime.now();

    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void addEvent(Event event){
        eventList.add(event);
    }

    public UUID getId() {
        return id;
    }

    public String getPassword()
    {
        return password;
    }

    public List<Event> getEventList()
    {
        return eventList;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public String getSex()
    {
        return sex;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }
}
