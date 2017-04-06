package pt.cmov.locmess.locmess.databases;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snackk on 06/04/2017.
 */

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Message> messages = new ArrayList<Message>();

    public User(){}

    public User(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getEmail(){
        return this.email;
    }
    public List<Message> getMessage(){
       return messages;
    }

}
