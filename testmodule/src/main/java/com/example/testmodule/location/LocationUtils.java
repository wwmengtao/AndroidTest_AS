package com.example.testmodule.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;

import com.example.testmodule.ALog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

/**
 * Created by mengtao1 on 2017/12/10.
 */

public class LocationUtils {
    private static final String TAG = "LocationUtils";
    private volatile static LocationUtils mLocationUtils = null;
    private Context mContext = null;
    private LocationManager mLocationManager = null;

    public static LocationUtils build(Context context){
        if(null == mLocationUtils){
            mLocationUtils = new LocationUtils(context);
        }
        return mLocationUtils;
    }

    private LocationUtils(Context context){
        this.mContext = context;
        this.mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * getAddresslocation:通过地理坐标获取地理信息
     */
    @SuppressLint("MissingPermission")
    public void getAddresslocation() {
        //Geocoder能通过经纬度来获取相应的城市等信息
        Geocoder geocoder = new Geocoder(mContext);
        //provider的类型
        String provider = LocationManager.NETWORK_PROVIDER;
        //通过最后一次的地理位置来获得Location对象
        Location location = mLocationManager.getLastKnownLocation(provider);
        String queryed_name = getLocationInfo(location);

        /*
         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米
         * 设定每30秒进行一次自动定位
         */
        if(null == location)mLocationManager.requestLocationUpdates(provider, 5000, 0,
                locationListener);
//        HandlerThread mHandlerThread;
//        mHandlerThread = new HandlerThread("du-location");
//        mHandlerThread.start();
//        final long LOCATION_REFRESH_TIME = 2 * 1000; //seconds
//        final float LOCATION_REFRESH_DISTANCE = 0; //meters
//        locationManager.requestLocationUpdates(LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE,
//                criteria, locationListener, mHandlerThread.getLooper());
        //移除监听器，在只有一个widget的时候，这个还是适用的
    }

    public void removeLocationListener(){
        mLocationManager.removeUpdates(locationListener);
    }

    private Criteria getCriteria(){
        Criteria criteria = new Criteria();//
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//设置定位精准度
        criteria.setAltitudeRequired(false);//是否要求海拔
        criteria.setBearingRequired(true);//是否要求方向
        criteria.setCostAllowed(false);//是否要求收费
        criteria.setSpeedRequired(false);//是否要求速度
        criteria.setPowerRequirement(Criteria.POWER_LOW);//设置相对省电
//        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);//设置方向精确度
//        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);//设置速度精确度
//        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);//设置水平方向精确度
//        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);//设置垂直方向精确度
        return criteria;
    }

    /**
     * 方位改变时触发，进行调用
     */
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            ALog.Log(TAG+"_onLocationChanged");
            getLocationInfo(location);
        }

        public void onProviderDisabled(String provider) {
            ALog.Log(TAG+"_onProviderDisabled");
            EventBus.getDefault().post(new MessageEvent(null));
        }

        public void onProviderEnabled(String provider) {
            ALog.Log(TAG+"_onProviderEnabled");
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            ALog.Log(TAG+"_onStatusChanged");
        }
    };

    private String getLocationInfo(Location location){
        String cityName = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        ALog.Log("Geocoder");
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            ALog.Log("addresses.size: "+addresses.size());
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String locality = addresses.get(0).getLocality();
                String subLocality = addresses.get(0).getSubLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                cityName = locality;
                ALog.Log("address: "+address+" locality: "+locality+" subLocality: "+subLocality+
                        " state: "+state+" country: "+country+" postalCode: "+postalCode+" knownName: "+knownName);
                MessageEvent mMessageEvent = new MessageEvent(addresses.get(0));
                EventBus.getDefault().post(mMessageEvent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return cityName;
        }
        return cityName;
    }


    /**
     * gotoLocServiceSettings：跳转定位服务开启界面
     */
    public void gotoLocServiceSettings() {
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 判断是否启动定位服务
     * @return 是否启动定位服务
     */
    public boolean isOpenLocService() {
        /**
         * 下列两种方式开启的前提是gotoLocServiceSettings函数中所指的定位服务开启。
         * 三种Location mode分别影响下列两者的数值：
         * High accuracy：两者皆为true；
         * Battery saving: isGpsWork为false，isNetwork为true；
         * Device only:isGpsWork为true，isNetwork为false。
         */
        boolean isGpsWork = false; //判断GPS定位是否启动
        boolean isNetwork = false; //判断网络定位是否启动
        //通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        isGpsWork = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        isNetwork = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        ALog.Log("isGpsWork: "+isGpsWork+" isNetwork: "+isNetwork);
        return (isGpsWork || isNetwork);
    }

    /**
     * 判断是否启动全部网络连接，包括WIFI和流量
     *
     * @param context 全局信息接口
     * @return 是否连接到网络
     */
    public boolean isNetworkConnected(Context context) {

        if (context != null) {

            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }

        }
        return false;
    }

    /**
     * 判断是否启动WIFI连接
     *
     * @param context 全局信息接口
     * @return 是否连接到WIFI
     */
    public boolean isWifiConnected(Context context) {

        if (context != null) {

            WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

            if (wifi != null) {
                return wifi.isWifiEnabled();
            }

        }

        return false;
    }

    /**
     * 跳转WIFI服务界面
     *
     * @param context 全局信息接口
     */
    public void gotoWifiServiceSettings(Context context) {
        final Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
