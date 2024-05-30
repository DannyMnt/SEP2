package mediator;

import java.io.Serializable;
import java.util.UUID;

/**
 * The LoginPackage class represents a package containing login credentials.
 * It includes the user's email and password for authentication purposes.
 */
public class LoginPackage implements Serializable {

    private String email;
    private String password;

    private UUID uuid;

    /**
     * Constructs a new LoginPackage object with the specified email and password.
     * @param email The email address of the user.
     * @param password The password of the user.
     */
    public LoginPackage(String email, String password) {
        this.email = email;
        this.password = password;
        this.uuid = null;
    }


    /**
     * Gets the UUID associated with this login package.
     * @return The UUID associated with this login package.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the email address associated with this login package.
     * @return The email address associated with this login package.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the email address associated with this login package.
     * @return The email address associated with this login package.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the password associated with this login package.
     * @return The password associated with this login package.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the email address for this login package.
     * @param email The email address to set.
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Sets the password for this login package.
     * @param password The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}

