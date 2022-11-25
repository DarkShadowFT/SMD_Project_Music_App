package com.example.smd_project_music_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

		private ViewPager viewPager;
		private TabLayout tabLayout;

		private PlaylistsFragment playlistsFragment;
		private SongsFragment songsFragment;

		private final int MY_PERMISSION_REQUEST = 100;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
				viewPager = findViewById(R.id.view_pager);
				tabLayout = findViewById(R.id.tabs);

				playlistsFragment = new PlaylistsFragment();
				songsFragment = new SongsFragment();

				grantedPermission();

				tabLayout.setupWithViewPager(viewPager);

				ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getContentResolver());
				viewPagerAdapter.addFragment(playlistsFragment, "Playlists");
				viewPagerAdapter.addFragment(songsFragment, "Songs");
				viewPager.setAdapter(viewPagerAdapter);
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
//												Snackbar snackbar = Snackbar.make(mDrawerLayout, "Provide the Storage Permission", Snackbar.LENGTH_LONG);
//												snackbar.show();
												finish();
										}
								}
				}
		}

}