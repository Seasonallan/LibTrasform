package com.example.testplugin;

import android.util.Log;

public class PluginTestClass {

    @TimeTotal
    public void init(){
        Log.e("TTT", "33>>"+ (new Throwable()).getStackTrace()[1].getMethodName());
        Log.e("TTT", "33>>"+ (new Throwable()).getStackTrace()[1].getClassName());
        System.out.println("PluginTestClass init");
        //这里将会插入System.out.println("我是插入的代码");
    }
}
