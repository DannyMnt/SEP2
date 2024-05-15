package mediator;

import java.io.Serializable;
import java.util.UUID;

public class LoginPackage implements Serializable {

    private String email;
    private String password;

    private UUID uuid;

    public LoginPackage(String email, String password) {
        this.email = email;
        this.password = password;
        this.uuid = null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

