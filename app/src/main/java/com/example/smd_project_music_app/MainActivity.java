package com.example.smd_project_music_app;

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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

		ArrayList<Songs> dataset = new ArrayList<Songs>();
		private RecyclerView recyclerView;
		private RecyclerView.LayoutManager layoutManager;
		private Playlist_content_Adapter mAdapter = new Playlist_content_Adapter(dataset);
		private EditText search;
		private TextView noOfSongs;
		private Filterable filterable;
		private ContentResolver contentResolver;

		private DrawerLayout mDrawerLayout;
		private final int MY_PERMISSION_REQUEST = 100;

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

				Songs obj1 = new Songs(R.drawable.my_book, "ButterCup", "Jack Staubr");
				Songs obj2 = new Songs(R.drawable.my_pencil, "Alone", "Alan Walker");
				dataset.add(obj1);
				dataset.add(obj2);
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
				System.out.println();
		}

		public void getMusic() {
				Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				Cursor songCursor = contentResolver.query(songUri, null, null, null, null);
				if (songCursor != null && songCursor.moveToFirst()) {
						int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
						int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
						int songPath = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

						do {
								dataset.add(new Songs(songCursor.getString(songTitle), songCursor.getString(songArtist), songCursor.getString(songPath)));
						}
						while (songCursor.moveToNext());
					songCursor.close();
				}
		}
}