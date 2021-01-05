package com.example.testplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TTT", ">>"+ (new Throwable()).getStackTrace()[1].getMethodName());
        PluginTestClass pluginTestClass=new PluginTestClass();
        pluginTestClass.init();

        onNext();
    }

    @TimeTotal
    public void onNext(){
        Log.e("TTT", "22>>"+ (new Throwable()).getStackTrace()[1].getMethodName());
        System.out.println("onNext");
    }

}