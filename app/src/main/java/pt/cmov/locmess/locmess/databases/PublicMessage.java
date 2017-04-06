package pt.cmov.locmess.locmess.databases;

import java.sql.Timestamp;

/**
 * Created by snackk on 06/04/2017.
 */

public class PublicMessage extends Message {
    //LOCATION NEEDED...

    public PublicMessage(String message, String fromEmail, Timestamp timestamp) {
        super(message, fromEmail, timestamp);
    }
}
