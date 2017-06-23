package com.example.x550v.five.view.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.x550v.five.R;
import com.example.x550v.five.controller.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UserInfo extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        getMyOrder();
        getWalletInfo();
        getMyFilmRemarks();
    }

    private void getMyFilmRemarks() {
        String url = Controller.SERVER + Controller.FILMREMARKSELF;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray remarks = jsonObject.getJSONArray("message");
                        for (int i = 0; i < remarks.length(); ++i) {
                            JSONObject remark = remarks.getJSONObject(i);
                            int filmId = remark.getInt("filmId");
                            String content = remark.getString("content");
                            long time = remark.getLong("time");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.GET, url,
                new HashMap<String, String>(), listener, cookie);
    }

    private void getMyOrder() {
        String url = Controller.SERVER + Controller.MYORDER;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray orders = jsonObject.getJSONArray("message");
                        for (int i = 0; i < orders.length(); ++i) {
                            JSONObject one  = orders.getJSONObject(i);
                            long time = one.getLong("time");
                            JSONArray seats = one.getJSONArray("sitting");
                            for (int j = 0; j < seats.length(); ++j) {
                                JSONObject seat = seats.getJSONObject(j);
                                int x = seat.getInt("x");
                                int y = seat.getInt("y");
                            }
                            double price = one.getDouble("price");
                            int filmSessionId = one.getInt("filmSessionId");
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.GET, url,
                new HashMap<String, String>(), listener, cookie);
    }

    private void getWalletInfo() {
        String url = Controller.SERVER + Controller.WALLET;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        double balance = jsonObject.getJSONObject("message").getDouble("balance");
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.POST, url,
                new HashMap<String, String>(), listener, cookie);
    }


}
