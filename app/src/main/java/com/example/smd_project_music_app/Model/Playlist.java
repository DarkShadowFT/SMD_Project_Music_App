package com.example.smd_project_music_app.Model;

import com.example.smd_project_music_app.DB.PlaylistFirebaseDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Playlist implements Serializable {
		private String id;
		private String name;
		private String path;

		private ArrayList<Song> dataset;
		private ConcurrentHashMap<String, Song> songsList;

		private transient PlaylistFirebaseDAO dao = null;

		public Playlist(){
				id = UUID.randomUUID().toString();
				songsList = new ConcurrentHashMap<>();
				dataset = new ArrayList<>();
				path = "";
		}

		Playlist(String name){
				id = UUID.randomUUID().toString();
				this.name = name;
				this.songsList = new ConcurrentHashMap<>();
				dataset = new ArrayList<>();
				this.path = "";
		}

		Playlist(String name, String path, ConcurrentHashMap<String, Song> songsList){
				id = UUID.randomUUID().toString();
				this.name = name;
				this.songsList = songsList;
				dataset = new ArrayList<>();
				for (Map.Entry<String, Song> entry: songsList.entrySet()){
					dataset.add(entry.getValue());
				}
				this.path = path;
		}

		public String getId() {
				return id;
		}

		public void setId(String id) {
				this.id = id;
		}

		public String getName() {
				return name;
		}

		public void setName(String name) {
				this.name = name;
		}

		public ConcurrentHashMap<String, Song> getSongsList() {
				return songsList;
		}

		public void setSongsList(ConcurrentHashMap<String, Song> songsList) {
				this.songsList = songsList;
		}

		public ArrayList<Song> getDataset() {
				return dataset;
		}

		public void setDataset(ArrayList<Song> dataset) {
				this.dataset = dataset;
		}

		public String getPath() {
				return path;
		}

		public void setPath(String path) {
				this.path = path;
		}

		public PlaylistFirebaseDAO getDao() {
				return dao;
		}

		public void setDao(PlaylistFirebaseDAO dao) {
				this.dao = dao;
		}

		public void addSong(Song song){
				if (songsList.put(song.getId(), song) == null){
						dataset.add(song);
				}
		}

		public void removeSong(String songID){
				Song song = songsList.remove(songID);
				if (song != null){
						dataset.remove(song);
				}
		}

		public void save(){
				if (dao != null){
						Hashtable<String,String> data = new Hashtable<String, String>();

						data.put("id",id);
						data.put("name",name);

						dao.save(data);
				}
		}

		public static ArrayList<String> load(PlaylistFirebaseDAO dao) {
				ArrayList<String> products = new ArrayList<String>();
				if (dao != null) {
						ArrayList<Hashtable<String, String>> objects = dao.load();
						for (Hashtable<String, String> obj : objects) {
								products.add(obj.get("name"));
						}
				}
				return products;
		}
}
