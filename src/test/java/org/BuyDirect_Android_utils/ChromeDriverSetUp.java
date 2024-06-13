package org.BuyDirect_Android_utils;

import io.appium.java_client.android.options.UiAutomator2Options;

public class ChromeDriverSetUp {

	public static UiAutomator2Options getChromeOptions() {

		UiAutomator2Options Option = new UiAutomator2Options();
		Option.setPlatformName("Android").setPlatformName("113.0.5672").setAutomationName("UiAutomator2")
				.setDeviceName("emulator-5554").noReset().withBrowserName("Chrome")
				.setCapability("chromedriver_autodownload", true);

		return Option;
	}

}
