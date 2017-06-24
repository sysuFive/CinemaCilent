package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.controller.Controller;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FilmActivity extends AppCompatActivity {
    private KenBurnsView img;
    private TextView detail_release_info_text, detail_type_text, detail_director_text,
            detail_rating_info_text, detail_starring_text,detail_story_brief_text;
    public SimpleRatingBar  ratebar;
    private Button buy, back;
    public FloatingActionButton fab;
    private CollapsingToolbarLayout ctbl;
    private Toolbar tb;
    SharedPreferences sharedPreferences;
    int FILM = 0;
    int CINEMA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);
        Intent intent = getIntent();
        findViews();
        setListeners();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) return;
            init(bundle);
        }
    }

    public void findViews() {
        detail_release_info_text = (TextView) findViewById(R.id.detail_release_info_text);
        detail_type_text = (TextView) findViewById(R.id.detail_type_text);
        detail_starring_text = (TextView) findViewById(R.id.detail_starring_text);
        detail_story_brief_text = (TextView) findViewById(R.id.detail_story_brief_text);
        detail_rating_info_text = (TextView) findViewById(R.id.detail_rating_info_text);
        detail_director_text= (TextView) findViewById(R.id.detail_director_text);
        back = (Button)findViewById(R.id.backbtn);
        ctbl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        tb = (Toolbar) findViewById(R.id.toolbar);
        ratebar = (SimpleRatingBar) findViewById(R.id.detail_rating_bar);
        ratebar.setNumberOfStars(5);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        buy = (Button) findViewById(R.id.buy);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        img = (KenBurnsView) findViewById(R.id.backdrop);
    }

    public void init(Bundle bundle) {
        String filmName = bundle.getString("filmName");
        String filmActor = "主演：" + bundle.getString("filmActor");
        String filmRate = bundle.getString("filmRate");
        if (filmRate.length() > 3)
            filmRate = filmRate.substring(0, 3);
        String filmType = bundle.getString("filmType");
        String publishTime = bundle.getString("publishTime");
        long pt = Long.parseLong(publishTime);
        Date d = new Date(pt);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        publishTime = formatter.format(d);
        getImg();
        float rate = Float.parseFloat(filmRate);
        detail_rating_info_text.setText(rate/2 + "分 / 5分");
        ratebar.setRating(rate/2);
        detail_release_info_text.setText(publishTime);
        detail_type_text.setText(filmType);
        detail_director_text.setText(bundle.getString("director"));
        detail_starring_text.setText(filmActor);
        detail_story_brief_text.setText("剧情"+bundle.getString("summary"));
        ctbl.setTitle(filmName);
        ctbl.setExpandedTitleColor(0xffffff);
        ctbl.setCollapsedTitleTextColor(0xffffff);

        tb.setTitle(filmName);
        tb.setTitleTextColor(0xffffff);
    }

    private void getImg() {
        int id  = sharedPreferences.getInt("filmId", -1);
        String url = Controller.SERVER + "/film/" + id + "/cover/0.jpg";
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("response error", volleyError.toString());
            }
        });
        RequestQueue queue =  Volley.newRequestQueue(FilmActivity.this);
        queue.add(request);
    }

    public void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(FilmActivity.this, MainPage.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", FILM);
                editor.apply();
                startActivity(t);
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(FilmActivity.this, MainPage.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("mode", CINEMA);
                editor.apply();
                startActivity(t);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(FilmActivity.this, FilmRemark.class);
                t.putExtra("filmName", tb.getTitle().toString());
                startActivity(t);
            }
        });

    }


}
