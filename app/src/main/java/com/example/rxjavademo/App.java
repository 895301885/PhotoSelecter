package com.example.rxjavademo;

import android.app.Application;

import com.lib_choosepic.utils.ChoosePic;
import com.lib_choosepic.utils.ChoosePicLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ChoosePicLog.SHOW_LOG = true;
        ChoosePic.init(this);
    }
}
