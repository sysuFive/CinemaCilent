package com.example.x550v.five;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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



public class Controller extends AsyncTask<String, Integer, JSONObject> {

    public static String SERVER = "http://172.18.69.88:8080";

    public static String LOGIN =  "/login";
    public static String REGISTER = "/register";

    public static String CINEMA = "/cinema";
    public static String CINEMAPIC = "/cinemaPic/cover/";

    public static String CINEMAREMARK = "/cinemaRemark/post";
    public static String CINEMAREMARKBYID = "/cinemaRemark/getByCinemaId/";
    public static String CINEMAREMARKSELF = "/cinemaRemark/getMyCinemaRemark";

    public static String FILM = "/film";
    public static String FILMBYID = "/filmByCinemaId";
    public static String FILMPIC = "/filmPic/cover/";
    public static String FILMSTILL = "/filmPic/still/";

    public static String FILMREMARK = "/filmRemark/post";
    public static String FILMREMARKBYID = "/filmRemark/getByFilmId/";
    public static String FILMREMARKSELF = "/filmRemark/getMyFilmRemark";

    public static String FILMSESSION = "/filmsession";
    public static String SIT = "/sit/";
    public static String MAKE = "/reservation/make";
    public static String PAY = "/sysupay";
    public static String WALLET = "/queryWallet";

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

    public static void sendRequest(Context context, int method, String url, Map<String, String> params,
                                   Response.Listener<JSONObject> listener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        PostRequest request = new PostRequest(method, url, params, listener,
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("response error", error.toString());
                }
        });
        requestQueue.add(request);
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }


}
