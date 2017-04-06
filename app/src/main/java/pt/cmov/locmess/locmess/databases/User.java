package pt.cmov.locmess.locmess.databases;


/**
 * Created by snackk on 06/04/2017.
 */

public class User {
    private String firstName;
    private String lastName;
    private String email;

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

}
