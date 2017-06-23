package com.example.x550v.five.view.activity;
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
import com.android.volley.toolbox.Volley;
import com.example.x550v.five.R;
import com.example.x550v.five.controller.Controller;
import com.example.x550v.five.controller.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    public EditText username, password, repassword, email;
    public Button signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViews();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(SignIn.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(SignIn.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else  if (password.getText().toString().compareTo(repassword.getText().toString()) != 0) {
                    Toast.makeText(SignIn.this, "两次输入的密码不相同！", Toast.LENGTH_SHORT).show();
                } else if (!isValidMailbox(email.getText().toString())) {
                    Toast.makeText(SignIn.this, "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show();
                } else {
                    //  SEND TO SERVER
                    //username password
                    signIn();
                }
            }
        });
    }

    private  boolean isValidMailbox(String email) {
        return true;
    }


    private void signIn() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final Map<String, String> params = new HashMap<>();
        params.put("username", username.getText().toString());
        params.put("password", Controller.MD5(password.getText().toString()));
        params.put("email", email.getText().toString());
        PostRequest request = new PostRequest(Request.Method.POST, Controller.SERVER + Controller.REGISTER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response == null) {
                            Toast.makeText(SignIn.this, "网络繁忙，请稍候重试！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int status = response.getInt("status");
                            boolean success = status == 1;
                            if (success) {
                                Toast.makeText(SignIn.this, "注册成功，请前去邮箱激活", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignIn.this, "注册失败：" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignIn.this, error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("response error", error.toString());
            }
        });
        requestQueue.add(request);
    }

    public void findViews() {
        username = (EditText) findViewById(R.id.signin_username);
        password = (EditText) findViewById(R.id.signin_password);
        repassword = (EditText) findViewById(R.id.signin_repassword);
        signin =  (Button) findViewById(R.id.signin_signin);
        email = (EditText) findViewById(R.id.signin_email);
    }
}