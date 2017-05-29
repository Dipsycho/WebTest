package com.example.webtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Darkness on 2017/5/24.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}