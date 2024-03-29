package pt.cmov.locmess.locmess.fragments.locations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationGpsCreateFragment extends Fragment {

    private EditText locationName;
    private EditText latitude;
    private EditText longitude;
    private EditText radius;
    private double lat = 0;
    private double longi = 0;
    private FirebaseRemoteConnection _firebaseConnection;
    private ILocMessApi _locMessApi;

    public LocationGpsCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("lat");
            longi = bundle.getDouble("long");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_gps_create, container, false);

        locationName = (EditText) view.findViewById(R.id.location_name);
        latitude = (EditText) view.findViewById(R.id.location_latitude);
        longitude = (EditText) view.findViewById(R.id.location_longitude);
        radius = (EditText) view.findViewById(R.id.location_radius);

        if(lat != 0 && longi != 0){
            latitude.setText(lat + "");
            longitude.setText(longi + "");
        }

        Button picker = (Button) view.findViewById(R.id.pick_on_map);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("radius", Integer.parseInt((radius.getText().toString()).isEmpty() ? "100" : (radius.getText().toString())));
                Fragment fragment = new LocationGpsMapPickerFragment();
                fragment.setArguments(args);
                FragmentTransaction mainFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mainFragmentTransaction.replace(R.id.app_content, fragment).commit();
            }
        });

        Button post = (Button) view.findViewById(R.id.post_location);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locationName.getText().toString().isEmpty() && !latitude.getText().toString().isEmpty() && !longitude.getText().toString().isEmpty()) {
                    Location location = new Location(locationName.getText().toString().trim(), Double.parseDouble(latitude.getText().toString()), Double.parseDouble(longitude.getText().toString()), Integer.parseInt((radius.getText().toString()).isEmpty() ? "100" : (radius.getText().toString())));

                    _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
                    Call<ResponseBody> call = _locMessApi.createLocation(location);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Location posted!", Toast.LENGTH_LONG).show();
                                locationName.setText("");
                                latitude.setText("");
                                longitude.setText("");
                                radius.setText("");
                            }
                            else Toast.makeText(getContext(), "Something went wrong, code: " + response.code(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            call.cancel();
                        }
                    });
                } else Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}
