package com.tadev.musicplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.models.video.Video;

import java.util.List;

/**
 * Created by Iris Louis on 13/04/2016.
 */
public class VideoAdapter extends RecyclerView.Adapter {
    private final String TAG = "VideoAdapter";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private List<Video> videos;
    private Context context;


    public VideoAdapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_card_video, parent, false);
            viewHolder = new VideoViewHolder(view);
        } else if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.include_loading, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoViewHolder) {
            Video video = videos.get(position);
            if (video != null) {
                ((VideoViewHolder) holder).txtTitle.setText(video.getMusicTitle());
                ((VideoViewHolder) holder).txtBitrate.setText(video.getMusicHeight());
                ((VideoViewHolder) holder).txtDownload.setText(video.getMusicDownloads());
                ((VideoViewHolder) holder).txtArtist.setText(video.getMusicArtist());
                if (TextUtils.isEmpty(video.getThumbnailUrl())) {
                    Glide.with(context).load(R.drawable.bg_card_top_music_vi)
                            .into(((VideoViewHolder) holder).imgThumb);
                } else {
                    Glide.with(context).load(video.getThumbStandard())
//                        .placeholder(R.drawable.bg_card_top_music_china)
                            .into(((VideoViewHolder) holder).imgThumb);
                }
            }
        } else if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressLoading.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return videos.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtArtist, txtBitrate, txtDownload;
        public RoundedImageView imgThumb;

        public VideoViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.item_card_video_txtTitle);
            txtArtist = (TextView) itemView.findViewById(R.id.item_card_video_txtArtist);
            txtBitrate = (TextView) itemView.findViewById(R.id.item_card_video_txtBitrate);
            txtDownload = (TextView) itemView.findViewById(R.id.item_card_video_txtDownload);
            imgThumb = (RoundedImageView) itemView.findViewById(R.id.item_card_video_imgThumb);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressLoading;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            progressLoading = (ProgressBar) itemView.findViewById(R.id.progressLoading);
        }
    }


}
