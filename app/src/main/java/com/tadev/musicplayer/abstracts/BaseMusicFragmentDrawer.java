package com.tadev.musicplayer.abstracts;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.tadev.musicplayer.MusicPlayerApplication;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.services.loaders.MusicLoaderTask;
import com.tadev.musicplayer.utils.design.MaterialImageHeader;
import com.tadev.musicplayer.utils.design.support.Utils;

/**
 * Created by Iris Louis on 31/03/2016.
 */
public abstract class BaseMusicFragmentDrawer extends FlexibleBaseFragment<ObservableRecyclerView>
        implements MusicLoaderTask.OnTaskLoading {
    private final String TAG = "BaseMusicFragmentDrawer";
    protected ObservableRecyclerView recyclerView;
    protected View headerView;
    protected ProgressDialog mDialogLoading;
    private MaterialImageHeader imageHeader;
    private Toolbar toolbar;
    private int mParallaxImageHeight;
    private View mViewOpacity;
    protected Context context;
    protected MusicPlayerApplication application;
    protected BaseMenuActivity baseMenuActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        application = ((MusicPlayerApplication.getInstance()));
        Log.i(TAG(), "onCreate ");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flexiblespacewithimagerecyclerview, container, false);
        recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);
        headerView = LayoutInflater.from(getActivity()).inflate(R.layout.recycler_header, null);
        final int flexibleSpaceImageHeight = Utils.getDimensRes(context, R.dimen.parallax_image_height);
        mParallaxImageHeight = Utils.getDimensRes(context, R.dimen.parallax_image_height_opacity);
        headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, flexibleSpaceImageHeight));
        imageHeader = (MaterialImageHeader) headerView.findViewById(R.id.imageHeader);
        mViewOpacity = (View) headerView.findViewById(R.id.opacityView);
        imageHeader.setImageDrawable(ContextCompat.getDrawable(getActivity(), setImageHeaderId()), 300);
        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        recyclerView.setTouchInterceptionViewGroup((ViewGroup) view.findViewById(R.id.fragment_root));

        // Scroll to the specified offset after layout
        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
            ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
                @Override
                public void run() {
                    int offset = scrollY % flexibleSpaceImageHeight;
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                    if (lm != null && lm instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) lm).scrollToPositionWithOffset(0, -offset);
                    }
                }
            });
            updateFlexibleSpace(scrollY, view);
        } else {
            updateFlexibleSpace(0, view);
        }

        recyclerView.setScrollViewCallbacks(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLoadTask();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = ((BaseMenuActivity) getActivity()).getToolbar();
    }


    @Override
    public void onAttach(Context context) {
        AppCompatActivity baseMenu = null;
        if (context instanceof AppCompatActivity) {
            baseMenu = (AppCompatActivity) context;
            baseMenuActivity = (BaseMenuActivity) baseMenu;
        }
        super.onAttach(context);
    }

    protected abstract int setImageHeaderId();

    @Override
    public void setScrollY(int scrollY, int threshold) {
        View view = getView();
        if (view == null) {
            return;
        }
        ObservableRecyclerView recyclerView = (ObservableRecyclerView) view.findViewById(R.id.scroll);
        if (recyclerView == null) {
            return;
        }
        View firstVisibleChild = recyclerView.getChildAt(0);
        if (firstVisibleChild != null) {
            int offset = scrollY;
            int position = 0;
            if (threshold < scrollY) {
                int baseHeight = firstVisibleChild.getHeight();
                position = scrollY / baseHeight;
                offset = scrollY % baseHeight;
            }
            RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
            if (lm != null && lm instanceof LinearLayoutManager) {
                ((LinearLayoutManager) lm).scrollToPositionWithOffset(position, -offset);
            }
        }
    }

    @Override
    protected void updateFlexibleSpace(int scrollY, View view) {
        int baseColor = Utils.getColorRes(getActivity(), R.color.colorPrimary);
        String colorPrimary = "#" + Integer.toHexString(baseColor).replace("ff", "").toUpperCase();

        float scrollYAlpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        //Calculate opacity
//        float alpha = 1.0f - scrollYAlpha;
//        ViewHelper.setAlpha(imageHeader, alpha);
        int hexColorTranspanrent = 0;
        try {
            hexColorTranspanrent = Color.parseColor(addAlpha(colorPrimary, scrollYAlpha));
        } catch (NumberFormatException e) {
            hexColorTranspanrent = Color.parseColor("#00000000");
        }

//        int hexColorWhite = Color.parseColor(addAlpha("#FFFFFF", scrollYAlpha));
        if (toolbar != null) {
            if ((float) scrollY >= mParallaxImageHeight) {
                toolbar.setBackgroundColor(hexColorTranspanrent);
                toolbar.setTitle(setTitle());
            } else {
                toolbar.setTitle("");
                mViewOpacity.setBackgroundColor(hexColorTranspanrent);
                toolbar.setBackgroundColor(Utils.getColorRes(getActivity(), android.R.color.transparent));
            }
        }

        // Translate list background
//        ViewHelper.setTranslationY(imageHeader, Math.max(0, -scrollY + mParallaxImageHeight));
        ViewHelper.setTranslationY(imageHeader, scrollY / 2);
        // Also pass this event to parent Activity
    }


//    @Override
//    public void onTaskLoadCompleted(List<BaseModel> musics) {
//        mDialogLoading.dismiss();
//    }


    @Override
    public void onTaskLoadFailed(Exception e) {
        mDialogLoading.dismiss();
    }

    @Override
    public void onPreparing() {
        mDialogLoading = ProgressDialog.show(getActivity(), "", "Loading...");
    }

    protected abstract String TAG();

    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        super.onScrollChanged(scrollY, firstScroll, dragging);
    }

    protected abstract void initLoadTask();

    public int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    public static String addAlpha(String originalColor, double alpha) {
        long alphaFixed = Math.round(alpha * 255);
        String alphaHex = Long.toHexString(alphaFixed);
        if (alphaHex.length() == 1) {
            alphaHex = "0" + alphaHex;
        }
        originalColor = originalColor.replace("#", "#" + alphaHex);


        return originalColor;
    }

    protected abstract String setTitle();



}
