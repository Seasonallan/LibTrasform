package com.example.testplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PluginTestClass pluginTestClass=new PluginTestClass();
        pluginTestClass.init();

        onNext();
    }

    @TimeTotal
    public void onNext(){
        System.out.println("onNext");
    }

}