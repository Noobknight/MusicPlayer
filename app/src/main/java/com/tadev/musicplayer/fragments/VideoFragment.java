package com.tadev.musicplayer.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.abstracts.EndlessRecyclerOnScrollListener;
import com.tadev.musicplayer.activities.VideoPlayingActivity;
import com.tadev.musicplayer.adapters.VideoAdapter;
import com.tadev.musicplayer.callbacks.OnRegisterCallback;
import com.tadev.musicplayer.constant.Extras;
import com.tadev.musicplayer.models.video.Video;
import com.tadev.musicplayer.services.MusicPlayService;
import com.tadev.musicplayer.services.loaders.VideoLoaderTask;
import com.tadev.musicplayer.utils.design.RecyclerItemClickListener;
import com.tadev.musicplayer.utils.design.actions.Actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class VideoFragment extends BaseFragment implements VideoLoaderTask.VideoLoader,
        RecyclerItemClickListener.OnItemClickListener {
    private final String TAG = "VideoFragment";
    private RecyclerView mRecyclerView;
    private View viewLoading;
    private TextView txtEmptyData;
    private VideoLoadMore mVideoLoadMore;
    private List<Video> videos;
    private VideoAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private OnRegisterCallback mOnRegisterCallback;

    public VideoFragment newInstance(String urlApi) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Extras.KEY_URL_API, urlApi);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }


    @Override
    protected int setLayoutById() {
        return R.layout.fragment_video_list;
    }

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_video_list_recycler);
        viewLoading = view.findViewById(R.id.fragment_video_list_loading);
        txtEmptyData = (TextView) view.findViewById(R.id.empty_view);
        mLinearLayoutManager = new LinearLayoutManager(context);
        if (videos == null) {
            videos = new ArrayList<>();
        }
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initViewData() {
        String urlApi = getUrlApi();
        doRequestApi(Extras.TYPE_GET_NORMAL, urlApi, this);
        mAdapter = new VideoAdapter(context, videos);
        mRecyclerView.setAdapter(mAdapter);
        mVideoLoadMore = new VideoLoadMore(mLinearLayoutManager, Extras.TYPE_GET_NORMAL);
        mRecyclerView.addOnScrollListener(mVideoLoadMore);
    }

    @Override
    protected void setViewEvents() {

    }

    @Override
    public void onAttach(Context context) {
        AppCompatActivity activity;
        if (context instanceof AppCompatActivity) {
            activity = (AppCompatActivity) context;
            try {
                mOnRegisterCallback = (OnRegisterCallback) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must be implement OnRegisterCallBack");
            }
        }
        super.onAttach(context);
    }


    private String getUrlApi() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            return bundle.getString(Extras.KEY_URL_API);
        }
        return null;
    }

    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mOnResultsScrollListener.reset(0, true);
    }


    @Override
    public void onLoadCompleted(List<Video> resultCallback) {
        if (resultCallback != null) {
            videos = resultCallback;
            mAdapter = new VideoAdapter(context, videos);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            viewLoading.setVisibility(View.GONE);
        }
    }

    public void updateResults(@NonNull List<Video> videosResult) {
        int newSize = videosResult.size();
        int oldSize = videos.size();
        videos.addAll(videosResult);
        mAdapter.notifyItemRangeChanged(0, newSize + oldSize);
    }


    @Override
    public void onItemClick(View childView, int position) {
        mOnPlayBarBottomListener.onPlayBarShowHide(true);
        if (mActivityMain.getService().isPlaying()) {
            Intent intent = new Intent(getActivity(), MusicPlayService.class);
            intent.setAction(Actions.ACTION_PLAY_PAUSE);
            mOnRegisterCallback.onServicePreparing(intent);
        }
        String id = videos.get(position).getMusicId();
        String urlTitle = videos.get(position).getMusicTitleUrl();
        Intent intent = new Intent(context, VideoPlayingActivity.class);
        intent.putExtra(VideoPlayingActivity.KEY_ID, id);
        intent.putExtra(VideoPlayingActivity.KEY_URL_TITLE, urlTitle);
        context.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
//        baseMenuActivity.transaction = baseMenuActivity.getSupportFragmentManager().beginTransaction();
//        baseMenuActivity.transaction.setCustomAnimations(R.anim.transition_fade_in, R.anim.transition_fade_out,
//                R.anim.transition_fade_in, R.anim.transition_fade_out);
//        baseMenuActivity.transaction.replace(R.id.container, VideoPlayingFragment.newInstance(id, urlTitle))
//                .addToBackStack(VideoPlayingFragment.TAG);
//        baseMenuActivity.transaction.commit();
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        // TODO: Handle something here !!! onItemLongPress
    }


    private class RequestDataApi extends VideoLoaderTask {

        public RequestDataApi(VideoLoader mVideoLoader, int typeGet) {
            super(mVideoLoader, typeGet);
        }

        @Override
        public void onPreparing() {
//            viewLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadFailed(Exception e) {
            viewLoading.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            txtEmptyData.setVisibility(View.VISIBLE);
        }
    }


    private class VideoLoadMore extends EndlessRecyclerOnScrollListener implements VideoLoaderTask.VideoLoader {

        public VideoLoadMore(LinearLayoutManager linearLayoutManager, int typeGet) {
            super(linearLayoutManager, typeGet);
        }

        @Override
        public void onLoadMore(int typeGet) {
            if (typeGet < 2) {
                doRequestApi(typeGet, getUrlApi(), this);
            }
        }

        @Override
        public void onLoadCompleted(List<Video> videos) {
            viewLoading.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            updateResults(videos);
        }
    }


    public void doRequestApi(int typeGet, String param, VideoLoaderTask.VideoLoader videoLoader) {
        new RequestDataApi(videoLoader, typeGet).execute(param);
    }
}
