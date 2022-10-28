package com.example.smd_project_music_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Playlist_content_Adapter  extends RecyclerView.Adapter<Playlist_content_Adapter.PlaylistContentViewHolder> {
    private ArrayList<Songs> mDataset;

    @Override
    public PlaylistContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_content_item, parent, false);

        PlaylistContentViewHolder vh = new PlaylistContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistContentViewHolder holder, int position) {
        String SongName = mDataset.get(position).getName();
        int SongImg = mDataset.get(position).getImg();
        String SongSinger = mDataset.get(position).getSinger();
        holder.Sname.setText(SongName);
        holder.Ssinger.setText(SongSinger);
        holder.Simage.setImageResource(SongImg);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public Playlist_content_Adapter(ArrayList<Songs> ds){
        mDataset = ds;
    }

    //=====================================================================================
    public class PlaylistContentViewHolder extends RecyclerView.ViewHolder{

        public TextView Sname;
        public TextView Ssinger;
        public ImageView Simage;

        public PlaylistContentViewHolder(View v){
            super(v);
            Sname = (TextView) v.findViewById(R.id.Song_name);
            Ssinger = (TextView) v.findViewById(R.id.Singer_name);
            Simage = (ImageView) v.findViewById(R.id.img);
        }
    }
    //=====================================================================================

}
