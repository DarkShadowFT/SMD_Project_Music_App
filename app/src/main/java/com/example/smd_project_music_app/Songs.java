package com.example.smd_project_music_app;

import android.widget.ImageView;

public class Songs {
    private int img;
    private String name;
    private String singer;

    public Songs(int i, String n, String s){
        img = i;
        name = n;
        singer = s;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }
}
