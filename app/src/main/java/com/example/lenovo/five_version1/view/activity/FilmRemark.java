package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.Map;

public class FilmRemark extends AppCompatActivity {

    TextView filmName;
    EditText myRemark;
    Button send;
    ListView remarkList;
    SharedPreferences sharedPreferences;
    int filmId = 0;
    ArrayList<HashMap<String, Object>> remarkData;
    SimpleAdapter sa = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_remark);
        findViews();
        getRemarks();
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null)
            filmName.setText(intent.getStringExtra("filmName"));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRemark();
                getRemarks();
            }
        });
    }

    private void findViews() {
        filmName = (TextView) findViewById(R.id.filmName);
        myRemark = (EditText) findViewById(R.id.my_remark);
        send = (Button) findViewById(R.id.send);
        remarkList = (ListView) findViewById(R.id.remark_list);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        filmId = sharedPreferences.getInt("filmId", -1);
    }

    private void getUserName() {
        for (int i = 0; i < remarkData.size(); ++i) {
            final HashMap<String, Object> remark = remarkData.get(i);
            int id = (int)remark.get("username");
            String url = Controller.SERVER + Controller.ID2NAME + id;
            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            JSONObject info = jsonObject.getJSONObject("message");
                            remark.put("username", jsonObject.getString("message"));
                            sa.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FilmRemark.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Controller.sendRequest(FilmRemark.this, Request.Method.GET, url, new HashMap<String, String>(), listener);
        }
    }

    private void getRemarks() {
        remarkData = new ArrayList<>();
        final Map<String, String> params = new HashMap<>();
        String url = Controller.SERVER + Controller.FILMREMARKBYID + filmId;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(FilmRemark.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int status = response.getInt("status");
                    boolean success = status == 1;
                    if (success) {
                        JSONArray remarks = response.getJSONArray("message");
                        for (int i = 0; i < remarks.length(); ++i) {
                            JSONObject remark = remarks.getJSONObject(i);
                            HashMap<String, Object> data = new HashMap<>();
                            // TODO: 2017/6/20 userId -> username
                            data.put("username", remark.get("userId"));
                            data.put("remark", remark.get("content"));
                            String time = remark.getString("time");
                            time = Controller.convertTime(time);
                            data.put("time", time);
                            remarkData.add(data);
                        }
                    } else {
                        Toast.makeText(FilmRemark.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    sa = new SimpleAdapter(FilmRemark.this, remarkData, R.layout.film_remark_item,
                            new String[]{"username", "time", "remark"},
                            new int[] {R.id.username, R.id.remark_time, R.id.remark});
                    remarkList.setAdapter(sa);
                    getUserName();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.GET, url, params, listener);
    }

    private void sendRemark() {
        final Map<String, String> params = new HashMap<>();
        params.put("filmId", filmId + "");
        params.put("content", myRemark.getText().toString());
        String url = Controller.SERVER + Controller.FILMREMARK;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(FilmRemark.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Toast.makeText(FilmRemark.this, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String cookie = sharedPreferences.getString("Cookie", "");
        Controller.sendRequestWithCookie(getApplicationContext(), Request.Method.POST, url, params, listener, cookie);
    }
}
