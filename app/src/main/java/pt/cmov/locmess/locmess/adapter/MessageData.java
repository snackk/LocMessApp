package pt.cmov.locmess.locmess.adapter;

/**
 * Created by snackk on 08/05/2017.
 */

public class MessageData {
    private String _user;
    private String _message;
    private String _timeStamp;
    private String _location;

    public MessageData(String user, String message, String timeStamp, String location){
        _user = user;
        _message = message;
        _timeStamp = timeStamp;
        _location = location;
    }

    public String getUser(){
        return _user;
    }

    public String getMessage(){
        return _message;
    }

    public String getTimeStamp(){
        return _timeStamp;
    }

    public String getLocation(){
        return _location;
    }

}
