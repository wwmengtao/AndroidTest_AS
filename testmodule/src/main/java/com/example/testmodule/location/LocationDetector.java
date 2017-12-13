package com.example.testmodule.location;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mengtao1 on 2017/12/10.
 */

public final class LocationDetector implements LocationListener {

    private final Context mContext;

    private boolean isNetworkEnabled = false;
    private boolean isGPSEnabled = false;
    private boolean canGetLocation = false;

    private Location location;
    private String providerUsed;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = (long) (1000 * 60 * 0.5); // 0.5 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    // constructor
    public LocationDetector(Context context) {
        this.mContext = context;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    // NOTE call  checkLocationServiceAvailability(); first before calling this!
    @SuppressLint("MissingPermission")
    public Location getLocation() {
// I SUSPECT SOMETHING IS WRONG HERE
        if (isNetworkEnabled) { // use network

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Log.d("LocationDetector", "Using Network");
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            providerUsed = "Network";

        } else if (isGPSEnabled) { // use GPS

            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("LocationDetector", "Using GPS");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            providerUsed = "GPS";

        } else { // neither the network nor the GPS is on

            providerUsed = null;

            Toast.makeText(mContext, "Location service is unavaliable", Toast.LENGTH_SHORT).show();
        }

        return location;
    }

    // call this to restart requesting the detecting
    @SuppressLint("MissingPermission")
    public void startLocalization() {

        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }
    }

    // call this to stop the detecting to save power
    public void stopLocalization() {

        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    // check location service availability
    public boolean checkLocationServiceAvailability() {
        // check GPS on or off
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // check Internet access
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            isNetworkEnabled = true;
        } else {
            isNetworkEnabled = false;
        }

        if (isGPSEnabled || isNetworkEnabled) {
            canGetLocation = true;
        } else {
            canGetLocation = false;
        }

        return canGetLocation;
    }

    public String getLocationProvider() {

        return providerUsed;
    }

    // show alert dialog to direct the users to the settings
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // make it uncancellable
        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("Forgot to turn GPS on?");
        // Setting Dialog Message
        alertDialog.setMessage("Currently there is no Internet access.\n\nLocalization requires GPS when Internet is unavailiable.\n\nDo you want to enable GPS so as to proceed?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "After enabling GPS, press the physical 'Back' button to return", Toast.LENGTH_LONG).show();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(mContext, "No location service, please choose map manually", Toast.LENGTH_LONG).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location _location) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}
