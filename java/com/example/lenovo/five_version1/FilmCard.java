package com.example.lenovo.five_version1;

/**
 * Created by lenovo on 2017/6/9.
 */



import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

public class FilmCard {
    String name, type, actors, rate;
    int img;
    FilmCard(String n, String t, String a, String r, int id) {
        name = n;
        type = t;
        actors = a;
        rate = r;
        img = id;
    }

    public int getImg() {
        return img;
    }

    public String getActors() {
        return actors;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getType() {
        return type;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setType(String type) {
        this.type = type;
    }
}
