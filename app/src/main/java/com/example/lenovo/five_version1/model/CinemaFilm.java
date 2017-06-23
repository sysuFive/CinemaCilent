package com.example.lenovo.five_version1.model;

/**
 * Created by X550V on 2017/6/10.
 */

public class CinemaFilm {
    int filmId, rid;
    public CinemaFilm(int filmId, int rid) {
        this.filmId = filmId;
        this.rid = rid;
    }

    public int getRid() {
        return rid;
    }

    public int getFilmId() {
        return filmId;
    }
}
