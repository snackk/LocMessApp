package pt.cmov.locmess.locmess.fragments.locations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.WifiLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationWifiCreateFragment extends Fragment {

    private EditText wifiName;
    private EditText wifiSSID;
    private ILocMessApi _locMessApi;

    public LocationWifiCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_wifi_create, container, false);

        wifiName = (EditText) view.findViewById(R.id.wifi_name);
        wifiSSID = (EditText) view.findViewById(R.id.wifi_ssid);

        Button post = (Button) view.findViewById(R.id.post_location);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!wifiName.getText().toString().isEmpty() && !wifiSSID.getText().toString().isEmpty()) {
                    WifiLocation location = new WifiLocation(wifiName.getText().toString().trim(), wifiSSID.getText().toString());

                    _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
                    Call<WifiLocation> call = _locMessApi.createWifiLocation(location);

                    call.enqueue(new Callback<WifiLocation>() {
                        @Override
                        public void onResponse(Call<WifiLocation> call, Response<WifiLocation> response) {
                            if (response.code() == 200)
                                Toast.makeText(getContext(), "Location posted!", Toast.LENGTH_LONG).show();
                            else Toast.makeText(getContext(), "Something went wrong, code: " + response.code(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<WifiLocation> call, Throwable t) {
                            call.cancel();
                        }
                    });
                } else Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
