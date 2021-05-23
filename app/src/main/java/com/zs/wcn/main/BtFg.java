package com.zs.wcn.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zs.wcn.R;
import com.zs.wcn.base.BaseFragment;
import com.zs.wcn.bt.BleAct;
import com.zs.wcn.bt.ClassicBtAct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BtFg extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_main_bt, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initAndSetClickListener(view, R.id.fg_main_bt_classic);
        initAndSetClickListener(view, R.id.fg_main_bt_ble);
    }

    private void initAndSetClickListener(View view, int id) {
        view.findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fg_main_bt_classic:
                startActivity(ClassicBtAct.class);
                break;
            case R.id.fg_main_bt_ble:
                startActivity(BleAct.class);
                break;
        }
    }
}
