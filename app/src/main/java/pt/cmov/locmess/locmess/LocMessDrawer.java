package pt.cmov.locmess.locmess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LocMessDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private TextView nameText;
    private TextView emailText;

    private NavigationView navigationView;
    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    private DatabaseReference dRef;


    private enum fragType{
        Messages, Locations, Account
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_mess_drawer);

        progressDialog = new ProgressDialog(this);

        //Set initial fragment
        loadFragment(fragType.Messages);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        nameText = (TextView) hView.findViewById(R.id.nameTextView);
        emailText = (TextView) hView.findViewById(R.id.emailTextView);

        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() != null){
            dRef = dRef.child("users");
            emailText.setText(mAuth.getCurrentUser().getEmail());

            Query queryRef = dRef.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    String groupKey = snapshot.getKey();

                    dRef.child(groupKey + "/firstName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            nameText.setText(snapshot.getValue().toString());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                    dRef.child(groupKey + "/lastName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            nameText.setText(nameText.getText() + " " + snapshot.getValue().toString());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
                // ....
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loc_mess_drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            loadFragment(fragType.Messages);
        } else if (id == R.id.nav_locations) {
            //loadFragment(fragType.Locations);
        } else if (id == R.id.nav_account) {
            //loadFragment(fragType.Account);
        }
        else if (id == R.id.nav_logout) {
            progressDialog.setMessage("Signing out...");
            progressDialog.show();
            mAuth.signOut();
            finish();
            progressDialog.dismiss();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(fragType c){
        Fragment fragment = null;
        switch(c){
            case Account:
                break;
            case Locations:
                break;
            case Messages:
                fragment = new MessagesFragment();
                break;
        }
        FragmentTransaction mainFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragmentTransaction.replace(R.id.fragment_container, fragment);
        mainFragmentTransaction.commit();
    }
}
