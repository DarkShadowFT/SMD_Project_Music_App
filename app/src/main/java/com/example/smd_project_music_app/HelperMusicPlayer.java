package com.example.smd_project_music_app;

import android.media.MediaPlayer;

public class HelperMusicPlayer {
    static MediaPlayer instance;
    public static int index=-1;

    public static MediaPlayer getInstance()
    {
        if(instance==null)
        {
            instance=new MediaPlayer();

        }
        return instance;
    }
}
