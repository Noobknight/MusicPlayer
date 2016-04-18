package com.tadev.musicplayer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tadev.musicplayer.R;
import com.tadev.musicplayer.interfaces.OnItemClickListener;
import com.tadev.musicplayer.models.SongFavorite;
import com.tadev.musicplayer.utils.design.TextViewTitle;

import java.util.ArrayList;

/**
 * Created by Iris Louis on 17/04/2016.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FovoriteViewHolder> {
    private final String TAG = "FavoriteAdapter";
    private Context context;
    private ArrayList<SongFavorite> mListFovorite;
    private OnItemClickListener onItemClickListener;

    public FavoriteAdapter(Context context, ArrayList<SongFavorite> mListFovorite, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mListFovorite = mListFovorite;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public FovoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_favorite, parent, false);
        return new FovoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FovoriteViewHolder holder, final int position) {
        SongFavorite favorite = mListFovorite.get(position);
        if (favorite != null) {
            holder.txtName.setText(favorite.getName());
            holder.txtArtist.setText(favorite.getArtist());
            if (holder.imgSong != null) {
                Glide.with(context).load(favorite.getImage()).into(holder.imgSong);
            }
            holder.btnFavorite.setFavorite(true, false);

        }
        setEventViews(holder, position);
    }


    private void setEventViews(FovoriteViewHolder holder, final int position) {
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
        holder.cardFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFovorite.size();
    }


    private int getPositionAdapter(int position) {
        return position;
    }

    public void removeAt(int position) {
        mListFovorite.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(getPositionAdapter(position), mListFovorite.size());
    }

    public static class FovoriteViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView imgSong;
        public TextViewTitle txtArtist, txtName;
        public MaterialFavoriteButton btnFavorite;
        public CardView cardFavorite;

        public FovoriteViewHolder(View itemView) {
            super(itemView);
            imgSong = (RoundedImageView) itemView.findViewById(R.id.item_favorite_imgSong);
            txtName = (TextViewTitle) itemView.findViewById(R.id.item_favorite_tvTitle);
            txtArtist = (TextViewTitle) itemView.findViewById(R.id.item_favorite_tvSinger);
            btnFavorite = (MaterialFavoriteButton) itemView.findViewById(R.id.item_favorite_btnFavorite);
            cardFavorite = (CardView) itemView.findViewById(R.id.card_favorite);
        }
    }

}
