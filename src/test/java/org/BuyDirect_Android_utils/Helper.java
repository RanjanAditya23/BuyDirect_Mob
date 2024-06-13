package org.BuyDirect_Android_utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class Helper {

    private AndroidDriver driver;
	@SuppressWarnings("unused")
	private WebDriverWait wait;

    public Helper(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Create list logic
    public static <T> List<T> createList(@SuppressWarnings("unchecked") T... elements) {
        List<T> list = new ArrayList<T>(elements.length);
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }

   // Scroll logic
    public void scroll() {
        boolean canScrollMore;

        try {
            if (driver != null && driver.getSessionId() != null) {
                do {
                    canScrollMore = (Boolean) ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", ImmutableMap.of(
                        "left", 100, "top", 100, "width", 200, "height", 200,
                        "direction", "down",
                        "percent", 1.0
                    ));
                } while (canScrollMore);
            } else {
                throw new NoSuchSessionException("Driver session is not active.");
            }
        } catch (NoSuchSessionException e) {
            // Handle session exception
            System.err.println("No such session exception: " + e.getMessage());
            // Additional logging or retry logic can be added here if necessary
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }

	// Method to generate a random email address
	public static String generateRandomEmail(String originalEmail) {
		String[] parts = originalEmail.split("@");
		String randomString = UUID.randomUUID().toString().substring(0, 5);
		return parts[0] + randomString + "@" + parts[1];
	}

	
	
}
