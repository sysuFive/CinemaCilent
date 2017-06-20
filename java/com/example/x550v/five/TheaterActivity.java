package com.example.x550v.five;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TheaterActivity extends AppCompatActivity {
    private RecyclerView movieslist;
    public ListView sessionlist;
    ArrayList<Map<String, Object>> sessionData;
    int cinemaId = -1;
    int FilmId = -1;
    ArrayList<HashMap<String, Object>> filmData;
    SharedPreferences sharedPreferences;
    TextView cinema_name, address, movie_name, movie_rate, movie_actors;
    SimpleRatingBar ratingBar;

    public void findViews() {
        movieslist = (RecyclerView) findViewById(R.id.movieintheaterlist);
        sessionlist = (ListView) findViewById(R.id.sessionlist);
        cinema_name = (TextView) findViewById(R.id.theatername);
        address = (TextView) findViewById(R.id.theateaddress);
        movie_name = (TextView) findViewById(R.id.moviename);
        movie_rate = (TextView) findViewById(R.id.moviegrade);
        movie_actors = (TextView) findViewById(R.id.movieactor);
        ratingBar = (SimpleRatingBar) findViewById(R.id.movie_rating_bar);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        sessionData = new ArrayList<>();
        filmData = new ArrayList<>();
        cinemaId = sharedPreferences.getInt("CinemaId", -1);
        cinema_name.setText(sharedPreferences.getString("CinemaName", ""));
        address.setText(sharedPreferences.getString("location", ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);
        findViews();
        setRecyclerview();

        sessionlist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> one  = sessionData.get(i);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("SessionId", (int)one.get("SessionId"));
                editor.putString("hall", one.get("hall").toString());
                editor.putString("beginTime", (String)one.get("beginTime"));
                editor.putString("filmName", movie_name.getText().toString());
                editor.putString("price", one.get("price").toString());
                editor.apply();
                Intent t = new Intent(TheaterActivity.this, SelectSeat.class);
                startActivity(t);
            }
        });

    }

    private void getFilms() {
        final Map<String, String> params = new HashMap<>();
        params.put("cinemaId", "" + cinemaId);
        String url = Controller.SERVER + Controller.FILMBYID;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                if (response == null) {
                    Toast.makeText(TheaterActivity.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int status = response.getInt("status");
                    boolean success = status == 1;
                    CinemaFilmAdapter ca;
                    ArrayList<CinemaFilm> cards = new ArrayList<>();
                    if (success) {
                        JSONArray films = response.getJSONArray("message");
                        for (int i = 0; i < films.length(); ++i) {
                            JSONObject one = (JSONObject) films.get(i);
                            HashMap<String, Object> tmp = new HashMap<>();
                            String name = one.getString("name");
                            String actors = one.getString("actor");
                            String rate = one.getString("score") + "000";
                            rate = rate.substring(0, 3);
                            String type = one.getString("category");
                            int id = one.getInt("id");
                            tmp.put("filmId", id);
                            tmp.put("filmName", name);
                            tmp.put("filmActor", actors);
                            tmp.put("filmRate", rate);
                            tmp.put("filmType", type);
                            tmp.put("publishTime", one.get("publishTime"));
                            tmp.put("lastTime", one.get("lastTime"));
                            tmp.put("director", one.get("director"));
                            tmp.put("lang", "language");
                            tmp.put("summary", one.get("summary"));
                            // TODO: 2017/6/8 get img
//                                    String imgSrc = one.getString("img");
                            int imgRid = R.drawable.test;
                            tmp.put("img", imgRid);
                            filmData.add(tmp);
                            cards.add(new CinemaFilm(id, imgRid));
                            if (i == 0) {
                                FilmId = id;
                                setFilmInfo(filmData, i);
                                setSessionList();
                            }
                        }
                    } else {
                        Toast.makeText(TheaterActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    ca = new CinemaFilmAdapter(TheaterActivity.this, cards);
                    ca.setOnItemClickListener(new CinemaFilmAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position, CinemaFilm item) {
                            FilmId = item.getFilmId();
                            setFilmInfo(filmData, position);
                            CardView cardView = (CardView) view.findViewById(R.id.card_view);
                            cardView.setScaleY(1.1f);
                            cardView.setScaleX(1.1f);
                            ImageView img = (ImageView) view.findViewById(R.id.movieimg);
                            // TODO: 2017/6/20 make selected img bigger
                            img.setSelected(true);
                            img.setPadding(2, 3, 2, 3);
//                            img.setMaxHeight((int)(img.getHeight()*1.2));
//                            img.setMaxWidth((int)(img.getWidth()*1.2));
//                            img.setAdjustViewBounds(true);
                            setSessionList();
                        }
                    });
                    movieslist.setAdapter(ca);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Controller.sendRequest(getApplication(), Request.Method.POST, url, params, listener);
    }

    private void setFilmInfo(ArrayList<HashMap<String, Object>> filmData, int i) {
        HashMap<String, Object> film = filmData.get(i);
        setFilmInfo(film.get("filmName").toString(), film.get("filmRate").toString(), film.get("filmActor").toString());
    }

    private void setFilmInfo(String name, String rate, String actors) {
        movie_name.setText(name);
        movie_rate.setText(rate);
        movie_actors.setText(actors);
        ratingBar.setRating(Float.parseFloat(rate));
    }

    private String Long2String(Long t) {
        Date bd = new Date(t);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return formatter.format(bd);
    }

    private void setSessionList() {
        String time = Controller.getCurTime();
        final Map<String, String> params = new HashMap<>();
        params.put("filmId", "" + FilmId);
        params.put("cinemaId", "" + cinemaId);
        params.put("time", time);
        String url = Controller.SERVER + Controller.FILMSESSION;
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
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
                        sessionData.clear();
                        JSONArray FilmSession = response.getJSONArray("message");
                        for (int i = 0; i < FilmSession.length(); ++i) {
                            JSONObject one = (JSONObject) FilmSession.get(i);
                            HashMap<String, Object> tmp = new HashMap<>();
                            tmp.put("SessionId", one.get("id"));
                            tmp.put("hall", one.get("hall"));
                            tmp.put("hallId", one.get("hallSittingId"));
                            tmp.put("beginTime", Long2String((long)one.get("beginTime")));
                            tmp.put("endTime", Long2String((long)one.get("endTime")));
                            tmp.put("classification", one.get("classification"));
                            String price = one.get("price") + "00000";
                            price = price.substring(0, 4);
                            tmp.put("price", price);
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
        };
        Controller.sendRequest(getApplicationContext(), Request.Method.POST, url, params, listener);
    }

    public void setRecyclerview() {
        LinearLayoutManager manager = new LinearLayoutManager(TheaterActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        movieslist.setLayoutManager(manager);
        getFilms();
    }

}
