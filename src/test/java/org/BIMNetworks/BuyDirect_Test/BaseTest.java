package org.BIMNetworks.BuyDirect_Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;

import org.BuyDirect_Android_utils.ChromeDriverSetUp;
import org.BuyDirect_Android_utils.DriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@Listeners(org.BuyDirect_Android_utils.ExtentTestNGListener.class)
public class BaseTest {
	public static AndroidDriver driver;
	public AppiumDriverLocalService service;
	
	@BeforeClass
	public void configureAppium() throws MalformedURLException, URISyntaxException {

		service = new AppiumServiceBuilder()
				.withAppiumJS(new File("C:\\Users\\ranjan\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
				.withIPAddress("127.0.0.1").usingPort(4723).build();

		service.start();

		// Chrome options setup
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("w3c", false); // To enable old JSON wire protocol

		// Specify the URL of your Appium server
		URL url = new URI("http://127.0.0.1:4723").toURL();

		// Create an instance of AndroidDriver with the Chrome options
		driver = new AndroidDriver(url, ChromeDriverSetUp.getChromeOptions());
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	
		//Launch URL
		driver.get("https://bimhep-qa.bimnetworkstech.com/?partnerId=MTI3");

		// Set the driver in the DriverManager
        DriverManager.setDriver(driver);
        
    }

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
		if (service != null && service.isRunning()) {
			service.stop();
		}
	}
	
}





