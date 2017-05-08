package pt.cmov.locmess.locmess.fragments.messages;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import pt.cmov.locmess.locmess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesCreateFragment extends Fragment {


    public MessagesCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_create, container, false);
        getActivity().setTitle("New Message");

        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");

        ArrayList<String> policy = new ArrayList<String>();
        policy.add("Whitelist");
        policy.add("Blacklist");

        Spinner sl = (Spinner) view.findViewById(R.id.spinner_locations);
        Spinner sp = (Spinner) view.findViewById(R.id.spinner_policy);
        sl.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, locations));
        sp.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, policy));

        return view;
    }

}
