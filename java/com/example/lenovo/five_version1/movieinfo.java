package com.example.lenovo.five_version1;

import android.net.Uri;

/**
 * Created by lenovo on 2017/6/7.
 */
public class movieinfo {
    private Uri img;

    public movieinfo(Uri i) {

        img = i;
    }

    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
        this.img = img;
    }


}
