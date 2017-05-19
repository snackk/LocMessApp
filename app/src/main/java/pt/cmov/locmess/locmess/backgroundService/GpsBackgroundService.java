package pt.cmov.locmess.locmess.backgroundService;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.adapter.locations.gps.LocationGpsData;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by snackk on 16/05/2017.
 */

public class GpsBackgroundService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Looper mServiceLooper;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mInProgress;
    private ILocMessApi _locMessApi;
    private FirebaseRemoteConnection _firebaseConnection;

    @Override
    public void onCreate() {
        // To avoid cpu-blocking, we create a background handler to run our service
        HandlerThread thread = new HandlerThread("BackgroundIntentService");
        // start the new handler thread
        thread.start();

        mServiceLooper = thread.getLooper();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        _firebaseConnection = FirebaseRemoteConnection.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mGoogleApiClient.isConnected() || mInProgress)
            return START_STICKY;

        if(!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting() && !mInProgress) {
            mInProgress = true;
            mGoogleApiClient.connect();
        }

        return START_STICKY;
    }

    protected void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
        Intent i = new Intent("location_update");
        Bundle a = new Bundle();
        a.putDouble("long", location.getLongitude());
        a.putDouble("lat", location.getLatitude());
        i.putExtra("coordinates", a);
        calculateCrap(location.getLongitude(), location.getLatitude());
        sendBroadcast(i);
        /*Calcular se estas coordenadas pertence a alguma das localizacoes*/
    }

    private void calculateCrap(final double longitude, final double latitude){
        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

        Call<GpsLocationsList> locationsListCall = _locMessApi.getGpsLocations();
        locationsListCall.enqueue(new Callback<GpsLocationsList>() {

            @Override
            public void onResponse(Call<GpsLocationsList> call, Response<GpsLocationsList> response) {
                if(response.code() == 200){
                    GpsLocationsList locationsList = response.body();
                    List<GpsLocationsList.Datum> datumList = locationsList.rows;

                    float[] result = new float[2];
                    ArrayList<String> locations = new ArrayList<String>();

                    for(GpsLocationsList.Datum d : datumList){
                        android.location.Location.distanceBetween(d.latitude, d.longitude, latitude, longitude, result);
                        if(result[0] < d.radius){
                            showToast("You're inside of location: " + d.name);
                            locations.add(d.name);
                        }
                    }

                    for(String l : locations){
                        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

                        Call<MessagesList> call1 = _locMessApi.getUnreadListMessages(_firebaseConnection.getFirebaseEmail(), l);

                        call1.enqueue(new Callback<MessagesList>() {
                            @Override
                            public void onResponse(Call<MessagesList> call, Response<MessagesList> response) {
                                if (response.code() == 200) {
                                    showToast("New message! Check your messages.");
                                } else showToast("Something went wrong, code: " + response.code());
                            }

                            @Override
                            public void onFailure(Call<MessagesList> call, Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<GpsLocationsList> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
