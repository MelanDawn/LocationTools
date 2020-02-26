package com.zs.location;

import android.app.Application;
import android.content.Context;

import com.zs.location.net.NetworkState;
import com.zs.location.telephony.Telephony;

public class App extends Application {

    public static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = App.this;
//        new NetworkState();
//        new Telephony();
    }
}