package pt.cmov.locmess.locmess.fragments.locations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.cmov.locmess.locmess.R;

public class LocationsWifiFragment extends Fragment {

    public static final String TAG = LocationsWifiFragment.class.getName();

    public LocationsWifiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wifi_locations, container, false);

        return view;
    }
}
