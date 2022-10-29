package com.example.smd_project_music_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	ArrayList<Songs> dataset = new ArrayList<Songs>();
	private RecyclerView recyclerView;
	private RecyclerView.LayoutManager layoutManager;
	private RecyclerView.Adapter mAdapter = new Playlist_content_Adapter(dataset);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.playlist_contents);

		// setting recyclerView
		recyclerView = (RecyclerView) findViewById(R.id.list);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);

		recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
		recyclerView.setAdapter(mAdapter);

		Songs obj1 = new Songs(R.drawable.my_book, "ButterCup", "Jack Staubr");
		Songs obj2 = new Songs(R.drawable.my_pencil, "Alone", "Alan Walker");
		dataset.add(obj1);
		dataset.add(obj2);
		mAdapter.notifyDataSetChanged();



	}
}