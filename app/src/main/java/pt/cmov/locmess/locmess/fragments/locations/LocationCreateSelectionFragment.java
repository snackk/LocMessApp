package pt.cmov.locmess.locmess.fragments.locations;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.FragmentPageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationCreateSelectionFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    public LocationCreateSelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_create_selection, container, false);

        getActivity().setTitle("Location Create");

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
        return view;
    }

    private void setupViewPager() {
        FragmentPageAdapter adapter = new FragmentPageAdapter(getChildFragmentManager());
        adapter.addFragment(new LocationGpsCreateFragment(), "Gps");
        adapter.addFragment(new LocationWifiCreateFragment(), "Wi-Fi");
        viewPager.setAdapter(adapter);
    }

}
