package com.example.lenovo.five_version1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity {


    public Button clickmovie, clickplace, clickself;
    public  void findview() {
        clickmovie = (Button) findViewById(R.id.clickmovie);
        clickplace = (Button) findViewById(R.id.clickplace);
        clickself = (Button) findViewById(R.id.clickself);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        findview();
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
                    tmp.put("moviepic", R.drawable.apple);
                    tmp.put("moviename", "电影");
                    tmp.put("movieinfo", "简介");
                    tmp.put("movieactor", "主演");
                    tmp.put("movierate", "评分");
                    data.add(tmp);
                }


                ListView list = (ListView)findViewById(R.id.movielist);
                final  SimpleAdapter sa = new SimpleAdapter(MainPage.this, data, R.layout.movieitems,
                        new String[]  {"moviepic","moviename", "movieinfo", "movieactor", "movierate"},
                        new int[] {R.id.movieimg,R.id.moviename, R.id.movieinfo, R.id.movieactor, R.id.movierate});
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


                ListView list = (ListView)findViewById(R.id.movielist);
                final  SimpleAdapter sa = new SimpleAdapter(MainPage.this, data, R.layout.theateritems,
                        new String[]  {/*"moviepic"*/"theatername", "theateraddress"},
                        new int[] {/*R.id.moviepic,*/ R.id.theatername, R.id.theateraddress});
                list.setAdapter(sa);
            }
        });

    }
}
