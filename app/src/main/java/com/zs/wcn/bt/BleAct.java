package com.zs.wcn.bt;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAssignedNumbers;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertisingSet;
import android.bluetooth.le.AdvertisingSetCallback;
import android.bluetooth.le.AdvertisingSetParameters;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.PeriodicAdvertisingParameters;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.View;

import com.zs.wcn.R;
import com.zs.wcn.base.BaseActivity;
import com.zs.wcn.utils.BtUtil;
import com.zs.wcn.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class BleAct extends BaseActivity {

    private BluetoothAdapter mAdapter;

    private BluetoothLeAdvertiser mBleAdvertiser;
    private AdvertiseCallback mAdvertiseCb = new AdvertiseCb();
    private AdvertisingSetCallback mAdvertisingSetCb = new AdvertisingSetCb();

    private BluetoothLeScanner mBleScanner;
    private ScanCallback mScanResultCb = new ScanResultCb();

    private LeScanResultCb mLeScanResultCb = new LeScanResultCb();

    private ParcelUuid mUuid = ParcelUuid.fromString("0000110e-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_ble_advertise);

        mAdapter = BtUtil.getInstance(this);
        mBleAdvertiser = mAdapter.getBluetoothLeAdvertiser();
        mBleScanner = mAdapter.getBluetoothLeScanner();
    }

    public void startAdvertising(View v) {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTimeout(10_000)
                .setConnectable(true)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
                .build();
        AdvertiseData data = new AdvertiseData.Builder()
                .addManufacturerData(BluetoothAssignedNumbers.QUALCOMM, new byte[] {(byte) 1})
                .addServiceData(mUuid, new byte[] {(byte) 1, (byte) 0})
                .addServiceUuid(mUuid)
                .setIncludeDeviceName(true)
                .setIncludeTxPowerLevel(true)
                .build();
        mBleAdvertiser.startAdvertising(settings, data, mAdvertiseCb);
    }

    public void stopAdvertising(View v) {
        mBleAdvertiser.stopAdvertising(mAdvertiseCb);
    }

    public void startAdvertisingSet(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AdvertisingSetParameters parameters = new AdvertisingSetParameters.Builder()
                    .setAnonymous(false)
                    .setConnectable(false)
                    .setIncludeTxPower(false)
                    .setLegacyMode(false)
                    .setScannable(false)
                    .setPrimaryPhy(BluetoothDevice.PHY_LE_1M)
                    .setSecondaryPhy(BluetoothDevice.PHY_LE_1M)
                    .setTxPowerLevel(AdvertisingSetParameters.TX_POWER_MEDIUM)
                    .setInterval(AdvertisingSetParameters.INTERVAL_LOW)
                    .build();
            AdvertiseData data = new AdvertiseData.Builder()
                    .addManufacturerData(BluetoothAssignedNumbers.QUALCOMM, new byte[] {(byte) 1})
                    .addServiceData(mUuid, new byte[] {(byte) 1, (byte) 0})
                    .addServiceUuid(mUuid)
                    .setIncludeDeviceName(true)
                    .setIncludeTxPowerLevel(true)
                    .build();
            AdvertiseData response = new AdvertiseData.Builder()
                    .addManufacturerData(BluetoothAssignedNumbers.QUALCOMM, new byte[] {(byte) 1})
                    .addServiceData(mUuid, new byte[] {(byte) 1, (byte) 0})
                    .addServiceUuid(mUuid)
                    .setIncludeDeviceName(true)
                    .setIncludeTxPowerLevel(true)
                    .build();
            PeriodicAdvertisingParameters params = new PeriodicAdvertisingParameters.Builder()
                    .setIncludeTxPower(false)
                    .setInterval(800)
                    .build();
            AdvertiseData periodicData = new AdvertiseData.Builder()
                    .addManufacturerData(BluetoothAssignedNumbers.QUALCOMM, new byte[] {(byte) 1})
                    .addServiceData(mUuid, new byte[] {(byte) 1, (byte) 0})
                    .addServiceUuid(mUuid)
                    .setIncludeDeviceName(true)
                    .setIncludeTxPowerLevel(true)
                    .build();
            mBleAdvertiser.startAdvertisingSet(parameters, data, response, params, periodicData,
                    100, 100, mAdvertisingSetCb);
        } else {
            LogUtil.w(mTag, "sdk version < 26, not support");
        }
    }

    public void stopAdvertisingSet(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBleAdvertiser.stopAdvertisingSet(mAdvertisingSetCb);
        } else {
            LogUtil.w(mTag, "sdk version < 26, not support");
        }
    }

    public void startScan(View v) {
        List<ScanFilter> list = new ArrayList<>();
        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid(mUuid)
                .build();
        list.add(filter);
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .build();
        mBleScanner.startScan(list, settings, mScanResultCb);
    }

    public void stopScan(View v) {
        mBleScanner.startScan(mScanResultCb);
    }

    public void flushPendingScanResults(View v) {
        mBleScanner.flushPendingScanResults(mScanResultCb);
    }

    public void startLeScan(View v) {
        mAdapter.startLeScan(mLeScanResultCb);
    }

    public void stopLeScan(View v) {
        mAdapter.stopLeScan(mLeScanResultCb);
    }

    private static class AdvertiseCb extends AdvertiseCallback {

        private static final String TAG = AdvertiseCb.class.getSimpleName();

        public AdvertiseCb() {
            super();
        }

        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            LogUtil.d(TAG, "ble advertise cb:", settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            LogUtil.w(TAG, "ble advertise failed code:", errorCode);
        }
    }

    @TargetApi(26)
    private static class AdvertisingSetCb extends AdvertisingSetCallback {

        private static final String TAG = AdvertisingSetCb.class.getSimpleName();

        public AdvertisingSetCb() {
            super();
        }

        @Override
        public void onAdvertisingSetStarted(AdvertisingSet advertisingSet, int txPower, int status) {
            super.onAdvertisingSetStarted(advertisingSet, txPower, status);
            LogUtil.d(TAG, "ble advertising set cb started:", advertisingSet, txPower, status);
        }

        @Override
        public void onAdvertisingSetStopped(AdvertisingSet advertisingSet) {
            super.onAdvertisingSetStopped(advertisingSet);
            LogUtil.d(TAG, "ble advertising set cb stopped:", advertisingSet);
        }

        @Override
        public void onAdvertisingEnabled(AdvertisingSet advertisingSet, boolean enable, int status) {
            super.onAdvertisingEnabled(advertisingSet, enable, status);
            LogUtil.d(TAG, "ble advertising set cb enabled:", advertisingSet, enable, status);
        }

        @Override
        public void onAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
            super.onAdvertisingDataSet(advertisingSet, status);
            LogUtil.d(TAG, "ble advertising set cb data:", advertisingSet, status);
        }

        @Override
        public void onScanResponseDataSet(AdvertisingSet advertisingSet, int status) {
            super.onScanResponseDataSet(advertisingSet, status);
            LogUtil.d(TAG, "ble advertising set cb response data:", advertisingSet, status);
        }

        @Override
        public void onAdvertisingParametersUpdated(AdvertisingSet advertisingSet, int txPower, int status) {
            super.onAdvertisingParametersUpdated(advertisingSet, txPower, status);
            LogUtil.d(TAG, "ble advertising set cb param updated:", advertisingSet, txPower, status);
        }

        @Override
        public void onPeriodicAdvertisingParametersUpdated(AdvertisingSet advertisingSet, int status) {
            super.onPeriodicAdvertisingParametersUpdated(advertisingSet, status);
            LogUtil.d(TAG, "ble advertising set cb period param updated:", advertisingSet, status);
        }

        @Override
        public void onPeriodicAdvertisingDataSet(AdvertisingSet advertisingSet, int status) {
            super.onPeriodicAdvertisingDataSet(advertisingSet, status);
            LogUtil.d(TAG, "ble advertising set cb period data:", advertisingSet, status);
        }

        @Override
        public void onPeriodicAdvertisingEnabled(AdvertisingSet advertisingSet, boolean enable, int status) {
            super.onPeriodicAdvertisingEnabled(advertisingSet, enable, status);
            LogUtil.d(TAG, "ble advertising set cb period enabled:", advertisingSet, enable, status);
        }
    }

    private static class ScanResultCb extends ScanCallback {

        private static final String TAG = ScanResultCb.class.getSimpleName();

        public ScanResultCb() {
            super();
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            LogUtil.d(TAG, "ble scan cb result:", callbackType, result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            LogUtil.d(TAG, "ble scan cb batch results:", results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtil.w(TAG, "ble scan failed code:", errorCode);
        }
    }

    private static class LeScanResultCb implements BluetoothAdapter.LeScanCallback {

        private static final String TAG = ScanResultCb.class.getSimpleName();

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            LogUtil.d(TAG, device, rssi, scanRecord);
        }
    }
}
