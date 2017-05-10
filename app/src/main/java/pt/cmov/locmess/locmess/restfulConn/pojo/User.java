package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snackk on 10/05/2017.
 */

public class User {
    @SerializedName("username")
    public String username;

    public User(String username){
        this.username = username;
    }
}
