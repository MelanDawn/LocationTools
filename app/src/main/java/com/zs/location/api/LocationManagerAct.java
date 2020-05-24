package com.zs.location.api;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Criteria;
import android.location.GnssMeasurementsEvent;
import android.location.GnssNavigationMessage;
import android.location.GnssStatus;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.zs.location.R;
import com.zs.location.base.BaseActivity;
import com.zs.location.utils.BundleUtil;
import com.zs.location.utils.LogUtil;
import com.zs.location.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;

public class LocationManagerAct extends BaseActivity {

    private static final String INTENT_ACTION4 = "com.zs.location.GNSS_PENDING_INTENT4";
    private static final String INTENT_ACTION5 = "com.zs.location.GNSS_PENDING_INTENT5";
    private static final String ACTION_PROXIMITY_ALERT = "com.zs.location.PROXIMITY_ALERT";

    private static final String CMD_DELETE_AIDING_DATA = "delete_aiding_data";
    private static final String EXTRA_EPHEMERIS = "ephemeris";
    private static final String EXTRA_ALMANAC = "almanac";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_TIME = "time";
    private static final String EXTRA_IONO = "iono";
    private static final String EXTRA_UTC = "utc";
    private static final String EXTRA_SVDIR = "svdir";
    private static final String EXTRA_SVSTEER = "svsteer";
    private static final String EXTRA_SADATA = "sadata";
    private static final String EXTRA_RTI = "rti";
    private static final String EXTRA_CELLDB_INFO = "celldb-info";

    private MyBroadcastReceiver mReceiver = new MyBroadcastReceiver();

    private LocationManager mLocationManager;

    private LocationListener mGnssLocationListener1 = new MyLocationListener("GNSS1");
    private LocationListener mGnssLocationListener2 = new MyLocationListener("GNSS2");
    private LocationListener mGnssLocationListener3 = new MyLocationListener("GNSS3");
    private LocationListener mGnssLocationListener4 = new MyLocationListener("GNSS4");
    private LocationListener mGnssLocationListener5 = new MyLocationListener("GNSS5");
    private LocationListener mNetworkLocationListener1 = new MyLocationListener("NETWORK1");
    private LocationListener mNetworkLocationListener2 = new MyLocationListener("NETWORK2");
    private LocationListener mNetworkLocationListener3 = new MyLocationListener("NETWORK3");
    private LocationListener mNetworkLocationListener4 = new MyLocationListener("NETWORK4");
    private LocationListener mPassiveLocationListener = new MyLocationListener("PASSIVE");
    private LocationListener mFusedLocationListener = new MyLocationListener("FUSED");
    private LocationListener mMockLocationListener = new MyLocationListener("MOCK");

    private PendingIntent mProximityAlertIntent = null;

    // GNSS/GPS Status and NMEA call back
    private GnssStatus.Callback mGnssStatusListener;
    private OnNmeaMessageListener mGnssNmeaListener;
    private GpsStatus.Listener mGpsStatusListener;
//    private GpsStatus.NmeaListener mGpsNmeaListener;
    private GnssMeasurementsEvent.Callback mGnssMeasurementsEventCb;
    private GnssNavigationMessage.Callback mGnssNavigationMessageCb;

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_location_manager);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        printSimpleMethod();
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
//        removeAll();
    }

    public void removeAll(View view) {
        removeAll();
    }

    /************************************************************
     ****************** Location Request *******************
     ************************************************************/
    public void requestUpdatesAPI1(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, mGnssLocationListener1);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (PermissionUtil.checkLocationPermission(LocationManagerAct.this)) {
//                    LogUtil.d(TAG, "looper:", Looper.myLooper());
//                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, mGnssLocationListener1);
//                } else {
//                    LogUtil.w(TAG, "no location permission");
//                }
//            }
//        }).start();
    }

    public void requestUpdatesAPI2(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 2F, mGnssLocationListener2, mHandler.getLooper());
        }
    }

    public void requestUpdatesAPI3(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            Criteria criteria3 = new Criteria();
            criteria3.setAccuracy(Criteria.ACCURACY_FINE);
            criteria3.setPowerRequirement(Criteria.POWER_HIGH);
            mLocationManager.requestLocationUpdates(3000, 3F, criteria3, mGnssLocationListener3, mHandler.getLooper());
        }
    }

    public void requestUpdatesAPI4(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            Intent intent4 = new Intent();
            intent4.setAction(INTENT_ACTION4);
            PendingIntent pendingIntent4 = PendingIntent.getBroadcast(this, 4, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 4F, pendingIntent4);

        }
    }

    public void requestUpdatesAPI5(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            Intent intent5 = new Intent();
            intent5.setAction(INTENT_ACTION5);
            Criteria criteria5 = new Criteria();
            criteria5.setAccuracy(Criteria.ACCURACY_FINE);
            criteria5.setPowerRequirement(Criteria.POWER_HIGH);
            PendingIntent pendingIntent5 = PendingIntent.getBroadcast(this, 5, intent5, PendingIntent.FLAG_UPDATE_CURRENT);
            mLocationManager.requestLocationUpdates(5000, 5F, criteria5, pendingIntent5);
        }
    }

    public void requestUpdatesNetwork1(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1F, mNetworkLocationListener1);
        }
    }

    public void requestUpdatesNetwork2(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30_000, 1F, mNetworkLocationListener2);
        }
    }

    public void requestUpdatesNetwork3(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40_000, 1F, mNetworkLocationListener3);
        }
    }

    public void requestUpdatesNetwork4(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50_000, 1F, mNetworkLocationListener4);
        }
    }

    public void requestUpdatesPassive(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 1F, mPassiveLocationListener);
        }
    }

    public void requestUpdatesFused(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            try {
                mLocationManager.requestLocationUpdates("fused", 1000, 1F, mFusedLocationListener);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesFused", e);
            }
        }
    }

    public void requestUpdatesMock(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            try {
                mLocationManager.requestLocationUpdates("mock", 1000, 1F, mMockLocationListener);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesMock", e);
            }
        }
    }
    // Android R add
    // requestLocationUpdates(long minTimeMs, float minDistanceM, Criteria criteria, Executor executor, LocationListener listener)
    // requestLocationUpdates(String provider, long minTimeMs, float minDistanceM, Executor executor, LocationListener listener)

    // 参数比 requestLocationUpdates() 方法少，接收一次结果后，系统会将该请求移除
    // requestSingleUpdate()

    /************************************************************
     ****************** Send Extra Command *******************
     ************************************************************/
    public void sendExtraCommand(View view) {
        // ACCESS_LOCATION_EXTRA_COMMANDS
            Bundle b = new Bundle();
            b.putBoolean(EXTRA_EPHEMERIS, true);
            b.putBoolean(EXTRA_ALMANAC, true);
            b.putBoolean(EXTRA_POSITION, true);
            b.putBoolean(EXTRA_TIME, true);
            b.putBoolean(EXTRA_IONO, true);
            b.putBoolean(EXTRA_UTC, true);
            b.putBoolean(EXTRA_SVDIR, true);
            b.putBoolean(EXTRA_SVSTEER, true);
            b.putBoolean(EXTRA_SADATA, true);
            b.putBoolean(EXTRA_RTI, true);
            b.putBoolean(EXTRA_CELLDB_INFO, true);

        mLocationManager.sendExtraCommand(LocationManager.GPS_PROVIDER, CMD_DELETE_AIDING_DATA, b);
    }


    /************************************************************
     ****************** NMEA Listener *******************
     ************************************************************/
    public void addNmeaListener(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            //Android 7.0中 GnssStatusCb.Callback部分接口无法回调，Android 7.1修复，API25用新接口
            LogUtil.i(TAG, "SDK version: " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                mGnssNmeaListener = new GnssNmea();
                if (mLocationManager.addNmeaListener(mGnssNmeaListener, mHandler))
                    LogUtil.d(TAG, "Gnss nmea register succeed");
            /*} else {
                mGpsNmeaListener = new GpsNmea();
                // removed at API 29 Android Q
                if (mLocationManager.addNmeaListener(mGpsNmeaListener))
                    LogUtil.d(TAG, "Gps nmea register succeed");*/
            }
        }
    }

    @TargetApi(24)
    private class GnssNmea implements OnNmeaMessageListener {
        @Override
        public void onNmeaMessage(String s, long l) {
            LogUtil.d(TAG, l, ":", s);
        }
    }

    private class GpsNmea implements GpsStatus.NmeaListener {
        @Override
        public void onNmeaReceived(long l, String s) {
            LogUtil.d(TAG, l, ":", s);
        }
    }

    /************************************************************
     ****************** GNSS/GPS Status *******************
     ************************************************************/
    public void registerGnssStatusCallback(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            //Android 7.0中 GnssStatusCb.Callback部分接口无法回调，Android 7.1修复，API25用新接口
            LogUtil.i(TAG, "SDK version: " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                mGnssStatusListener = new GnssStatusCb();
                if (mLocationManager.registerGnssStatusCallback(mGnssStatusListener, mHandler))
                    LogUtil.d(TAG, "Gnss status register succeed");
            } else {
                mGpsStatusListener = new GpsStatusCb();
                if (mLocationManager.addGpsStatusListener(mGpsStatusListener))
                    LogUtil.d(TAG, "Gps info register succeed");
            }
        }
    }
    @TargetApi(24)
    private class GnssStatusCb extends GnssStatus.Callback{

        @Override
        public void onFirstFix(int ttffMillis) {
            LogUtil.i(TAG, "navigation onFirstFix:", ttffMillis);
            super.onFirstFix(ttffMillis);
        }

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            super.onSatelliteStatusChanged(status);
            LogUtil.i(TAG, "navigation onSatelliteStatusChanged:", status);
        }

        @Override
        public void onStarted() {
            super.onStarted();
            LogUtil.i(TAG, "navigation onStarted");
        }

        @Override
        public void onStopped() {
            super.onStopped();
            LogUtil.i(TAG, "navigation onStopped");
        }
    }

    private class GpsStatusCb implements GpsStatus.Listener {

        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    if (PermissionUtil.checkLocationPermission(LocationManagerAct.this)) {
                        GpsStatus status = mLocationManager.getGpsStatus(null);
                        LogUtil.d(TAG, status, "ttff:", status.getTimeToFirstFix());
                    } else {
                        LogUtil.w(TAG, "no location permission");
                    }
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    if (PermissionUtil.checkLocationPermission(LocationManagerAct.this)) {
                        GpsStatus status = mLocationManager.getGpsStatus(null);
                        LogUtil.d(TAG, status, "maxSatellites:", status.getMaxSatellites());
                    } else {
                        LogUtil.w(TAG, "no location permission");
                    }
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i(TAG, "定位启动");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i(TAG, "定位结束");
                    break;
            }
        }
    }

    /************************************************************
     ****************** Navigation Message *******************
     ************************************************************/
    public void registerGnssNavigationMessageCallback(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            //Android 7.0中 GnssStatusCb.Callback部分接口无法回调，Android 7.1修复，API25用新接口
            LogUtil.i(TAG, "SDK version: " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                mGnssNavigationMessageCb = new GnssNavigationMessageCb();
                if (mLocationManager.registerGnssNavigationMessageCallback(mGnssNavigationMessageCb, mHandler)) {
                    LogUtil.d(TAG, "Gnss navigation message register succeed");
                }
            }
        }
    }
    @TargetApi(24)
    private class GnssNavigationMessageCb extends GnssNavigationMessage.Callback {

        @Override
        public void onGnssNavigationMessageReceived(GnssNavigationMessage event) {
            super.onGnssNavigationMessageReceived(event);
            LogUtil.d(TAG, "GnssNavigationMessageCb", "GnssNavigationMessage:", event.toString());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getMessageId:", event.getMessageId());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getSubmessageId:", event.getSubmessageId());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getStatus:", event.getStatus());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getSvid:", event.getSvid());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getType:", event.getType());
            LogUtil.d(TAG, "GnssNavigationMessageCb", "getData:", event.getData());
        }

        @Override
        public void onStatusChanged(int status) {
            super.onStatusChanged(status);
            LogUtil.d(TAG, "GnssNavigationMessageCb", "status:", statusToString(status));
        }

        private String statusToString(int status) {
            String result = "unknown";
            switch (status) {
                case GnssNavigationMessage.Callback.STATUS_NOT_SUPPORTED:
                    result = "STATUS_NOT_SUPPORTED";
                    break;
                case GnssNavigationMessage.Callback.STATUS_READY:
                    result = "STATUS_READY";
                    break;
                case GnssNavigationMessage.Callback.STATUS_LOCATION_DISABLED:
                    result = "STATUS_LOCATION_DISABLED";
                    break;
            }
            return result;
        }

    }

    /************************************************************
     ****************** Measurement Event *******************
     ************************************************************/
    public void registerGnssMeasurementsCallback(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            //Android 7.0中 GnssStatusCb.Callback部分接口无法回调，Android 7.1修复，API25用新接口
            LogUtil.i(TAG, "SDK version: " + Build.VERSION.SDK_INT);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                mGnssMeasurementsEventCb = new GnssMeasurementEventCb();
                if (mLocationManager.registerGnssMeasurementsCallback(mGnssMeasurementsEventCb, mHandler)) {
                    LogUtil.d(TAG, "Gnss measurement event register succeed");
                }
            }
        }
    }
    @TargetApi(24)
    private class GnssMeasurementEventCb extends GnssMeasurementsEvent.Callback {

        @Override
        public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
            super.onGnssMeasurementsReceived(eventArgs);
            LogUtil.d(TAG, "GnssMeasurementEventCb", "GnssMeasurementsEvent:", eventArgs.toString());
            LogUtil.d(TAG, "GnssMeasurementEventCb", "GnssClock:", eventArgs.getClock());
            LogUtil.d(TAG, "GnssMeasurementEventCb", "GnssMeasurement list:", eventArgs.getMeasurements());
        }


        @Override
        public void onStatusChanged(int status) {
            super.onStatusChanged(status);
            LogUtil.d(TAG, "GnssMeasurementEventCb", "status:", statusToString(status));
        }

        private String statusToString(int status) {
            String result = "unknown";
            switch (status) {
                case GnssMeasurementsEvent.Callback.STATUS_NOT_SUPPORTED:
                    result = "STATUS_NOT_SUPPORTED";
                    break;
                case GnssMeasurementsEvent.Callback.STATUS_READY:
                    result = "STATUS_READY";
                    break;
                case GnssMeasurementsEvent.Callback.STATUS_LOCATION_DISABLED:
                    result = "STATUS_LOCATION_DISABLED";
                    break;
                case GnssMeasurementsEvent.Callback.STATUS_NOT_ALLOWED:
                    result = "STATUS_NOT_ALLOWED";
                    break;
            }
            return result;
        }
    }

    /************************************************************
     ****************** ProximityAlert (Geofence) *******************
     ************************************************************/
    public void addProximityAlert(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            try {
                Intent intent = new Intent();
                intent.setAction(ACTION_PROXIMITY_ALERT);
                mProximityAlertIntent = PendingIntent.getBroadcast(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                mLocationManager.addProximityAlert(31.065395, 121.394601, 1000F, -1, mProximityAlertIntent);
                mLocationManager.addProximityAlert(30, 120, 100F, -1, mProximityAlertIntent);
            } catch (Exception e) {
                LogUtil.e(TAG, "requestUpdatesMock", e);
            }
        }
    }

    public void removeProximityAlert(View view) {
        if (PermissionUtil.checkLocationPermission(this)) {
            if (mProximityAlertIntent != null) mLocationManager.removeProximityAlert(mProximityAlertIntent);
        }
    }


    /************************************************************
     ****************** Test Provider (Mock) *******************
     ************************************************************/
    public void addTestProvider(View view) {
        String name = LocationManager.GPS_PROVIDER;
        mLocationManager.addTestProvider(name, true, true, false, false, true, true, true, Criteria.POWER_HIGH, Criteria.ACCURACY_HIGH);

        mLocationManager.setTestProviderEnabled(name, true);
    }

    public void setTestProviderLocation(View view) {
        String name = LocationManager.GPS_PROVIDER;
        Location location = new Location(name);
        location.setLatitude(31.065395);
        location.setLongitude(121.394601);
        location.setLatitude(30D);
        location.setLongitude(120D);
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
    // Deprecated method for test provider
    // clearTestProviderEnabled()
    // clearTestProviderLocation()
    // clearTestProviderStatus()
    // setTestProviderStatus()

    private void removeAll() {
        mLocationManager.removeUpdates(mGnssLocationListener1);
        mLocationManager.removeUpdates(mGnssLocationListener2);
        mLocationManager.removeUpdates(mGnssLocationListener3);
        mLocationManager.removeUpdates(mGnssLocationListener4);
        mLocationManager.removeUpdates(mGnssLocationListener5);
        mLocationManager.removeUpdates(mNetworkLocationListener1);
        mLocationManager.removeUpdates(mNetworkLocationListener2);
        mLocationManager.removeUpdates(mNetworkLocationListener3);
        mLocationManager.removeUpdates(mNetworkLocationListener4);
        mLocationManager.removeUpdates(mPassiveLocationListener);
        mLocationManager.removeUpdates(mFusedLocationListener);
        mLocationManager.removeUpdates(mMockLocationListener);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            if (mGnssNmeaListener != null) {
                mLocationManager.removeNmeaListener(mGnssNmeaListener);
                mGnssNmeaListener = null;
            }
            if (mGnssStatusListener != null) {
                mLocationManager.unregisterGnssStatusCallback(mGnssStatusListener);
                mGnssStatusListener = null;
            }
            if (mGnssMeasurementsEventCb != null) {
                mLocationManager.unregisterGnssMeasurementsCallback(mGnssMeasurementsEventCb);
                mGnssMeasurementsEventCb = null;
            }
            if (mGnssNavigationMessageCb != null) {
                mLocationManager.unregisterGnssNavigationMessageCallback(mGnssNavigationMessageCb);
                mGnssNavigationMessageCb = null;
            }
        } else {
            /*if (mGpsNmeaListener != null) {
                // removed at API 29 Android Q
                mLocationManager.removeNmeaListener(mGpsNmeaListener);
                mGpsNmeaListener = null;
            }*/
            if (mGpsStatusListener != null) {
                mLocationManager.removeGpsStatusListener(mGpsStatusListener);
                mGpsStatusListener = null;
            }
        }
    }

    private class MyLocationListener implements LocationListener {

        private String mName;

        MyLocationListener(String name) {
            mName = name;
        }

        @Override
        public void onLocationChanged(final Location location) {
            LogUtil.d(TAG, mName, "onLocationChanged", location);
            if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
                Bundle bundle = location.getExtras();
                LogUtil.d(TAG, "location extra:", BundleUtil.getContent(bundle));
                if (bundle != null) {
                    Address address = (Address) bundle.get("address");
                    if (address != null) {
                        LogUtil.d(TAG, "address extra:", BundleUtil.getContent(bundle));
                    }
                }
            }
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
            LogUtil.d(TAG, "action：", intent.getAction());
            if (LocationManager.MODE_CHANGED_ACTION.equals(intent.getAction())) {
                LogUtil.d(TAG, SUB_TAG, "location service status changed");
            } else if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                String extra = "NULL";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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

    /************************************************************
     ****************** Simple Method *******************
     ************************************************************/
    private void printSimpleMethod() {
        LogUtil.e(TAG, "method start with IS");
        LogUtil.d(TAG, LocationManager.GPS_PROVIDER, "enabled:", mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LogUtil.d(TAG, "Location Service enabled:", mLocationManager.isLocationEnabled(), "ADD API 28 Android P");
        }
        LogUtil.d(TAG, "===============================================");

        LogUtil.e(TAG, "method start with GET");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            LogUtil.d(TAG, "GnssHardwareModelName:", mLocationManager.getGnssHardwareModelName(), "ADD API 28 Android P");
            LogUtil.d(TAG, "GnssYearOfHardware:", mLocationManager.getGnssYearOfHardware(), "ADD API 28 Android P");

        }
        LogUtil.d(TAG, "===============================================");

        List<String> allProviders = new ArrayList<>();
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

            if (PermissionUtil.checkLocationPermission(this)) {
                LogUtil.d(TAG, "LastKnownLocation:", mLocationManager.getLastKnownLocation(name));
            }
            LogUtil.d(TAG, "----------------------");
        }
        LogUtil.d(TAG, "===============================================");

        List<String> enabledProviders = mLocationManager.getProviders(true);
        for (String provider: enabledProviders) {
            LogUtil.d(TAG, "enabled provider:", provider);
        }
        LogUtil.d(TAG, "===============================================");
        List<String> allProviderList = mLocationManager.getProviders(false);
        for (String provider: allProviderList) {
            LogUtil.d(TAG, "all provider:", provider, "enabled:", mLocationManager.isProviderEnabled(provider));
        }
        LogUtil.d(TAG, "===============================================");

        Criteria criteria3 = new Criteria();
        criteria3.setPowerRequirement(Criteria.POWER_LOW);
        List<String> providers = mLocationManager.getProviders(criteria3, false);
        for (String name : providers) {
            LogUtil.d(TAG, "power low provider:", name, "enabled:", mLocationManager.isProviderEnabled(name));
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

//        mLocationManager.getGnssCapabilities();
//        mLocationManager.getCurrentLocation();

        if (PermissionUtil.checkLocationPermission(this)) {
            LogUtil.d(TAG, mLocationManager.getGpsStatus(null), "Deprecated API 24 Android N");
        }
        LogUtil.d(TAG, "===============================================");

        LocationProvider networkProvider = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        LogUtil.d(TAG, "getName:", networkProvider.getName());
        LogUtil.d(TAG, "requiresNetwork:", networkProvider.requiresNetwork());
        LogUtil.d(TAG, "requiresSatellite:", networkProvider.requiresSatellite());
        LogUtil.d(TAG, "requiresCell:", networkProvider.requiresCell());
        LogUtil.d(TAG, "hasMonetaryCost:", networkProvider.hasMonetaryCost());
        LogUtil.d(TAG, "supportsAltitude:", networkProvider.supportsAltitude());
        LogUtil.d(TAG, "supportsSpeed:", networkProvider.supportsSpeed());
        LogUtil.d(TAG, "supportsBearing:", networkProvider.supportsBearing());
        LogUtil.d(TAG, "getPowerRequirement:", getPowerRequirement(networkProvider.getPowerRequirement()));
        LogUtil.d(TAG, "getAccuracy:", getAccuracy(networkProvider.getAccuracy()));

        LogUtil.d(TAG, "===============================================");

//        PendingIntent pendingIntent4 = PendingIntent.getActivity(this, 4, new Intent(LocationManagerAct.this, TesterAct.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 4F, pendingIntent4);
//
//        Criteria criteria5 = new Criteria();
//        criteria5.setPowerRequirement(Criteria.ACCURACY_FINE);
//        PendingIntent pendingIntent5 = PendingIntent.getActivity(this, 5, new Intent(LocationManagerAct.this, TesterAct.class), PendingIntent.FLAG_ONE_SHOT);
//        mLocationManager.requestLocationUpdates(5000, 5F, criteria5, pendingIntent5);
    }

    private String getPowerRequirement(int requirement) {
        switch (requirement) {
            case Criteria.POWER_LOW:
                return "Criteria.POWER_LOW";
            case Criteria.POWER_MEDIUM:
                return "Criteria.POWER_MEDIUM";
            case Criteria.POWER_HIGH:
                return "Criteria.POWER_HIGH";
            case Criteria.NO_REQUIREMENT:
                return "Criteria.NO_REQUIREMENT";
        }
        return "UNKNOWN";
    }

    private String getAccuracy(int accuracy) {
        if (Criteria.ACCURACY_FINE == accuracy) {
            return "Criteria.ACCURACY_FINE";
        } else if (Criteria.ACCURACY_COARSE == accuracy) {
            return "Criteria.ACCURACY_COARSE";
        }
        return "UNKNOWN";
    }
}