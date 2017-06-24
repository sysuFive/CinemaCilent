package com.example.lenovo.five_version1.view.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import static java.security.AccessController.getContext;

public class Mycomments extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView remark_list;
    ArrayList<HashMap<String, Object>> remarkData;
    SimpleAdapter sa = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycomments);
        findViews();
        getMyFilmRemarks();
    }

    void findViews() {
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        remark_list = (ListView) findViewById(R.id.remark_list);
    }

    private void getMyFilmRemarks() {
        remarkData = new ArrayList<>();
        String url = Controller.SERVER + Controller.FILMREMARKSELF;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray remarks = jsonObject.getJSONArray("message");
                        for (int i = 0; i < remarks.length(); ++i) {
                            HashMap<String, Object> one = new HashMap<>();
                            JSONObject remark = remarks.getJSONObject(i);
                            int filmId = remark.getInt("filmId");
                            String content = remark.getString("content");
                            long time = remark.getLong("time");
                            String strTime = Controller.convertTime(time + "");
                            one.put("name", filmId);
                            one.put("content", content);
                            one.put("time", strTime);
                            remarkData.add(one);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                    sa = new SimpleAdapter(Mycomments.this, remarkData, R.layout.film_remark_item,
                            new String[]{"name", "time", "content"},
                            new int[] {R.id.username, R.id.remark_time, R.id.remark});
                    remark_list.setAdapter(sa);
                    getFilmName();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.GET, url,
                new HashMap<String, String>(), listener, cookie);
    }

    private void getFilmName() {
        for (int i = 0;  i < remarkData.size(); ++i) {
            final HashMap<String, Object> one = remarkData.get(i);
            int id = (int)one.get("name");
            String url = Controller.SERVER + Controller.FINDFILM + id;
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            one.put("name", jsonObject.getJSONObject("message").getString("name"));
                            sa.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Mycomments.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Controller.sendRequestWithGET(Mycomments.this, url, listener);
        }
    }
}
