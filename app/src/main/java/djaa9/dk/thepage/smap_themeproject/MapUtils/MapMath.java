package djaa9.dk.thepage.smap_themeproject.MapUtils;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapMath {
    private double _minHeight;
    private double _minWidth;
    private double _height;
    private double _width;

    public double getMinHeight() {
        return _minHeight;
    }

    public double getMinWidth() {
        return _minWidth;
    }

    public double getHeight() {
        return _height;
    }

    public double getWidth() {
        return _width;
    }

    public void setNewMiniumumDimentions(GoogleMap map){
        Location ne = new Location("");
        ne.setLatitude(map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
        ne.setLongitude(map.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);

        Location sw = new Location("");
        sw.setLatitude(map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
        sw.setLongitude(map.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);
/*
        Location nw = new Location("");
        nw.setLatitude(map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
        nw.setLongitude(map.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);
*/
        Location se = new Location("");
        se.setLatitude(map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
        se.setLongitude(map.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);

        _minWidth = se.distanceTo(sw);
        _minHeight = se.distanceTo(ne);
    }

    public void setNewDimentions(LatLngBounds bounds){
        Location ne = new Location("");
        ne.setLatitude(bounds.northeast.latitude);
        ne.setLongitude(bounds.northeast.longitude);

        Location sw = new Location("");
        sw.setLatitude(bounds.southwest.latitude);
        sw.setLongitude(bounds.southwest.longitude);
/*
        Location nw = new Location("");
        nw.setLatitude(bounds.northeast.latitude);
        nw.setLongitude(bounds.southwest.longitude);
*/
        Location se = new Location("");
        se.setLatitude(bounds.southwest.latitude);
        se.setLongitude(bounds.northeast.longitude);

        _width =  se.distanceTo(sw);
        _height = se.distanceTo(ne);
    }


}
