package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.controller.Controller;
import com.example.lenovo.five_version1.model.CinemaCard;
import com.example.lenovo.five_version1.model.FilmCard;
import com.example.lenovo.five_version1.view.adapter.CinemaAdapter;
import com.example.lenovo.five_version1.view.adapter.FilmAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    EditText content;
    Button send;
    TextView film, cinema;
    RecyclerView film_list, cinema_list;

    ArrayList<HashMap<String, Object>> filmData, cinemaData;
    ArrayList<FilmCard> filmCards;
    ArrayList<CinemaCard> cinemaCards;
    CinemaAdapter cinemaAdapter;
    FilmAdapter filmAdapter;
    Bitmap origin;
    SharedPreferences sharedPreferences;

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
        origin = BitmapFactory.decodeResource(getResources(), R.drawable.none);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
    }

    private void search() {
        filmData = new ArrayList<>();
        cinemaData = new ArrayList<>();
        filmCards = new ArrayList<>();
        cinemaCards = new ArrayList<>();
        film.setText("");
        cinema.setText("");
        String url = Controller.SERVER + Controller.SEARCH + content.getText().toString();
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONArray info = jsonObject.getJSONArray("message");
                        for (int i = 0; i < info.length(); ++i) {
                            JSONObject one = info.getJSONObject(i);
                            if (one.has("address")) {
                                cinema.setText("电影院");
                                HashMap<String, Object> tmp = new HashMap<>();
                                String name = one.getString("name");
                                String address = one.getString("address");
                                String phone = one.getString("phone");
                                tmp.put("name", name);
                                tmp.put("location", address);
                                tmp.put("phone", phone);
                                tmp.put("CinemaId", one.get("id"));
                                cinemaData.add(tmp);
                                cinemaCards.add(new CinemaCard(name, address, phone, origin));
                            } else {
                                film.setText("电影");
                                HashMap<String, Object> tmp = new HashMap<>();
                                String name = one.getString("name");
                                String actors = one.getString("actor");
                                String rate = one.getString("score");
                                rate = Float.parseFloat(rate)/2 +"";
                                String type = one.getString("category");
                                tmp.put("filmId", one.get("id"));
                                tmp.put("filmName", name);
                                tmp.put("filmActor", actors);
                                tmp.put("filmRate", rate);
                                tmp.put("filmType", type);
                                tmp.put("publishTime", one.get("publishTime"));
                                tmp.put("lastTime", one.get("lastTime"));
                                tmp.put("director", one.get("director"));
                                tmp.put("lang", "language");
                                tmp.put("summary", one.get("summary"));
                                int imgRid = R.drawable.none;
                                tmp.put("img", imgRid);
                                filmData.add(tmp);
                                filmCards.add(new FilmCard(name, type, actors, rate, origin));
                            }
                        }
                        setCinemaList();
                        setFilmList();
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

    public void setFilmList() {
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        film_list.setLayoutManager(manager);
        filmAdapter = new FilmAdapter(SearchActivity.this, filmCards);
        filmAdapter.setOnItemClickListener(new FilmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, FilmCard item) {
                Intent intent = new Intent(SearchActivity.this, FilmActivity.class);
                Bundle bundle = new Bundle();
                Map<String, Object> one  = filmData.get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("filmId", (int)one.get("filmId"));
                editor.apply();
                Set<String> keys = one.keySet();
                for (String key : keys) {
                    bundle.putString(key, one.get(key).toString());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        film_list.setAdapter(filmAdapter);
        getFilmImg();
    }

    public void setCinemaList() {
        LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        cinema_list.setLayoutManager(manager);
        cinemaAdapter = new CinemaAdapter(SearchActivity.this, cinemaCards);
        cinemaAdapter.setOnItemClickListener(new CinemaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CinemaCard item) {
                Intent intent = new Intent(SearchActivity.this, TheaterActivity.class);
                Map<String, Object> one  = cinemaData.get(position);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("CinemaId", (int)one.get("CinemaId"));
                editor.putString("CinemaName", one.get("name").toString());
                editor.putString("location", one.get("location").toString());
                editor.apply();
                startActivity(intent);
            }
        });
        cinema_list.setAdapter(cinemaAdapter);
        getCinemaImg();
    }

    private void getCinemaImg() {
        for (int i = 0; i < cinemaData.size(); ++i) {
            final HashMap<String, Object> one = cinemaData.get(i);
            final int id  = (int)one.get("CinemaId");
            String url = Controller.SERVER + "/cinema/" + id + "/cover/0.jpg";
            final int finalI = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    cinemaCards.get(finalI).setBitmap(bitmap);
                    cinemaAdapter.notifyDataSetChanged();
                }
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("response error", volleyError.toString());
                }
            });
            RequestQueue queue =  Volley.newRequestQueue(SearchActivity.this);
            queue.add(request);
        }
    }

    private void getFilmImg() {
        for (int i = 0; i < filmData.size(); ++i) {
            final HashMap<String, Object> one = filmData.get(i);
            final int id  = (int)one.get("filmId");
            String url = Controller.SERVER + "/film/" + id + "/cover/0.jpg";
            final int finalI = i;
            ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    filmCards.get(finalI).setImg(bitmap);
                    filmAdapter.notifyDataSetChanged();
                }
            }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("response error", volleyError.toString());
                }
            });
            RequestQueue queue =  Volley.newRequestQueue(SearchActivity.this);
            queue.add(request);
        }
    }

}

