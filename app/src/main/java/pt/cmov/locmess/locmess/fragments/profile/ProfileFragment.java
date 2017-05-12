package pt.cmov.locmess.locmess.fragments.profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.cmov.locmess.locmess.R;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");

        /*List<MessageData> todelete = new ArrayList<MessageData>();
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));
        todelete.add(new MessageData("Diogo", "Vendo bike a 10 paus", "timestamp", "Arco do cego"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MessagesRVAdapter(this, todelete));
*/
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.new_profile_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Message sent!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent myIntent = new Intent(getContext(), MessageCreateActivity.class);
                startActivity(myIntent);*/
                Fragment fragment = new ProfileCreateFragment();
                FragmentTransaction mainFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                mainFragmentTransaction.replace(R.id.app_content, fragment).commit();
            }
        });
        return view;


    }
}
