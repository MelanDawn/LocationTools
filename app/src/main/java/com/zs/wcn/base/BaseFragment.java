package com.zs.wcn.base;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    protected final String mTag = this.getClass().getSimpleName();

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public String getTAG() {
        return mTag;
    }

    protected void startActivity(Class<? extends BaseActivity> cls) {
        startActivity(new Intent(getActivity(), cls));
    }
}

