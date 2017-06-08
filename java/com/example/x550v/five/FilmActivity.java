package com.example.x550v.five;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilmActivity extends AppCompatActivity {
    private ImageView img;
    private TextView name, score, time,type,info;
    private Button buy;
    private RecyclerView recyclerView;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        findViews();
        setListeners();
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getExtras();
            String filmName = bundle.getString("filmName");
            String filmActor = bundle.getString("filmActor");
            String filmRate = bundle.getString("filmRate");
            String filmType = bundle.getString("filmType");
            String publishTime = bundle.getString("publishTime");
            String lastTime = bundle.getString("lastTime");
            String director = bundle.getString("director");
            String lang = bundle.getString("lang");
            int ImgRid = bundle.getInt("img");
            // TODO: 2017/6/8 set Views
        }
    }

    public void findViews() {
        img = (ImageView) findViewById(R.id.filmImg);
        name = (TextView) findViewById(R.id.fileName);
        score = (TextView) findViewById(R.id.score);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        info = (TextView) findViewById(R.id.info);
        buy = (Button) findViewById(R.id.buy);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    public void setListeners() {

    }

    public void setRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(FilmActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ArrayList<PersonInfo> persons = new ArrayList<>();
        // get person info
        FilmInfoAdapter adapter = new FilmInfoAdapter(FilmActivity.this, persons);
        recyclerView.setAdapter(adapter);
    }
}
