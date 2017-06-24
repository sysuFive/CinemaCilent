package com.example.lenovo.five_version1.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.controller.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Mytickets extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView orderList;
    ArrayList<HashMap<String, Object>> orderData;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytickets);
        findViews();
        getMyOrder();
    }

    private void findViews() {
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        orderList = (ListView) findViewById(R.id.order_list);
    }

    private void getMyOrder() {
        orderData = new ArrayList<>();
        String url = Controller.SERVER + Controller.MYORDER;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray orders = jsonObject.getJSONArray("message");
                        for (int i = 0; i < orders.length(); ++i) {
                            HashMap<String, Object> tmp = new HashMap<>();
                            JSONObject one  = orders.getJSONObject(i);
                            long time = one.getLong("time");
                            String strTime = Controller.convertTime("" + time);
                            JSONArray seats = new JSONArray(one.getString("sitting"));
                            String sit = "";
                            for (int j = 0; j < seats.length(); ++j) {
                                JSONObject seat = seats.getJSONObject(j);
                                int x = seat.getInt("x");
                                int y = seat.getInt("y");
                                sit += y + "排" + x + "座\t";
                            }
                            double price = one.getDouble("price");
                            int filmSessionId = one.getInt("filmSessionId");
                            int orderStatus = one.getInt("status");
                            String sta = "已完成";
                            if (orderStatus == 0)
                                sta = "未支付";
                            if (orderStatus == 2)
                                sta = "已过期";
                            tmp.put("filmName", "");
                            tmp.put("time", strTime);
                            tmp.put("cinemaName", "");
                            tmp.put("hall", "");
                            tmp.put("seats", sit);
                            tmp.put("price", price);
                            tmp.put("status", sta);
                            orderData.add(tmp);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                    sa = new SimpleAdapter(Mytickets.this, orderData, R.layout.order_item,
                            new String[]{"filmName", "time", "cinemaName", "hall", "sit", "price", "status"},
                            new int[]{R.id.film_name, R.id.time, R.id.cinema_name, R.id.hall_name, R.id.seats, R.id.price, R.id.status});
                    orderList.setAdapter(sa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.GET, url,
                new HashMap<String, String>(), listener, cookie);
    }

}
