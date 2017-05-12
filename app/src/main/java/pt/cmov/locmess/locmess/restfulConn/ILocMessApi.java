package pt.cmov.locmess.locmess.restfulConn;

/**
 * Created by snackk on 10/05/2017.
 */

import pt.cmov.locmess.locmess.restfulConn.pojo.GpsLocationsList;
import pt.cmov.locmess.locmess.restfulConn.pojo.Location;
import pt.cmov.locmess.locmess.restfulConn.pojo.Message;
import pt.cmov.locmess.locmess.restfulConn.pojo.MessagesList;
import pt.cmov.locmess.locmess.restfulConn.pojo.User;
import pt.cmov.locmess.locmess.restfulConn.pojo.WifiLocationsList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ILocMessApi {
    @POST("/locmess/users")
    Call<User> createUser(@Body User user);

    @POST("/locmess/messages")
    Call<Message> createMessage(@Body Message message);

    @POST("/locmess/locations")
    Call<Location> createLocation(@Body Location location);

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
