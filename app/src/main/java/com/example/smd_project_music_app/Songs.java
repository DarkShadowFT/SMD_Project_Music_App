package com.example.smd_project_music_app;

public class Songs {
    private int img;
    private String title;
    private String artist;
    private String path;

    public Songs(int i, String n, String s){
        img = i;
        title = n;
        artist = s;
    }

    public Songs(String n, String s, String p){
        title = n;
        artist = s;
        path = p;
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
}
