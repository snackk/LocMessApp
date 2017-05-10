package pt.cmov.locmess.locmess.restfulConn;

/**
 * Created by snackk on 10/05/2017.
 */

public class RestfulRemoteConnection {
    private static RestfulRemoteConnection _instance = null;

    protected RestfulRemoteConnection(){

    }

    public static RestfulRemoteConnection getInstance(){
        if (_instance == null)
            _instance = new RestfulRemoteConnection();
        return _instance;
    }
}
