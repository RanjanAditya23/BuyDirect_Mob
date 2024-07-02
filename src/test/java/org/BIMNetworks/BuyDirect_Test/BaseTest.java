package org.BIMNetworks.BuyDirect_Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.BuyDirect_Android_utils.DriverManager;
import org.BuyDirect_Android_utils.DriverSetUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.Properties;

@Listeners(org.BuyDirect_Android_utils.ExtentTestNGListener.class)
public class BaseTest {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    public static AndroidDriver driver;
    public AppiumDriverLocalService service;
    private Properties properties;

    @BeforeClass
    public void configureAppium() throws IOException, URISyntaxException {
        logger.info("Configuring Appium...");

        // Load properties from the config file
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("config.properties not found in classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Failed to load config.properties", e);
            throw e;
        }

        // Start Appium service
        String appiumJSPath = properties.getProperty("appium.js.path", "C:\\Users\\ranjan\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js");
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File(appiumJSPath))
                .withIPAddress(properties.getProperty("appium.server.ip", "127.0.0.1"))
                .usingPort(Integer.parseInt(properties.getProperty("appium.server.port", "4723")))
                .build();
        service.start();
        logger.info("Appium service started successfully.");

        // Browser options setup
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", false); // To enable old JSON wire protocol

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
                break;
            case "edge":
                options = DriverSetUp.getEdgeOptions(properties, edgeOptions);
                break;
            case "chrome":
            default:
                options = DriverSetUp.getChromeOptions(properties, chromeOptions);
                break;
        }
        
        // Add newCommandTimeout capability
        options.setNewCommandTimeout(Duration.ofMinutes(10)); 

        // Specify the URL of your Appium server
        URL url = new URI(properties.getProperty("appium.server.url")).toURL();

        // Encode partner ID to Base64
        String partnerId = properties.getProperty("partner.id");
        String encodedPartnerId = Base64.getEncoder().encodeToString(partnerId.getBytes());

        // Construct the URL with encoded partner ID
        String partnerUrl = "https://bimhep-qa.bimnetworkstech.com/?partnerId=" + encodedPartnerId;

        // Create an instance of AndroidDriver with the options
        driver = new AndroidDriver(url, options);
        logger.info("Session ID after initialization: " + driver.getSessionId());

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // Launch URL
        driver.get(partnerUrl);
        logger.info("Session ID after launching URL: " + driver.getSessionId());

        // Set the driver in the DriverManager
        DriverManager.setDriver(driver);
    }

    @AfterClass
    public void tearDown() {
        logger.info("Tearing down the test...");

        if (driver != null) {
            logger.info("Session ID before quitting driver: " + driver.getSessionId());
            driver.quit();
            driver = null; // Ensure the driver is fully cleaned up
        }
        if (service != null && service.isRunning()) {
            service.stop();
            service = null; // Ensure the service is fully cleaned up
        }
    }
}
