package pt.cmov.locmess.locmess.fragments.messages;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.cmov.locmess.locmess.R;

public class MessagesReadFragment extends Fragment {
    private int message_pos;

    public MessagesReadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message_pos = bundle.getInt("message_pos");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_read, container, false);
        getActivity().setTitle("Read Message");

        TextView t = (TextView) view.findViewById(R.id.title);
        t.setText(message_pos + "");
        return view;
    }

}
