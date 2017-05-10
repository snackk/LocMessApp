package pt.cmov.locmess.locmess.fragments.locations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.cmov.locmess.locmess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationWifiCreateFragment extends Fragment {


    public LocationWifiCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_wifi_create, container, false);

        return view;
    }

}
