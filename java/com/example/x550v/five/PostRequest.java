package com.example.x550v.five;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by X550V on 2017/6/8.
 */
public class PostRequest extends Request<JSONObject> {
    private Map<String, String> params;
    private Response.Listener<JSONObject> listener;

    private String cookieFromResponse;
    private String header;
    private Map<String, String> sendHeader = new HashMap<>();

    public PostRequest(int method, String url, Map<String, String> params, Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.params = params;
        this.listener = listener;
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            JSONObject jsonObject = new JSONObject(je);
            sendHeader = response.headers;
            if (sendHeader.containsKey("Set-Cookie")) {
                cookieFromResponse = sendHeader.get("Set-Cookie");
                jsonObject.put("Cookie",cookieFromResponse);
            }
//            header = response.headers.toString();
//            //使用正则表达式从reponse的头中提取cookie内容的子串
//            Pattern pattern = Pattern.compile("Set-Cookie.*?;");
//            Matcher m = pattern.matcher(header);
//            if(m.find()){
//                cookieFromResponse = m.group();
//                //去掉cookie末尾的分号
//                cookieFromResponse = cookieFromResponse.substring(11,cookieFromResponse.length()-1);
//                //将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
//                jsonObject.put("Cookie",cookieFromResponse);
//            }
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return sendHeader;
    }

    public void setSendCookie(String cookie){
        sendHeader.put("Cookie",cookie);
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        listener.onResponse(jsonObject);
    }
}
