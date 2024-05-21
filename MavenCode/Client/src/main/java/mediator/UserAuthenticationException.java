package mediator;

import java.io.Serializable;

public class UserAuthenticationException extends Exception implements Serializable {
    public UserAuthenticationException(String message) {
        super(message);
    }
}
