package com.zs.location.telephony;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.zs.location.App;
import com.zs.location.utils.LogUtil;

import java.util.List;

import androidx.core.content.ContextCompat;

public class Telephony {

    private static  final String TAG = Telephony.class.getSimpleName();

    public Telephony() {
        LogUtil.i(TAG, "init Telephony");
        /* 测试获取基站信息的四个接口：
        * listen API 1-至今（29）
        * getCellLocation API 1-25， Android O （API 26）过期，不再测试，范围是单个基站
        * getAllCellInfo() API 17 添加至今（29）
        * getNeighboringCellInfo() 与 Android M （API 23）移除，不再测试。
        * */

        TelephonyManager phone = (TelephonyManager) App.context.getSystemService(Context.TELEPHONY_SERVICE);
        if (phone == null) {
            LogUtil.w(TAG, "can NOT get TelephonyManager");
            return;
        }
        runGetAllCellInfo(phone);

        runListen(phone);

        runGetCellLocation(phone);

    }

    private void runGetAllCellInfo(final TelephonyManager phone) {
        new Thread(new Runnable() {

            private int times = 1;

            @Override
            public void run() {
                if (ContextCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_DENIED) {
                    return;
                }
                while (times < 10) {
                    List<CellInfo> list = phone.getAllCellInfo();
                    if (list != null && list.size() > 0) {
                        LogUtil.d(TAG, times, "cell info size:", list.size());
                        for (CellInfo cell : list) {
                            if (cell instanceof CellInfoGsm) {
                                CellInfoGsm cellInfo = (CellInfoGsm) cell;
                                LogUtil.v(TAG, "GSM", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
                                CellIdentityGsm cellIdentity = cellInfo.getCellIdentity();
                                // API 28，getMcc、getMnc 被 getMccString、getMncString 代替
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    LogUtil.v(TAG, "GSM", cellIdentity.getMccString(), cellIdentity.getMncString(), cellIdentity.getLac(), cellIdentity.getCid());
                                } else {
                                    LogUtil.v(TAG, "GSM", cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid());
                                }
                            } else if (cell instanceof CellInfoCdma) {
                                CellInfoCdma cellInfo = (CellInfoCdma) cell;
                                LogUtil.v(TAG, "CDMA", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
                                CellIdentityCdma cellIdentity = cellInfo.getCellIdentity();
                                LogUtil.v(TAG, "CDMA", cellIdentity.getBasestationId(), cellIdentity.getSystemId(), cellIdentity.getNetworkId(), cellIdentity.getBasestationId());
                            } else if (cell instanceof CellInfoWcdma) {
                                CellInfoWcdma cellInfo = (CellInfoWcdma) cell;
                                LogUtil.v(TAG, "WCDMA", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
                                CellIdentityWcdma cellIdentity = cellInfo.getCellIdentity();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    LogUtil.v(TAG, "WCDMA", cellIdentity.getMccString(), cellIdentity.getMncString(), cellIdentity.getLac(), cellIdentity.getCid());
                                } else {
                                    LogUtil.v(TAG, "WCDMA", cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid());
                                }
                            } else if (cell instanceof CellInfoLte) {
                                CellInfoLte cellInfo = (CellInfoLte) cell;
                                LogUtil.v(TAG, "LTE", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
                                CellIdentityLte cellIdentity = cellInfo.getCellIdentity();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    LogUtil.v(TAG, "LTE", cellIdentity.getMccString(), cellIdentity.getMncString(), cellIdentity.getTac(), cellIdentity.getCi());
                                } else {
                                    LogUtil.v(TAG, "LTE", cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getTac(), cellIdentity.getCi());
                                }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (cell instanceof CellInfoTdscdma) {
                                        CellInfoTdscdma cellInfo = (CellInfoTdscdma) cell;
                                        LogUtil.v(TAG, "TDSCDMA", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
                                        CellIdentityTdscdma cellIdentity = cellInfo.getCellIdentity();
                                        LogUtil.v(TAG, "TDSCDMA", cellIdentity.getMccString(), cellIdentity.getMncString(), cellIdentity.getLac(), cellIdentity.getCid());
                                    } else if (cell instanceof CellInfoNr) {
                                        CellInfoNr cellInfo = (CellInfoNr) cell;
                                        LogUtil.v(TAG, "NR", cellInfo.isRegistered(), cellInfo.getCellSignalStrength().getDbm());
//                                        CellIdentityNr cellIdentity = cellInfo.getCellIdentity();
//                                        LogUtil.v(TAG, cellIdentity.getMccString(), cellIdentity.getMncString(), cellIdentity.getLac(), cellIdentity.getCid());
//                                        LogUtil.v(TAG, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid());
                                    } else {
                                        LogUtil.w(TAG, "Unknown CellInfo type, after Android Q");
                                    }
                                } else {
                                    LogUtil.w(TAG, "Unknown CellInfo type");
                                }
                            }
                        }
                    } else {
                        LogUtil.d(TAG, times, "cell info size: 0");
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    times++;
                }
            }
        }).start();

    }

    private void runListen(final TelephonyManager phone) {
        phone.listen(new PhoneStateListener() {

            /**
             * Callback invoked when device service state changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param serviceState
             * @see ServiceState#STATE_EMERGENCY_ONLY
             * @see ServiceState#STATE_IN_SERVICE
             * @see ServiceState#STATE_OUT_OF_SERVICE
             * @see ServiceState#STATE_POWER_OFF
             */
            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
                LogUtil.d(TAG, "onServiceStateChanged", serviceState);
            }

            /**
             * Callback invoked when network signal strength changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
//             * {@link SubscriptionManager#getDefaultSubscriptionId()}.
             *
             * @param asu
             * @see ServiceState#STATE_EMERGENCY_ONLY
             * @see ServiceState#STATE_IN_SERVICE
             * @see ServiceState#STATE_OUT_OF_SERVICE
             * @see ServiceState#STATE_POWER_OFF
             */
            @Override
            public void onSignalStrengthChanged(int asu) {
                super.onSignalStrengthChanged(asu);
                LogUtil.d(TAG, "onSignalStrengthChanged", asu);
            }

            /**
             * Callback invoked when the message-waiting indicator changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param mwi
             */
            @Override
            public void onMessageWaitingIndicatorChanged(boolean mwi) {
                super.onMessageWaitingIndicatorChanged(mwi);
                LogUtil.d(TAG, "onMessageWaitingIndicatorChanged", mwi);
            }

            /**
             * Callback invoked when the call-forwarding indicator changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param cfi
             */
            @Override
            public void onCallForwardingIndicatorChanged(boolean cfi) {
                super.onCallForwardingIndicatorChanged(cfi);
                LogUtil.d(TAG, "onCallForwardingIndicatorChanged", cfi);

            }

            /**
             * Callback invoked when device cell location changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param location
             */
            @Override
            public void onCellLocationChanged(CellLocation location) {
                super.onCellLocationChanged(location);
                LogUtil.d(TAG, "onCellLocationChanged", location);
            }

            /**
             * Callback invoked when device call state changes.
             * <p>
             * Reports the state of Telephony (mobile) calls on the device for the registered subscription.
             * <p>
             * Note: the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             * <p>
             * Note: The state returned here may differ from that returned by
             * {@link TelephonyManager#getCallState()}. Receivers of this callback should be aware that
             * calling {@link TelephonyManager#getCallState()} from within this callback may return a
             * different state than the callback reports.
             *
             * @param state       call state
             * @param phoneNumber call phone number. If application does not have
             *                    {@link Manifest.permission#READ_CALL_LOG READ_CALL_LOG} permission or carrier
             *                    privileges (see {@link TelephonyManager#hasCarrierPrivileges}), an empty string will be
             */
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                LogUtil.d(TAG, "onCallStateChanged", state, phoneNumber);
            }

            /**
             * Callback invoked when connection state changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param state
             * @see TelephonyManager#DATA_DISCONNECTED
             * @see TelephonyManager#DATA_CONNECTING
             * @see TelephonyManager#DATA_CONNECTED
             * @see TelephonyManager#DATA_SUSPENDED
             */
            @Override
            public void onDataConnectionStateChanged(int state) {
                super.onDataConnectionStateChanged(state);
                LogUtil.d(TAG, "onDataConnectionStateChanged", state);
            }

            /**
             * same as above, but with the network type.  Both called.
             *
             * @param state
             * @param networkType
             */
            @Override
            public void onDataConnectionStateChanged(int state, int networkType) {
                super.onDataConnectionStateChanged(state, networkType);
                LogUtil.d(TAG, "onDataConnectionStateChanged", state, networkType);
            }

            /**
             * Callback invoked when data activity state changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param direction
             * @see TelephonyManager#DATA_ACTIVITY_NONE
             * @see TelephonyManager#DATA_ACTIVITY_IN
             * @see TelephonyManager#DATA_ACTIVITY_OUT
             * @see TelephonyManager#DATA_ACTIVITY_INOUT
             * @see TelephonyManager#DATA_ACTIVITY_DORMANT
             */
            @Override
            public void onDataActivity(int direction) {
                super.onDataActivity(direction);
                LogUtil.d(TAG, "onDataActivity", direction);
            }

            /**
             * Callback invoked when network signal strengths changes on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param signalStrength
             */
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                LogUtil.d(TAG, "onSignalStrengthsChanged", signalStrength);
            }

            /**
             * Callback invoked when a observed cell info has changed or new cells have been added
             * or removed on the registered subscription.
             * Note, the registration subId s from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param cellInfo is the list of currently visible cells.
             */
            @Override
            public void onCellInfoChanged(List<CellInfo> cellInfo) {
                super.onCellInfoChanged(cellInfo);
                LogUtil.d(TAG, "onCellInfoChanged", cellInfo);
            }

            /**
             * Callback invoked when the user mobile data state has changed on the registered subscription.
             * Note, the registration subId comes from {@link TelephonyManager} object which registers
             * PhoneStateListener by {@link TelephonyManager#listen(PhoneStateListener, int)}.
             * If this TelephonyManager object was created with
             * {@link TelephonyManager#createForSubscriptionId(int)}, then the callback applies to the
             * subId. Otherwise, this callback applies to
             *
             * @param enabled indicates whether the current user mobile data state is enabled or disabled.
             */
            @Override
            public void onUserMobileDataStateChanged(boolean enabled) {
                super.onUserMobileDataStateChanged(enabled);
                LogUtil.d(TAG, "onUserMobileDataStateChanged", enabled);
            }

            /**
             * Callback invoked when active data subId changes.
             * Note, this callback triggers regardless of registered subscription.
             * <p>
             * Requires the READ_PHONE_STATE permission.
             *
             * @param subId current subscription used to setup Cellular Internet data.
             *              For example, it could be the current active opportunistic subscription in use,
             *              or the subscription user selected as default data subscription in DSDS mode.
             */
            @Override
            public void onActiveDataSubscriptionIdChanged(int subId) {
                super.onActiveDataSubscriptionIdChanged(subId);
                LogUtil.d(TAG, "onActiveDataSubscriptionIdChanged", subId);
            }
        }, PhoneStateListener.LISTEN_CELL_INFO);
    }

    private void runGetCellLocation(TelephonyManager phone) {
        if (ContextCompat.checkSelfPermission(App.context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            return;
        }
        final int phoneType = phone.getPhoneType();
        String networkOperator = phone.getNetworkOperator();
        int mcc = Integer.parseInt(phone.getNetworkOperator().substring(0, 3));
        int mnc = Integer.parseInt(phone.getNetworkOperator().substring(3));
        int cid = -1;
        int lac = -1;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
                GsmCellLocation cellLocation = (GsmCellLocation) phone.getCellLocation();
                if (cellLocation != null) {
                    cid = cellLocation.getCid();
                    lac = cellLocation.getLac();
                } else {
                    LogUtil.d(TAG, cellLocation, networkOperator);
                }
            } else if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                CdmaCellLocation cellLocation = (CdmaCellLocation) phone.getCellLocation();
                if (cellLocation != null) {
                    cid = cellLocation.getBaseStationId();
                    lac = cellLocation.getNetworkId();
                } else {
                    LogUtil.d(TAG, cellLocation, networkOperator);
                }
            }
        }
        LogUtil.d(TAG, mcc, mnc, cid, lac);
    }
}
