package com.example.smd_project_music_app;


import android.content.ContentResolver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FixedFragmentStatePagerAdapter {
		private ContentResolver contentResolver;
		private ArrayList<Fragment> fragments = new ArrayList<>();
		private ArrayList<String> fragmentTitles = new ArrayList<>();
		private long baseId = 0;

		public ViewPagerAdapter(@NonNull FragmentManager fm, ContentResolver contentResolver) {
				super(fm);
				this.contentResolver = contentResolver;
		}
		//add fragment to the viewpager
		public void addFragment(Fragment fragment, String title){
				fragments.add(fragment);
				fragmentTitles.add(title);
				notifyDataSetChanged();
		}

		//add fragment to the viewpager
		public void removeFragment(Fragment fragment, String title){
				fragments.remove(fragment);
				fragmentTitles.remove(title);
				super.
				notifyDataSetChanged();
		}
		@NonNull
		@Override
		public Fragment getItem(int position) {
//				if (position == 0){
//						return PlaylistsFragment.getInstance(position, contentResolver);
//				}
//
//				else if (position == 1){
//						return SongsFragment.getInstance(position, contentResolver);
//				}
//				else
//					return null;
				return fragments.get(0);
		}

		/**
		 * Notify that the position of a fragment has been changed.
		 * Create a new ID for each position to force recreation of the fragment
		 * @param n number of items which have been changed
		 */
		public void notifyChangeInPosition(int n) {
				// shift the ID returned by getItemId outside the range of all previous fragments
				baseId += getCount() + n;
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