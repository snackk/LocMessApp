package pt.cmov.locmess.locmess.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.Adapter;
import pt.cmov.locmess.locmess.adapter.MessagesFragmentPageAdapter;

public class MessagesFragment extends Fragment {

    public MessagesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle("Messages");
        activity.setSupportActionBar(toolbar);
/*
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        assert viewPager != null;
        tabLayout.setupWithViewPager(viewPager);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Message sent!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new MessagesReceivedFragment(), "Received");
        adapter.addFragment(new MessagesSentFragment(), "Sent");
        viewPager.setAdapter(adapter);
    }

}


