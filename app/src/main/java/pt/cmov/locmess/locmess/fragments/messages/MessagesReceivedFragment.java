package pt.cmov.locmess.locmess.fragments.messages;

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
import pt.cmov.locmess.locmess.adapter.messages.MessageData;
import pt.cmov.locmess.locmess.adapter.messages.MessagesRVAdapter;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesReceivedFragment extends Fragment {

    private ILocMessApi _locMessApi;
    private FirebaseRemoteConnection _firebaseConnection;
    private List<MessageData> _messagesList = new ArrayList<>();
    private MessagesRVAdapter _messagesRVAdapter;

    public MessagesReceivedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        _messagesRVAdapter = new MessagesRVAdapter(_messagesList);

        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
        Call<MessagesList> messagesListCall = _locMessApi.getListMessages(_firebaseConnection.getFirebaseEmail());
        messagesListCall.enqueue(new Callback<MessagesList>() {

            @Override
            public void onResponse(Call<MessagesList> call, Response<MessagesList> response) {
                if(response.code() == 200){
                    MessagesList messagesList = response.body();
                    List<MessagesList.Datum> datumList = messagesList.rows;

                    for(MessagesList.Datum d : datumList){
                        if(!_firebaseConnection.getFirebaseEmail().equals(d.creator))
                            _messagesList.add(new MessageData(d.creator, d.text, "", d.location, d.title));
                    }
                    _messagesRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MessagesList> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages_received, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        _messagesRVAdapter.passFrag(this);
        recyclerView.setAdapter(_messagesRVAdapter);

        return view;
    }
}
