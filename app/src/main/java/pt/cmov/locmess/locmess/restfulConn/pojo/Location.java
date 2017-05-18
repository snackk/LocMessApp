package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by snackk on 10/05/2017.
 */

public class Location {
    @SerializedName("name")
    public String name;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;
    @SerializedName("radius")
    public int radius;

    public Location(String name, double latitude, double longitude, int radius){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }
}
