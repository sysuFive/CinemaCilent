package com.example.lenovo.five_version1.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.five_version1.model.CinemaCard;
import com.example.lenovo.five_version1.controller.Controller;
import com.example.lenovo.five_version1.model.FilmCard;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.view.adapter.FilmAdapter;
import com.example.lenovo.five_version1.view.adapter.CinemaAdapter;
import com.example.lenovo.five_version1.view.service.CityService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


public class MainPage extends SlidingFragmentActivity {
    int cityCode = 0;
    int page = 0;
    int FILM = 0;
    int CINEMA = 1;

    private ViewPager viewPager;
    private RecyclerView list, theaterlist;
    private TabLayout tabLayout;
    private  Button tolocate;
    private TextView position;
    private View view1,view2;
    private List<View> viewList;//view数组
    private FloatingActionButton fab;

    ArrayList<HashMap<String, Object>> filmData, cinemaData;
    ArrayList<FilmCard> filmCards;
    ArrayList<CinemaCard> cinemaCards;
    CinemaAdapter cinemaAdapter;
    FilmAdapter filmAdapter;
    Bitmap origin;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findViews();
        getCity();
        init();
        setViewPager();
        setListener();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
    }


    public void findViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tolocate = (Button) findViewById(R.id.tolocate);
        position = (TextView) findViewById(R.id.pos);
        filmData = new ArrayList<>();
        cinemaData = new ArrayList<>();
        filmCards = new ArrayList<>();
        cinemaCards = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        origin = BitmapFactory.decodeResource(getResources(), R.drawable.none);
    }

    public void initLeftMenu() {
        MenuLeftFragment leftMenuFragment = new MenuLeftFragment();
        setBehindContentView(R.layout.left_menu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_left_menu_frame, leftMenuFragment).commit();
        SlidingMenu menu = getSlidingMenu();
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		menu.setBehindWidth()
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        // menu.setBehindScrollScale(1.0f);
        menu.setSecondaryShadowDrawable(R.drawable.shadow);

        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    public void init() {
        getFilms();
        getCinemas();
        initLeftMenu();
    }
    

    public  void setListener() {
        position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCity();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, SearchActivity.class);
                startActivity(intent);
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

    public void setFilmList() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        FilmAdapter sa = new FilmAdapter(MainPage.this, filmCards);
        sa.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, FilmCard item) {
                Intent intent = new Intent(MainPage.this, FilmActivity.class);
                Bundle bundle = new Bundle();
                Map<String, Object> one  = filmData.get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("filmId", (int)one.get("filmId"));
                editor.apply();
                Set<String> keys = one.keySet();
                for (String key : keys) {
                    bundle.putString(key, one.get(key).toString());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        list.setAdapter(sa);
        getFilmImg();
    }

    public void setCinemaList() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        theaterlist.setLayoutManager(manager);

        cinemaAdapter = new CinemaAdapter(MainPage.this, cinemaCards);
        cinemaAdapter.setOnItemClickListener(new CinemaAdapter.OnItemClickListener() {
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
        theaterlist.setAdapter(cinemaAdapter);
        getCinemaImg();
    }

    private void getCity() {
        cityCode = CityService.cityCode;
        position.setText(CityService.city);
    }

    private void getFilms() {
        final Map<String, String> params = new HashMap<>();
        params.put("citycode", "156" + CityService.cityCode);
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
                            rate = Float.parseFloat(rate)/2 +"";
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
                            int imgRid = R.drawable.none;
                            tmp.put("img", imgRid);
                            filmData.add(tmp);
                            filmCards.add(new FilmCard(name, type, actors, rate, origin));
                        }
                        setFilmList();
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
        params.put("citycode", "156" + CityService.cityCode);
        params.put("longtitude", "" + CityService.longitude);
        params.put("latitude", "" + CityService.latitude);
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
                            String name = one.getString("name");
                            String address = one.getString("address");
                            String phone = one.getString("phone");
                            tmp.put("name", name);
                            tmp.put("location", address);
                            tmp.put("phone", phone);
                            tmp.put("CinemaId", one.get("id"));
                            cinemaData.add(tmp);
                            cinemaCards.add(new CinemaCard(name, address, phone, origin));
                        }
                        setCinemaList();
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

    private void getFilmImg() {
        for (int i = 0; i < filmData.size(); ++i) {
            final HashMap<String, Object> one = filmData.get(i);
            final int id  = (int)one.get("filmId");
            String url = Controller.SERVER + "/film/" + id + "/cover/0.jpg";
            final int finalI = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    filmCards.get(finalI).setImg(bitmap);
                    filmAdapter.notifyDataSetChanged();
                }
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("response error", volleyError.toString());
                }
            });
            RequestQueue queue =  Volley.newRequestQueue(MainPage.this);
            queue.add(request);
        }
    }

    private void getCinemaImg() {
        for (int i = 0; i < cinemaData.size(); ++i) {
            final HashMap<String, Object> one = cinemaData.get(i);
            final int id  = (int)one.get("CinemaId");
            String url = Controller.SERVER + "/cinema/" + id + "/cover/0.jpg";
            final int finalI = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    cinemaCards.get(finalI).setBitmap(bitmap);
                    cinemaAdapter.notifyDataSetChanged();
                }
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("response error", volleyError.toString());
                }
            });
            RequestQueue queue =  Volley.newRequestQueue(MainPage.this);
            queue.add(request);
        }
    }

}
