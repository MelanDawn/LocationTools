package com.zs.wcn.utils;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class BtDeviceUtil {

    public static String getContent(BluetoothDevice device) {
        if (device == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" address=").append(device.getAddress())
                .append(" name=").append(device.getName())
                .append(" type=").append(getTypeName(device.getType()))
                .append(" state=").append(getBondState(device.getBondState()))
                .append(" class={").append(BtClassUtil.getContent(device.getBluetoothClass())).append("}");
        ParcelUuid[] parcelUuidArray = device.getUuids();
        if (parcelUuidArray == null) {
            sb.append(" uuid=").append("NULL");
        } else {
            int i = 0;
            for (ParcelUuid uuid : parcelUuidArray) {
                i++;
                sb.append(" uuid-").append(i).append("=").append("{");
                sb.append(" timestamp=").append(uuid.getUuid().timestamp());
                sb.append(" version=").append(uuid.getUuid().version());
                sb.append(" variant=").append(uuid.getUuid().variant());
                sb.append(" clockSequence=").append(String.format("%04x", uuid.getUuid().clockSequence()));
                sb.append(" node=").append(String.format("%012x", uuid.getUuid().node()));
                sb.append(" toString=").append(uuid.getUuid().toString());
                sb.append(" }");
            }
        }
        return sb.substring(0);
    }

    public static String getTypeName(int type) {
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                return "UNKNOWN";
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                return "CLASSIC";
            case BluetoothDevice.DEVICE_TYPE_LE:
                return "BLE";
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                return "DUAL";
            default:
                return "ERROR";
        }
    }

    public static String getBondState(int state) {
        switch (state) {
            case BluetoothDevice.BOND_NONE:
                return "NONE";
            case BluetoothDevice.BOND_BONDING:
                return "BONDING";
            case BluetoothDevice.BOND_BONDED:
                return "BONDED";
            default:
                return "ERROR";
        }
    }
}
