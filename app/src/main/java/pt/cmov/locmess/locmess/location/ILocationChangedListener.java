package pt.cmov.locmess.locmess.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by snackk on 08/05/2017.
 */

public interface ILocationChangedListener {
    void onLocationChange(LatLng location);
}
