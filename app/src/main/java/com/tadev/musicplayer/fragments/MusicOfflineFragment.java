package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.adapters.MusicOfflineAdapter;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.interfaces.OnBackFragmentListener;
import com.tadev.musicplayer.interfaces.OnItemClickListener;
import com.tadev.musicplayer.models.MusicOffline;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.services.loaders.MediaStoreLoader;
import com.tadev.musicplayer.utils.actions.Actions;
import com.tadev.musicplayer.utils.support.StringUtils;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class MusicOfflineFragment extends BaseFragment implements
        MediaStoreLoader.StoreLoaderListener, OnItemClickListener {
    private final String TAG = "MusicOfflineFragment";
    private RecyclerView mRecyclerView;
    private View mViewLoading;
    private TextView txtEmptyData, txtCountChecked;
    private MusicOfflineAdapter mAdapter;
    private ArrayList<MusicOffline> mListOffline;
    private OnRegisterCallback mOnRegisterCallback;
    private OnBackFragmentListener mOnBackFragmentListener;

    public static MusicOfflineFragment newInstance() {
        return new MusicOfflineFragment();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recyclerView);
        mViewLoading = view.findViewById(R.id.fragment_list_loading);
        txtEmptyData = (TextView) view.findViewById(R.id.fragment_list_empty_view);
        txtCountChecked = (TextView) view.findViewById(R.id.fragment_list_txtCountChecked);
        initToolbar();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initViewData() {
        new MediaStoreLoader(context, this).execute();
    }

    @Override
    protected void setViewEvents() {

    }

    private void initToolbar() {
        mActivityMain.getToolbar().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mActivityMain.getToolbar().setTitle(StringUtils.getStringRes(R.string.category_music_offline));
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onLoadCompleted(ArrayList<MusicOffline> results) {
        mViewLoading.setVisibility(View.GONE);
        if (results != null) {
            mListOffline = results;
            mAdapter = new MusicOfflineAdapter(context, results, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            txtEmptyData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadFailed() {
        mViewLoading.setVisibility(View.GONE);
        txtEmptyData.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onPreparing() {
        // TODO: Handle something here !!! onPreparing
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        intent.setAction(Actions.ACTION_TOGGLE);
        intent.putExtra(Extras.KEY_INDEX_LIST, position);
        intent.putExtra(Extras.KEY_MODE_OFFLINE, true);
        mOnRegisterCallback.onServicePreparing(intent);
        mActivityMain.getService().setListOffline(mListOffline);
        mOnBackFragmentListener.onBack(true);
    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mOnRegisterCallback = (OnRegisterCallback) activity;
                mOnBackFragmentListener = (OnBackFragmentListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must be implement OnRegisterCallBack");
            }
        }
        super.onAttach(context);
    }

}
