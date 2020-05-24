package com.zs.location;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zs.location.api.GeocodeAct;
import com.zs.location.api.LocationManagerAct;
import com.zs.location.base.BaseActivity;
import com.zs.location.utils.PermissionUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        com.zs.androidappfw.storage.database.DataManager.getInstance(this).openDb();
        com.zs.androidappfw.storage.database.DataManager.getInstance(this).closeDb();
    }

    public void toLocationManager(View v) {
        if (PermissionUtil.checkLocationPermission(this)) {
            toLocationManager();
        } else {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void toLocationManager() {
        startActivity(new Intent(MainActivity.this, LocationManagerAct.class));
    }

    public void toGeocodeAct(View v) {
        startActivity(new Intent(MainActivity.this, GeocodeAct.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            toLocationManager();
        } else {
            Toast.makeText(MainActivity.this, "No Location Permission", Toast.LENGTH_SHORT).show();
        }
    }
}
