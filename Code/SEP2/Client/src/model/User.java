package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String email;
    private String password;

    private List<Event> eventList;

    public User(String email, String password) {
        this.id = UUID.fromString("c16ed0b7-325d-4147-81a6-11187a7ef1a1");
        this.email = email;
        this.password = password;
        this.eventList = new ArrayList<>();
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
}
