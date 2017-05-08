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
import pt.cmov.locmess.locmess.adapter.MessageData;
import pt.cmov.locmess.locmess.adapter.MessagesRVAdapter;

public class MessagesSentFragment extends Fragment {

    public MessagesSentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_sent, container, false);

        List<MessageData> todelete = new ArrayList<MessageData>();
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.messages_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MessagesRVAdapter(todelete));

        return view;
    }
}
