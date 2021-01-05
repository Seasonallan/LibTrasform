package com.example.testplugin;

import java.util.HashMap;

/**
 * Created by zjy on 2019-05-05
 */
public class TimeManager {
    private static HashMap<String, Long> startTimeMap = new HashMap<>();
    private static HashMap<String, Long> endTimeMap = new HashMap<>();

    public static void addStartTime(String key, long time) {
        startTimeMap.put(key, time);
    }

    public static void addEndTime(String key, long time) {
        endTimeMap.put(key, time);
    }

    public static void calculateTime(String key) {
        long startTime = startTimeMap.get(key);
        long endTime = endTimeMap.get(key);
        System.out.println(key + "======time(纳秒):" + (endTime - startTime)
                + " ≈ " +(endTime - startTime)/1000 + "微秒"
                + " ≈ " +(endTime - startTime)/1000000 + "毫秒");
    }
}
