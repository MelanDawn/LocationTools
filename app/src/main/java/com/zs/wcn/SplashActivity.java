package com.zs.wcn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.zs.wcn.base.BaseActivity;
import com.zs.wcn.utils.LogUtil;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    private static final int MSG_BASE_SPLASH = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new BetterHandler(SplashActivity.this).sendEmptyMessageDelayed(MSG_BASE_SPLASH, 1000);

        LogUtil.v(TAG, "create successfully");
    }

    private static class BetterHandler extends Handler {

        private WeakReference<Activity> activityWeakReference;

        BetterHandler(Activity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Activity activity = activityWeakReference.get();
            if (activity != null) {
                if (msg.what == MSG_BASE_SPLASH){
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
            }
        }
    }
}
