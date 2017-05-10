package pt.cmov.locmess.locmess.fragments.locations;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pt.cmov.locmess.locmess.R;

public class LocationGpsCreateFragment extends Fragment {


    public LocationGpsCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_gps_create, container, false);
        Button picker = (Button) view.findViewById(R.id.pick_on_map);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LocationGpsMapPickerFragment();
                FragmentTransaction mainFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mainFragmentTransaction.replace(R.id.app_content, fragment).commit();
            }
        });
        return view;
    }

}
