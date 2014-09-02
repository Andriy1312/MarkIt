package com.android.markit;

import com.android.markit.entry.Mark;
import com.android.markit.storage.ChecksSQLiteManager;
import com.android.markit.storage.IChecksSQLiteManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MarkItMapActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{

    private final static int CONNECTION_ERROR_REQUEST = 9876;
    private LocationClient googleLocationClient;
    private GoogleMap googleMap;
    private MarkerOptions mOptions;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_it_map);
        if(isGooglePlayServiceConnected())
            googleLocationClient = new LocationClient(this, this, this);
        googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.markit_map)).getMap();
        mOptions = new MarkerOptions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleLocationClient != null)
            googleLocationClient.connect();
    }

    @Override
    protected void onStop() {
        if(googleLocationClient != null)
            googleLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CONNECTION_ERROR_REQUEST) {
            switch(resultCode) {
                 case Activity.RESULT_OK:
                     break;
                 default:
                     break;
            }
            Log.e("MarkIt", "Connection Error:" + resultCode );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mark_it_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.markit_check:
                if(googleLocationClient.isConnected()) {
                   Location currentLocation = googleLocationClient.getLastLocation();
                   double latitude = currentLocation.getLatitude();
                   double longitude = currentLocation.getLongitude();
                   long time = currentLocation.getTime();
                   IChecksSQLiteManager dataManager = new ChecksSQLiteManager(this);
                   dataManager.putValues(new Mark(latitude, longitude, time));
                   mOptions.position(new LatLng(latitude, longitude)).title("Lat: " + String.format( "%.2f", latitude) + ", "  + "Long: " + String.format( "%.2f", longitude));
                   googleMap.addMarker(mOptions);
                }
                break;
            case R.id.markit_history:
                Intent intent = new Intent(this, MarkItChecksListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isGooglePlayServiceConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode == ConnectionResult.SUCCESS)
            return true;
        else
            showErrorDialog(resultCode);
        return false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_ERROR_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else
            showErrorDialog(connectionResult.getErrorCode());
    }

    @Override
    public void onConnected(Bundle data) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleLocationClient.requestLocationUpdates(locationRequest, this);
        Toast.makeText(this, "Google Play service is connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Google Play service is disconnected. Try again", Toast.LENGTH_LONG).show();
    }

    public void showErrorDialog(int errorId) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorId, this, CONNECTION_ERROR_REQUEST);
        if(errorDialog != null) {
            MarkItErrorDialogFragment markitErrorDialog = new MarkItErrorDialogFragment();
            markitErrorDialog.setDialog(errorDialog);
            markitErrorDialog.show(getFragmentManager(),"connection_dialog");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        
    }
}