package pt.cmov.locmess.locmess.restfulConn.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snackk on 10/05/2017.
 */

public class WifiLocationsList {
    @SerializedName("rows")
    public List<Datum> rows = new ArrayList();

    public class Datum {

        @SerializedName("name")
        public String name;
        @SerializedName("address")
        public String address;

    }
}

