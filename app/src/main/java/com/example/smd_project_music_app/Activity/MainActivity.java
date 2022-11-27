package com.example.smd_project_music_app.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.smd_project_music_app.HelperMusicPlayer;
import com.example.smd_project_music_app.Model.Playlist;
import com.example.smd_project_music_app.Fragment.PlaylistsFragment;
import com.example.smd_project_music_app.R;
import com.example.smd_project_music_app.Model.Song;
import com.example.smd_project_music_app.Fragment.SongsFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaylistsFragment.PlaylistsFragmentListener, SongsFragment.SongsFragmentListener {

		private TabLayout tabLayout;
		private FrameLayout frameLayout;
		Fragment fragment;
		FragmentManager fragmentManager;
		FragmentTransaction fragmentTransaction;

		private PlaylistsFragment playlistsFragment;
		private Fragment songsFragment;

		private final int MY_PERMISSION_REQUEST = 200;
		boolean openPlaylist = false;
		Playlist playlist = new Playlist();

		/////
		ActivityResultLauncher<Intent> MusicPlayerLauncher;

		@RequiresApi(api = Build.VERSION_CODES.R)
		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				tabLayout = findViewById(R.id.tabs);
				frameLayout = findViewById(R.id.fragment);

				playlistsFragment = new PlaylistsFragment();
				songsFragment = new SongsFragment();

				grantedPermission();

				fragmentManager = getSupportFragmentManager();
				fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.fragment, playlistsFragment);
				fragmentTransaction.attach(songsFragment);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.commit();

				tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
							 @Override
							 public void onTabSelected(TabLayout.Tab tab) {
									 switch (tab.getPosition()) {
											 case 0:
													 fragment = playlistsFragment;
													 break;
											 case 1:
													 fragment = songsFragment;
													 break;
									 }
									 FragmentManager fm = getSupportFragmentManager();
									 FragmentTransaction ft = fm.beginTransaction();
									 if (openPlaylist){
											 Bundle arguments = new Bundle();
											 arguments.putSerializable("data", playlist);
											 fragment.setArguments(arguments);
											 openPlaylist = false;
									 }
									 ft.replace(R.id.fragment, fragment);
									 ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
									 ft.commitNow();
							 }

							 @Override
							 public void onTabUnselected(TabLayout.Tab tab) {

							 }

							 @Override
							 public void onTabReselected(TabLayout.Tab tab) {

							 }

			 	});

				MusicPlayerLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
						@Override
						public void onActivityResult(ActivityResult result) {
								if(result.getResultCode()==RESULT_OK)
								{
										Intent intent4=result.getData();
								}

						}
				});

				if (savedInstanceState != null){
						songsFragment = getSupportFragmentManager().getFragment(savedInstanceState, "songs");
				}

		}

		@Override
		protected void onSaveInstanceState(@NonNull Bundle outState) {
				super.onSaveInstanceState(outState);

				//Save the fragment's instance
				getSupportFragmentManager().putFragment(outState, "songs", songsFragment);
		}



		/**
		 * Function to ask user to grant the permission.
		 */

		@RequiresApi(api = Build.VERSION_CODES.R)
		private void grantedPermission() {
				if (ContextCompat.checkSelfPermission(MainActivity.this,
								Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions(MainActivity.this,
										new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
						if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
										Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
								ActivityCompat.requestPermissions(MainActivity.this,
												new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
						} else {
								if (ContextCompat.checkSelfPermission(MainActivity.this,
												Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
		 */
		@Override
		public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				switch (requestCode) {
						case MY_PERMISSION_REQUEST:
								if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
										if (ContextCompat.checkSelfPermission(MainActivity.this,
														Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
												Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
//												setPagerLayout();
										} else {
												Snackbar snackbar = Snackbar.make(frameLayout, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
												snackbar.show();
												finish();
										}
								}
				}
		}

		@Override
		public void onPlaylistSelected(Playlist playlist) {
				openPlaylist = true;
				this.playlist = playlist;
				tabLayout = (TabLayout) findViewById(R.id.tabs);
				TabLayout.Tab tab = tabLayout.getTabAt(1);
				assert tab != null;
				tab.select();
		}

		@Override
		public void onSongSelected(Playlist playlist) {
				Intent intent = new Intent(this , MusicPlayerActivity.class);
				intent.putExtra("MySongs", playlist);

				//set flag or it will give error
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				MusicPlayerLauncher.launch(intent);
		}

		@Override
		public void onAddToPlaylistClicked(Song song) {
				TabLayout.Tab tab = tabLayout.getTabAt(0);
				assert tab != null;
				tab.select();
				playlistsFragment.addSongToPlaylist(song);
		}

		@Override
		public void deleteSelectedItems(String playlistName, ArrayList<String> songPaths) {
				TabLayout.Tab tab = tabLayout.getTabAt(0);
				assert tab != null;
				tab.select();
				playlistsFragment.deleteSongsFromPlaylist(playlistName, songPaths);
				tab = tabLayout.getTabAt(1);
				assert tab != null;
				tab.select();
		}

		@Override
		protected void onDestroy() {
				super.onDestroy();
				HelperMusicPlayer.getInstance().release();
		}
}