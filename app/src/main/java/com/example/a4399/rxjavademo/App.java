package com.example.a4399.rxjavademo;

import android.app.Application;

import com.xm4399.lib_choosepic.utils.ChoosePic;
import com.xm4399.lib_choosepic.utils.ChoosePicLog;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ChoosePicLog.SHOW_LOG = true;
        ChoosePic.init(this);
    }
}
