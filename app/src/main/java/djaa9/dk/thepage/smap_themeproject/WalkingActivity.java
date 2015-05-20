package djaa9.dk.thepage.smap_themeproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class WalkingActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback{
    private LocationManager _locationManager;
    private GoogleMap _map;
    private PolylineOptions _polylineOptions;
    private LatLngBounds.Builder _boundsBuilder;
    private Criteria _crit;
    private Location _previousLocation;

    private double _totalDistance;
    private double _donationAmount;
    private double minimumMapWidth;
    private double minimumMapHeight;

    private TextView _distanceTextView;
    private TextView _moneyAmountTextView;
    private Polyline _polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        _distanceTextView = (TextView) findViewById(R.id.txv_distance);
        _moneyAmountTextView = (TextView) findViewById(R.id.txv_money_collected);

        _totalDistance = 0;
        _donationAmount = 0;

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        LocationRequest request = new LocationRequest();
        _boundsBuilder = new LatLngBounds.Builder();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        _crit = new Criteria();
        _crit.setAccuracy(Criteria.ACCURACY_FINE);

         _polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .geodesic(true)
                .visible(true)
                .width(10);
    }

    @Override
    public void onLocationChanged(Location location) {
        _polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));

        updateTotalDistance(location);
        updateDonationAmount();
        updateMapCamera(location);

        _polyline = _map.addPolyline(_polylineOptions);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        _map = googleMap;
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setAllGesturesEnabled(false);
        _map.getUiSettings().setMyLocationButtonEnabled(false);

        _polyline = _map.addPolyline(_polylineOptions);

        Location lastKnownLocation = _locationManager.getLastKnownLocation(_locationManager.getBestProvider(_crit, false));

        if (lastKnownLocation != null){
            _map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 17));

            Location ne = new Location("");
            ne.setLatitude(_map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
            ne.setLongitude(_map.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);

            Location sw = new Location("");
            sw.setLatitude(_map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
            sw.setLongitude(_map.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);

            Location nw = new Location("");
            nw.setLatitude(_map.getProjection().getVisibleRegion().latLngBounds.northeast.latitude);
            nw.setLongitude(_map.getProjection().getVisibleRegion().latLngBounds.southwest.longitude);

            Location se = new Location("");
            se.setLatitude(_map.getProjection().getVisibleRegion().latLngBounds.southwest.latitude);
            se.setLongitude(_map.getProjection().getVisibleRegion().latLngBounds.northeast.longitude);

            minimumMapWidth = ne.distanceTo(nw);
            minimumMapHeight = se.distanceTo(ne);

            _locationManager.requestLocationUpdates(_locationManager.getBestProvider(_crit, true), 3*1000,  10, this);
            }
    }

    // Called when the user indicated that he is done walking
    public void StopWalking(View view) {
        Intent intent = new Intent(this, WalkDoneActivity.class);
        intent.putExtra(getPackageName() + ".DISTANCE_TRAVELED", _totalDistance)
                .putExtra(getPackageName() + ".DONATION_AMOUNT", _donationAmount)
                .putExtra(getPackageName() + "ROUTE_MAP_URL", createStaticMapUrl());

        startActivity(intent);
        _locationManager.removeUpdates(this);
    }

    // Updates _totalDistance, with the cumulated distance the user have walked
    private void updateTotalDistance(Location newLocation) {
        if (_previousLocation == null ){
            _previousLocation = newLocation;
            _distanceTextView.setText("0,00");
            return;
        }

        _totalDistance += _previousLocation.distanceTo(newLocation)/1000;
        _previousLocation = newLocation;

        _distanceTextView.setText(String.format("%.2f", _totalDistance));
    }

    private void updateDonationAmount(){
         _donationAmount = _totalDistance*10;
        _moneyAmountTextView.setText(String.format("%.2f", _donationAmount));
    }

    private void updateMapCamera(Location location) {

        _boundsBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));

        LatLngBounds bounds = _boundsBuilder.build();

        Location ne = new Location("");
        ne.setLatitude(bounds.northeast.latitude);
        ne.setLongitude(bounds.northeast.longitude);

        Location sw = new Location("");
        sw.setLatitude(bounds.southwest.latitude);
        sw.setLongitude(bounds.southwest.longitude);

        Location nw = new Location("");
        nw.setLatitude(bounds.northeast.latitude);
        nw.setLongitude(bounds.southwest.longitude);

        Location se = new Location("");
        se.setLatitude(bounds.southwest.latitude);
        se.setLongitude(bounds.northeast.longitude);

        if (minimumMapHeight < ne.distanceTo(se) || minimumMapWidth < se.distanceTo(sw)) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            _map.animateCamera(cameraUpdate, 1000, null);
        }
        else{
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(bounds.getCenter());
            _map.animateCamera(cameraUpdate, 1000, null);
        }
    }

    private String createStaticMapUrl() {

        // API URL
        String url = "https://maps.googleapis.com/maps/api/staticmap?";

        // size
        url = url + "size=600x600";

        //path setup
        url = url + "&path=color:0xff0000|weight:5"; //

        // add LatLng to path
        for (LatLng coordinate : _polyline.getPoints()){
            url = url + "|" + coordinate.latitude + "," + coordinate.longitude;
        }
            return url;
    }
}
