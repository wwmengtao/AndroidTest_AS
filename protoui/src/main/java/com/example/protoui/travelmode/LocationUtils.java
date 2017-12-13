package com.example.protoui.travelmode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.protoui.ALog;

import java.util.List;
import java.util.Locale;

/**
 * Created by mengtao1 on 2017/12/10.
 */

public class LocationUtils implements LocationListener {
    private static final String TAG = "LocationUtils";
    private volatile static LocationUtils mLocationUtils = null;
    private Context mContext = null;
    private boolean isNetworkEnabled = false;
    private boolean isGPSEnabled = false;
    private boolean canGetLocation = false;
    protected LocationManager locationManager;
    private Location location;
    private String providerUsed;
    private LocationListener mLocationListener = null;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = (long) (1000 * 5); // 5 seconds


    public static synchronized LocationUtils getInstance(Context mContext) {
        if (null == mLocationUtils) {
            mLocationUtils = new LocationUtils(mContext);
        }
        return mLocationUtils;
    }

    private LocationUtils(Context mContext){
        this.mContext = mContext;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if(!isOpenLocService()){
            showSettingsAlert();
        }
    }

    public void setLocationListener(LocationListener mLocationListener){
        this.mLocationListener = mLocationListener;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        if(null == mLocationListener)mLocationListener = this;
        // I SUSPECT SOMETHING IS WRONG HERE
        if (isNetworkEnabled) { // use network
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
            ALog.Log(TAG+"Using Network");
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            providerUsed = "Network";
        } else if (isGPSEnabled) { // use GPS
            if (location == null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
                ALog.Log(TAG+"Using GPS");
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

    @SuppressLint("MissingPermission")
    public void stopLocalization() {
        if (locationManager != null) {
            locationManager.removeUpdates(mLocationListener);
        }
    }

    public String getCityName(Location location){
        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return cityName;
        }
        return cityName;
    }

    private boolean isOpenLocService() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        ALog.Log("isGPSEnabled: "+isGPSEnabled+" isNetworkEnabled: "+isNetworkEnabled);
        canGetLocation = (isGPSEnabled || isNetworkEnabled);
        return canGetLocation;
    }

    // show alert dialog to direct the users to the settings
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        // make it uncancellable
        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("Turn Location on?");
        // Setting Dialog Message
        alertDialog.setMessage("Following city name could not be shown for Location off.");
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(mContext, "City name will not be shown!", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog ad = alertDialog.create();
        ad.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        // Showing Alert Message
        ad.show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
