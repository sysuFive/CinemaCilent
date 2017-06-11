package com.example.x550v.five;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class FilmActivity extends AppCompatActivity {
    private ImageView img;
    private TextView name, score, time,type,info;
    private Button buy;
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
            if (bundle == null)
                return;
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
            String lastTime = bundle.getString("lastTime");
            float lt = Float.parseFloat(lastTime);
            int minu = (int)lt / 60;
            lastTime = minu + "分钟";
            String director = "导演：" + bundle.getString("director");
            String lang = "语言" + bundle.getString("lang");
            int ImgRid = bundle.getInt("img");
            // TODO: 2017/6/8 set Views
            name.setText(filmName);
            score.setText(filmRate);
            time.setText(publishTime);
            info.setText(lastTime);
            type.setText(filmType);
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
    }

    public void setListeners() {
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMainPage = new Intent(FilmActivity.this, MainPage.class);
                startActivity(toMainPage);
            }
        });
    }

}
