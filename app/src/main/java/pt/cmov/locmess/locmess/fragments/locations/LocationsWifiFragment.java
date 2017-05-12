package pt.cmov.locmess.locmess.fragments.locations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.locations.wifi.LocationWifiData;
import pt.cmov.locmess.locmess.adapter.locations.wifi.LocationsWifiRVAdapter;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.WifiLocationsList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsWifiFragment extends Fragment {

    private ILocMessApi _locMessApi;
    private List<LocationWifiData> _locationsList = new ArrayList<>();
    private LocationsWifiRVAdapter _locationsRVAdapter;

    public LocationsWifiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _locationsRVAdapter = new LocationsWifiRVAdapter(_locationsList);

        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

        Call<WifiLocationsList> locationsListCall = _locMessApi.getWifiLocations();
        locationsListCall.enqueue(new Callback<WifiLocationsList>() {

            @Override
            public void onResponse(Call<WifiLocationsList> call, Response<WifiLocationsList> response) {
                if(response.code() == 200){
                    WifiLocationsList locationsList = response.body();
                    List<WifiLocationsList.Datum> datumList = locationsList.rows;

                    for(WifiLocationsList.Datum d : datumList){
                        _locationsList.add(new LocationWifiData(d.name, d.address));
                    }
                    _locationsRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<WifiLocationsList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_wifi_locations, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.locations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _locationsRVAdapter.passFrag(this);
        recyclerView.setAdapter(_locationsRVAdapter);

        return view;
    }
}
