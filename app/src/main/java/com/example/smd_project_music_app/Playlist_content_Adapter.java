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

public class Playlist_content_Adapter  extends RecyclerView.Adapter<Playlist_content_Adapter.PlaylistContentViewHolder> implements Filterable {
    private ArrayList<Songs> songs;
    private ArrayList<Songs> filteredSongs;
    private Filter filter;

    @Override
    public PlaylistContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_content_item, parent, false);

        PlaylistContentViewHolder vh = new PlaylistContentViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistContentViewHolder holder, int position) {
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

    public Playlist_content_Adapter(ArrayList<Songs> ds){
        songs = ds;
        filteredSongs = ds;
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
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter = new ProductFilter();
        }
        return filter;
    }

    private class ProductFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0){
                ArrayList<Songs> filteredList = new ArrayList<Songs>();
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
            filteredSongs = (ArrayList<Songs>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
