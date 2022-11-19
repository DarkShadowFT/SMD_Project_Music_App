package com.example.smd_project_music_app;

import java.io.Serializable;

public class Songs implements Serializable {
    private int img;
    private String title;
    private String artist;
    private String path;
    private int duration;

    public Songs(int i, String t, String a, int d){
        img = i;
        title = t;
        artist = a;
        duration = d;
    }

    public Songs(String t, String a, String p, int d){
        title = t;
        artist = a;
        path = p;
        duration = d;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return this.path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
