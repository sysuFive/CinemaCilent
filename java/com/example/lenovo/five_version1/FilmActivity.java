package com.example.lenovo.five_version1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilmActivity extends AppCompatActivity {
    private ImageView img;
    private TextView name, score, time,type,info;
    private Button buy;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
      //  findViews();
     //   setListeners();
   //     setRecycleView();
    }

//    public void findViews() {
//        img = (ImageView) findViewById(R.id.filmImg);
//        name = (TextView) findViewById(R.id.fileName);
//        score = (TextView) findViewById(R.id.score);
//        time = (TextView) findViewById(R.id.time);
//        type = (TextView) findViewById(R.id.type);
//        info = (TextView) findViewById(R.id.info);
//        buy = (Button) findViewById(R.id.buy);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//    }

    public void setListeners() {

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(FilmActivity.this, TheaterActivity.class);
                startActivity(t);

            }
        });
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
