package com.zs.wcn.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zs.wcn.R;
import com.zs.wcn.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NfcFg extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_main_nfc, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        initAndSetClickListener(view, R.id.fg_main_nfc_nfc);
    }

    private void initAndSetClickListener(View view, int id) {
        view.findViewById(id).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fg_main_nfc_nfc:
                break;
        }
    }
}
