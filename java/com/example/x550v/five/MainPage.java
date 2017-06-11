package com.example.x550v.five;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    int page = 0;

    SharedPreferences sharedPreferences;
    int MODE = 0;
    int FILM = 0;
    int CINEMA = 1;

    private ViewPager viewPager;
    private RecyclerView list, theaterlist;
    private TabLayout tabLayout;
    private  Button tolocate;
    private TextView position;
    private View view1,view2;
    private List<View> viewList;//view数组
    ArrayList<HashMap<String, Object>> filmData, cinemaData;
    ArrayList<FilmCard> cards;
    ArrayList<CinemaCard> theatercards;
    private FloatingActionButton fab;

    public void findview() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tolocate = (Button) findViewById(R.id.tolocate);
        position = (TextView) findViewById(R.id.pos);
        locationManager = (LocationManager) getSystemService((LOCATION_SERVICE));
        filmData = new ArrayList<>();
        cinemaData = new ArrayList<>();
        cards = new ArrayList<>();
        theatercards = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
    }
    public void init() {
        getFilms();
        getCinemas();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findview();
        getCity();
        init();
        setViewPager();
        setlistener();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
      //  System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        Toast.makeText(MainPage.this,df.format(new Date()),Toast.LENGTH_LONG);
        Log.e("tm",df.format(new Date())+" " );

    }

    public  void setlistener() {
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity();
            }
        });

    }

    public void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.tab_item, null);
        view2 = inflater.inflate(R.layout.theater_item,null);

        list = (RecyclerView) view1.findViewById(R.id.movielist);
        theaterlist = (RecyclerView) view2.findViewById(R.id.theaterlist);


        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);

        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setupWithViewPager(viewPager);

        PagerAdapter pagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(viewList.get(position));


                return viewList.get(position);
            }
        };

        viewPager.setAdapter(pagerAdapter);
        tabLayout.getTabAt(0).setText("正在上映");
        tabLayout.getTabAt(1).setText("影院");
        int type = sharedPreferences.getInt("mode", 0);
        viewPager.setCurrentItem(type);
        if (type == CINEMA) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("mode", FILM);
            editor.apply();
        }
    }

    public void setRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        CardAdapter sa = new CardAdapter(MainPage.this, cards);
        sa.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
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
                startActivity(intent);
            }
        });
        list.setAdapter(sa);
    }

    public void setTheaterRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        theaterlist.setLayoutManager(manager);

        CinemaAdapter sa = new CinemaAdapter(MainPage.this, theatercards);
        sa.setOnItemClickListener(new CinemaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CinemaCard item) {
                Intent intent = new Intent(MainPage.this, TheaterActivity.class);
                Map<String, Object> one  = cinemaData.get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("CinemaId", (int)one.get("CinemaId"));
                editor.putString("CinemaName", one.get("name").toString());
                editor.putString("location", one.get("location").toString());
                editor.apply();
                startActivity(intent);
            }
        });
        theaterlist.setAdapter(sa);


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
        String url = urlHead + latitude + "," + longitude + urlEnd;
        Map<String, String> params = new HashMap<>();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
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
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.GET, url, params, listener);
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

    private void getFilms() {
        final Map<String, String> params = new HashMap<>();
        // TODO: 2017/6/11 delete
        cityCode = 1;
        params.put("citycode", cityCode + "");
        String url = Controller.SERVER + Controller.FILM;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
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
                        JSONArray films = response.getJSONArray("message");
                        for (int i = 0; i < films.length(); ++i) {
                            JSONObject one = (JSONObject) films.get(i);
                            HashMap<String, Object> tmp = new HashMap<>();
                            String name = one.getString("name");
                            String actors = one.getString("actor");
                            String rate = one.getString("score");
                            String type = one.getString("category");
                            tmp.put("filmId", one.get("id"));
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
                            int imgRid = R.drawable.test;
                            tmp.put("img", imgRid);
                            filmData.add(tmp);
                            cards.add(new FilmCard(name, type, actors, rate, imgRid));
                        }
                        setRecyclerview();
                    } else {
                        Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.POST, url, params, listener);
    }

    private void getCinemas() {
        final Map<String, String> params = new HashMap<>();
        // TODO: 2017/6/11 delete
        cityCode = 0;
        longitude = 0;
        latitude = 0;
        page = 0;
        params.put("citycode", "" + cityCode);
        params.put("longtitude", "" + longitude);
        params.put("latitude", "" + latitude);
        params.put("page", "" + page);
        String url = Controller.SERVER + Controller.CINEMA;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
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
                            ++page;
                            theatercards.add(new CinemaCard(name, address, phone, rid));
                        }
                        setTheaterRecyclerview();
                    } else {
                        Toast.makeText(MainPage.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.POST, url, params,listener);
    }

}
