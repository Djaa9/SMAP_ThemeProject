package djaa9.dk.thepage.smap_themeproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;


public class WalkingActivity extends Activity implements LocationListener{
    private LocationManager _locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        //_locationManager.s

    }

    public void StopWalking(View view) {
        Intent intent = new Intent(this, WalkDoneActivity.class);

        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        int ff = 1+1;
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
}
