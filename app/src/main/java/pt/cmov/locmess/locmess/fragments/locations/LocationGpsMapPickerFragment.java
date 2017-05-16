package pt.cmov.locmess.locmess.fragments.locations;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.location.FixedUpdateLocation;
import pt.cmov.locmess.locmess.location.ILocationChangedListener;

public class LocationGpsMapPickerFragment extends Fragment implements
        OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private Circle radius;
    private FixedUpdateLocation _updateLocation = null;
    private boolean updated = false;
    private int radius_circle;

    public LocationGpsMapPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _updateLocation = FixedUpdateLocation.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            radius_circle = bundle.getInt("radius");
        }

        _updateLocation.addOnLocationChangedListener(new ILocationChangedListener() {
            @Override
            public void onLocationChange(LatLng latLng) {
                drawMap(latLng);
                updated = true;
                _updateLocation.setApiOff();
            }
        });
        _updateLocation.doLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gps_location_map_picker, container, false);
        getActivity().setTitle("Map Picker");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!updated)
            _updateLocation.setApiOn();
    }

    @Override
    public void onPause() {
        super.onPause();
        _updateLocation.setApiOff();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _updateLocation.setApiOff();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        drawMap(latLng);
    }

    private void drawMap(LatLng latLng){
        if(marker != null){
            marker.remove();
            radius.remove();
        }
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Me").snippet("I'am here"));
        radius = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(radius_circle)
                .strokeColor(Color.argb(0, 63, 81, 181))
                .fillColor(Color.argb(128, 63, 81, 181)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);
    }
}
