package com.unipi.webfluxdemo.service;

public class SleepUtil {

    // Artificially made latency for demo purpose
    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
