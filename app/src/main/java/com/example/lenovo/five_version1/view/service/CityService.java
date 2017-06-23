package com.example.lenovo.five_version1.view.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.lenovo.five_version1.controller.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CityService extends Service {
    private String bestProvider = LocationManager.NETWORK_PROVIDER;
    private LocationManager locationManager = null;
    private Location curLocation = null;
    public  static double latitude = -1;
    public  static double longitude = -1;
    String urlHead = "http://apis.map.qq.com/ws/geocoder/v1/?location=";
    String urlEnd = "&key=34GBZ-4NOCO-FUHWJ-SPXD5-AZHGZ-TYFYN&get_poi=0";
    public static String address = "", city = "";
    public static int cityCode = 0;
    Context context;
    public  IBinder binder;
    public CityService() {}
    public CityService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService((LOCATION_SERVICE));
        setCity();
    }

    public class MyBinder extends Binder {
        public CityService getService() {
            return CityService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void getLatLon() {
        if (!checkPermission())
            return;
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
        locationManager.getLastKnownLocation(bestProvider);
        curLocation = locationManager.getLastKnownLocation(bestProvider);
        if (curLocation != null) {
            latitude = curLocation.getLatitude();
            longitude = curLocation.getLongitude();
        }
    }

    private void setCity() {
        getLatLon();
        String url = urlHead + latitude + "," + longitude + urlEnd;
        Map<String, String> params = new HashMap<>();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(context, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject results = (JSONObject) response.get("result");
                    if (results == null) {
                        Log.e("get city error", "null");
                        return;
                    }
                    address = results.getString("address");
                    city = ((JSONObject) results.get("address_component")).getString("city");
                    String code = ((JSONObject) results.get("ad_info")).getString("adcode");
                    cityCode = Integer.parseInt(code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(context, Request.Method.GET, url, params, listener);
    }

    private boolean checkPermission() {
        return !(ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!checkPermission())
                            return;
                        locationManager.getLastKnownLocation(bestProvider);
                        curLocation = locationManager.getLastKnownLocation(bestProvider);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            try {
                if (curLocation == null) {
                    if (!checkPermission())
                        return;
                    curLocation = locationManager.getLastKnownLocation(bestProvider);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

}
