package com.example.x550v.five;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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

    private RecyclerView list;
    private TextView position;
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
    String address = "", city = "";
    int cityCode = 0;
    int page = 0;
    ArrayList<HashMap<String, Object>> filmData, cinemaData;
    Map<Integer, String[]> filmUrls, cinemaUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findViews();
        getCity();
        if (MODE == FILM)
            getFilms();
        else
            getCinemas();
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCity();
            }
        });
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

//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent;
//                if (MODE == FILM) {
//                    intent = new Intent(MainPage.this, FilmActivity.class);
//                    Bundle bundle = new Bundle();
//                    Map<String, Object> one  = filmData.get(position);
//                    Set<String> keys = one.keySet();
//                    for (String key : keys) {
//                        bundle.putString(key, one.get(key).toString());
//                    }
//                    intent.putExtras(bundle);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("mode", CINEMA);
//                    editor.putInt("filmId", (int)one.get("filmId"));
//                    editor.apply();
//                } else {
//                    intent = new Intent(MainPage.this, TheaterActivity.class);
//                    Map<String, Object> one  = filmData.get(position);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("CinemaId", (int)one.get("CinemaId"));
//                    editor.apply();
//                }
//                startActivity(intent);
//            }
//        });
    }

    private void getFilmsImgUrl(final Integer FilmId) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        String url = Controller.SERVER + Controller.FILMPIC + FilmId;
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
                            if (success) {
                                String[] paths = (String[])response.get("message");
                                filmUrls.put(FilmId, paths);
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
        String cookie = sharedPreferences.getString("Cookie", "");
//        if (!cookie.equals(""))
//            request.setSendCookie(cookie);
        requestQueue.add(request);
    }

    private void getFilmsImg(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {

            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    public void findViews() {
        position = (TextView) findViewById(R.id.currentpos);
        clickmovie = (Button) findViewById(R.id.clickmovie);
        clickplace = (Button) findViewById(R.id.clickplace);
        clickself = (Button) findViewById(R.id.clickself);
        list = (RecyclerView)findViewById(R.id.movielist);
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        filmData = new ArrayList<>();
        cinemaData = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        MODE = sharedPreferences.getInt("mode", FILM);
        filmUrls = new HashMap<>();
        cinemaUrls = new HashMap<>();
    }

    private void getFilms() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        params.put("citycode", cityCode + "");
        String url = Controller.SERVER + Controller.FILM;
        final PostRequest request = new PostRequest(Request.Method.POST, url, params,
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
                            CardAdapter ca;
                            ArrayList<FilmCard> cards = new ArrayList<>();
                            if (success) {
                                JSONArray films = response.getJSONArray("message");
                                for (int i = 0; i < films.length(); ++i) {
                                    JSONObject one = (JSONObject) films.get(i);
                                    HashMap<String, Object> tmp = new HashMap<>();
                                    String name = one.getString("name");
                                    String actors = one.getString("actor");
                                    String rate = one.getString("score");
                                    String type = one.getString("category");
                                    int filmId = one.getInt("id");
                                    getFilmsImgUrl(filmId);
                                    tmp.put("filmId", filmId);
                                    tmp.put("filmName", name);
                                    tmp.put("filmActor", actors);
                                    tmp.put("filmRate", rate);
                                    tmp.put("filmType", type);
                                    tmp.put("publishTime", one.get("publishTime"));
                                    tmp.put("lastTime", one.get("lastTime"));
                                    tmp.put("director", one.get("director"));
                                    tmp.put("lang", "language");
                                    tmp.put("summary", one.get("summary"));
                                    // TODO: 2017/6/8 get img
//                                    String imgSrc = one.getString("img");
                                    int imgRid = R.mipmap.ic_launcher;
                                    tmp.put("img", imgRid);
                                    filmData.add(tmp);
                                    cards.add(new FilmCard(name, type, actors, rate, imgRid));
                                }
                            } else {
                                Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            ca = new CardAdapter(MainPage.this, cards);
                            ca.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position, FilmCard item) {
                                    Intent intent = new Intent(MainPage.this, FilmActivity.class);
                                    Bundle bundle = new Bundle();
                                    Map<String, Object> one  = filmData.get(position);
                                    Set<String> keys = one.keySet();
                                    for (String key : keys) {
                                        bundle.putString(key, one.get(key).toString());
                                    }
                                    intent.putExtras(bundle);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("mode", CINEMA);
                                    editor.putInt("filmId", (int)one.get("filmId"));
                                    editor.apply();
                                    // t.putExtra("name", data.get(i).get("name"));
                                    //  t.putStringArrayListExtra("have", have);
                                    Log.e("ok", "ok");
                                    startActivity(intent);
                                }
                            });
                            list.setAdapter(ca);
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
        String cookie = sharedPreferences.getString("Cookie", "");
//        if (!cookie.equals(""))
//            request.setSendCookie(cookie);
        requestQueue.add(request);
    }

    private void getCinemas() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        cityCode = 0;
        longitude = 0;
        latitude = 0;
        page = 0;
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
                            CinemaAdapter ca;
                            ArrayList<CinemaCard> cards = new ArrayList<>();
                            if (success) {
                                JSONArray films = response.getJSONArray("message");
                                for (int i = 0; i < films.length(); ++i) {
                                    JSONObject one = (JSONObject) films.get(i);
                                    HashMap<String, Object> tmp = new HashMap<>();
                                    // TODO: 2017/6/8 get cinema info
                                    String name = one.getString("name");
                                    String address = one.getString("address");
                                    String phone = one.getString("phone");
                                    tmp.put("name", name);
                                    tmp.put("location", address);
                                    tmp.put("phone", phone);
                                    tmp.put("CinemaId", one.get("id"));
                                    cinemaData.add(tmp);
                                    // TODO: 2017/6/10  get img
                                    int rid = R.drawable.test;
                                    cards.add(new CinemaCard(name, address, phone, rid));
                                    ++page;
                                }
                            } else {
                                Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            ca = new CinemaAdapter(MainPage.this, cards);
                            ca.setOnItemClickListener(new CinemaAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position, CinemaCard item) {
                                    Intent intent = new Intent(MainPage.this, TheaterActivity.class);
                                    Map<String, Object> one  = cinemaData.get(position);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("CinemaId", (int)one.get("CinemaId"));
                                    editor.apply();
                                    startActivity(intent);
                                }
                            });
                            list.setAdapter(ca);
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

    private void getCity() {
        getLatLon();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = urlHead + latitude + "," + longitude + urlEnd;
        Map<String, String> params = new HashMap<>();
        PostRequest request = new PostRequest(Request.Method.GET, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MainPage.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
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
                            position.setText(city);
                            String code = ((JSONObject) results.get("ad_info")).getString("adcode");
                            cityCode = Integer.parseInt(code);
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
