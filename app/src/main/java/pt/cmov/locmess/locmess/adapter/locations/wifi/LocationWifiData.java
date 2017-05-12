package pt.cmov.locmess.locmess.adapter.locations.wifi;

/**
 * Created by snackk on 12/05/2017.
 */

public class LocationWifiData {

    private String _name;
    private String _macAddress;

    public LocationWifiData(String name, String mac){
        _name = name;
        _macAddress = mac;
    }

    public String getName(){
        return _name;
    }

    public String getMacAddress(){
        return _macAddress;
    }
}
