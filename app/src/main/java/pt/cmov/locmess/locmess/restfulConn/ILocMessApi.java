package pt.cmov.locmess.locmess.restfulConn;

/**
 * Created by snackk on 10/05/2017.
 */

import okhttp3.ResponseBody;
import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Location;
import pt.cmov.locmess.locmess.restfulConn.pojo.Message;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import pt.cmov.locmess.locmess.restfulConn.pojo.User;
import pt.cmov.locmess.locmess.restfulConn.pojo.WifiLocation;
import pt.cmov.locmess.locmess.restfulConn.pojo.WifiLocationsList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ILocMessApi {
    @POST("/locmess/users")
    Call<ResponseBody> createUser(@Body User user);

    @POST("/locmess/messages")
    Call<ResponseBody> createMessage(@Body Message message);

    @POST("/locmess/locations")
    Call<ResponseBody> createLocation(@Body Location location);

    @POST("/locmess/locations")
    Call<ResponseBody> createWifiLocation(@Body WifiLocation location);

    //Read messages
    @GET("/locmess/messages/{username}")
    Call<MessagesList> getListMessages(@Path("username") String username);

    //Unread messages
    @GET("/locmess/messages/{username}/{location}")
    Call<MessagesList> getUnreadListMessages(@Path("username") String username, @Path("location") String location);

    @GET("/locmess/locations/gps")
    Call<GpsLocationsList> getGpsLocations();

    @GET("/locmess/locations/wifi")
    Call<WifiLocationsList> getWifiLocations();
}
