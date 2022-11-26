package com.example.smd_project_music_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
		ImageView PauseBtn;

		ArrayList<Song> MySongsDataset;
		int currSong;
		Song MyCurrSong;

		Runnable runnable;
		Handler handler;

		MediaPlayer MyMediaPlayer=HelperMusicPlayer.getInstance();

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

				NextSong=findViewById(R.id.next);
				PreviousSong=findViewById(R.id.prev);
				PlayBtn=findViewById(R.id.Play);


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
				PlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24);
				MyMediaPlayer.reset();
				try {
						MyMediaPlayer.setDataSource(MyCurrSong.getPath());
						MyMediaPlayer.prepare();
						setControls();
						MyMediaPlayer.start();
//						ProgressBar.setProgress(0);
						long milliseconds = MyMediaPlayer.getDuration();
//
//						ProgressBar.setMax((int) milliseconds);
						TotalTime.setText(getFormattedTime(milliseconds));
//						ElapsedTime.setText(getFormattedTime(MyMediaPlayer.getCurrentPosition()));
						playCycle();
				} catch (IOException e) {
						e.printStackTrace();
				}

		}
		private void PauseMusic()
		{
				if(MyMediaPlayer.isPlaying())
				{
						MyMediaPlayer.pause();
						PlayBtn.setImageResource(R.drawable.ic_baseline_play_circle_24);
				}
				else
				{
						MyMediaPlayer.start();
						PlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24);
				}

		}
		private void PlayNextSong()
		{
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
				SongName.setText(MyCurrSong.getTitle());
				Singer.setText(MyCurrSong.getArtist());
				PlayMusic();

		}

		public String getFormattedTime(long milliseconds){
				// formula for conversion for
				// milliseconds to minutes.
				long minutes = (milliseconds / 1000) / 60;

				// formula for conversion for
				// milliseconds to seconds
				long seconds = (milliseconds / 1000) % 60;

				return minutes + ":" + seconds;
		}

		/**
		 * Function to set the controls according to the song
		 */

		private void setControls() {
				ProgressBar.setMax(MyMediaPlayer.getDuration());
				MyMediaPlayer.start();
				playCycle();
				ElapsedTime.setText(getFormattedTime(MyMediaPlayer.getDuration()));
				if (MyMediaPlayer.isPlaying()) {
						PauseMusic();
				}
				else {
					PlayMusic();
				}

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

}