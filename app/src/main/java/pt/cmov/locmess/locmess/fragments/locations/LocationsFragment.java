package pt.cmov.locmess.locmess.fragments.locations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.FragmentPageAdapter;

public class LocationsFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    public LocationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        getActivity().setTitle("Locations");

        viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        if (viewPager != null) {
            setupViewPager();
        }

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_location_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new LocationGpsCreateFragment();
                FragmentTransaction mainFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mainFragmentTransaction.addToBackStack(null);
                mainFragmentTransaction.replace(R.id.app_content, fragment).commit();
            }
        });
        return view;
    }

    private void setupViewPager() {
        FragmentPageAdapter adapter = new FragmentPageAdapter(getChildFragmentManager());
        adapter.addFragment(new LocationsGpsFragment(), "Gps");
        adapter.addFragment(new LocationsWifiFragment(), "Wi-fi");
        viewPager.setAdapter(adapter);
    }
}


