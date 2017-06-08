package com.example.x550v.five;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SmallActivity extends AppCompatActivity {
    private String bestProvider = LocationManager.NETWORK_PROVIDER;
    private LocationManager locationManager = null;
    private Location curLocation = null;
    double latitude = -1;
    double longitude = -1;
    String urlHead = "http://apis.map.qq.com/ws/geocoder/v1/?location=";
    String urlEnd = "&key=34GBZ-4NOCO-FUHWJ-SPXD5-AZHGZ-TYFYN&get_poi=0";
    RequestQueue requestQueue = null;
    String address = "", city = "";
    int cityCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small);
        Button button = (Button) findViewById(R.id.button);
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        final TextView text = (TextView) findViewById(R.id.text);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://v.juhe.cn/weather/ip";
                final Map<String, String> params = new HashMap<>();
                params.put("ip", "58.215.185.154");
                params.put("dtype", "json");
                params.put("key", "1609c87b3cdb9cd0edd3fd5d3a744c4b");
                PostRequest request = new PostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(SmallActivity.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SmallActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
                requestQueue.add(request);
//                try {
//                    sendRequest();
//                    if (curLocation != null) {
//                        JSONObject response = Controller.response;
//                        analysisJson(response);
//                        if (response == null) {
//                            Toast.makeText(SmallActivity.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        try {
//                            JSONObject results = (JSONObject) response.get("result");
//                            if (results == null) {
//                                Log.e("get city error", "null");
//                                return;
//                            }
//                            address = results.getString("address");
//                            city = ((JSONObject) results.get("address_component")).getString("city");
//                            String code = ((JSONObject) results.get("ad_info")).getString("adcode");
//                            cityCode = Integer.parseInt(code);
//                            text.setText(city);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }

    public void findViews() {

    }

    public void setListeners() {

    }

    private void sendRequest() {
        getLatLon();
        if (curLocation != null) {
            String url = urlHead + latitude + "," + longitude + urlEnd;
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject json = new JSONObject();
            Controller controller = new Controller(requestQueue, url, json, Request.Method.GET);
            controller.execute("");
        }
    }

    private void analysisJson(JSONObject response) {
        // TODO: 2017/6/7  解析json格式
        if (response == null) {
            Log.e("response error", "null");
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


    // return true if PERMISSION_GRANTED else false
    private boolean checkPermission() {
        return !(ActivityCompat.checkSelfPermission(SmallActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SmallActivity.this,
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
        public void onProviderEnabled(String provider) {
            Toast.makeText(SmallActivity.this, provider + " enable", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {}
    };
}
