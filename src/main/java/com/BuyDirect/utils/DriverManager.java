package com.BuyDirect.utils;

import io.appium.java_client.android.AndroidDriver;

public class DriverManager {
    private static AndroidDriver driver;

    public static AndroidDriver getDriver() {
        return driver;
    }

    public static void setDriver(AndroidDriver driver) {
        DriverManager.driver = driver;
    }
}
