package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snackk on 10/05/2017.
 */

public class Message {
    @SerializedName("username")
    public String username;
    @SerializedName("message")
    public String message;
    @SerializedName("location")
    public String location;
    @SerializedName("title")
    public String title;

    public Message(String username, String message, String location, String title){
        this.username = username;
        this.message = message;
        this.location = location;
        this.title = title;
    }
}
