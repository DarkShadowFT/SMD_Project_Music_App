package com.example.smd_project_music_app;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class PlaylistFirebaseDAO {

		public interface DataObserver{
				public void update();
		}

		private DataObserver observer;
		FirebaseDatabase database;
		DatabaseReference myRef;

		ArrayList<Hashtable<String,String>> data;

		public PlaylistFirebaseDAO(DataObserver obs){
				observer = obs;
				database = FirebaseDatabase.getInstance();
				//"true" tells firebase to store your uncommitted changes to the firebase DB on your disk
				if (database == null)
						database.setPersistenceEnabled(true);
				myRef = database.getReference("playlistData");

				myRef.addValueEventListener(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
								try {
										data = new ArrayList<Hashtable<String,String>>();
//                    A DataSnapshot instance contains data from a Firebase Database location.
//                    Any time you read Database data, you receive the data as a DataSnapshot.
										for (DataSnapshot d : dataSnapshot.getChildren()) {
												GenericTypeIndicator<HashMap<String,Object>> type = new GenericTypeIndicator<HashMap<String, Object>>() {};

//                        Cast the data to HashMap<String,Object> and store the values in map object
												HashMap<String,Object> map =  d.getValue(type);

												Hashtable<String,String> obj = new Hashtable<String,String>();

												//Store the row/object in obj Hashtable
												for(String key : map.keySet()){
														obj.put(key,map.get(key).toString());
												}
												data.add(obj);
										}

										observer.update();
								}
								catch (Exception ex) {
										Log.e("firebasedb", ex.getMessage());
								}
						}

						@Override
						public void onCancelled(DatabaseError error) {
								Log.w("firebasedb", "Failed to read value.", error.toException());
						}
				});

		}

		public void save(Hashtable<String, String> attributes) {
				myRef.child(attributes.get("id")).setValue(attributes);
		}

		public void save(ArrayList<Hashtable<String, String>> objects) {
				for(Hashtable<String,String> obj : objects){
						save(obj);
				}
		}

		public ArrayList<Hashtable<String, String>> load() {
				if (data == null){
						data = new ArrayList<Hashtable<String,String>>();
				}
				return data;
		}

		public Hashtable<String, String> load(String id) {
				return null;
		}
}
