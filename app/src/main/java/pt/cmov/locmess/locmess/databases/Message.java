package pt.cmov.locmess.locmess.databases;

import java.sql.Timestamp;

/**
 * Created by snackk on 06/04/2017.
 */

public abstract class Message {
    private String _message;
    private String _fromEmail;
    private Timestamp _timestamp;

    public Message(String message, String fromEmail, Timestamp timestamp){
        _message = message;
        _fromEmail = fromEmail;
        _timestamp = timestamp;
    }

    public String getMessage(){
        return _message;
    }

    public String getFromEmail(){
        return _fromEmail;
    }

    public Timestamp getTimeStamp(){
        return _timestamp;
    }
}
