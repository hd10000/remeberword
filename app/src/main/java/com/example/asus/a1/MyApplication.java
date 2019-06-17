package com.example.asus.a1;

import android.app.Application;

public class MyApplication extends Application {

    private int difIndex;
    MyApplication(){
        difIndex=0;
    }
    public int getDifIndex() {
        return difIndex;
    }

    public void setDifIndex(int difIndex) {
        this.difIndex = difIndex;
    }
}
