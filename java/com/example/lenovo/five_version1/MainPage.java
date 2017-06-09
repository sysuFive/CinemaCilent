package com.example.lenovo.five_version1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity {
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


    public Button clickmovie, clickplace, clickself, locate;
    public ListView list;
    public TextView currentpos;
    public int which = 0;
    public  void findview() {
        clickmovie = (Button) findViewById(R.id.clickmovie);
        clickplace = (Button) findViewById(R.id.clickplace);
        clickself = (Button) findViewById(R.id.clickself);
        list = (ListView)findViewById(R.id.movielist);
        locate = (Button) findViewById(R.id.locate);
        currentpos = (TextView) findViewById(R.id.currentpos);
    }

    public void setlistener() {
        clickmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDATA
                //
                //
                //添加图标方式 http://blog.csdn.net/xcysuccess3/article/details/7261116
                //
                //
                int sz = 5;
                ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
                for (int i = 0; i < sz; i++) {
                    HashMap<String, Object> tmp=new HashMap<String, Object>();
                    tmp.put("moviepic", R.drawable.test);
                    tmp.put("moviename", "神奇女侠");
                    tmp.put("movieinfo", "动作 冒险");
                    tmp.put("movieactor", "主演 xxx xxx");
                    tmp.put("moviegrade", "7.7");
                    data.add(tmp);
                }
                which = 0;

                final  SimpleAdapter sa = new SimpleAdapter(MainPage.this, data, R.layout.movieitems,
                        new String[]  {"moviepic","moviename", "movieinfo", "movieactor", "moviegrade"},
                        new int[] {R.id.movieimg,R.id.moviename, R.id.movieinfo, R.id.movieactor, R.id.moviegrade});
                list.setAdapter(sa);
            }
        });

        clickplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getDATA
                //
                //
                //添加图标方式 http://blog.csdn.net/xcysuccess3/article/details/7261116
                //
                //
                int sz = 5;
                ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
                for (int i = 0; i < sz; i++) {
                    HashMap<String, Object> tmp=new HashMap<String, Object>();
                    // tmp.put("moviepic", R.mipmap.ic_launcher);
                    tmp.put("theatername", "电影院名称");
                    tmp.put("theateraddress", "电影院地址。。。。。。");
                    data.add(tmp);

                }
                which = 1;
                final  SimpleAdapter sa = new SimpleAdapter(MainPage.this, data, R.layout.theateritems,
                        new String[]  {/*"moviepic"*/"theatername", "theateraddress"},
                        new int[] {/*R.id.moviepic,*/ R.id.theatername, R.id.theateraddress});
                list.setAdapter(sa);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (which == 0) {
                    Intent t = new Intent(MainPage.this, FilmActivity.class);
                    // t.putExtra("name", data.get(i).get("name"));
                    //  t.putStringArrayListExtra("have", have);
                    startActivity(t);
                } else if (which == 1) {
                    Intent t = new Intent(MainPage.this, TheaterActivity.class);
                    // t.putExtra("name", data.get(i).get("name"));
                    //  t.putStringArrayListExtra("have", have);
                    startActivity(t);
                }
            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCity();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        findview();
        setlistener();
        setCity();

    }

    private void setCity() {
        getLatLon();
        if (curLocation != null) {
            String url = urlHead + latitude + "," + longitude + urlEnd;
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            JSONObject json = new JSONObject();
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.GET , url, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
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
                                currentpos.setText(city);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("response error", error.getMessage());
                }
            });
            requestQueue.add(jsonRequest);
            requestQueue.start();
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
        return !(ActivityCompat.checkSelfPermission(MainPage.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainPage.this,
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
            Toast.makeText(MainPage.this, provider + " enable", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {}
    };
}
