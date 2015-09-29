package com.quixom.musixom.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quixom.musixom.R;
import com.quixom.musixom.util.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29-Sep-15.
 */
public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<Music> playlist;

    public MusicListAdapter(ArrayList<Music> list) {
        this.playlist = list;
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
