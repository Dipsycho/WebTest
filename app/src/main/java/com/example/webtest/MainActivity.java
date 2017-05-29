package com.example.webtest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private EditText nameText;
    private EditText pwText;
    private int LOG_STATUS = 0;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberMe;
    private CheckBox autoLogin;
    private int LOG_OUT_CHECK=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button registerButton = (Button) findViewById(R.id.register);
        Button loginButton = (Button) findViewById(R.id.login);
        nameText = (EditText) findViewById(R.id.name);
        pwText = (EditText) findViewById(R.id.pw);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        LOG_OUT_CHECK=getIntent().getIntExtra("logout",0);

        boolean ifRemember = pref.getBoolean("rememberMe", false);
        boolean ifAutoLogin = pref.getBoolean("autoLogin", false);


        if(LOG_OUT_CHECK==0) {
            if (ifRemember || ifAutoLogin) {
                String name = pref.getString("name", "");
                String pw = pref.getString("pw", "");
                pwText.setText(name);
                nameText.setText(pw);
                rememberMe.setChecked(true);
            }

            if (ifAutoLogin) {
                autoLogin.setChecked(true);
                loginProcess();
            }
        }

        registerButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        ActivityCollector.finishAll();
    }

    public void registerProcess() {
        if (LOG_STATUS == 0) {
            AVUser user = new AVUser();

            final String pw = pwText.getText().toString();
            final String name = nameText.getText().toString();
            if (name.length() < 7 || pw.length() < 7) {
                Toast.makeText(MainActivity.this, "请按照要求注册", Toast.LENGTH_LONG).show();
            } else {
                user.setUsername(name);
                user.setPassword(pw);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            LOG_STATUS = 1;
                            editor = pref.edit();
                            if (autoLogin.isChecked())
                            {
                                editor.putBoolean("autoLogin",true);
                            }
                            if (rememberMe.isChecked()||autoLogin.isChecked()) {
                                editor.putBoolean("rememberMe", true);
                                editor.putString("name", name);
                                editor.putString("pw", pw);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MapMain.class);
                            startActivity(intent);
                            MainActivity.this.finish();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(MainActivity.this, "已登录，请勿重复注册", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginProcess() {
        if (LOG_STATUS == 0) {
            final String pw = pwText.getText().toString();
            final String name = nameText.getText().toString();
            AVUser.logInInBackground(pw, name, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        LOG_STATUS = 1;
                        editor = pref.edit();
                        if (autoLogin.isChecked())
                        {
                            editor.putBoolean("autoLogin",true);
                        }
                        if (rememberMe.isChecked()||autoLogin.isChecked()) {
                            editor.putBoolean("rememberMe", true);
                            editor.putString("name", name);
                            editor.putString("pw", pw);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MapMain.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "已登录，请勿重复登录", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                registerProcess();
                break;
            case R.id.login:
                loginProcess();
                break;
            default:
                break;
        }
    }
}
