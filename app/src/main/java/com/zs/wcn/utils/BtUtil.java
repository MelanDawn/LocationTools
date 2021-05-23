package com.zs.wcn.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class BtUtil {

    private static final String TAG = BtUtil.class.getSimpleName();

    public static BluetoothAdapter getInstance(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                return bluetoothManager.getAdapter();
            } else {
                throw new RuntimeException("bluetoothManager is null");
            }
        } else {
            return BluetoothAdapter.getDefaultAdapter();
        }
    }

    public static boolean hasFeatureBt(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    public static boolean hasFeatureBle(Context context) {
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}
