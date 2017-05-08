package pt.cmov.locmess.locmess.location;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snackk on 08/05/2017.
 */

public class FixedUpdateLocation implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static FixedUpdateLocation instance = null;
    private GoogleApiClient apiClient = null;
    private LocationRequest mLocationRequest;

    private static List<ILocationChangedListener> listeners = new ArrayList<ILocationChangedListener>();

    private FragmentActivity _activity = null;

    protected FixedUpdateLocation(FragmentActivity activity){
        _activity = activity;
    }

    public static void addOnLocationChangedListener(ILocationChangedListener listner) {
        listeners.add(listner);
    }

    public static FixedUpdateLocation getInstance(FragmentActivity activity){
        if(instance == null)
            instance = new FixedUpdateLocation(activity);
        else instance.setActivity(activity);
        return instance;
    }

    private void setActivity(FragmentActivity activity){
        _activity = activity;
    }

    public void doLocationRequest() {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(_activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();

            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(10 * 1000)
                    .setFastestInterval(1 * 1000);
        }
    }

    public void setApiOff(){
        if(apiClient.isConnected())
            apiClient.disconnect();
    }

    public void setApiOn(){
        if(!apiClient.isConnected())
            apiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
            if (location == null)
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, mLocationRequest, this);
            else locationHasChanged(location);
        }catch(SecurityException e){

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        locationHasChanged(location);
    }

    public void locationHasChanged(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        for (ILocationChangedListener l : listeners) {
            l.onLocationChange(latLng);
        }
    }
}

