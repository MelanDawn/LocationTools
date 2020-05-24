package com.zs.location.utils;

import android.location.Address;

public class AddressUtil {

    public static String getContent(Address address) {
        StringBuilder sb = new StringBuilder();
        sb.append("Address{");
        sb.append("feature=");
        sb.append(address.getFeatureName());
        sb.append(", admin=");
        sb.append(address.getAdminArea());
        sb.append(", sub-admin=");
        sb.append(address.getSubAdminArea());
        sb.append(", locality=");
        sb.append(address.getLocality());
        sb.append(", sub-locality");
        sb.append(address.getSubLocality());
        sb.append(", thoroughfare=");
        sb.append(address.getThoroughfare());
        sb.append(", sub-thoroughfare=");
        sb.append(address.getSubThoroughfare());
        sb.append(", postalCode=");
        sb.append(address.getPostalCode());
        sb.append(", countryCode=");
        sb.append(address.getCountryCode());
        sb.append(", countryName=");
        sb.append(address.getCountryName());
        sb.append(", hasLatitude=");
        sb.append(address.hasLatitude());
        sb.append(", latitude=");
        sb.append(address.hasLatitude() ? address.getLatitude() : Integer.MIN_VALUE);
        sb.append(", hasLongitude=");
        sb.append(address.hasLongitude());
        sb.append(", longitude=");
        sb.append(address.hasLongitude() ? address.hasLongitude() : Integer.MIN_VALUE);
        sb.append(", phone=");
        sb.append(address.getPhone());
        sb.append(", url=");
        sb.append(address.getUrl());
        sb.append(", addressLines=[");
        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(i);
            sb.append(':');
            String line = address.getAddressLine(i);
            if (line == null) {
                sb.append("null");
            } else {
                sb.append('\"');
                sb.append(line);
                sb.append('\"');
            }
        }
        sb.append(']');
        sb.append(", extras=");
        sb.append(BundleUtil.getContent(address.getExtras()));
        sb.append('}');
        return sb.toString();
    }
}
