package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private LocalDate dateOfBirth;
    private String salt;

    public User(String email, String password,String firstname, String lastname, String sex, String phoneNumber,LocalDate birthdate) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.eventList = new ArrayList<>();
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.creationDate = LocalDateTime.now();
        this.dateOfBirth = birthdate;


    }
// Manual start with already created user inside the database
    public User(String email, String password){
        this.id = UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7");
        this.email = email;
        this.password = password;
        this.eventList = new ArrayList<>();
        this.firstname = "Keanu";
        this.lastname = "Reeves";
        this.sex = "helicopter";
        this.phoneNumber = "69 69 69 69";
        this.creationDate = LocalDateTime.now();
        this.dateOfBirth = LocalDate.now();

    }

    public User(UUID id, String email, String password,
        String firstname, String lastname, String sex, String phoneNumber,
        LocalDateTime creationDate, LocalDate dateOfBirth)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.eventList = null; //temporary, need to get events from database
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.creationDate = creationDate;
        this.dateOfBirth = dateOfBirth;

    }

    public LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
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
