package com.zs.location.api;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;

import com.zs.location.R;
import com.zs.location.base.BaseActivity;
import com.zs.location.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class LMTesterAct extends BaseActivity {

    private static final String INTENT_ACTION4 = "com.zs.location.GNSS_PENDING_INTENT4";
    private static final String INTENT_ACTION5 = "com.zs.location.GNSS_PENDING_INTENT5";
    private static final String ACTION_PROXIMITY_ALERT = "com.zs.location.PROXIMITY_ALERT";

    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();

    private LocationManager mLocationManager;

    private LocationListener mGnssLocationListener1 = new MyLocationListener("GNSS1");
    private LocationListener mGnssLocationListener2 = new MyLocationListener("GNSS2");
    private LocationListener mGnssLocationListener3 = new MyLocationListener("GNSS3");
    private LocationListener mGnssLocationListener4 = new MyLocationListener("GNSS4");
    private LocationListener mGnssLocationListener5 = new MyLocationListener("GNSS5");
    private LocationListener mNetworkLocationListener = new MyLocationListener("NETWORK");
    private LocationListener mPassiveLocationListener = new MyLocationListener("PASSIVE");
    private LocationListener mFusedLocationListener = new MyLocationListener("FUSED");
    private LocationListener mMockLocationListener = new MyLocationListener("MOCK");

    private PendingIntent mProximityAlertIntent = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_lm_tester);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LogUtil.d(TAG, "Location Service enabled:", mLocationManager.isLocationEnabled());

            LogUtil.d(TAG, "GnssHardwareModelName:", mLocationManager.getGnssHardwareModelName());

            LogUtil.d(TAG, "GnssYearOfHardware:", mLocationManager.getGnssYearOfHardware());

            LogUtil.d(TAG, "===============================================");
        }

        List<String> allProviders = new ArrayList<String>();
        try {
            allProviders = mLocationManager.getAllProviders();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String name : allProviders) {
            LogUtil.d(TAG, name);

            LogUtil.d(TAG, name, "enabled:", mLocationManager.isProviderEnabled(name));

            LocationProvider locationProvider = mLocationManager.getProvider(name);
            LogUtil.d(TAG, locationProvider);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                Location location = mLocationManager.getLastKnownLocation(name);
                LogUtil.d(TAG, "LastKnownLocation:", location);
            }
            LogUtil.d(TAG, "===============================================");
        }

        List<String> enabledProviders = mLocationManager.getProviders(true);
        for (String provider: enabledProviders) {
            LogUtil.d(TAG, "enabled provider:", provider);
        }
        LogUtil.d(TAG, "===============================================");

        Criteria criteria1 = new Criteria();
        criteria1.setAltitudeRequired(true);
        String criteriaProvider1 = mLocationManager.getBestProvider(criteria1, true);
        LogUtil.d(TAG, criteria1, criteriaProvider1);

        Criteria criteria2 = new Criteria();
        criteria2.setPowerRequirement(Criteria.POWER_LOW);
        String criteriaProvider2 = mLocationManager.getBestProvider(criteria2, true);
        LogUtil.d(TAG, criteria2, criteriaProvider2);

        LogUtil.d(TAG, "===============================================");


//        PendingIntent pendingIntent4 = PendingIntent.getActivity(this, 4, new Intent(LMTesterAct.this, TesterAct.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 4F, pendingIntent4);

//        Criteria criteria5 = new Criteria();
//        criteria5.setPowerRequirement(Criteria.ACCURACY_FINE);
//        PendingIntent pendingIntent5 = PendingIntent.getActivity(this, 5, new Intent(LMTesterAct.this, TesterAct.class), PendingIntent.FLAG_ONE_SHOT);
//        mLocationManager.requestLocationUpdates(5000, 5F, criteria5, pendingIntent5);


        LogUtil.d(TAG, "===============================================");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION4);
        intentFilter.addAction(INTENT_ACTION5);
        intentFilter.addAction(ACTION_PROXIMITY_ALERT);
        intentFilter.addAction(LocationManager.MODE_CHANGED_ACTION);
        intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeAll();
    }

    public void removeAll(View view) {
        removeAll();
    }

    public void requestUpdatesAPI1(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, mGnssLocationListener1);
        }
    }

    public void requestUpdatesAPI2(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2F, mGnssLocationListener2, Looper.getMainLooper());
        }
    }

    public void requestUpdatesAPI3(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Criteria criteria3 = new Criteria();
            criteria3.setPowerRequirement(Criteria.ACCURACY_HIGH);
            criteria3.setPowerRequirement(Criteria.POWER_HIGH);
            mLocationManager.requestLocationUpdates(3000, 3F, criteria3, mGnssLocationListener3, Looper.getMainLooper());
        }
    }

    public void requestUpdatesAPI4(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent4 = new Intent();
            intent4.setAction(INTENT_ACTION4);
            PendingIntent pendingIntent4 = PendingIntent.getBroadcast(this, 4, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 4F, pendingIntent4);

        }
    }

    public void requestUpdatesAPI5(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Intent intent5 = new Intent();
            intent5.setAction(INTENT_ACTION5);
            Criteria criteria5 = new Criteria();
            criteria5.setPowerRequirement(Criteria.ACCURACY_FINE);
            criteria5.setPowerRequirement(Criteria.POWER_HIGH);
            PendingIntent pendingIntent5 = PendingIntent.getBroadcast(this, 5, intent5, PendingIntent.FLAG_ONE_SHOT);
            mLocationManager.requestLocationUpdates(5000, 5F, criteria5, pendingIntent5);
        }
    }

    public void requestUpdatesNetwork(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1F, mNetworkLocationListener);
        }
    }

    public void requestUpdatesPassive(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 1F, mPassiveLocationListener);
        }
    }

    public void requestUpdatesFused(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                mLocationManager.requestLocationUpdates("fused", 1000, 1F, mFusedLocationListener);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesFused", e);
            }
        }
    }

    public void requestUpdatesMock(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                mLocationManager.requestLocationUpdates("mock", 1000, 1F, mMockLocationListener);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesMock", e);
            }
        }
    }

    public void addProximityAlert(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                Intent intent = new Intent();
                intent.setAction(ACTION_PROXIMITY_ALERT);
                mProximityAlertIntent = PendingIntent.getBroadcast(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mLocationManager.addProximityAlert(31.065395, 121.394601, 1000F, -1, mProximityAlertIntent);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesMock", e);
            }
        }
    }

    public void removeProximityAlert(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mProximityAlertIntent != null) mLocationManager.removeProximityAlert(mProximityAlertIntent);
        }
    }

    public void addTestProvider(View view) {
        String name = LocationManager.GPS_PROVIDER;
        mLocationManager.addTestProvider(name, false, true, false, false, true, true, true, Criteria.POWER_HIGH, Criteria.ACCURACY_HIGH);

        mLocationManager.setTestProviderEnabled(name, true);
    }

    public void setTestProviderLocation(View view) {
        String name = LocationManager.GPS_PROVIDER;
        Location location = new Location(name);
        location.setLatitude(31.065395);
        location.setLongitude(121.394601);
        location.setAccuracy(20F);
        location.setAltitude(100D);
        location.setBearing(181F);
        location.setSpeed(2.5F);
        location.setTime(System.currentTimeMillis());
        // 时间单位一定要正确，否则会导致其他问题
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setExtras(null);
        mLocationManager.setTestProviderLocation(name, location);
    }

    public void removeTestProvider(View view) {
        mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
    }

    private void removeAll() {
        mLocationManager.removeUpdates(mGnssLocationListener1);
        mLocationManager.removeUpdates(mGnssLocationListener2);
        mLocationManager.removeUpdates(mGnssLocationListener3);
        mLocationManager.removeUpdates(mGnssLocationListener4);
        mLocationManager.removeUpdates(mGnssLocationListener5);
        mLocationManager.removeUpdates(mNetworkLocationListener);
        mLocationManager.removeUpdates(mPassiveLocationListener);
        mLocationManager.removeUpdates(mFusedLocationListener);
        mLocationManager.removeUpdates(mMockLocationListener);
    }

    private class MyLocationListener implements LocationListener {

        private String mName;

        MyLocationListener(String name) {
            mName = name;
        }
        @Override
        public void onLocationChanged(final Location location) {
            LogUtil.d(TAG, mName, "onLocationChanged", location);
        }

        @Override
        public void onProviderDisabled(String s) {
            LogUtil.d(TAG, mName, "onProviderDisabled:", s);
        }

        @Override
        public void onProviderEnabled(String s) {
            LogUtil.d(TAG, mName, "onProviderEnabled:", s);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            LogUtil.d(TAG, mName, "onStatusChanged:", s, "status:", i);
            if (bundle != null) {
                Set<String> set = bundle.keySet();
                for (String key : set) {
                    Object value = bundle.get(key);
                    LogUtil.d(TAG, mName, key, value == null ? "NULL" : value.toString());
                }
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        private static final String SUB_TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (LocationManager.MODE_CHANGED_ACTION.equals(intent.getAction())) {
                LogUtil.d(TAG, SUB_TAG, "location service status changed");
            } else if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                String extra = "NULL";
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    extra = intent.getStringExtra(LocationManager.EXTRA_PROVIDER_NAME);
                }
                LogUtil.d(TAG, SUB_TAG, "provider changed", extra);
            } else if (ACTION_PROXIMITY_ALERT.equals(intent.getAction())) {
                boolean extra = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
                LogUtil.d(TAG, SUB_TAG, "Proximity alert, enter in:", extra);
            } else {
                if (INTENT_ACTION4.equals(intent.getAction())) {
                    LogUtil.d(TAG, SUB_TAG, "intent4");
                } else if (INTENT_ACTION5.equals(intent.getAction())) {
                    LogUtil.d(TAG, SUB_TAG, "intent5");
                }
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Location location = (Location) bundle.get(LocationManager.KEY_LOCATION_CHANGED);
                    boolean enabled = bundle.getBoolean(LocationManager.KEY_PROVIDER_ENABLED);
                    String statusChanged = bundle.getString(LocationManager.KEY_STATUS_CHANGED);
                    LogUtil.d(TAG, SUB_TAG, location, enabled, statusChanged);

                } else {
                    LogUtil.d(TAG, SUB_TAG, "NULL");
                }
            }
        }
    }
}
