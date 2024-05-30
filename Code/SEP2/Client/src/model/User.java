package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User entity with various attributes.
 */
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

    /**
     * Constructs a User object with default values.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
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


    /**
     * Constructs a User object with provided values.
     *
     * @param id           The unique identifier of the user.
     * @param email        The email address of the user.
     * @param password     The password of the user.
     * @param firstname    The first name of the user.
     * @param lastname     The last name of the user.
     * @param sex          The gender of the user.
     * @param phoneNumber  The phone number of the user.
     * @param creationDate The creation date of the user.
     * @param dateOfBirth  The date of birth of the user.
     * @param profilePicture The profile picture of the user.
     */
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

    /**
     * Sets the profile picture of the user.
     *
     * @param profilePicture The new profile picture of the user.
     */
    public synchronized void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Retrieves the profile picture of the user.
     *
     * @return The profile picture of the user.
     */
    public synchronized byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * Retrieves the date of birth of the user.
     *
     * @return The date of birth of the user.
     */
    public synchronized LocalDate getDateOfBirth()
    {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the user.
     *
     * @param dateOfBirth The new date of birth of the user.
     */
    public synchronized void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The new email address of the user.
     */
    public synchronized void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's password.
     *
     * @param password The new password of the user.
     */
    public synchronized void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The email address of the user.
     */
    public synchronized String getEmail() {
        return email;
    }


    /**
     * Retrieves the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public synchronized UUID getId() {
        return id;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password of the user.
     */
    public synchronized String getPassword()
    {
        return password;
    }

    /**
     * Retrieves the user's first name.
     *
     * @return The first name of the user.
     */
    public synchronized String getFirstname()
    {
        return firstname;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return The last name of the user.
     */
    public synchronized String getLastname()
    {
        return lastname;
    }

    /**
     * Retrieves the gender of the user.
     *
     * @return The gender of the user.
     */
    public synchronized String getSex()
    {
        return sex;
    }

    /**
     * Retrieves the phone number of the user.
     *
     * @return The phone number of the user.
     */
    public synchronized String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Retrieves the creation date of the user.
     *
     * @return The creation date of the user.
     */
    public synchronized LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstname The new first name of the user.
     */
    public synchronized void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastname The new last name of the user.
     */
    public synchronized void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    /**
     * Sets the gender of the user.
     *
     * @param sex The new gender of the user.
     */
    public synchronized void setSex(String sex)
    {
        this.sex = sex;
    }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The new phone number of the user.
     */ /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber The new phone number of the user.
     */
    public synchronized void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Returns a string representation of the User object.
     *
     * @return A string representing the User object.
     */
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

    /**
     * Updates the user with the provided user's information.
     *
     * @param user The user object containing updated information.
     */
    public synchronized void updateUser(User user) {
        setEmail(user.getEmail());
        setPassword(user.getPassword());
        setPhoneNumber(user.getPhoneNumber());
    }

    /**
     * Compares this user to another object for equality.
     *
     * @param o The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(sex, user.sex) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(creationDate, user.creationDate) && Objects.equals(dateOfBirth, user.dateOfBirth) && Arrays.equals(profilePicture, user.profilePicture);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, email, password, firstname, lastname, sex, phoneNumber, creationDate, dateOfBirth);
        result = 31 * result + Arrays.hashCode(profilePicture);
        return result;
    }
}
