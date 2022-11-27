package com.example.smd_project_music_app;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.example.smd_project_music_app.DB.PlaylistFirebaseDAO;
import com.example.smd_project_music_app.Model.Playlist;

import java.util.ArrayList;

public class PlaylistViewModel extends ViewModel {
		private ArrayList<String> playlistNames;
		PlaylistFirebaseDAO dao;

		public ArrayList<String> getPlaylistNames(Bundle savedInstanceState, String key){
				if (playlistNames == null){
						if (savedInstanceState == null) {
								if (dao != null){
										playlistNames = Playlist.load(dao);
								}
								else playlistNames = new ArrayList<String>();
						}
						else{
								playlistNames = (ArrayList<String>) savedInstanceState.get(key);
						}
				}
				return playlistNames;
		}

		public void setDao(PlaylistFirebaseDAO d){
				dao = d;
		}

		public ArrayList<String> update(){
				if (dao != null){
						playlistNames = Playlist.load(dao);
				}
				else playlistNames = new ArrayList<String>();
				return playlistNames;
		}
}
