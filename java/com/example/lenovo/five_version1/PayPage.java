package com.example.lenovo.five_version1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class PayPage extends AppCompatActivity {
    public TextView remain_time, moviename, movielang, moviedate, theaterandsess, seat, price;
    public Button pay;
    public void findview() {
        remain_time = (TextView) findViewById(R.id.remain_time);
        moviename = (TextView) findViewById(R.id.moviename);
        movielang = (TextView) findViewById(R.id.movielang);
        moviedate = (TextView) findViewById(R.id.moviedate);
        theaterandsess = (TextView) findViewById(R.id.theateraddress);
        seat = (TextView) findViewById(R.id.seat);
        price = (TextView) findViewById(R.id.price);
        pay = (Button) findViewById(R.id.pay);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        findview();
    }
}
