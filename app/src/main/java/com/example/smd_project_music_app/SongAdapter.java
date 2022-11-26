package com.example.smd_project_music_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongContentViewHolder> implements Filterable {
    private ArrayList<Song> songs;
    private ArrayList<Song> filteredSongs;
    private Filter filter;

    private onSongClick songClickListener;

    @Override
    public SongContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songs_list_item, parent, false);

        SongContentViewHolder vh = new SongContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongContentViewHolder holder, int position) {
        String SongName = filteredSongs.get(position).getTitle();
        int SongImg = filteredSongs.get(position).getImg();
        String SongSinger = filteredSongs.get(position).getArtist();
        holder.Sname.setText(SongName);
        holder.Ssinger.setText(SongSinger);
        holder.Simage.setImageResource(SongImg);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return filteredSongs.size();
    }

    public SongAdapter(ArrayList<Song> ds, onSongClick ClickListener){
        songs = ds;
        filteredSongs = ds;
        this.songClickListener =ClickListener;
    }

    public void updateData(ArrayList<Song> ds){
        songs = ds;
        filteredSongs = songs;
        notifyDataSetChanged();
    }

    public interface onSongClick {
        //void onItemClick(int position);
        public void onItemClick(Song song);
    }

    //=====================================================================================
    public class SongContentViewHolder extends RecyclerView.ViewHolder{

        public TextView Sname;
        public TextView Ssinger;
        public ImageView Simage;

        public SongContentViewHolder(View v){
            super(v);
            Sname = (TextView) v.findViewById(R.id.Song_name);
            Ssinger = (TextView) v.findViewById(R.id.Singer_name);
            Simage = (ImageView) v.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    HelperMusicPlayer.getInstance().reset();
                    HelperMusicPlayer.index=pos;
                    songClickListener.onItemClick(filteredSongs.get(pos));
                }
            });
        }
    }
    //=====================================================================================
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new SongsFilter();
        }
        return filter;
    }

    private class SongsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0){
                ArrayList<Song> filteredList = new ArrayList<Song>();
                for (int i = 0; i < songs.size(); i++){
                    if (songs.get(i).getTitle().toLowerCase(Locale.ROOT).contains(constraint) ||
                            songs.get(i).getTitle().contains(constraint)
                    ){
                        filteredList.add(songs.get(i));
                    }
                }

                filterResults.count = filteredList.size();
                filterResults.values = filteredList;
            }

            else {
                filterResults.count = songs.size();
                filterResults.values = songs;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredSongs = (ArrayList<Song>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
