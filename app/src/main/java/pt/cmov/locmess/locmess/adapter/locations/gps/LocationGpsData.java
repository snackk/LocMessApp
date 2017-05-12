package pt.cmov.locmess.locmess.adapter.locations.gps;

/**
 * Created by snackk on 12/05/2017.
 */

public class LocationGpsData {

    private String _name;
    private String _latitude;
    private String _longitude;
    private int _radius;

    public LocationGpsData(String name, String latitude, String longitude, int radius){
        _name = name;
        _latitude = latitude;
        _longitude = longitude;
        _radius = radius;
    }

    public String getName(){
        return _name;
    }

    public String getLatitude(){
        return _latitude;
    }

    public String getLongitude(){
        return _longitude;
    }

    public int getRadius(){
        return _radius;
    }
}
