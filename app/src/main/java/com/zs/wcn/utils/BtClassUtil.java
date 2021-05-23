package com.zs.wcn.utils;

import android.bluetooth.BluetoothClass;

public class BtClassUtil {

    public static String getContent(BluetoothClass clazz) {
        if (clazz == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" clazzDec=").append(clazz.hashCode())
                .append(" major=").append(clazz.getMajorDeviceClass())
                .append(" minor=").append(clazz.getDeviceClass())
                .append(" clazzHex=").append(clazz.toString());
        return sb.substring(0);
    }
}
