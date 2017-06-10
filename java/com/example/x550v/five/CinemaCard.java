package com.example.x550v.five;

/**
 * Created by X550V on 2017/6/10.
 */

public class CinemaCard {
    String name, address, phone;
    int rid;

    public CinemaCard(String name, String address, String phone, int rid) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.rid = rid;
    }

    public int getRid() {
        return rid;
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
