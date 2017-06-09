package com.example.lenovo.five_version1;

/**
 * Created by lenovo on 2017/6/8.
 */
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by X550V on 2017/6/6.
 */

public class Controller extends AsyncTask<String, Integer, JSONObject> {

    public static String SERVER = "localhost:8080";
    public static String LOGIN =  SERVER + "/login";
    public static String REGISTER = SERVER + "/register";
    public static String CINEMA = SERVER + "/cinema";
    public static String FILM = SERVER + "/film";
    public static String FILMSESSION = SERVER + "/filmsession";
    public static String SIT = SERVER + "/sit";
    public static String MAKE = SERVER + "/reservation/make";
    public static String PAY = SERVER + "/reservation/sysupay";

    public static JSONObject response = null;

    private RequestQueue requestQueue;
    private String url;
    private JSONObject json;
    private int method;

    public Controller(RequestQueue r, String u, JSONObject jn, int m) {
        requestQueue = r;
        url = u;
        json = jn;
        method = m;
    }

    public static void sendRequest(final RequestQueue requestQueue, String url, JSONObject json)  {
        response = null;
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Controller.response = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", error.getMessage());
            }
        });
        requestQueue.add(jsonRequest);
        requestQueue.start();
    }


    @Override
    protected JSONObject doInBackground(String... params) {
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(method, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Controller.response = response;
                        Log.i("do in background", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response error", error.getMessage());
            }
        });
        requestQueue.add(jsonRequest);
        requestQueue.start();
        while (response == null)
            publishProgress();
        return response;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // TODO: 2017/6/7
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO: 2017/6/7
    }

    public  static  String MD5(String src) {
        String res = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bs = src.getBytes();
            messageDigest.update(bs);
            byte[] bytes = messageDigest.digest();
            res = new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  res;
    }

    public static String getCurTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }




}