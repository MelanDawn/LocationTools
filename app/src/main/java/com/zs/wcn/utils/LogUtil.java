package com.zs.wcn.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "WcnTools";

    public static boolean V = true;
    public static boolean D = true;
    public static boolean I = true;
    public static boolean W = true;
    public static boolean E = true;

    public static void v(String tag, Object... msg){
        if (V) Log.v(TAG, formatLog(tag, msg));
    }
    public static void d(String tag, Object... msg){
        if (D) Log.d(TAG, formatLog(tag, msg));
    }
    public static void i(String tag, Object... msg){
        if (I) Log.i(TAG, formatLog(tag, msg));
    }
    public static void w(String tag, Object... msg){
        if (W) Log.w(TAG, formatLog(tag, msg));
    }
    public static void e(String tag, Object... msg){
        if (E) Log.e(TAG, formatLog(tag, msg));
    }

    private static String formatLog(String tag, Object... msg){
        Thread t = Thread.currentThread();
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(tag)
                .append("--")
                .append(t.getName())
                .append(",")
                .append(t.getId())
                .append(",")
                .append(t.getThreadGroup() == null ? "NULL" : t.getThreadGroup().getName())
                .append("]");
        if (msg != null) {
            for (Object message : msg) {
                sb.append(" ").append(message == null ? "NULL" : message.toString());
            }
        }
        return sb.substring(0, sb.length());
    }
}
