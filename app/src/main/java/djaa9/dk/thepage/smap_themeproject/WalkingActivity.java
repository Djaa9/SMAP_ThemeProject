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

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class WalkingActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback{
    private LocationManager _locationManager;
    private GoogleMap _map;
    private PolylineOptions _polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        LocationRequest request = new LocationRequest();

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);

        _locationManager.requestLocationUpdates(_locationManager.getBestProvider(crit, true), 500,  0, this);

         _polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .geodesic(true)
                .visible(true)
                .width(10);

    }

    public void StopWalking(View view) {
        Intent intent = new Intent(this, WalkDoneActivity.class);

        startActivity(intent);
        _locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        _polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));

        _map.addPolyline(_polylineOptions);
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

        _map.addPolyline(_polylineOptions);
    }
}
