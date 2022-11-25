package com.example.smd_project_music_app;


import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
		private ContentResolver contentResolver;
		private ArrayList<Fragment> fragments = new ArrayList<>();
		private ArrayList<String> fragmentTitles = new ArrayList<>();
		public ViewPagerAdapter(@NonNull FragmentManager fm, ContentResolver contentResolver) {
				super(fm);
				this.contentResolver = contentResolver;
		}
		//add fragment to the viewpager
		public void addFragment(Fragment fragment, String title){
				fragments.add(fragment);
				fragmentTitles.add(title);
		}
		@NonNull
		@Override
		public Fragment getItem(int position) {
				if (position == 0){
						return PlaylistsFragment.getInstance(position, contentResolver);
				}

				else if (position == 1){
						return SongsFragment.getInstance(position, contentResolver);
				}
				else
					return null;
		}
		@Override
		public int getCount() {
				return fragments.size();
		}
		//to setup title of the tab layout
		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
				return fragmentTitles.get(position);
		}
}