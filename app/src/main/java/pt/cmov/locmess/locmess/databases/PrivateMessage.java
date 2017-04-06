package pt.cmov.locmess.locmess.databases;


import java.sql.Timestamp;

/**
 * Created by snackk on 06/04/2017.
 */

public class PrivateMessage extends Message {

    public PrivateMessage(String message, String fromEmail) {
        super(message, fromEmail, new Timestamp(System.currentTimeMillis()));
    }
}
