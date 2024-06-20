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
import java.net.MalformedURLException;
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
    public void configureAppium() throws MalformedURLException, IOException, URISyntaxException {
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
            throw e; // Handle or rethrow as appropriate
        }

        // Start Appium service
        service = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Users\\ranjan\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
                .withIPAddress("127.0.0.1").usingPort(4723).build();

        service.start();
        logger.info("Appium service started successfully.");

        // Chrome options setup
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("w3c", false); // To enable old JSON wire protocol

        // Firefox options setup
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("moz:firefoxOptions", true);
        // Add any additional Firefox options as needed

        // Edge options setup
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setCapability("ms:edgeOptions", true);
        // Add any additional Edge options as needed

        // Get the browser from properties
        String browser = properties.getProperty("appium.browser", "chrome"); // Default to "chrome" if not specified

        UiAutomator2Options options;
        switch (browser.toLowerCase()) {
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
