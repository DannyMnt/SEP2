package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
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

    private byte[] profilePicture;

//    public User(String email, String password, String firstname, String lastname, String sex, String phoneNumber, LocalDate birthdate, byte[] profilePicture) {
//        this.id = UUID.randomUUID();
//        this.email = email;
//        this.password = password;
//        this.eventList = new ArrayList<>();
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.sex = sex;
//        this.phoneNumber = phoneNumber;
//        this.creationDate = LocalDateTime.now();
//        this.dateOfBirth = birthdate;
//        this.profilePicture = profilePicture;
//
//
//    }
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
                LocalDateTime creationDate, LocalDate dateOfBirth, byte[] profilePicture)
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
        this.profilePicture = profilePicture;

    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
               ", eventList=" + eventList +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", creationDate=" + creationDate +
                ", dateOfBirth=" + dateOfBirth +
//                ", salt='" + salt + '\'' +
                '}';
    }

    public void updateUser(User user) {
        setEmail(user.getEmail());
        setPassword(user.getPassword());
        setPhoneNumber(user.getPhoneNumber());
    }
}
