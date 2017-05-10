package pt.cmov.locmess.locmess.fragments.messages;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.cmov.locmess.locmess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesReadFragment extends Fragment {


    public MessagesReadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_read, container, false);
        /*if (savedInstanceState != null) {
            TextView t = (TextView) view.findViewById(R.id.ola);
            t.setText(savedInstanceState.getInt("ola"));
        }*/
        return view;
    }

}
