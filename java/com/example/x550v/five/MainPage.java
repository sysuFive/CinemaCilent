package com.example.x550v.five;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainPage extends AppCompatActivity {

    private ListView list;
    private Button clickmovie, clickplace, clickself;
    private String bestProvider = LocationManager.NETWORK_PROVIDER;
    private LocationManager locationManager = null;
    private Location curLocation = null;
    double latitude = -1;
    double longitude = -1;
    SharedPreferences sharedPreferences;
    int MODE = 0;
    int FILM = 0;
    int CINEMA = 1;
    String urlHead = "http://apis.map.qq.com/ws/geocoder/v1/?location=";
    String urlEnd = "&key=34GBZ-4NOCO-FUHWJ-SPXD5-AZHGZ-TYFYN&get_poi=0";
    RequestQueue requestQueue = null;
    String address = "", city = "";
    int cityCode = 0;
    int page = 0;
    ArrayList<HashMap<String, Object>> filmData, cinemaData;

    public  void findview() {
        clickmovie = (Button) findViewById(R.id.clickmovie);
        clickplace = (Button) findViewById(R.id.clickplace);
        clickself = (Button) findViewById(R.id.clickself);
        list = (ListView)findViewById(R.id.movielist);
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        filmData = new ArrayList<>();
        cinemaData = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        MODE = sharedPreferences.getInt("mode", FILM);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findview();
        if (MODE == FILM)
            getFilms();
        else
            getCinemas();
        clickmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getFilms();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", FILM);
                editor.apply();
            }
        });

        clickplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCinemas();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", CINEMA);
                editor.apply();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (MODE == FILM) {
                    intent = new Intent(MainPage.this, FilmActivity.class);
                    Bundle bundle = new Bundle();
                    Map<String, Object> one  = filmData.get(position);
                    Set<String> keys = one.keySet();
                    for (String key : keys) {
                        bundle.putString(key, (String) one.get(key));
                    }
                    intent.putExtras(bundle);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("mode", CINEMA);
                    editor.apply();
                } else {
                    intent = new Intent(MainPage.this, MainPage.class);
                }
                startActivity(intent);
            }
        });

    }

    private void getFilms() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        String url = Controller.SERVER + Controller.FILM;
        final PostRequest request = new PostRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MainPage.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;

                            SimpleAdapter sa;
                            if (success) {
                                JSONObject film = response.getJSONObject("message");
                                JSONArray films = film.getJSONArray("Film");
                                for (int i = 0; i < films.length(); ++i) {
                                    JSONObject one = (JSONObject) films.get(i);
                                    HashMap<String, Object> tmp = new HashMap<>();
                                    tmp.put("filmId", one.get("filmId"));
                                    tmp.put("filmName", one.get("name"));
                                    tmp.put("filmActor", one.get("actor"));
                                    tmp.put("filmRate", one.get("score"));
                                    tmp.put("filmType", one.get("category"));
                                    tmp.put("publishTime", one.get("publishtime"));
                                    tmp.put("lastTime", one.get("lasttime"));
                                    tmp.put("director", one.get("director"));
                                    tmp.put("lang", "language");
                                    String imgSrc = one.getString("img");
                                    // TODO: 2017/6/8 get img
                                    int imgRid = R.mipmap.ic_launcher;
                                    tmp.put("img", imgRid);
                                    filmData.add(tmp);
                                }
                                sa = new SimpleAdapter(MainPage.this, filmData, R.layout.movieitems,
                                        new String[]  {"img","filmName", "filmType", "filmActor", "filmRate"},
                                        new int[] {R.id.movieimg,R.id.moviename, R.id.movieinfo, R.id.movieactor, R.id.movierate});
                            } else {
                                Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                sa = new SimpleAdapter(MainPage.this, filmData, R.layout.movieitems,
                                        new String[]  {},
                                        new int[] {});
                            }
                            list.setAdapter(sa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void getCinemas() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        params.put("citycode", "" + cityCode);
        params.put("longtitude", "" + longitude);
        params.put("latitude", "" + latitude);
        params.put("page", "" + page);
        String url = Controller.SERVER + Controller.CINEMA;
        PostRequest request = new PostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MainPage.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;
                            SimpleAdapter sa;
                            if (success) {
                                JSONObject film = response.getJSONObject("message");
                                JSONArray films = film.getJSONArray("Cinema");
                                for (int i = 0; i < films.length(); ++i) {
                                    JSONObject one = (JSONObject) films.get(i);
                                    HashMap<String, Object> tmp = new HashMap<>();
                                    // TODO: 2017/6/8 get cinema info
                                    tmp.put("name", one.get("name"));
                                    tmp.put("location", one.get("location"));
                                    cinemaData.add(tmp);
                                }
                                sa = new SimpleAdapter(MainPage.this, cinemaData, R.layout.theateritems,
                                        new String[]  {"name", "location"},
                                        new int[] {R.id.theatername, R.id.theateraddress});
                            } else {
                                Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                sa = new SimpleAdapter(MainPage.this, cinemaData, R.layout.theateritems,
                                        new String[]  {},
                                        new int[] {});
                            }
                            list.setAdapter(sa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }

    private void getUserInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        String url = Controller.SERVER + Controller.FILM;
        PostRequest request = new PostRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MainPage.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;
                            if (success) {

                            } else {
                                Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
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
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };
}
