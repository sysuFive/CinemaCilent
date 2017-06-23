package com.example.x550v.five.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.x550v.five.R;
import com.example.x550v.five.controller.Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {
    EditText content;
    Button send;
    TextView film, cinema;
    RecyclerView film_list, cinema_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViews();
        setListener();
    }

    private void setListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
    }

    private void findViews() {
        content = (EditText) findViewById(R.id.content);
        send = (Button) findViewById(R.id.send);
        film = (TextView) findViewById(R.id.film);
        cinema = (TextView) findViewById(R.id.cinema);
        film_list = (RecyclerView) findViewById(R.id.film_list);
        cinema_list = (RecyclerView) findViewById(R.id.cinema_list);
    }

    private void search() {
        String url = Controller.SERVER + Controller.SEARCH + content.getText().toString();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray infos = jsonObject.getJSONArray("message");
                    } else {
                        Toast.makeText(SearchActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequestWithGET(SearchActivity.this, url, listener);
    }
}
