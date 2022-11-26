package com.example.smd_project_music_app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SongsFragment extends Fragment implements SongAdapter.onSongClick {

		SongsFragmentListener listener;
		private RecyclerView recyclerView;
		private SongAdapter adapter;
		private RecyclerView.LayoutManager layoutManager;

		private Playlist playlist = new Playlist();
		private ContentResolver contentResolver;
		private EditText search;
		Filterable filterable;

		private TextView noOfSongs;
		private TextView allSongsLabel;
		private Menu optionsMenu;

		String playlistName;
		private boolean defaultMode = true;

		@Override
		public void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);

		}

		@Override
		public void onAttach(@NonNull Context context) {
				super.onAttach(context);
				if (context instanceof SongsFragmentListener) {
						listener = (SongsFragmentListener) context;
				}
		}

		public void onResume() {
				super.onResume();
				Bundle arguments = getArguments();
				if (arguments != null && arguments.size() > 0) {
						changeDataset((Playlist) arguments.getSerializable("data"));
						arguments.clear();
				}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				View view = inflater.inflate(R.layout.songs_fragment, container, false);

				search = (EditText) view.findViewById(R.id.search);
				search.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
								filterable.getFilter().filter(search.getText().toString());
						}

						@Override
						public void afterTextChanged(Editable editable) {

						}
				});

				recyclerView = view.findViewById(R.id.list);
				recyclerView.setHasFixedSize(true);
				layoutManager = new LinearLayoutManager(getActivity());
				recyclerView.setLayoutManager(layoutManager);

				adapter = new SongAdapter(playlist.getDataset(), this);
				filterable = adapter;
				recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
				recyclerView.setAdapter(adapter);

				noOfSongs = (TextView) view.findViewById(R.id.no_of_songs);
				allSongsLabel = (TextView) view.findViewById(R.id.all_songs_label);
				return view;
		}

		@Override
		public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
				contentResolver = getActivity().getContentResolver();
				setContent();
		}

		public void onSaveInstanceState(Bundle state) {
				super.onSaveInstanceState(state);
				state.putSerializable("playlist",playlist);
		}

		@Override
		public void onActivityCreated(@Nullable Bundle savedInstanceState) {
				super.onActivityCreated(savedInstanceState);
				if (savedInstanceState != null) {
						//Restore the fragment's state here
						playlist = (Playlist) savedInstanceState.getSerializable("playlist");
				}
		}

		/**
		 * Setting the content in the listView and sending the data to the Activity
		 */
		public void setContent() {
				playlist.getSongsList().clear();
				playlist.getDataset().clear();
				getMusic();
				noOfSongs.setText(Integer.toString(playlist.getSongsList().size()));
		}

		public void getMusic() {
				Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
				if (songCursor != null && songCursor.moveToFirst()) {
						int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
						int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
						int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
						int songDuration = songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

						do {
								if (songCursor.getString(songPath).contains("/storage/emulated/0/Songs")) {
										playlist.addSong(new Song(songCursor.getString(songTitle),
														songCursor.getString(songArtist), songCursor.getString(songPath),
														songCursor.getInt(songDuration)));
								}
						}
						while (songCursor.moveToNext());
						adapter.notifyDataSetChanged();
						songCursor.close();
				}
		}

		public void changeDataset(Playlist playlist) {
				this.playlist.getDataset().clear();
				this.playlist.getSongsList().clear();

				if (playlist != null) {
						this.playlist = playlist;
						if (adapter == null) {
								adapter = new SongAdapter(playlist.getDataset(), this);
						}
						adapter.updateData(playlist.getDataset());
						if (noOfSongs == null)
								noOfSongs = (TextView) getView().findViewById(R.id.no_of_songs);
						noOfSongs.setText(Integer.toString(playlist.getSongsList().size()));
						playlistName = playlist.getName();
						allSongsLabel.setText(playlistName);
						defaultMode = false;
				}
		}

		// action mode
		private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {

						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.main_action, menu);
						return true;
				}

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
				}

				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						switch (item.getItemId()) {
								case R.id.delete:
										adapter.removeSelectedItems();
										mode.finish();
										noOfSongs.setText(Integer.toString(playlist.getSongsList().size()));
										return true;
								default:
										return false;
						}
				}

				public void onDestroyActionMode(ActionMode mode) {
						adapter.setMode(SongAdapter.DEFAULT_MODE);
						defaultMode = true;
				}
		};

		@Override
		public void onItemClick(Song song) {
				playlist.addSong(song);
				listener.onSongSelected(this.playlist);
		}

		@Override
		public void onLongItemClick(Song song) {
				if (!defaultMode){
						getActivity().startActionMode(actionModeCallback);
						adapter.setMode(SongAdapter.SELECTABLE_MODE);
				}
		}

		@Override
		public void deleteSelectedItems(ArrayList<String> songPaths) {
				for (int i = 0; i < songPaths.size(); i++){
						Iterator<Map.Entry<String, Song>> iter = playlist.getSongsList().entrySet().iterator();
						while (iter.hasNext()) {
								Map.Entry<String, Song> newMap = (Map.Entry<String, Song>) iter.next();
								if (newMap.getValue().getPath().equals(songPaths.get(i))){
										playlist.removeSong(newMap.getValue().getId());
								}
						}
				}
				listener.deleteSelectedItems(playlistName, songPaths);
		}

		@Override
		public void onAddBtnClick(Song song) {
				listener.onAddToPlaylistClicked(song);
		}

		public interface SongsFragmentListener {
				public void onSongSelected(Playlist playlist);
				public void onAddToPlaylistClicked(Song song);
				public void deleteSelectedItems(String playlistName, ArrayList<String> songIDs);
		}

		public boolean onCreateOptionsMenu(Menu menu) {
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.main, menu);
				optionsMenu = menu;
				return super.getActivity().onCreateOptionsMenu(menu);
		}

		public boolean onOptionsItemSelected(MenuItem item) {
				return super.onOptionsItemSelected(item);
		}
}
