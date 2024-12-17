package com.BuyDirect.tests;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import com.BuyDirect.utils.DriverManager;
import com.BuyDirect.utils.DriverSetUp;
import com.aventstack.extentreports.ExtentTest;
import com.BuyDirect.listeners.ExtentReportManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.Properties;

@Listeners(com.BuyDirect.listeners.ExtentTestNGListener.class)
public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    public static AndroidDriver driver;
    public AppiumDriverLocalService service;
    private Properties properties;
    private ExtentTest test;

    @BeforeClass
    public void configureAppium() throws IOException, URISyntaxException {
        ExtentReportManager.init(); // Ensure Extent Reports is initialized
        ExtentReportManager.createTest("Configure Appium"); // Start a new test entry
        test = ExtentReportManager.getTest(); // Get the current test instance

        logger.info("Configuring Appium...");
        test.log(com.aventstack.extentreports.Status.INFO, "Starting Appium configuration...");

        // Load properties from the config file
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("config.properties not found in classpath");
            }
            properties.load(inputStream);
            test.log(com.aventstack.extentreports.Status.INFO, "Loaded configuration properties successfully.");
        } catch (IOException e) {
            test.log(com.aventstack.extentreports.Status.FAIL, "Failed to load config.properties");
            logger.error("Failed to load config.properties", e);
            throw e;
        }

        // Set system info in Extent Reports
        setTestEnvironmentInfo();

        // Start Appium service
        String appiumJSPath = properties.getProperty("appium.js.path", "C:\\Users\\ranjan\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js");
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumJSPath))
                .withIPAddress(properties.getProperty("appium.server.ip", "127.0.0.1"))
                .usingPort(Integer.parseInt(properties.getProperty("appium.server.port", "4723")))
                .build();
        service.start();
        logger.info("Appium service started successfully.");
        test.log(com.aventstack.extentreports.Status.INFO, "Appium service started successfully.");

        // Configure browser options
        UiAutomator2Options options = configureBrowserOptions();
        options.setNewCommandTimeout(Duration.ofMinutes(10));

        // Specify the URL of your Appium server
        URL url = new URI(properties.getProperty("appium.server.url")).toURL();

        // Encode partner ID to Base64
        String partnerId = properties.getProperty("partner.id");
        String encodedPartnerId = Base64.getEncoder().encodeToString(partnerId.getBytes());

        // Construct the URL with encoded partner ID
        String partnerUrl = "https://uqa-va-buydirect.azurewebsites.net/?partnerId=" + encodedPartnerId;

        // Create an instance of AndroidDriver with the options
        driver = new AndroidDriver(url, options);
        logger.info("Session ID after initialization: " + driver.getSessionId());
        test.log(com.aventstack.extentreports.Status.INFO, "AndroidDriver initialized with session ID: " + driver.getSessionId());

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // Launch URL
        driver.get(partnerUrl);
        logger.info("Session ID after launching URL: " + driver.getSessionId());
        test.log(com.aventstack.extentreports.Status.INFO, "Launched URL with session ID: " + driver.getSessionId());

        // Set the driver in the DriverManager
        DriverManager.setDriver(driver);
    }

    private void setTestEnvironmentInfo() {
        // Set system and environment info in Extent Reports
        ExtentReportManager.setSystemInfo("Operating System", System.getProperty("os.name"));
        ExtentReportManager.setSystemInfo("Browser", properties.getProperty("appium.browser"));
        ExtentReportManager.setSystemInfo("Device Name", properties.getProperty("appium.device.name"));
        ExtentReportManager.setSystemInfo("Platform Name", properties.getProperty("appium.platform.name"));
        ExtentReportManager.setSystemInfo("Platform Version", properties.getProperty("appium.platform.version"));
        ExtentReportManager.setSystemInfo("Appium Server URL", properties.getProperty("appium.server.url"));
    }

    private UiAutomator2Options configureBrowserOptions() {
        // Browser options setup
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", false);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("moz:firefoxOptions", true);

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setCapability("ms:edgeOptions", true);

        // Get the browser from properties
        String browser = properties.getProperty("appium.browser", "chrome").toLowerCase();

        UiAutomator2Options options;
        switch (browser) {
            case "firefox":
                options = DriverSetUp.getFirefoxOptions(properties, firefoxOptions);
                test.log(com.aventstack.extentreports.Status.INFO, "Configured Firefox options.");
                break;
            case "edge":
                options = DriverSetUp.getEdgeOptions(properties, edgeOptions);
                test.log(com.aventstack.extentreports.Status.INFO, "Configured Edge options.");
                break;
            case "chrome":
            default:
                options = DriverSetUp.getChromeOptions(properties, chromeOptions);
                test.log(com.aventstack.extentreports.Status.INFO, "Configured Chrome options.");
                break;
        }

        return options;
    }

    @AfterClass
    public void tearDown() {
        ExtentReportManager.createTest("Tear Down"); // Start a new test entry for teardown
        test = ExtentReportManager.getTest(); // Get the current test instance

        logger.info("Tearing down the test...");
        if (driver != null) {
            logger.info("Session ID before quitting driver: " + driver.getSessionId());
            test.log(com.aventstack.extentreports.Status.INFO, "Quitting AndroidDriver with session ID: " + driver.getSessionId());
            driver.quit();
            driver = null;
            test.log(com.aventstack.extentreports.Status.PASS, "AndroidDriver quit successfully.");
        }
        if (service != null && service.isRunning()) {
            service.stop();
            service = null;
            test.log(com.aventstack.extentreports.Status.INFO, "Appium service stopped successfully.");
        }
    }

    @AfterSuite
    public void afterSuite() {
        ExtentReportManager.flush();
    }

    public void captureScreenshot(String screenshotName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "screenshots/" + screenshotName + ".png";
            FileUtils.copyFile(screenshot, new File(path));

            // Log screenshot using the path
            ExtentReportManager.logScreenshot(path);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }
    
    
}
