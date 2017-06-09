package com.example.lenovo.five_version1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TheaterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public ListView sessionlist;
    public void findview()  {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sessionlist = (ListView) findViewById(R.id.sessionlist);
    }
    public void setRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(TheaterActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ArrayList<movieinfo> movies = new ArrayList<>();
        // get movie info
        movieinfoAdapter adapter = new movieinfoAdapter(TheaterActivity.this, movies);
        recyclerView.setAdapter(adapter);
    }
    public void setsessionlist() {
        //get sessions
        int sz = 5;
        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
        for (int i = 0; i < sz; i++) {
            HashMap<String, Object> tmp=new HashMap<String, Object>();
            tmp.put("start", "19:00");
            tmp.put("end", "21:22");
            tmp.put("lang", "英语");
            tmp.put("whichsess", "5号厅");
            tmp.put("price", "￥41");
            data.add(tmp);
        }

        final SimpleAdapter sa = new SimpleAdapter(TheaterActivity.this, data, R.layout.sessionitems,
                new String[]  {"start","end", "lang", "whichsess", "price"},
                new int[] {R.id.start,R.id.end, R.id.lang, R.id.whichsess, R.id.price});
        sessionlist.setAdapter(sa);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        findview();
        setRecycleView();
        setsessionlist();
        sessionlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //send message
                Intent t = new Intent(TheaterActivity.this, SelectSeat.class);
                startActivity(t);

            }
        });
    }
}
