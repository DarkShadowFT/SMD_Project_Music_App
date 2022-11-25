package com.example.smd_project_music_app;

import java.io.Serializable;
import java.util.UUID;

public class Song implements Serializable {
    private int img;
    private int duration;
    private String id;
    private String title;
    private String artist;
    private String path;

    public Song(){
        id = UUID.randomUUID().toString();
        title = "";
        artist = "";
        path = "";
        img = -1;
    }

    public Song(int i, String t, String a, int d){
        id = UUID.randomUUID().toString();
        img = i;
        title = t;
        artist = a;
        duration = d;
    }

    public Song(String t, String a, String p, int d){
        id = UUID.randomUUID().toString();
        title = t;
        artist = a;
        path = p;
        duration = d;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
