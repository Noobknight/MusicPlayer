package com.tadev.musicplayer.ui.activities.fragments;

import android.util.Log;
import android.view.View;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseMusicFragmentDrawer;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.services.loaders.MusicLoaderTask;
import com.tadev.musicplayer.utils.design.support.Utils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Iris Louis on 28/03/2016.
 */
public class MusicUsUkFragment extends BaseMusicFragmentDrawer {
    public static final String TAG = "MusicUsUkFragment";
    private List mListMusicUsUk;

    public static MusicUsUkFragment newInstance() {
        return new MusicUsUkFragment();
    }


    @Override
    protected int setImageHeaderId() {
        return R.drawable.bg_card_top_music_ukus;
    }


    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    protected void initLoadTask() {
        try {
            mListMusicUsUk = application.getMusicContainer().getListNeed(Constants.US_TAG);
            Log.i(TAG, "initLoadTask " + mListMusicUsUk.size());
            boolean isListNull = mListMusicUsUk.isEmpty();
            if (isListNull) {
                new MusicLoaderTask(this, Constants.US_TAG).execute(Api.getApiMusicUSUK());
            } else {
                setDummyDataWithHeader(recyclerView, headerView, mListMusicUsUk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String setTitle() {
        return Utils.getTitleUS(context);
    }


    @Override
    public void onItemClick(View view, int position) {
        mOnPlayBarBottomListener.onPlayBarShowHide(true);
        BaseModel usModel = (BaseModel) mListMusicUsUk.get(position);
        baseMenuActivity.transaction = baseMenuActivity.getSupportFragmentManager().beginTransaction();
        baseMenuActivity.transaction.setCustomAnimations(R.anim.transition_fade_in, R.anim.transition_fade_out,
                R.anim.transition_fade_in, R.anim.transition_fade_out);
        baseMenuActivity.transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(usModel))
                .addToBackStack(MainMusicPlayFragment.TAG);
        baseMenuActivity.transaction.commit();
        getToolbar().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTaskLoadCompleted(List<BaseModel> musics) {
        mDialogLoading.dismiss();
        try {
            setDummyDataWithHeader(recyclerView, headerView,
                    application.getMusicContainer().getListNeed(Constants.US_TAG));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
