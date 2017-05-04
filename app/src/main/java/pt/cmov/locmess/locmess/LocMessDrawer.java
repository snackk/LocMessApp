package pt.cmov.locmess.locmess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import pt.cmov.locmess.locmess.locations.LocationsFragment;
import pt.cmov.locmess.locmess.messages.MessagesFragment;

public class LocMessDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;

    //Header drawer
    private TextView nameText;
    private TextView emailText;

    private NavigationView navigationView;

    private ProgressDialog progressDialog;

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

        //init drawer
        initDrawer();

        //init firebase user
        handleFireBaseUser();
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
            loadFragment(fragType.Locations);
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

    private void loadFragment(fragType c){
        Fragment fragment = null;

        switch(c){
            case Account:
                break;
            case Locations:
                fragment = new LocationsFragment();
                break;
            case Messages:
                fragment = new MessagesFragment();
                break;
        }
        FragmentTransaction mainFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragmentTransaction.replace(R.id.app_content, fragment).commit();
    }

    private void initDrawer(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void handleFireBaseUser(){
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
            });
        }
    }
}
