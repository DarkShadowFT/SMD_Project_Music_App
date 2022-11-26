package com.example.smd_project_music_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongContentViewHolder> implements Filterable {
    private ArrayList<Song> songs;
    private ArrayList<Song> filteredSongs;
    private ArrayList<Integer> selectedItems = new ArrayList<>();
    private Filter filter;

    private onSongClick songClickListener;

    private int mode = DEFAULT_MODE;
    public static final int DEFAULT_MODE = 0;
    public static final int SELECTABLE_MODE = 1;

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
        if (SongName.length() >= 23){
            SongName = SongName.substring(0, 23) + "...";
        }
        int SongImg = filteredSongs.get(position).getImg();
        String SongSinger = filteredSongs.get(position).getArtist();
        holder.Sname.setText(SongName);
        holder.Ssinger.setText(SongSinger);
//        holder.Simage.setImageResource(SongImg);
        holder.itemView.setTag(position);
        if (mode == DEFAULT_MODE){
            holder.selected.setChecked(false);
            holder.selected.setVisibility(View.INVISIBLE);
        }
        else{
            holder.selected.setVisibility(View.VISIBLE);
        }
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
        public void onAddBtnClick(Song song);
        public void onLongItemClick(Song song);
        public void deleteSelectedItems(ArrayList<String> songIDs);
    }

    //=====================================================================================
    public class SongContentViewHolder extends RecyclerView.ViewHolder{

        public TextView Sname;
        public TextView Ssinger;
        public ImageView Simage;
        public ImageButton addToPlaylist;
        public CheckBox selected;

        public SongContentViewHolder(View v){
            super(v);
            Sname = (TextView) v.findViewById(R.id.Song_name);
            Ssinger = (TextView) v.findViewById(R.id.Singer_name);
            Simage = (ImageView) v.findViewById(R.id.img);
            addToPlaylist = (ImageButton) v.findViewById(R.id.add_btn);
            selected = (CheckBox) v.findViewById(R.id.item_check);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) v.getTag();
                    if (mode == SELECTABLE_MODE){
                        selected.setChecked(!selected.isChecked()); // toggle
                        selected.callOnClick();
                    }
                    else {
                        HelperMusicPlayer.getInstance().reset();
                        HelperMusicPlayer.index=pos;
                        songClickListener.onItemClick(filteredSongs.get(pos));
                    }
                }
            });

            addToPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) v.getTag();
                    songClickListener.onAddBtnClick(filteredSongs.get(pos));
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int index = (int) v.getTag();
                    selected.setChecked(true);
                    selected.callOnClick();
                    songClickListener.onLongItemClick(filteredSongs.get(index));
                    return true;
                }
            });
            selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox) view).isChecked();
                    int index = (int) v.getTag();
                    if (checked){
                        selectedItems.add(index);
                    }
                    else{
                        selectedItems.remove(index);
                    }
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

    public void setMode(int m){
        mode = m;
        if (mode == DEFAULT_MODE) {
            selectedItems.clear();
        }
        notifyDataSetChanged();
    }

    public void removeSelectedItems(){
        ArrayList<Song> removableItems = new ArrayList<>();
        for (int i=0; i < selectedItems.size(); i++ ){
            Integer item = selectedItems.get(i);
            removableItems.add( filteredSongs.get(item) );
        }

        ArrayList<String> songIDs = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++){
            songIDs.add(filteredSongs.get(selectedItems.get(i)).getPath());
        }

        for(Song product : removableItems){
            filteredSongs.remove(product);
            songs.remove(product);
        }

        songClickListener.deleteSelectedItems(songIDs);

        selectedItems.clear();
        notifyDataSetChanged();
    }
}
