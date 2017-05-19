package pt.cmov.locmess.locmess.backgroundService;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;

/**
 * Created by snackk on 19/05/2017.
 */

public class SharedWifiConnection {
    private static SharedWifiConnection _instance = null;

    private Messenger mService = null;
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Context context;
    private SimWifiP2pSocketServer mSrvSocket = null;

    protected SharedWifiConnection(){

    }

    public static SharedWifiConnection getInstance(){
        if(_instance == null){
            _instance = new SharedWifiConnection();
        }
        return _instance;
    }

    public Messenger getMessageService(){
        return mService;
    }

    public void setMessageService(Messenger mess){
        mService = mess;
    }

    public SimWifiP2pManager getP2pManager(){
        return mManager;
    }

    public void setP2pManager(SimWifiP2pManager p2p){
        mManager = p2p;
    }

    public SimWifiP2pManager.Channel getChannel(){
        return mChannel;
    }

    public void setP2pChannel(SimWifiP2pManager.Channel channel){
        mChannel = channel;
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public SimWifiP2pSocketServer getmSrvSocket(){
        return mSrvSocket;
    }

    public void setmSrvSocket(SimWifiP2pSocketServer socket){
        mSrvSocket = socket;
    }
}
