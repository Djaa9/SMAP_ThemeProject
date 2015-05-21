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
import android.support.v4.util.Pair;
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

import djaa9.dk.thepage.smap_themeproject.MapUtils.DynamicToStatic;
import djaa9.dk.thepage.smap_themeproject.MapUtils.MapMath;

public class WalkingActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {
    private LocationManager _locationManager;
    private GoogleMap _map;
    private PolylineOptions _polylineOptions;
    private LatLngBounds.Builder _boundsBuilder;
    private Location _previousLocation;
    private MapMath _mapMath;
    private DynamicToStatic _toStatic;
    private boolean _firstLocation;

    private double _totalDistance;
    private double _donationAmount;

    private TextView _distanceTextView;
    private TextView _moneyAmountTextView;
    private Polyline _polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        // Get Views
        _distanceTextView = (TextView) findViewById(R.id.txv_distance);
        _moneyAmountTextView = (TextView) findViewById(R.id.txv_money_collected);

        // Instantiate fields
        _mapMath = new MapMath();
        _boundsBuilder = new LatLngBounds.Builder();
        _toStatic = new DynamicToStatic();
        _polylineOptions = new PolylineOptions()
                .color(Color.RED)
                .geodesic(true)
                .visible(true)
                .width(10);

        _totalDistance = 0;
        _donationAmount = 0;
        _firstLocation = true;

        // Setup MapFragment - Throws Event OnMapReady when done
        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        // Setup  Priority of LocationRequests
        (new LocationRequest()).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Setup LocationManager
        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    /* Called when the Google map is ready for use */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Get Map and Setup
        _map = googleMap;
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setAllGesturesEnabled(false);
        _map.getUiSettings().setMyLocationButtonEnabled(false);

        _polyline = _map.addPolyline(_polylineOptions); // Delete?

        // Setup LocationManager and Request onLocationChanged events
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        _locationManager.requestLocationUpdates(_locationManager.getBestProvider(criteria, true), 3 * 1000, 10, this);
    }

    /* Called by LocationListener when the Location is updated as specified in Setup */
    @Override
    public void onLocationChanged(Location location) {
        if (location == null)
            return;

        if (_firstLocation) {
            _map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
            _mapMath.setNewMiniumumDimentions(_map);
            _firstLocation = false;
        }

        _polylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));

        updateTotalDistance(location);
        updateDonationAmount();
        updateMapCamera(location);

        _polyline = _map.addPolyline(_polylineOptions);
    }

    /* Called when the provider status changes. This method is called when a provider is unable
    to fetch a location or if the provider has recently become available after a period of
    unavailability. */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /* Called when the provider is enabled by the user. */
    @Override
    public void onProviderEnabled(String provider) {
    }

    /* Called when the provider is disabled by the user. If requestLocationUpdates is called
    on an already disabled provider, this method is called immediately. */
    @Override
    public void onProviderDisabled(String provider) {
    }

    // Called when the user indicated that he is done walking
    public void StopWalking(View view) {
        Intent intent = new Intent(this, WalkDoneActivity.class);
        intent.putExtra(getPackageName() + ".DISTANCE_TRAVELED", _totalDistance)
                .putExtra(getPackageName() + ".DONATION_AMOUNT", _donationAmount)
                .putExtra(getPackageName() + ".ROUTE_MAP_URL", _toStatic.createStaticMapUrl(_polyline));

        startActivity(intent);
        _locationManager.removeUpdates(this);

<<<<<<< HEAD
        //Remove current activity from the stack
        finish();
=======
        // Communicate with sql
        new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "AWESOME"));
>>>>>>> 39e8f556025983030cfa31c4192d408bdec775ae
    }

    // Updates _totalDistance, with the cumulated distance the user have walked
    private void updateTotalDistance(Location newLocation) {
        if (_previousLocation == null) {
            _previousLocation = newLocation;
            _distanceTextView.setText("0,00");
            return;
        }

        _totalDistance += _previousLocation.distanceTo(newLocation) / 1000;
        _previousLocation = newLocation;

        _distanceTextView.setText(String.format("%.2f", _totalDistance));
    }

    private void updateDonationAmount() {
        _donationAmount = _totalDistance * 10;
        _moneyAmountTextView.setText(String.format("%.2f", _donationAmount));
    }

    private void updateMapCamera(Location location) {
        _boundsBuilder.include(new LatLng(location.getLatitude(), location.getLongitude()));

        LatLngBounds bounds = _boundsBuilder.build();
        _mapMath.setNewDimentions(bounds);

        if (_mapMath.getMinHeight() < _mapMath.getHeight() || _mapMath.getMinWidth() < _mapMath.getWidth()) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            _map.animateCamera(cameraUpdate, 1000, null);
        } else {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(bounds.getCenter());
            _map.animateCamera(cameraUpdate, 1000, null);
        }
    }
}
