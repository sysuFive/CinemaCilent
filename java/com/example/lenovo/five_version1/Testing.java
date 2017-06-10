package com.example.lenovo.five_version1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Testing extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        final Button bt = (Button) findViewById(R.id.moviename);
        final android.support.v7.widget.RecyclerView list = (android.support.v7.widget.RecyclerView)findViewById(R.id.list);
        LinearLayoutManager manager = new LinearLayoutManager(Testing.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(manager);
        ArrayList<FilmCard> items = new ArrayList<FilmCard>();
        int sz = 6;
        for (int i = 0; i < sz; i++) {
            FilmCard tmp = new FilmCard("神奇女侠", "动作", "xxx", "7.8", R.drawable.test);
            items.add(tmp);
        }
        CardAdapter sa = new CardAdapter(Testing.this, items);
        sa.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, FilmCard item) {
                Intent t = new Intent(Testing.this, FilmActivity.class);
                // t.putExtra("name", data.get(i).get("name"));
                //  t.putStringArrayListExtra("have", have);
                Log.e("ok", "ok");
                startActivity(t);
            }
        });
        list.setAdapter(sa);

    }
}
