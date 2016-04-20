package com.tadev.musicplayer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.models.BaseModel;
import com.tadev.musicplayer.supports.design.CircleImageView;

import java.util.List;

/**
 * Created by Iris Louis on 31/03/2016.
 */
public class BaseMusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "BaseMusicAdapter";
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private List<BaseModel> musics;
    private View mHeaderView;
    private LayoutInflater mInflater;

    public BaseMusicAdapter(Context context, List<BaseModel> musics, View headerView, OnItemClickListener onItemClickListener) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.musics = musics;
        mHeaderView = headerView;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (musics != null) {
            if (mHeaderView == null) {
                return musics.size();
            } else {
                return musics.size() + 1;
            }
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else {
            return new ItemViewHolder(mInflater.inflate(R.layout.item_card_music, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ItemViewHolder) {
            BaseModel music = (BaseModel) musics.get(position - 1);
            if (music != null) {
                if (!music.getArtist_face_url().isEmpty()) {
                    Glide.with(context).load(music.getArtist_face_url())
                            .into(((ItemViewHolder) viewHolder).imgArtistFace);
                } else {
                    Glide.with(context).load(R.drawable.icon_music_placeholder)
                            .into(((ItemViewHolder) viewHolder).imgArtistFace);
                }
                ((ItemViewHolder) viewHolder).tvTitle.setText(music.getMusic_title());
                ((ItemViewHolder) viewHolder).tvArtist.setText(music.getMusic_artist());
                ((ItemViewHolder) viewHolder).tvDownloads.setText(music.getMusic_downloads());
                ((ItemViewHolder) viewHolder).tvBitrate.setText(music.getMusic_bitrate());
                ((ItemViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(v, position - 1);
                    }
                });
            }
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgArtistFace;
        public TextView tvTitle;
        public TextView tvArtist;
        public TextView tvDownloads;
        public TextView tvBitrate;
        public CardView cardView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view_small);
            imgArtistFace = (CircleImageView) itemView.findViewById(R.id.list_item_card_small_avtSinger);
            tvTitle = (TextView) itemView.findViewById(R.id.list_item_card_small_tvTitle);
            tvArtist = (TextView) itemView.findViewById(R.id.list_item_card_small_tvSinger);
            tvDownloads = (TextView) itemView.findViewById(R.id.list_item_card_small_tvDownloads);
            tvBitrate = (TextView) itemView.findViewById(R.id.list_item_card_small_tvMusicType);
        }
    }

}
