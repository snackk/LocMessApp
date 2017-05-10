package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snackk on 10/05/2017.
 */

public class MessagesList {
    @SerializedName("rows")
    public List<Datum> rows = new ArrayList();

    public class Datum {

        @SerializedName("text")
        public String text;
        @SerializedName("creator")
        public String creator;
        @SerializedName("location")
        public String location;
        @SerializedName("message_id")
        public String message_id;
        @SerializedName("title")
        public String title;
    }
}
