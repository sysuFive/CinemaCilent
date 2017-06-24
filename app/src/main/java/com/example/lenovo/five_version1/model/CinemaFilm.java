package com.example.lenovo.five_version1.model;

import android.graphics.Bitmap;

/**
 * Created by X550V on 2017/6/10.
 */

public class CinemaFilm {
    int filmId;
    Bitmap img;
    public CinemaFilm(int filmId, Bitmap rid) {
        this.filmId = filmId;
        this.img = rid;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public int getFilmId() {
        return filmId;
    }
}
