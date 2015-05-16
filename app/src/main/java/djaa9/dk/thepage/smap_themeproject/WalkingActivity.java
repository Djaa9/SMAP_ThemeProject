package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;

import java.util.List;


public class WalkingActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback{
    private LocationManager _locationManager;
    private GoogleMap _map;
    private List<LatLng> _route;
    private PolylineOptions _polylineOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        //_route = new List<>();

         _polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .geodesic(true)
                .visible(true)
                .width(10);

    }

    public void StopWalking(View view) {
        Intent intent = new Intent(this, WalkDoneActivity.class);

        startActivity(intent);
        //_locationManager.removeUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        //_route.add(new LatLng(location.getLatitude(), location.getLongitude()));

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
