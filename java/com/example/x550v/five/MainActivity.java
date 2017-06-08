package com.example.x550v.five;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public EditText username, password;
    public Button login, signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else  {
                    //  SEND TO SERVER
                    //username password
                    sendRequest();
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(MainActivity.this, SignIn.class);
                startActivity(t);

            }
        });
    }

    private void sendRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username.getText().toString());
        params.put("password", Controller.MD5(password.getText().toString()));
        JSONObject json = new JSONObject(params);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, Controller.SERVER + Controller.LOGIN, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response == null) {
                            Toast.makeText(MainActivity.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;
                            if (success) {
                                Intent intent = new Intent(MainActivity.this, UserInfo.class);
                                intent.putExtra("json", response.toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                 @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("response error", error.toString());
                }
            });
        requestQueue.add(jsonRequest);
        requestQueue.start();
    }

    public void findViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signin =  (Button) findViewById(R.id.signin);
    }

    private View.OnClickListener clickLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private View.OnClickListener clickRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
