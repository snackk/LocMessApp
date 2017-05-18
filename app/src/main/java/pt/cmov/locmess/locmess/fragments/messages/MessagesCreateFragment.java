package pt.cmov.locmess.locmess.fragments.messages;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesCreateFragment extends Fragment {

    private ILocMessApi _locMessApi;
    private List<String> _locationsList = new ArrayList<>();
    private FirebaseRemoteConnection _firebaseConnection;

    private Spinner location_spinner;
    private Spinner policy_spinner;
    private TextView title_TextView;
    private TextView message_TextView;

    private ArrayAdapter<String> locationsAd;
    public MessagesCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        _firebaseConnection = FirebaseRemoteConnection.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_create, container, false);
        getActivity().setTitle("New Message");

        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

        locationsAd = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, _locationsList);
        location_spinner = (Spinner) view.findViewById(R.id.spinner_locations);
        location_spinner.setAdapter(locationsAd);

        Call<GpsLocationsList> locationsListCall = _locMessApi.getGpsLocations();
        locationsListCall.enqueue(new Callback<GpsLocationsList>() {

            @Override
            public void onResponse(Call<GpsLocationsList> call, Response<GpsLocationsList> response) {
                if(response.code() == 200){
                    GpsLocationsList locationsList = response.body();
                    List<GpsLocationsList.Datum> datumList = locationsList.rows;

                    for(GpsLocationsList.Datum d : datumList){
                        _locationsList.add(d.name);
                    }
                    location_spinner.setAdapter(locationsAd);
                }
            }

            @Override
            public void onFailure(Call<GpsLocationsList> call, Throwable t) {
                call.cancel();
            }
        });

        ArrayList<String> policy = new ArrayList<String>();
        policy.add("Whitelist");
        policy.add("Blacklist");

        policy_spinner = (Spinner) view.findViewById(R.id.spinner_policy);
        policy_spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, policy));

        title_TextView = (TextView) view.findViewById(R.id.message_title);
        message_TextView = (TextView) view.findViewById(R.id.message_text);

        ImageButton send = (ImageButton) view.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title_TextView.getText().toString().isEmpty() && !message_TextView.getText().toString().isEmpty()) {
                    final Message message = new Message(_firebaseConnection.getFirebaseEmail(), message_TextView.getText().toString().trim(), location_spinner.getSelectedItem().toString(), title_TextView.getText().toString().trim());

                    _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
                    Call<ResponseBody> call = _locMessApi.createMessage(message);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), "Messaged posted!", Toast.LENGTH_LONG).show();
                                title_TextView.setText("");
                                message_TextView.setText("");
                            }
                            else Toast.makeText(getContext(), "Something went wrong, code: " + response.code(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            call.cancel();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
