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
            if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null)
                locationManager
                        .requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                                this);
            else if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
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
