package com.quixom.musixom.adapter;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quixom.musixom.R;
import com.quixom.musixom.util.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29-Sep-15.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<Music> playlist;
    Context adapterContext;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    String TAG = "musixom";

    public MusicListAdapter(ArrayList<Music> list, Context context) {
        this.playlist = list;
        this.adapterContext = context;
    }

    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_card_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MusicListAdapter.ViewHolder holder, int position) {
        Music music = playlist.get(position);
        holder.trackTitle.setText(music.getTrackTitle());
        holder.trackArtistName.setText(music.getTrackArtist());

        retriever.setDataSource(adapterContext, music.getTrackUri());
        byte coverArt[] = retriever.getEmbeddedPicture();
        Glide.with(adapterContext)
                .load(coverArt)
                .centerCrop()
                .placeholder(R.drawable.music_note)
                .override(100, 100)
                .error(R.drawable.music_note)
                .into(holder.trackCoverArt);
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView trackTitle, trackArtistName;
        public ImageView trackCoverArt;

        public ViewHolder(View itemView) {
            super(itemView);
            trackTitle = (TextView) itemView.findViewById(R.id.tv_track_title);
            trackArtistName = (TextView) itemView.findViewById(R.id.tv_track_artist);
            trackCoverArt = (ImageView) itemView.findViewById(R.id.iv_track_cover_art);
        }
    }
}
