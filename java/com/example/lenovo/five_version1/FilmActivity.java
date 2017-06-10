package com.example.lenovo.five_version1;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;
import java.util.List;

public class FilmActivity extends AppCompatActivity {
    private ImageView img;
    private TextView detail_release_info_text, detail_type_text,
            detail_rating_info_text, detail_starring_text,detail_story_brief_text;
    public SimpleRatingBar  ratebar;
    private Button buy;
    public FloatingActionButton fab;
    private RecyclerView recyclerView;

    public void findview() {
        detail_release_info_text = (TextView) findViewById(R.id.detail_release_info_text);
        detail_type_text = (TextView) findViewById(R.id.detail_type_text);
        detail_starring_text = (TextView) findViewById(R.id.detail_starring_text);
        detail_story_brief_text = (TextView) findViewById(R.id.detail_story_brief_text);
        detail_rating_info_text = (TextView) findViewById(R.id.detail_rating_info_text);

        detail_release_info_text.setText("5月26日");
        detail_type_text.setText("动作 冒险");
        detail_starring_text.setText("xxx xxxx");
        detail_story_brief_text.setText("在很久很久以前....");
        detail_rating_info_text.setText("7.7");

        ratebar = (SimpleRatingBar) findViewById(R.id.detail_rating_bar);
        ratebar.setNumberOfStars(4);

        buy = (Button) findViewById(R.id.buy);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        findview();
        setListeners();

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(FilmActivity.this, TheaterActivity.class);
                startActivity(t);

            }
        });

    }

//    public void setRecycleView() {
//        LinearLayoutManager manager = new LinearLayoutManager(FilmActivity.this);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(manager);
//        ArrayList<PersonInfo> persons = new ArrayList<>();
//        // get person info
//        FilmInfoAdapter adapter = new FilmInfoAdapter(FilmActivity.this, persons);
//        recyclerView.setAdapter(adapter);
//    }
}
