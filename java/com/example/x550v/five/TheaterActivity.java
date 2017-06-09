package com.example.x550v.five;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TheaterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public ListView sessionlist;
    ArrayList<Map<String, Object>> sessionData;
    String name = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        findViews();
        setRecycleView();
        setSessionList();

        sessionlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //send message
                Map<String, Object> one  = sessionData.get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("SessionId", (int)one.get("SessionId"));
                editor.putString("hall", (String)one.get("hall"));
                editor.putString("beginTime", (String)one.get("beginTime"));
                editor.putString("filmName", name);
                editor.putFloat("price", (float)one.get("price"));
                editor.apply();
                Intent t = new Intent(TheaterActivity.this, SelectSeat.class);
                startActivity(t);
            }
        });
    }

    public void findViews()  {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        sessionlist = (ListView) findViewById(R.id.sessionlist);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        sessionData = new ArrayList<>();
    }

    public void setRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(TheaterActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        ArrayList<movieinfo> movies = new ArrayList<>();
        // get movie info
        movieinfoAdapter adapter = new movieinfoAdapter(TheaterActivity.this, movies);
        recyclerView.setAdapter(adapter);
    }

    private void setSessionList() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        int FilmId = sharedPreferences.getInt("FilmId", -1);
        int CinemaId = sharedPreferences.getInt("CinemaId", -1);
        String time = Controller.getCurTime();
        final Map<String, String> params = new HashMap<>();
        params.put("FilmId", "" + FilmId);
        params.put("CinemaId", "" + CinemaId);
        params.put("time", time);
        String url = Controller.SERVER + Controller.FILMSESSION;
        PostRequest request = new PostRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(TheaterActivity.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;
                            SimpleAdapter sa;
                            if (success) {
                                JSONObject message = response.getJSONObject("message");
                                JSONArray FilmSession = message.getJSONArray("FilmSession");
                                for (int i = 0; i < FilmSession.length(); ++i) {
                                    JSONObject one = (JSONObject) FilmSession.get(i);
                                    HashMap<String, Object> tmp = new HashMap<>();
                                    tmp.put("SessionId", one.get("SessionId"));
                                    tmp.put("hall", one.get("hall"));
                                    tmp.put("beginTime", one.get("beginTime"));
                                    tmp.put("endTime", one.get("endTime"));
                                    tmp.put("classification", one.get("classification"));
                                    tmp.put("price", one.get("price"));
                                    sessionData.add(tmp);
                                }
                                sa = new SimpleAdapter(TheaterActivity.this, sessionData, R.layout.sessionitems,
                                        new String[]  {"beginTime","endTime", "classification", "hall", "price"},
                                        new int[] {R.id.start,R.id.end, R.id.lang, R.id.whichsess, R.id.price});
                                sessionlist.setAdapter(sa);
                            } else {
                                Toast.makeText(TheaterActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                                sa = new SimpleAdapter(TheaterActivity.this, sessionData, R.layout.sessionitems,
                                        new String[]  {},
                                        new int[] {});
                                sessionlist.setAdapter(sa);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TheaterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }

    public void setsessionlist() {

        //get sessions
        int sz = 5;
        ArrayList<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
        for (int i = 0; i < sz; i++) {
            HashMap<String, Object> tmp=new HashMap<String, Object>();
            tmp.put("start", "19:00");
            tmp.put("end", "21:22");
            tmp.put("lang", "英语");
            tmp.put("whichsess", "5号厅");
            tmp.put("price", "￥41");
            data.add(tmp);
        }

        final SimpleAdapter sa = new SimpleAdapter(TheaterActivity.this, data, R.layout.sessionitems,
                new String[]  {"start","end", "lang", "whichsess", "price"},
                new int[] {R.id.start,R.id.end, R.id.lang, R.id.whichsess, R.id.price});
        sessionlist.setAdapter(sa);
    }
}
