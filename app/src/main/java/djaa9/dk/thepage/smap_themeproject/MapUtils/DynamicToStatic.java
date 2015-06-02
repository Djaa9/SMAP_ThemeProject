package djaa9.dk.thepage.smap_themeproject.MapUtils;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

public class DynamicToStatic {

    public String createStaticMapUrl(Polyline polyline) {

        // API URL
        String url = "https://maps.googleapis.com/maps/api/staticmap?";

        // size
        url = url + "size=600x600";

        //path setup
        url = url + "&path=color:0xff0000|weight:5"; //

        // add LatLng to path
        if (polyline != null) {
            for (LatLng coordinate : polyline.getPoints()) {
                url = url + "|" + coordinate.latitude + "," + coordinate.longitude;
            }
        }
        return url;
    }
}
