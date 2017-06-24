package com.example.lenovo.five_version1.view.activity;

import android.content.Intent;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ForgetPassword extends AppCompatActivity {

    EditText username, code;
    Button send, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        findViews();
        setListener();
    }

    private void setListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length() == 0) {
                    Toast.makeText(ForgetPassword.this, "用户名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = Controller.SERVER + Controller.FORGET;
                Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                Controller.sendRequest(ForgetPassword.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Toast.makeText(ForgetPassword.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Controller.SERVER + Controller.CONFIRM;
                Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString());
                params.put("code", code.getText().toString());
                Controller.sendRequest(ForgetPassword.this, Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Intent intent = new Intent(ForgetPassword.this, ResetPassword.class);
                                intent.putExtra("code", jsonObject.getString("message"));
                                intent.putExtra("username", username.getText().toString());
                                startActivity(intent);
                            } else {
                                Toast.makeText(ForgetPassword.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void findViews() {
        username = (EditText) findViewById(R.id.username);
        code = (EditText) findViewById(R.id.confirmCode);
        send = (Button) findViewById(R.id.send);
        next = (Button) findViewById(R.id.next);
    }
}
