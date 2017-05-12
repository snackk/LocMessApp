package pt.cmov.locmess.locmess.fragments.locations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.locations.LocationData;
import pt.cmov.locmess.locmess.adapter.locations.LocationsRVAdapter;
import pt.cmov.locmess.locmess.adapter.messages.MessageData;
import pt.cmov.locmess.locmess.adapter.messages.MessagesRVAdapter;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Location;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsGpsFragment extends Fragment {

    private ILocMessApi _locMessApi;
    private List<LocationData> _locationsList = new ArrayList<>();
    private LocationsRVAdapter _locationsRVAdapter;

    public LocationsGpsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _locationsRVAdapter = new LocationsRVAdapter(_locationsList);

        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

        Call<GpsLocationsList> locationsListCall = _locMessApi.getGpsLocations();
        locationsListCall.enqueue(new Callback<GpsLocationsList>() {

            @Override
            public void onResponse(Call<GpsLocationsList> call, Response<GpsLocationsList> response) {
                if(response.code() == 200){
                    GpsLocationsList locationsList = response.body();
                    List<GpsLocationsList.Datum> datumList = locationsList.rows;

                    for(GpsLocationsList.Datum d : datumList){
                        _locationsList.add(new LocationData(d.name, d.latitude + "", d.longitude + "", d.radius));
                    }
                    _locationsRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<GpsLocationsList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gps_locations, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.locations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _locationsRVAdapter.passFrag(this);
        recyclerView.setAdapter(_locationsRVAdapter);

        return view;
    }
}
