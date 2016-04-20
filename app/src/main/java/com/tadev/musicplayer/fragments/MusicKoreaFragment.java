package com.tadev.musicplayer.fragments;

import android.view.View;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseMusicFragmentDrawer;
import com.tadev.musicplayer.common.Api;
import com.tadev.musicplayer.constant.Constants;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.services.loaders.MusicLoaderTask;
import com.tadev.musicplayer.utils.support.Utils;

import java.io.IOException;
import java.util.List;

/**
 * Created by Iris Louis on 29/03/2016.
 */
public class MusicKoreaFragment extends BaseMusicFragmentDrawer {
    public static final String TAG = "MusicKoreaFragment";
    private List mListMusicKorea;


    public static MusicKoreaFragment newInstance() {
        return new MusicKoreaFragment();
    }


    @Override
    protected int setImageHeaderId() {
        return R.drawable.bg_card_top_music_korea;
    }


    @Override
    protected String TAG() {
        return TAG;
    }

    @Override
    protected void initLoadTask() {
        try {
            mListMusicKorea = application.getMusicContainer().getListNeed(Constants.KOREA_TAG);
            boolean isListNull = false;
            if (mListMusicKorea == null) {
                isListNull = true;
            } else if (mListMusicKorea != null && mListMusicKorea.isEmpty()) {
                isListNull = true;
            }
            if (isListNull) {
                new MusicLoaderTask(this, Constants.KOREA_TAG).execute(Api.getApiMusicKorea());
            } else {
                setDummyDataWithHeader(recyclerView, headerView, mListMusicKorea);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String setTitle() {
        return Utils.getTitleKorea(context, Constants.TYPE_TTTLE_MUSIC);
    }


    @Override
    public void onItemClick(View view, int position) {
        mOnPlayBarBottomListener.onPlayBarShowHide(true);
        BaseModel koreaModel = (BaseModel) mListMusicKorea.get(position);
        baseMenuActivity.transaction = baseMenuActivity.getSupportFragmentManager().beginTransaction();
        baseMenuActivity.transaction.setCustomAnimations(R.anim.transition_fade_in, R.anim.transition_fade_out,
                R.anim.transition_fade_in, R.anim.transition_fade_out);
        baseMenuActivity.transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(koreaModel))
                .addToBackStack(MainMusicPlayFragment.TAG);
        baseMenuActivity.transaction.commit();
        getToolbar().setVisibility(View.INVISIBLE);
    }


    @Override
    public void onTaskLoadCompleted(List<BaseModel> musics) {
        mDialogLoading.dismiss();
        try {
            setDummyDataWithHeader(recyclerView, headerView,
                    application.getMusicContainer().getListNeed(Constants.KOREA_TAG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

