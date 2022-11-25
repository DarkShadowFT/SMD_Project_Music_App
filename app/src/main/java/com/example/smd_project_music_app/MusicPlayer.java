package com.example.smd_project_music_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity {

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

    ArrayList<Songs> MySongsDataset;
    int currSong;
    Songs MyCurrSong;

    MediaPlayer MyMediaPlayer=HelperMusicPlayer.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_click);

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
        MySongsDataset=(ArrayList<Songs>) intent.getSerializableExtra("MySongs");
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


        Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
        MusicPlayer.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(MyMediaPlayer != null){
                    int mCurrentPosition = MyMediaPlayer.getCurrentPosition();
                    ProgressBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }

    private void PlayMusic()
    {
        PlayBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24);
        MyMediaPlayer.reset();
        try {
            MyMediaPlayer.setDataSource(MyCurrSong.getPath());
            MyMediaPlayer.prepare();
            MyMediaPlayer.start();
            ProgressBar.setProgress(0);
            ProgressBar.setMax(MyMediaPlayer.getDuration());
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

}
