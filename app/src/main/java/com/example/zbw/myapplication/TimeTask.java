package com.example.zbw.myapplication;

import android.util.Log;

import java.util.TimerTask;

/**
 * Created by zbw on 2016/5/28.
 */
public class TimeTask extends TimerTask {
    int _second=0;
    public void run(){
        ++_second;
        Log.d("second",Integer.toString(_second));
    }
}
