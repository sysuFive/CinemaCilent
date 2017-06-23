package com.example.x550v.five.controller;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class Controller {

    public static String SERVER = "";

    public static String LOGIN =  "/login";
    public static String REGISTER = "/register";
    public static String ID2NAME = "/user/findUser?userId=";
    public static String FILMREMARKSELF = "/filmRemark/getMyFilmRemark";
    public static String CINEMAREMARKSELF = "/cinemaRemark/getMyCinemaRemark";
    public static String FILMREMARK = "/filmRemark/post";
    public static String WALLET = "/queryWallet";

    public static String CINEMA = "/cinema";
    public static String CINEMAPIC = "/cinemaPic/cover/";

    public static String CINEMAREMARK = "/cinemaRemark/post";
    public static String CINEMAREMARKBYID = "/cinemaRemark/getByCinemaId/";

    public static String FILM = "/film";
    public static String FILMBYID = "/filmByCinemaId";
    public static String FILMPIC = "/filmPic/cover/";
    public static String FILMSTILL = "/filmPic/still/";
    public static String FILMREMARKBYID = "/filmRemark/getByFilmId/";

    public static String FILMSESSION = "/filmsession";
    
    public static String SIT = "/sit/";

    public static String MAKE = "/reservation/make";
    public static String MYORDER = "/reservation/getMyOrder";

    public static String PAY = "/sysupay";

    public static String SEARCH = "/search?text=";

    public Controller() {}

    public static void IPFile() {
        File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/FiveShow");
        File ip = new File(dir, "server.txt");
        if (ip.exists()) {
            try {
                FileReader inputStream = new FileReader(ip);
                BufferedReader reader = new BufferedReader(inputStream);
                String ipAddress = reader.readLine();
                Controller.SERVER = "http://" + ipAddress;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                dir.mkdir();
                ip.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendRequest(Context context, int method, String url, Map<String, String> params,
                                   Response.Listener<JSONObject> listener) {
        sendRequestWithCookie(context, method, url, params, listener, "");
    }

    public static void sendRequestWithGET(Context context, String url,
                                          Response.Listener<JSONObject> listener) {
        sendRequest(context, Request.Method.GET, url, new HashMap<String, String>(), listener);
    }

    public static void sendRequestWithCookie(Context context, int method, String url, Map<String, String> params,
                                   Response.Listener<JSONObject> listener, String cookie) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        PostRequest request = new PostRequest(method, url, params, listener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("response error", error.toString());
                    }
                });
        request.setSendCookie(cookie);
        requestQueue.add(request);
    }

    public static String convertTime(String src) {
        long pt = Long.parseLong(src);
        Date d = new Date(pt);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(d);
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
