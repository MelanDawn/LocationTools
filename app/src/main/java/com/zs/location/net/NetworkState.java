package com.zs.location.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import com.zs.location.App;
import com.zs.location.utils.LogUtil;

import java.net.NetworkInterface;

import androidx.annotation.NonNull;

public class NetworkState {

    private static final String TAG = "NetworkState";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            boolean disconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            LogUtil.d(TAG, networkInfo, disconnected);
            updateNetworkState(context);
        }
    };

    public NetworkState() {
//        registerBroadcastReceiver(App.context);

        ConnectivityManager cm = (ConnectivityManager) App.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
            /**
             * Called when the framework connects and has declared a new network ready for use.
             * This callback may be called more than once if the {@link Network} that is
             * satisfying the request changes. This will always immediately be followed by a
             * call to {@link #onCapabilitiesChanged(Network, NetworkCapabilities)} then by a
             * call to {@link #onLinkPropertiesChanged(Network, LinkProperties)}, and a call to
             * {@link #onBlockedStatusChanged(Network, boolean)}.
             *
             * @param network The {@link Network} of the satisfying network.
             */
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                LogUtil.d(TAG, "onAvailable", network);
            }

            /**
             * Called when the network is about to be disconnected.  Often paired with an
             * {@link NetworkCallback#onAvailable} call with the new replacement network
             * for graceful handover.  This may not be called if we have a hard loss
             * (loss without warning).  This may be followed by either a
             * {@link NetworkCallback#onLost} call or a
             * {@link NetworkCallback#onAvailable} call for this network depending
             * on whether we lose or regain it.
             *
             * @param network     The {@link Network} that is about to be disconnected.
             * @param maxMsToLive The time in ms the framework will attempt to keep the
             *                    network connected.  Note that the network may suffer a
             */
            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                super.onLosing(network, maxMsToLive);
                LogUtil.d(TAG, "onLosing", network, maxMsToLive);
            }

            /**
             * Called when the framework has a hard loss of the network or when the
             * graceful failure ends.
             *
             * @param network The {@link Network} lost.
             */
            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                LogUtil.d(TAG, "onLost", network);
            }

            // 只针对某个特定方法回调
            /**
             * Called if no network is found in the timeout time specified in
             * {@link #requestNetwork(NetworkRequest, NetworkCallback, int)} call or if the
             * requested network request cannot be fulfilled (whether or not a timeout was
             * specified). When this callback is invoked the associated
             * {@link NetworkRequest} will have already been removed and released, as if
             * {@link #unregisterNetworkCallback(NetworkCallback)} had been called.
             */
            @Override
            public void onUnavailable() {
                super.onUnavailable();
                LogUtil.d(TAG, "onUnavailable");
            }

            /**
             * Called when the network the framework connected to for this request
             * changes capabilities but still satisfies the stated need.
             *
             * @param network             The {@link Network} whose capabilities have changed.
             * @param networkCapabilities The new {@link NetworkCapabilities} for this
             */
            @Override
            public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
                LogUtil.d(TAG, "onCapabilitiesChanged", network, networkCapabilities);
            }

            /**
             * Called when the network the framework connected to for this request
             * changes {@link LinkProperties}.
             *
             * @param network        The {@link Network} whose link properties have changed.
             * @param linkProperties The new {@link LinkProperties} for this network.
             */
            @Override
            public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                super.onLinkPropertiesChanged(network, linkProperties);
                LogUtil.d(TAG, "onLinkPropertiesChanged", network, linkProperties);
            }

            /**
             * Called when access to the specified network is blocked or unblocked.
             *
             * @param network The {@link Network} whose blocked status has changed.
             * @param blocked The blocked status of this {@link Network}.
             */
            @Override
            public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
                super.onBlockedStatusChanged(network, blocked);
                LogUtil.d(TAG, "onBlockedStatusChanged", network, blocked);
            }
        };
        if (cm != null) {
            cm.requestNetwork(request, callback);
        } else {
            LogUtil.e(TAG, "error, can NOT update network status");
        }
    }

    private void registerBroadcastReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mBroadcastReceiver, intentFilter);
    }


    private void updateNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        LogUtil.d(TAG, "updateNetworkState", cm.getActiveNetworkInfo());
    }
}
