package com.example.x550v.five;

import android.net.Uri;

/**
 * Created by X550V on 2017/6/6.
 */

public class PersonInfo {
    private String name, job;
    private Uri img;

    public  PersonInfo(String n, String j, Uri i) {
        name = n;
        job = j;
        img = i;
    }

    public Uri getImg() {
        return img;
    }

    public String getJob() {
        return job;
    }

    public String getName() {
        return name;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setName(String name) {
        this.name = name;
    }
}
