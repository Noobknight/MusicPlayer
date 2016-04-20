package com.tadev.musicplayer.abstracts;

import android.app.Activity;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.adapters.BaseMusicAdapter;

import java.util.List;

/**
 * Created by Iris Louis on 29/03/2016.
 */
public abstract class BaseFragmentDrawer extends Fragment implements BaseMusicAdapter.OnItemClickListener{
    private BaseMusicAdapter adapter;

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        return activity.findViewById(android.R.id.content).getHeight();
    }

    protected void setDummyDataWithHeader(RecyclerView recyclerView, View headerView, List musics) {
        adapter = new BaseMusicAdapter(getActivity(), musics, headerView, this);
        recyclerView.setAdapter(adapter);
    }

}