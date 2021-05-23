package com.zs.wcn.utils;

import android.os.Bundle;

import java.util.Set;

public class BundleUtil {

    public static String getContent(Bundle bundle) {
        if (bundle == null || bundle.keySet().isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Set<String> set = bundle.keySet();
        for (String key : set) {
            sb.append(" ").append(key).append(" = ").append(bundle.get(key)).append(",");
        }
        sb.replace(0, 1, "[ ");
        sb.replace(sb.length()-1, sb.length(), " ]");
        return sb.substring(0);
    }
}
