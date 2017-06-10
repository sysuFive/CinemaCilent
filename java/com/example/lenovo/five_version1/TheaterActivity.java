package com.example.lenovo.five_version1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TheaterActivity extends AppCompatActivity {
    private RecyclerView movieslist;
    public ListView sessionlist;
    public void findview() {
        movieslist = (RecyclerView) findViewById(R.id.movieintheaterlist);
        sessionlist = (ListView) findViewById(R.id.sessionlist);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        findview();
        setRecyclerview();

        sessionlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent t = new Intent(TheaterActivity.this, SelectSeat.class);
//                t.putExtra("name", data.get(i).get("name"));
//                t.putStringArrayListExtra("have", have);
                startActivity(t);
            }
        });

    }
    public void setRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(TheaterActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieslist.setLayoutManager(manager);
        ArrayList<CinemaFilm> items = new ArrayList<CinemaFilm>();
        int sz = 6;
        for (int i = 0; i < sz; i++) {
            CinemaFilm tmp = new CinemaFilm(i, R.drawable.test);
            items.add(tmp);
        }
        CinemaFilmAdapter sa = new CinemaFilmAdapter(TheaterActivity.this, items);
        sa.setOnItemClickListener(new CinemaFilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CinemaFilm item) {
                    //TODO
                    int sz = 6;
                List<Map<String, Object>> data= new ArrayList<>();

                for (int i = 0; i < sz; i++) {
                    Map<String, Object> tmp = new LinkedHashMap<>();
                    tmp.put("start", "21.00");
                    tmp.put("end", "21.30");
                    tmp.put("lang", "English");
                    tmp.put("whichsess", "9号厅");
                    tmp.put("price", "41");
                    data.add(tmp);
                }
                SimpleAdapter sl = new SimpleAdapter(TheaterActivity.this, data, R.layout.sessionitems,
                        new String[]  {"start", "end", "lang", "whichsess", "price"},
                        new int[] {R.id.start, R.id.name, R.id.lang, R.id.whichsess, R.id.price});
                sessionlist.setAdapter(sl);

            }
        });
        movieslist.setAdapter(sa);
    }

}
