package com.zs.wcn.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionUtil {

    public static boolean checkLocationPermission(Context context) {
        return checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean checkPermission(Context context, String permission) {
        return context == null ||
                context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
