package com.tadev.musicplayer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.interfaces.OnItemClickListener;
import com.tadev.musicplayer.models.MusicOffline;
import com.tadev.musicplayer.supports.design.CircleImageView;
import com.tadev.musicplayer.supports.design.TextViewTitle;
import com.tadev.musicplayer.utils.support.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class MusicOfflineAdapter extends RecyclerView.Adapter<MusicOfflineAdapter.OfflineViewHolder> {
    private final String TAG = "MusicOfflineAdapter";
    private Context context;
    private ArrayList<MusicOffline> mListOffline;
    private OnItemClickListener onItemClickListener;

    public MusicOfflineAdapter(Context context, ArrayList<MusicOffline> mListOffline,
                               OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mListOffline = mListOffline;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public OfflineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_offline, parent, false);
        return new OfflineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfflineViewHolder holder, final int position) {
        MusicOffline offline = mListOffline.get(position);
        if (offline != null) {
            holder.txtName.setText(offline.getTitle());
            holder.txtArtist.setText(offline.getArtist());
            holder.txtDuration.setText(Utils.getTimeDuration(offline.getDuration()));
            if (!TextUtils.isEmpty(offline.getCoverUri())) {
                Glide.with(context).load(new File(offline.getCoverUri())).into(holder.imgSong);
            }
        }
        holder.cardOfflineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListOffline.size();
    }


    public static class OfflineViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgSong;
        public TextViewTitle txtArtist, txtName;
        public TextView txtDuration;
        public CardView cardOfflineItem;

        public OfflineViewHolder(View itemView) {
            super(itemView);
            imgSong = (CircleImageView) itemView.findViewById(R.id.item_song_offline_avtSinger);
            txtName = (TextViewTitle) itemView.findViewById(R.id.item_song_offline_tvTitle);
            txtArtist = (TextViewTitle) itemView.findViewById(R.id.item_song_offline_tvSinger);
            cardOfflineItem = (CardView) itemView.findViewById(R.id.card_view_small);
            txtDuration = (TextView) itemView.findViewById(R.id.item_song_offline_tvDuration);
        }
    }
}
