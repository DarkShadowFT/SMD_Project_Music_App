package com.example.smd_project_music_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity implements Playlist_content_Adapter.OnMusicClick {

		ArrayList<Songs> dataset = new ArrayList<Songs>();
		private RecyclerView recyclerView;
		private RecyclerView.LayoutManager layoutManager;
		private Playlist_content_Adapter mAdapter = new Playlist_content_Adapter(dataset, this);
		private EditText search;
		private TextView noOfSongs;
		private Filterable filterable;
		private ContentResolver contentResolver;

		private DrawerLayout mDrawerLayout;
		private final int MY_PERMISSION_REQUEST = 100;

		ActivityResultLauncher<Intent> MusicPlayerLauncher;

		////

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.playlist_contents);

				search = (EditText) findViewById(R.id.search);
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

				noOfSongs = (TextView) findViewById(R.id.no_of_songs);
				contentResolver = this.getContentResolver();
				grantedPermission();
				setContent();

				// setting recyclerView
				recyclerView = (RecyclerView) findViewById(R.id.list);
				recyclerView.setHasFixedSize(true);
				layoutManager = new LinearLayoutManager(this);
				recyclerView.setLayoutManager(layoutManager);

				recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
				filterable = mAdapter;
				recyclerView.setAdapter(mAdapter);


				/**launcher**/
				MusicPlayerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
						@Override
						public void onActivityResult(ActivityResult result) {
								if (result.getResultCode() == RESULT_OK) {
										Intent intent4 = result.getData();
								}

						}
				});
				exportToM3U("playlist.m3u");
				dataset.clear();
				mAdapter.notifyDataSetChanged();;
				importPlaylist("playlist.m3u");
				noOfSongs.setText(Integer.toString(dataset.size()));
				mAdapter.notifyDataSetChanged();
		}

		/**
		 * Function to ask user to grant the permission.
		 */

		private void grantedPermission() {
				if (ContextCompat.checkSelfPermission(MainActivity.this,
								Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(MainActivity.this,
										new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
						if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
										Manifest.permission.READ_EXTERNAL_STORAGE)) {
								ActivityCompat.requestPermissions(MainActivity.this,
												new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
						} else {
								if (ContextCompat.checkSelfPermission(MainActivity.this,
												Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//										Snackbar snackbar = Snackbar.make(mDrawerLayout, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
//										snackbar.show();
								}
						}
				} else {
//						setPagerLayout();
				}
		}

		/**
		 * Checking if the permission is granted or not
		 *
		 * @param requestCode
		 * @param permissions
		 * @param grantResults
		 */
		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				switch (requestCode) {
						case MY_PERMISSION_REQUEST:
								if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
										if (ContextCompat.checkSelfPermission(MainActivity.this,
														Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
												Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
//												setPagerLayout();
										} else {
												Snackbar snackbar = Snackbar.make(mDrawerLayout, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
												snackbar.show();
												finish();
										}
								}
				}
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
										dataset.add(new Songs(songCursor.getString(songTitle),
														songCursor.getString(songArtist), songCursor.getString(songPath),
														songCursor.getInt(songDuration)));
								}
						}
						while (songCursor.moveToNext());
						songCursor.close();
				}
		}

		@Override
		public void onItemClick(Songs p) {
				Intent intent = new Intent(this, MusicPlayer.class);

				intent.putExtra("MySongs", dataset);

				//set flag or it will give error
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				MusicPlayerLauncher.launch(intent);
		}

		public void exportToM3U(String playlistName) {
				try {
						File file = new File(getFilesDir(), playlistName);
						if (dataset.size() > 0 && file.exists())
								file.delete();
						BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
						writer.append("#EXTM3U");
						writer.newLine();
						String output;
						for (int i = 0; i < dataset.size(); i++) {
								output = "#EXTINF:" + dataset.get(i).getDuration() + "," +
												dataset.get(i).getArtist() + " – " + dataset.get(i).getTitle();
								writer.append(output);
								writer.newLine();
								writer.append(dataset.get(i).getPath());
								writer.newLine();
						}
						writer.close();
				} catch (Exception ex) {
						ex.printStackTrace();
				}
		}

		public void importPlaylist(String playlistName){
				File playlistFile = new File(getFilesDir(), playlistName);
				try {
						String line;
						BufferedReader reader = new BufferedReader(new FileReader(playlistFile));
						line = reader.readLine();
						if (line.equals("#EXTM3U")){
								String artist;
								String title;
								String path;
								int duration;
								while ((line = reader.readLine()) != null){
										String components[] = line.split(" – ", 2);
										if (components.length > 1){
												title = components[1];
										}
										else {
												title = "unknown";
										}
										duration = Integer.parseInt(components[0].split(":")[1].split(",")[0]);
										artist = components[0].split(":")[1].split(",")[1];
										line = reader.readLine();
										path = line;
										dataset.add(new Songs(title, artist, path, duration));
								}
						}
						else {
								Log.e("ERROR", "The file is unsupported!");
						}
				}
				catch (Exception ex){
						ex.printStackTrace();
				}
		}
}