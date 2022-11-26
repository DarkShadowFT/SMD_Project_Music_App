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

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistContentViewHolder> implements Filterable {
		private ArrayList<Playlist> playlists;
		private ArrayList<Playlist> filteredPlaylists;
		private Filter filter;

		private PlaylistAdapter.onPlaylistClick playlistClickListener;

		public PlaylistAdapter(ArrayList<Playlist> ds, PlaylistAdapter.onPlaylistClick ClickListener){
				playlists = ds;
				filteredPlaylists = ds;
				this.playlistClickListener = ClickListener;
		}

		public interface onPlaylistClick {
				//void onItemClick(int position);
				public void onItemClick(Playlist playlist);
		}

		//=====================================================================================
		public class PlaylistContentViewHolder extends RecyclerView.ViewHolder{

				public TextView playlistName;
				public TextView playlistCount;
				public ImageView playlistImage;

				public PlaylistContentViewHolder(View v){
						super(v);
						playlistName = (TextView) v.findViewById(R.id.playlist_name);
						playlistCount = (TextView) v.findViewById(R.id.no_of_playlists);
						playlistImage = (ImageView) v.findViewById(R.id.img);
						itemView.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
										int pos = (int) v.getTag();
										playlistClickListener.onItemClick(filteredPlaylists.get(pos));
								}
						});
				}
		}
		//=====================================================================================
		@Override
		public PlaylistContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View v = LayoutInflater.from(parent.getContext())
								.inflate(R.layout.playlist_list_item, parent, false);

				PlaylistContentViewHolder vh = new PlaylistContentViewHolder(v);
				return vh;
		}

		@Override
		public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistContentViewHolder holder, int position) {
				holder.playlistName.setText(filteredPlaylists.get(position).getName());
				holder.playlistCount.setText(Integer.toString(filteredPlaylists.get(position).getSongsList().size()));
//				holder.playlistImage.setImageURI();
				holder.itemView.setTag(position);
		}

		@Override
		public Filter getFilter() {
				if (filter == null){
						filter = new PlaylistFilter();
				}
				return filter;
		}

		@Override
		public int getItemCount() {
				return filteredPlaylists.size();
		}

		private class PlaylistFilter extends Filter {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
						FilterResults filterResults = new FilterResults();

						if (constraint != null && constraint.length() > 0){
								ArrayList<Playlist> filteredList = new ArrayList<Playlist>();
								for (int i = 0; i < playlists.size(); i++){
										if (playlists.get(i).getName().toLowerCase(Locale.ROOT).contains(constraint) ||
														playlists.get(i).getName().contains(constraint)
										){
												filteredList.add(playlists.get(i));
										}
								}

								filterResults.count = filteredList.size();
								filterResults.values = filteredList;
						}

						else {
								filterResults.count = playlists.size();
								filterResults.values = playlists;
						}

						return filterResults;
				}

				@Override
				protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
						filteredPlaylists = (ArrayList<Playlist>) filterResults.values;
						notifyDataSetChanged();
				}
		}

		public void updateData(ArrayList<Playlist> ds){
				playlists = ds;
				filteredPlaylists = ds;
				notifyDataSetChanged();
		}
}
