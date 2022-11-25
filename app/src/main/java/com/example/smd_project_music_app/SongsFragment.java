package com.example.smd_project_music_app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import java.util.Hashtable;

public class SongsFragment extends Fragment implements SongAdapter.OnMusicClick {

		SongsFragmentListener listener;
		private RecyclerView recyclerView;
		private SongAdapter mAdapter;
		private RecyclerView.LayoutManager layoutManager;

		private static ContentResolver contentResolver1;

		public ArrayList<Song> songsList = new ArrayList<>();
		private Hashtable<String, Song> index = new Hashtable<String, Song>();
		private ContentResolver contentResolver;
		private EditText search;
		Filterable filterable;
		private TextView noOfSongs;

		public static Fragment getInstance(int position, ContentResolver mcontentResolver) {
				Bundle bundle = new Bundle();
				bundle.putInt("pos", position);
				SongsFragment tabFragment = new SongsFragment();
				tabFragment.setArguments(bundle);
				contentResolver1 = mcontentResolver;
				return tabFragment;
		}

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

				SongAdapter adapter = new SongAdapter(songsList, this);
				mAdapter = adapter;
				filterable = adapter;
				recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
				recyclerView.setAdapter(mAdapter);

				noOfSongs = (TextView) view.findViewById(R.id.no_of_songs);

				return view;
		}

		@Override
		public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
				contentResolver = contentResolver1;
				setContent();
				noOfSongs.setText(Integer.toString(songsList.size()));
		}

		/**
		 * Setting the content in the listView and sending the data to the Activity
		 */
		public void setContent() {
				getMusic();
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
								if (songCursor.getString(songPath).contains("/storage/emulated/0/Songs")){
										songsList.add(new Song(songCursor.getString(songTitle),
														songCursor.getString(songArtist), songCursor.getString(songPath),
														songCursor.getInt(songDuration)));
								}
						}
						while (songCursor.moveToNext());
						mAdapter.notifyDataSetChanged();
						songCursor.close();
				}
		}

		@Override
		public void onItemClick(Song song) {
				index.put(song.getId(), song);
				listener.onSongSelected(song);
		}

//		private void showDialog(final int position) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//				builder.setMessage(getString(R.string.play_next))
//								.setCancelable(true)
//								.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//
//										}
//								})
//								.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialog, int which) {
//												createDataParse.currentSong(songsList.get(position));
//												setContent();
//										}
//								});
//				AlertDialog alertDialog = builder.create();
//				alertDialog.show();
//		}

		public interface SongsFragmentListener{
				public void onSongSelected(Song song);
		}
}
