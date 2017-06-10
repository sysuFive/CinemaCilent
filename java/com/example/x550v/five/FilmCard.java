package com.example.x550v.five;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by X550V on 2017/6/9.
 */

public class FilmCard {
    String name, type, actors, rate;
    int img;
    FilmCard(String n, String t, String a, String r, int id) {
        name = n;
        type = t;
        actors = a;
        rate = r;
        if (rate.length() > 3)
            rate = rate.substring(0, 3);
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
