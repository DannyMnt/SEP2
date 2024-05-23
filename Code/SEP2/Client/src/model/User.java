package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class User implements Serializable {
    private UUID id;
    private String email;
    private String password;



    private String firstname;
    private String lastname;
    private String sex;

    private String phoneNumber;

    private LocalDateTime creationDate;
    private LocalDate dateOfBirth;

    private byte[] profilePicture;


    // Manual start with already created user inside the database
    public User(String email, String password){
        this.id = UUID.fromString("ccde07db-cc2a-41bb-9090-e5f072e065d7");
        this.email = email;
        this.password = password;
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
        this.firstname = firstname;
        this.lastname = lastname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.creationDate = creationDate;
        this.dateOfBirth = dateOfBirth;
        this.profilePicture = profilePicture;

    }

    public synchronized void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public synchronized byte[] getProfilePicture() {
        return profilePicture;
    }

    public synchronized LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    public synchronized void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public synchronized void setEmail(String email) {
        this.email = email;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }


    public synchronized String getEmail() {
        return email;
    }


    public synchronized UUID getId() {
        return id;
    }

    public synchronized String getPassword()
    {
        return password;
    }


    public synchronized String getFirstname()
    {
        return firstname;
    }

    public synchronized String getLastname()
    {
        return lastname;
    }

    public synchronized String getSex()
    {
        return sex;
    }

    public synchronized String getPhoneNumber()
    {
        return phoneNumber;
    }

    public synchronized LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public synchronized void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public synchronized void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public synchronized void setSex(String sex)
    {
        this.sex = sex;
    }

    public synchronized void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public synchronized String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", creationDate=" + creationDate +
                ", dateOfBirth=" + dateOfBirth +
//                ", salt='" + salt + '\'' +
                '}';
    }

    public synchronized void updateUser(User user) {
        setEmail(user.getEmail());
        setPassword(user.getPassword());
        setPhoneNumber(user.getPhoneNumber());
    }
}
