package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.lenovo.five_version1.R;
import com.example.lenovo.five_version1.controller.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {
    public EditText password, repassword;
    public Button send;
    public Button bt_pwd_clear, bt_repwd_clear;
    public TextWatcher password_watcher, repassword_watcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        findViews();
        initWatcher();
        setListener();
    }

    private void setListener() {
        password.addTextChangedListener(password_watcher);
        repassword.addTextChangedListener(repassword_watcher);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().compareTo(repassword.getText().toString()) != 0) {
                    Toast.makeText(ResetPassword.this, "两次输入的密码不相同！", Toast.LENGTH_SHORT).show();
                }  else {
                    reset();
                }
            }
        });
    }

    private void reset() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String username = intent.getStringExtra("username");
            String code = intent.getStringExtra("code");
            String url = Controller.SERVER + Controller.RESET;
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("code", code);
            params.put("password", Controller.MD5(password.getText().toString()));
            Controller.sendRequest(ResetPassword.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        Toast.makeText(ResetPassword.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        if (jsonObject.getInt("status") == 1) {
                            Intent toMain = new Intent(ResetPassword.this, MainActivity.class);
                            startActivity(toMain);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void findViews() {
        password = (EditText) findViewById(R.id.signin_password);
        repassword = (EditText) findViewById(R.id.signin_repassword);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_repwd_clear = (Button) findViewById(R.id.bt_repwd_clear);
        send = (Button) findViewById(R.id.send);
    }

    private void initWatcher() {
        repassword_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    bt_repwd_clear.setVisibility(View.VISIBLE);
                }else{
                    bt_repwd_clear.setVisibility(View.INVISIBLE);
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
}
