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
import java.util.Map;

/**
 * Created by X550V on 2017/6/8.
 */
public class PostRequest extends Request<JSONObject> {
    private Map<String, String> params;
    private Response.Listener<JSONObject> listener;
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
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        listener.onResponse(jsonObject);
    }
}
