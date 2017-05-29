package com.example.webtest;

import android.app.Application;
import android.content.Context;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by Dark on 2017/5/19.
 * 腾讯云LeanCloudAPI key必须
 */

public class MyLeanCloudApp extends Application {
    private final String APP_ID = "wFO4kBNS02SsVWTAvbMU4S9L-9Nh9j0Va";
    private final String APP_KEY = "w9UktpLu5tAk1AAX7HNfkIWK";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        //AVOSCloud.initialize(this,APP_ID, APP_KEY);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);

        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}