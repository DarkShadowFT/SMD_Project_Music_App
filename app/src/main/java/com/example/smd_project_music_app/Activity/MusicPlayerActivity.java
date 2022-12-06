package com.example.smd_project_music_app.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smd_project_music_app.CreateNotification;
import com.example.smd_project_music_app.HelperMusicPlayer;
import com.example.smd_project_music_app.Model.Playlist;
import com.example.smd_project_music_app.R;
import com.example.smd_project_music_app.Model.Song;
import com.example.smd_project_music_app.Services.OnClearFromRecentService;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerActivity extends AppCompatActivity {

		SeekBar ProgressBar;
		ImageView Avatar;
		TextView SongName;
		TextView Singer;
		TextView TotalTime;
		TextView ElapsedTime;

		ImageView NextSong;
		ImageView PreviousSong;
		ImageView PlayBtn;

		ArrayList<Song> MySongsDataset;
		int currSong;
		Song MyCurrSong;

		Handler handler;
		Runnable runnable;

		MediaPlayer MyMediaPlayer= HelperMusicPlayer.getInstance();

		NotificationManager notificationManager;


		@Override
		protected void onCreate(@Nullable Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_music);

				//gget all views
				ProgressBar=findViewById(R.id.bar);
				Avatar=findViewById(R.id.MusicAvatar);
				SongName=findViewById(R.id.SongTitle);
				Singer=findViewById(R.id.SingerName);
				TotalTime=findViewById(R.id.total_time);
				ElapsedTime=findViewById(R.id.curr_time);
				Avatar.setImageResource(R.drawable.song_artist);

				NextSong=findViewById(R.id.next);
				PreviousSong=findViewById(R.id.prev);
				PlayBtn=findViewById(R.id.Play);
				handler = new Handler();

				Intent intent = getIntent();
				MySongsDataset = ((Playlist) intent.getSerializableExtra("MySongs")).getDataset();
				SetASong();

				PlayBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
								PauseMusic();
						}
				});

				NextSong.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) { PlayNextSong();
						}
				});

				PreviousSong.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) { PlayPrevSong();
						}
				});

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
						createChannel();
						registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
						startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
				}
		}

		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
						String action = intent.getExtras().getString("actionname");

						switch (action){
								case CreateNotification.ACTION_PREVIUOS:
										PlayPrevSong();
										break;
								case CreateNotification.ACTION_PLAY:
										PauseMusic();
										break;
								case CreateNotification.ACTION_NEXT:
										PlayNextSong();
										break;
						}
				}
		};

		private void createChannel() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
						NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
										"smd_project", NotificationManager.IMPORTANCE_LOW);

						notificationManager = getSystemService(NotificationManager.class);
						if (notificationManager != null){
								notificationManager.createNotificationChannel(channel);
						}
				}
		}

		private void playCycle() {
				try {
						ProgressBar.setProgress(MyMediaPlayer.getCurrentPosition());
						ElapsedTime.setText(getFormattedTime(MyMediaPlayer.getCurrentPosition()));
						if (MyMediaPlayer.isPlaying()) {
								runnable = new Runnable() {
										@Override
										public void run() {
												playCycle();
										}
								};
								handler.postDelayed(runnable, 100);
						}
				} catch (Exception e) {
						e.printStackTrace();
				}
		}

		private void PlayMusic()
		{
				CreateNotification.createNotification(MusicPlayerActivity.this, MyCurrSong,
								R.drawable.ic_baseline_pause_circle_24, currSong, MySongsDataset.size()-1);
				PlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24);
				MyMediaPlayer.reset();
				try {
						MyMediaPlayer.setDataSource(MyCurrSong.getPath());
						MyMediaPlayer.prepare();
						setControls();
				} catch (IOException e) {
						e.printStackTrace();
				}

		}
		private void PauseMusic()
		{
				if(MyMediaPlayer.isPlaying())
				{
						CreateNotification.createNotification(MusicPlayerActivity.this, MyCurrSong,
										R.drawable.ic_baseline_play_circle_24, currSong, MySongsDataset.size()-1);
						MyMediaPlayer.pause();
						PlayBtn.setImageResource(R.drawable.ic_baseline_play_circle_24);
				}
				else
				{
						CreateNotification.createNotification(MusicPlayerActivity.this, MyCurrSong,
										R.drawable.ic_baseline_pause_circle_24, currSong, MySongsDataset.size()-1);
						MyMediaPlayer.start();
						PlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24);
						playCycle();
				}
		}
		private void PlayNextSong()
		{
				CreateNotification.createNotification(MusicPlayerActivity.this, MyCurrSong,
								R.drawable.ic_baseline_pause_circle_24, currSong, MySongsDataset.size()-1);
				if(HelperMusicPlayer.index<MySongsDataset.size()-1) {
						HelperMusicPlayer.index++;
						SetASong();
				}
				else if(HelperMusicPlayer.index==MySongsDataset.size()-1)
				{
						HelperMusicPlayer.index=-1;

				}

		}
		private void PlayPrevSong()
		{
				CreateNotification.createNotification(MusicPlayerActivity.this, MyCurrSong,
								R.drawable.ic_baseline_pause_circle_24, currSong, MySongsDataset.size()-1);
				if(HelperMusicPlayer.index>0) {
						HelperMusicPlayer.index--;
						SetASong();
				}
				else if(HelperMusicPlayer.index==0)
				{
						HelperMusicPlayer.index=MySongsDataset.size();

				}
		}
		private void SetASong()
		{
				currSong=HelperMusicPlayer.index;
				//set curr song
				MyCurrSong=MySongsDataset.get(currSong);
				String text = MyCurrSong.getTitle();
				if (text.length() >= 23){
						text = text.substring(0, 23) + "...";
				}
				SongName.setText(text);
				text = MyCurrSong.getArtist();
				if (text.length() >= 24){
						text = text.substring(0, 23) + "...";
				}
				Singer.setText(text);
				PlayMusic();
		}

		public String getFormattedTime(long milliseconds){
				// formula for conversion of milliseconds to minutes.
				long minutes = (milliseconds / 1000) / 60;

				// formula for conversion of milliseconds to seconds
				long seconds = (milliseconds / 1000) % 60;
				if (seconds < 10)
						return minutes + ":" + "0" + seconds;
				else
						return minutes + ":" + seconds;
		}

		/**
		 * Function to set the controls according to the song
		 */

		private void setControls() {
				long milliseconds = MyMediaPlayer.getDuration();
				ProgressBar.setMax((int) milliseconds);
				TotalTime.setText(getFormattedTime(milliseconds));
				MyMediaPlayer.start();
				playCycle();

				ProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
								if (fromUser) {
										MyMediaPlayer.seekTo(progress);
										ElapsedTime.setText(getFormattedTime(progress));
								}
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {

						}
				});
		}

		@Override
		protected void onDestroy() {
				super.onDestroy();
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
						notificationManager.cancelAll();
				}

				unregisterReceiver(broadcastReceiver);
		}
}