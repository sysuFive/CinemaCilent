package com.example.lenovo.five_version1;

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

public class SignIn extends AppCompatActivity {

    public EditText username, password, repassword;
    public Button signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findview();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(SignIn.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(SignIn.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else  if (password.getText().toString().compareTo(repassword.getText().toString()) != 0) {

                    Toast.makeText(SignIn.this, "两次输入的密码不相同！", Toast.LENGTH_SHORT).show();
                } else {

                    //  SEND TO SERVER
                    //username password
                    boolean success = true;


                    if (success) {

                        Dialog dg = new AlertDialog.Builder(SignIn.this)
                                .setMessage("注册成功!")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent t = new Intent(SignIn.this, MainPage.class);
                                        startActivity(t);
                                    }

                                })
                                .setPositiveButton("直接登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //  SEND TO SERVER
                                        //username password
                                        Intent t = new Intent(SignIn.this, MainActivity.class);
                                        startActivity(t);
                                    }

                                })
                                .create();
                        dg.show();

                    } else {
                        Toast.makeText(SignIn.this, "注册失败，用户名已被使用", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void findview() {
        username = (EditText) findViewById(R.id.signin_username);
        password = (EditText) findViewById(R.id.signin_password);
        repassword = (EditText) findViewById(R.id.signin_repassword);
        signin =  (Button) findViewById(R.id.signin_signin);
    }
}
