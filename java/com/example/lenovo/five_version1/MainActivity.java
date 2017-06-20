package com.example.lenovo.five_version1;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
    public Button login, signIn, bt_username_clear, bt_pwd_clear;
    SharedPreferences sharedPreferences;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;

    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                password.setText("");
                if(s.toString().length()>0){
                    bt_username_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("login", false);
        if (isLogin) {
            Intent intent = new Intent(MainActivity.this, MainPage.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        findViews();
        initWatcher();
        username.addTextChangedListener(username_watcher);
        password.addTextChangedListener(password_watcher);

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
                    Login();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(MainActivity.this, SignIn.class);
                startActivity(t);

            }
        });

        bt_username_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
            }
        });
        bt_pwd_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });
    }

    private void Login() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username.getText().toString());
        params.put("password", Controller.MD5(password.getText().toString()));
        PostRequest request = new PostRequest(Request.Method.POST, Controller.SERVER + Controller.LOGIN, params,
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
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Cookie", response.getString("Cookie"));
                                editor.putBoolean("login", true);
                                editor.apply();
                                Intent intent = new Intent(MainActivity.this, MainPage.class);
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
        String cookie = sharedPreferences.getString("Cookie", "");
//        if (!cookie.equals(""))
//            request.setSendCookie(cookie);
        requestQueue.add(request);
    }

    public void findViews() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signIn =  (Button) findViewById(R.id.signin);
        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
    }


}