package com.example.lenovo.five_version1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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



    private ViewPager viewPager;
    private RecyclerView list, theaterlist;
    private TabLayout tabLayout;

    public void findview() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

    }

    private View view1,view2;
    private List<View> viewList;//view数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findview();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.tab_item, null);
        view2 = inflater.inflate(R.layout.theater_item,null);

        list = (RecyclerView) view1.findViewById(R.id.movielist);
        theaterlist = (RecyclerView) view2.findViewById(R.id.theaterlist);
        setRecyclerview();
        setTheaterRecyclerview();

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
       // viewPager.setCurrentItem(1);

    }
    public void setRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);
        ArrayList<FilmCard> items = new ArrayList<FilmCard>();
        int sz = 6;
        for (int i = 0; i < sz; i++) {
            FilmCard tmp = new FilmCard("神奇女侠", "动作", "xxx", "7.8", R.drawable.test);
            items.add(tmp);
        }
        CardAdapter sa = new CardAdapter(MainPage.this, items);
        sa.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, FilmCard item) {
                Intent t = new Intent(MainPage.this, FilmActivity.class);
                // t.putExtra("name", data.get(i).get("name"));
                //  t.putStringArrayListExtra("have", have);
                Log.e("ok", "ok");
                startActivity(t);
            }
        });
        list.setAdapter(sa);
    }

    public void setTheaterRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(MainPage.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        theaterlist.setLayoutManager(manager);
        ArrayList<CinemaCard > items = new ArrayList<CinemaCard >();
        int sz = 6;
        for (int i = 0; i < sz; i++) {
            CinemaCard tmp = new CinemaCard("中影国际影城北京昌平永旺店", "北京市昌平区北清路1号", "010-88177970", R.drawable.theater);
            items.add(tmp);
        }
        CinemaAdapter sa = new CinemaAdapter(MainPage.this, items);
        sa.setOnItemClickListener(new CinemaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CinemaCard item) {
                Intent t = new Intent(MainPage.this, TheaterActivity.class);
                Log.e("ok", "ok");
                startActivity(t);
            }
        });
        theaterlist.setAdapter(sa);
    }
}
