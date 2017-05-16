package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snackk on 16/05/2017.
 */

public class WifiLocation {
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;

    public WifiLocation(String name, String address){
        this.name = name;
        this.address = address;
    }
}
