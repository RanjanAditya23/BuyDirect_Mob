package org.BuyDirect_Android_utils;

import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Properties;

public class DriverSetUp {
    private static final Logger logger = LogManager.getLogger(DriverSetUp.class);

    public static UiAutomator2Options getChromeOptions(Properties properties, ChromeOptions chromeOptions) {
        logger.debug("Setting up ChromeOptions");
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(properties.getProperty("appium.platform.name"))
                .setPlatformVersion(properties.getProperty("appium.platform.version"))
                .setAutomationName("UiAutomator2")
                .setDeviceName(properties.getProperty("appium.device.name"))
                .noReset()
                .withBrowserName("Chrome")
                .setCapability("chromedriver_autodownload", true);

        options.merge(chromeOptions);
        logger.debug("ChromeOptions set: {}", options);
        return options;
    }

    public static UiAutomator2Options getFirefoxOptions(Properties properties, FirefoxOptions firefoxOptions) {
        logger.debug("Setting up FirefoxOptions");
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(properties.getProperty("appium.platform.name"))
                .setPlatformVersion(properties.getProperty("appium.platform.version"))
                .setAutomationName("UiAutomator2")
                .setDeviceName(properties.getProperty("appium.device.name"))
                .noReset()
                .withBrowserName("Firefox")
                .setCapability("geckodriver_autodownload", true);

        options.merge(firefoxOptions);
        logger.debug("FirefoxOptions set: {}", options);
        return options;
    }

    public static UiAutomator2Options getEdgeOptions(Properties properties, EdgeOptions edgeOptions) {
        logger.debug("Setting up EdgeOptions");
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(properties.getProperty("appium.platform.name"))
                .setPlatformVersion(properties.getProperty("appium.platform.version"))
                .setAutomationName("UiAutomator2")
                .setDeviceName(properties.getProperty("appium.device.name"))
                .noReset()
                .withBrowserName("Edge")
                .setCapability("edgedriver_autodownload", true);

        options.merge(edgeOptions);
        logger.debug("EdgeOptions set: {}", options);
        return options;
    }
}
