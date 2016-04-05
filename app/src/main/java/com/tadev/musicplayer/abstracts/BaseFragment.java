package com.tadev.musicplayer.abstracts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tadev.musicplayer.MainActivity;

/**
 * Created by Iris Louis on 01/04/2016.
 */
public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected BaseMenuActivity baseMenuActivity;
    protected MainActivity mActivityMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG(), "onCreate ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(setLayoutById(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getDataCallBack();
        setViewEvents();
        initViewData();
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            baseMenuActivity = (BaseMenuActivity) activity;
            mActivityMain = (MainActivity) activity;
        }
        super.onAttach(context);
    }

    protected abstract int setLayoutById();

    protected abstract void initView(View view);

    protected abstract void initViewData();

    protected abstract void setViewEvents();

    protected abstract String TAG();

    protected void getDataCallBack() {
    }

}
