package com.example.x550v.five;

/**
 * Created by X550V on 2017/6/10.
 */

public class CinemaFilm {
    int filmId, rid;
    CinemaFilm(int filmId, int rid) {
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
