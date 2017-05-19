package pt.cmov.locmess.locmess.fragments.messages;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.backgroundService.SharedWifiConnection;
import pt.cmov.locmess.locmess.backgroundService.WifiBackgroundService;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Message;
import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.GroupInfoListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;

public class MessagesCreateFragment extends Fragment implements PeerListListener, GroupInfoListener {

    public static final String TAG = "msgsender";

    private ILocMessApi _locMessApi;
    private List<String> _locationsList = new ArrayList<>();
    private FirebaseRemoteConnection _firebaseConnection;

    private Spinner location_spinner;
    private Spinner policy_spinner;
    private TextView title_TextView;
    private TextView message_TextView;
    private TextView targetIp;

    private ArrayAdapter<String> locationsAd;
    private ArrayList<String> virtIp = new ArrayList<>();
    private SharedWifiConnection _sWifi;

    private Messenger mService = null;
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;

    //WIFI
    private WifiBackgroundService mReceiver;
    private SimWifiP2pSocketServer mSrvSocket = null;
    private SimWifiP2pSocket mCliSocket = null;
    private CheckBox p2p;

    public MessagesCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        //_sWifi = SharedWifiConnection.getInstance();
        //WIFI - START
        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(getContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new WifiBackgroundService(getActivity().getApplicationContext());
        getActivity().registerReceiver(mReceiver, filter);


        //WIFI ON

        Intent intent = new Intent(getActivity(), SimWifiP2pService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        // spawn the chat server background task
        new MessagesCreateFragment.IncommingCommTask().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
        //WIFI - END
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_create, container, false);
        getActivity().setTitle("New Message");

        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);

        locationsAd = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, _locationsList);
        location_spinner = (Spinner) view.findViewById(R.id.spinner_locations);
        location_spinner.setAdapter(locationsAd);

        Call<GpsLocationsList> locationsListCall = _locMessApi.getGpsLocations();
        locationsListCall.enqueue(new Callback<GpsLocationsList>() {

            @Override
            public void onResponse(Call<GpsLocationsList> call, Response<GpsLocationsList> response) {
                if(response.code() == 200){
                    GpsLocationsList locationsList = response.body();
                    List<GpsLocationsList.Datum> datumList = locationsList.rows;

                    for(GpsLocationsList.Datum d : datumList){
                        _locationsList.add(d.name);
                    }
                    location_spinner.setAdapter(locationsAd);
                }
            }

            @Override
            public void onFailure(Call<GpsLocationsList> call, Throwable t) {
                call.cancel();
            }
        });

        ArrayList<String> policy = new ArrayList<String>();
        policy.add("Whitelist");
        policy.add("Blacklist");

        policy_spinner = (Spinner) view.findViewById(R.id.spinner_policy);
        policy_spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, policy));

        title_TextView = (TextView) view.findViewById(R.id.message_title);
        message_TextView = (TextView) view.findViewById(R.id.message_text);
        targetIp = (TextView) view.findViewById(R.id.target_ip);

        p2p = (CheckBox) view.findViewById(R.id.p2p);

        ImageButton send = (ImageButton) view.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p2p.isChecked()){
                    mManager.requestPeers(mChannel, MessagesCreateFragment.this);
                    mManager.requestGroupInfo(mChannel, MessagesCreateFragment.this);

                    if (!message_TextView.getText().toString().isEmpty()) {
                        for(String ip : virtIp){
                            new OutgoingCommTask().executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR, ip);

                            new SendCommTask().executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR,
                                    message_TextView.getText().toString());

                            virtIp.remove(ip);
                        }

                    } else {
                        Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (!title_TextView.getText().toString().isEmpty() && !message_TextView.getText().toString().isEmpty()) {
                        final Message message = new Message(_firebaseConnection.getFirebaseEmail(), message_TextView.getText().toString().trim(), location_spinner.getSelectedItem().toString(), title_TextView.getText().toString().trim());

                        _locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
                        Call<ResponseBody> call = _locMessApi.createMessage(message);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Messaged posted!", Toast.LENGTH_LONG).show();
                                    title_TextView.setText("");
                                    message_TextView.setText("");
                                } else
                                    Toast.makeText(getContext(), "Something went wrong, code: " + response.code(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                call.cancel();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }




    //_______________________________________________________________________________
    //                      w I F i
    //_______________________________________________________________________________



	/*
	 * Asynctasks implementing message exchange
	 */

    public class IncommingCommTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

            try {
                mSrvSocket = new SimWifiP2pSocketServer(10001);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    SimWifiP2pSocket sock = mSrvSocket.accept();
                    try {
                        BufferedReader sockIn = new BufferedReader(
                                new InputStreamReader(sock.getInputStream()));
                        String st = sockIn.readLine();
                        publishProgress(st);
                        sock.getOutputStream().write(("\n").getBytes());
                    } catch (IOException e) {
                        Log.d("Error reading socket:", e.getMessage());
                    } finally {
                        sock.close();
                    }
                } catch (IOException e) {
                    Log.d("Error socket:", e.getMessage());
                    break;
                    //e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            targetIp.append(values[0] + "\n");
        }
    }

    public class OutgoingCommTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //mTextOutput.setText("Connecting...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                mCliSocket = new SimWifiP2pSocket(params[0],
                        Integer.parseInt(getString(R.string.port)));
            } catch (UnknownHostException e) {
                return "Unknown Host:" + e.getMessage();
            } catch (IOException e) {
                return "IO error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    public class SendCommTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... msg) {
            try {
                mCliSocket.getOutputStream().write((msg[0] + "\n").getBytes());
                BufferedReader sockIn = new BufferedReader(
                        new InputStreamReader(mCliSocket.getInputStream()));
                sockIn.readLine();
                mCliSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCliSocket = null;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

	/*
	 * Listeners associated to Termite
	 */

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        StringBuilder peersStr = new StringBuilder();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }

        // display list of devices in range
        new AlertDialog.Builder(getActivity())
                .setTitle("Devices in WiFi Range")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices,
                                     SimWifiP2pInfo groupInfo) {

        // compile list of network members
        StringBuilder peersStr = new StringBuilder();
        for (String deviceName : groupInfo.getDevicesInNetwork()) {
            SimWifiP2pDevice device = devices.getByName(deviceName);
            String devstr = "" + deviceName + " (" +
                    ((device == null)?"??":device.getVirtIp()) + ")\n";
            peersStr.append(devstr);
            virtIp.add(device.getVirtIp());
        }

        // display list of network members
        new AlertDialog.Builder(getActivity())
                .setTitle("Devices in WiFi Network")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    //_______________________________________________________________________________
    //                      w I F i
    //_______________________________________________________________________________

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mManager = new SimWifiP2pManager(mService);
            mChannel = mManager.initialize(getActivity(), getMainLooper(), null);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;
            mManager = null;
            mChannel = null;
        }
    };
}
