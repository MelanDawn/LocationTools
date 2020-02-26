package com.zs.location.utils;

import android.os.Bundle;
import android.util.Log;

import com.zs.location.App;

import java.util.Set;

public class LogUtil {

    private static final String TAG = "LocationTools";

    public static boolean V = true;
    public static boolean D = true;
    public static boolean I = true;
    public static boolean W = true;
    public static boolean E = true;

    public static void v(String tag, Object... msg){
        if (V) Log.v(tag, formatLog(msg));
    }
    public static void d(String tag, Object... msg){
        if (D) Log.d(tag, formatLog(msg));
    }
    public static void i(String tag, Object... msg){
        if (I) Log.i(tag, formatLog(msg));
    }
    public static void w(String tag, Object... msg){
        if (W) Log.w(tag, formatLog(msg));
    }
    public static void e(String tag, Object... msg){
        if (E) Log.e(tag, formatLog(msg));
    }

    private static String formatLog(Object... msg){
        Thread t = Thread.currentThread();
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(TAG)
                .append("--")
                .append(t.getName())
                .append(", ")
                .append(t.getId())
                .append(", ")
                .append(t.getThreadGroup() == null ? "NULL" : t.getThreadGroup().getName())
                .append("]");
        if (msg != null) {
            for (Object message : msg) {
                sb.append(" ").append(message == null ? "NULL" : message.toString());
            }
        }
        return sb.substring(0, sb.length());
    }

    public static void printBundle(String tag, Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        if (bundle == null) {
            sb.append("NULL");
        } else {
            Set<String> set = bundle.keySet();
            if (set.size() == 0) {
                sb.append("NULL");
            } else {
                for (String key : set) {
                    Object value = bundle.get(key);
                    sb.append(key).append("=").append(value == null ? "NULL" : value.toString()).append(", ");
                }
            }
        }
        d(tag, sb.substring(0));
    }
}
