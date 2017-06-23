package com.example.x550v.five.model;

import android.graphics.Bitmap;

public class CinemaCard {
    private String name, address, phone;
    private Bitmap bitmap;

    public CinemaCard(String name, String address, String phone, Bitmap bitmap) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
