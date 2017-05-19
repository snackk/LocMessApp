package pt.cmov.locmess.locmess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import pt.cmov.locmess.locmess.backgroundService.GpsBackgroundService;
import pt.cmov.locmess.locmess.backgroundService.SharedWifiConnection;
import pt.cmov.locmess.locmess.backgroundService.WifiBackgroundService;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.firebaseConn.IUserDetailsResponseListener;
import pt.cmov.locmess.locmess.fragments.locations.LocationsFragment;
import pt.cmov.locmess.locmess.fragments.messages.MessagesCreateFragment;
import pt.cmov.locmess.locmess.fragments.messages.MessagesFragment;
import pt.cmov.locmess.locmess.fragments.profile.ProfileFragment;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

import static android.os.Looper.getMainLooper;

public class LocMessDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Header drawer
    private TextView nameText;
    private TextView emailText;

    private NavigationView navigationView;
    private ProgressDialog progressDialog;
    private FirebaseRemoteConnection _firebaseConnection;

    private WifiBackgroundService mReceiver;
    public static Context context;
    public static SharedWifiConnection _sWifi;
    public static Looper mainLoop;

    private enum fragType{
        Messages, Locations, Account
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_mess_drawer);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mainLoop = getMainLooper();
        //WIFI - START
        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(getApplicationContext());
        _sWifi = SharedWifiConnection.getInstance();
        _sWifi.setContext(getApplicationContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new WifiBackgroundService(this);
        registerReceiver(mReceiver, filter);


        //WIFI ON
        Intent intent = new Intent(getApplicationContext(), SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        // spawn the chat server background task
        new MessagesCreateFragment.IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
        //WIFI - END


        //Start background service
        Intent intent1 = new Intent(getApplicationContext(), GpsBackgroundService.class);
        startService(intent1);

        progressDialog = new ProgressDialog(this);

        //Set initial fragment
        loadFragment(fragType.Messages);

        //init drawer
        initDrawer();

        //init firebase user
        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        updateUserDetails();
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
            loadFragment(fragType.Account);
        }
        else if (id == R.id.nav_logout) {
            progressDialog.setMessage("Signing out...");
            progressDialog.show();

            _firebaseConnection.doSignOut();
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
                fragment = new ProfileFragment();
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

    private void updateUserDetails(){
        View hView =  navigationView.getHeaderView(0);

        nameText = (TextView) hView.findViewById(R.id.nameTextView);
        emailText = (TextView) hView.findViewById(R.id.emailTextView);
        emailText.setText(_firebaseConnection.getFirebaseEmail());

        _firebaseConnection.getUserDetails();
        _firebaseConnection.addOnUserDetailsResponseListener(new IUserDetailsResponseListener() {
            @Override
            public void OnUserDetailsResponseListener(String name) {
                nameText.setText(name);
            }
        });
    }


    public static ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            _sWifi.setMessageService(new Messenger(service));
            _sWifi.setP2pManager(new SimWifiP2pManager(_sWifi.getMessageService()));
            _sWifi.setP2pChannel(_sWifi.getP2pManager().initialize(context, mainLoop, null));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            _sWifi.setMessageService(null);
            _sWifi.setP2pManager(null);
            _sWifi.setP2pChannel(null);
        }
    };
}

