package com.tadev.musicplayer.ui.activities.fragments;

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
 * Created by Iris Louis on 24/03/2016.
 */
public class MusicVietNamFragment extends BaseMusicFragmentDrawer {
    public static final String TAG = "MusicVietNamFragment";


    public static MusicVietNamFragment newInstance() {
        return new MusicVietNamFragment();
    }


    @Override
    protected int setImageHeaderId() {
        return R.drawable.header_viewpager_900;
    }


    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    protected void initLoadTask() {
        try {
            List mListMusicVn = application.getMusicContainer().getListNeed(Constants.VIETNAM_TAG);
            boolean isListNull = mListMusicVn == null;
            if (isListNull) {
                new MusicLoaderTask(this, Constants.VIETNAM_TAG).execute(Api.getApiMusicVietNam());
            } else {
                setDummyDataWithHeader(recyclerView, headerView, mListMusicVn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String setTitle() {
        return Utils.getTitleVN(context);
    }


    @Override
    public void onItemClick(View view, int position) {
        try {
            BaseModel baseModel = (BaseModel) application.getMusicContainer().getListNeed(Constants.VIETNAM_TAG).get(position);
            baseMenuActivity.transaction = baseMenuActivity.getSupportFragmentManager().beginTransaction();
            baseMenuActivity.transaction.setCustomAnimations(R.anim.transition_fade_in, R.anim.transition_fade_out,
                    R.anim.transition_fade_in, R.anim.transition_fade_out);
            baseMenuActivity.transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(baseModel))
                    .addToBackStack(MainMusicPlayFragment.TAG);
            baseMenuActivity.transaction.commit();
            getToolbar().setVisibility(View.INVISIBLE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTaskLoadCompleted(List<BaseModel> musics) {
        mDialogLoading.dismiss();
        setDummyDataWithHeader(recyclerView, headerView, musics);
    }


}
