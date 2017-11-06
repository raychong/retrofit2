package com.pentagon.retrofitexample.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by RayChongJH on 9/2/17.
 */

public class LocationService extends Service implements LocationListener {

    private static final String TAG = "LocationSvc";
    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LNG = "location_lng";
    public static final String LOCATION_ACTION = "locationAction";
    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        try {
            solution2();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void solution1(){
        try{
            if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null)
                locationManager
                        .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                                this);
            else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager
                        .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                                this);
            }else{ Toast.makeText(this, "无法定位", Toast.LENGTH_SHORT).show();}
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void solution2(){
        try{
            String provider = "";
            // 获取所有可用的位置提供器
            List<String> providerList = locationManager.getProviders(true);
            if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                //优先使用gps
                System.out.println("[GPS provider] GPS");
                provider = LocationManager.GPS_PROVIDER;
            } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                System.out.println("[GPS provider] Network");
                provider = LocationManager.NETWORK_PROVIDER;
            } else {
                // 没有可用的位置提供器
                Toast.makeText(this, "没有位置提供器可供使用", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if(!provider.equalsIgnoreCase("")){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    // 显示当前设备的位置信息
                    String firstInfo = "第一次请求的信息";
//                    showLocation(location, firstInfo);
                } else {
                    String info = "无法获得当前位置";
                    Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
//                    positionText.setText(info);
                }

                // 更新当前位置

            }else{
                Toast.makeText(this, "provider is null", Toast.LENGTH_LONG)
                        .show();
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0,
                    this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Get the current position \n" + location);

        //通知Activity
        Intent intent = new Intent();
        intent.setAction(LOCATION_ACTION);
        intent.putExtra(LOCATION_LAT, location.getLatitude());
        intent.putExtra(LOCATION_LNG, location.getLongitude());
        sendBroadcast(intent);

        // 如果只是需要定位一次，这里就移除监听，停掉服务。如果要进行实时定位，可以在退出应用或者其他时刻停掉定位服务。
        locationManager.removeUpdates(this);
        stopSelf();
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
