package pt.cmov.locmess.locmess.fragments.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.MessageData;
import pt.cmov.locmess.locmess.adapter.MessagesRVAdapter;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Location;
import pt.cmov.locmess.locmess.restfulConn.pojo.Message;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import pt.cmov.locmess.locmess.restfulConn.pojo.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesReceivedFragment extends Fragment {

    ILocMessApi locMessApi;

    public MessagesReceivedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
 /*
        Location user = new Location("arco do ceguians", 31, 31, 30);
        Call<Location> call = locMessApi.createLocation(user);
        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Toast.makeText(getActivity(), "yupi" + response.code(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                call.cancel();
            }
        });

        Call<GpsLocationsList> call2 = locMessApi.getGpsLocations();
        call2.enqueue(new Callback<GpsLocationsList>() {
            @Override
            public void onResponse(Call<GpsLocationsList> call, Response<GpsLocationsList> response) {

                GpsLocationsList userList = response.body();
                List<GpsLocationsList.Datum> datumList = userList.rows;
                Toast.makeText(getActivity(), datumList.size() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GpsLocationsList> call, Throwable t) {
                call.cancel();
            }
        });*/


        List<MessageData> todelete = new ArrayList<MessageData>();
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));

        View view = inflater.inflate(R.layout.fragment_messages_received, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MessagesRVAdapter(todelete));

        return view;
    }

}
