package com.example.testplugin;

public class TimeCount {

    public static void test(){
        long recordTime = System.currentTimeMillis();

        long currentTime = System.currentTimeMillis();
        System.out.println("cost time= "+(currentTime - recordTime)+" ms");
    }

}
