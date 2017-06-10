package com.example.lenovo.five_version1;


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
