package com.tadev.musicplayer.abstracts;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Iris Louis on 12/09/2015.
 */
public abstract class EndlessVideoStaggredGridView extends RecyclerView.OnScrollListener {
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 15; // The minimum amount of items to have below your current scroll position before loading more.
    int visibleItemCount, totalItemCount;
    private RecyclerView recyclerView;
    int firstVisibleItemsGrid[] = new int[2];
    int typeGet = 0;

    private StaggeredGridLayoutManager mLayoutManager;

    public EndlessVideoStaggredGridView(StaggeredGridLayoutManager mLayoutManager, RecyclerView recyclerView) {
        this.mLayoutManager = mLayoutManager;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        firstVisibleItemsGrid = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItemsGrid);
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                typeGet++;
            }
        }
        if (!loading && (totalItemCount - firstVisibleItemsGrid[0]) <= (firstVisibleItemsGrid[0] + visibleThreshold)) {
            loadMore(typeGet);
            loading = true;
        }

    }

    public void reset(int previousTotal, boolean loading) {
        this.previousTotal = previousTotal;
        this.loading = loading;
    }

    private boolean isEnd() {
        return !recyclerView.canScrollVertically(1) && firstVisibleItemsGrid[0] != 0;
    }

    public abstract void loadMore(int typeGet);


}
