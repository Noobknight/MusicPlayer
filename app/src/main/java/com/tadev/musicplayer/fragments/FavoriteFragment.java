package com.tadev.musicplayer.fragments;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tadev.musicplayer.R;
import com.tadev.musicplayer.abstracts.BaseFragment;
import com.tadev.musicplayer.adapters.FavoriteAdapter;
import com.tadev.musicplayer.interfaces.OnItemClickListener;
import com.tadev.musicplayer.interfaces.RetrieveListener;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.models.SongFavorite;
import com.tadev.musicplayer.provider.DBFavoriteManager;
import com.tadev.musicplayer.utils.support.StringUtils;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class FavoriteFragment extends BaseFragment implements RetrieveListener
        , OnItemClickListener, View.OnClickListener {
    public static final String TAG = "FavoriteFragment";
    private RecyclerView mRecyclerView;
    private View mViewLoading;
    private TextView txtEmptyData, txtCountChecked;
    private Button btnDelete;
    private FavoriteAdapter mAdapter;
    private ArrayList<SongFavorite> mListFavorites;
    private DBFavoriteManager dbFavoriteManager;


    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    protected int setLayoutById() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initView(View view) {
        dbFavoriteManager = application.getDatabaseFavorite();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_list_recyclerView);
        mViewLoading = view.findViewById(R.id.fragment_list_loading);
        txtEmptyData = (TextView) view.findViewById(R.id.fragment_list_empty_view);
        txtCountChecked = (TextView) view.findViewById(R.id.fragment_list_txtCountChecked);
        btnDelete = (Button) view.findViewById(R.id.fragment_list_btnDel);
        initToolbar();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void initViewData() {
        RetrieveFavoriteDB dataCallback = new RetrieveFavoriteDB(this);
        dataCallback.execute();
    }

    @Override
    protected void setViewEvents() {
        btnDelete.setOnClickListener(this);
    }

    @Override
    protected String TAG() {
        return TAG;
    }


    private void initToolbar() {
        mActivityMain.getToolbar().setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        mActivityMain.getToolbar().setTitle(StringUtils.getStringRes(R.string.category_favorite));
    }

    @Override
    public void onRetriveCompleted(ArrayList<SongFavorite> results) {
        mViewLoading.setVisibility(View.GONE);
        if (results != null) {
            mListFavorites = results;
            mAdapter = new FavoriteAdapter(context, results, this);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            txtEmptyData.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetriveFailed() {
        mViewLoading.setVisibility(View.GONE);
        txtEmptyData.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.card_favorite:
                BaseModel baseModel = new BaseModel();
                baseModel.setMusic_title_url(mListFavorites.get(position).getUrlTitle());
                baseModel.setMusic_id(mListFavorites.get(position).getId());
                baseMenuActivity.transaction = baseMenuActivity.getSupportFragmentManager().beginTransaction();
                baseMenuActivity.transaction.setCustomAnimations(R.anim.transition_fade_in, R.anim.transition_fade_out,
                        R.anim.transition_fade_in, R.anim.transition_fade_out);
                baseMenuActivity.transaction.replace(R.id.container, MainMusicPlayFragment.newInstance(baseModel))
                        .addToBackStack(MainMusicPlayFragment.TAG);
                baseMenuActivity.transaction.commit();
                mActivityMain.getToolbar().setVisibility(View.INVISIBLE);
                break;
            case R.id.item_favorite_btnFavorite:
                if (dbFavoriteManager.isDeleteSucess(mListFavorites.get(position).getId())) {
                    mAdapter.removeAt(position);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }


    private class RetrieveFavoriteDB extends AsyncTask<Void, Void, ArrayList<SongFavorite>> {
        private RetrieveListener mCallback;

        public RetrieveFavoriteDB(RetrieveListener mCallback) {
            this.mCallback = mCallback;
        }

        @Override
        protected ArrayList<SongFavorite> doInBackground(Void... params) {
            return application.getDatabaseFavorite().getFavorites();
        }

        @Override
        protected void onPostExecute(ArrayList<SongFavorite> songFavorites) {
            if (songFavorites != null) {
                mCallback.onRetriveCompleted(songFavorites);
            } else {
                mCallback.onRetriveFailed();
            }
        }
    }
}
