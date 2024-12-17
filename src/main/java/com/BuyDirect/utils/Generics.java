package com.BuyDirect.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class Generics {

	private AndroidDriver driver;
	public Generics(AndroidDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		new WebDriverWait(driver, Duration.ofSeconds(30));
		
	}

	// Partner Logo
	@FindBy(id = "buydirectMobileLogo")
	private WebElement partnerlogo;

	public String partnerlogo() {
		return partnerlogo.getAttribute("src");
	}

	public boolean partnerLogoisDisplayed() {
		return partnerlogo.isDisplayed();
	}

	// Tender Name
	@FindBy(xpath = "//h5[contains(text(), 'BIM Grocery Pay')]")
	private WebElement partnertendername;

	public String partnerTenderNameHeader() {
		return partnertendername.getText();
	}

	public boolean partnerTenderNameisDisplayed() {
		return partnertendername.isDisplayed();
	}

	// Progress Indicators
	@FindBy(xpath = "//li[@class='nav-item']")
	private List<WebElement> progressIndicatorElements;
	
	// Method to verify all progress indicator elements are displayed
	public boolean allProgressIndicatorIsDisplayed() {
		boolean allDisplayed = true;

		// Iterate through each element in the list
		for (WebElement element : progressIndicatorElements) {
			// Check if the element is displayed
			if (!element.isDisplayed()) {
				// If any element is not displayed, set the flag to false
				allDisplayed = false;
				break;
			}
		}
		return allDisplayed;
	}

	// All Progress Indicators Text
	public List<String> getAllIndicatorsText() {
		List<String> allText = new ArrayList<>();

		// Find all elements matching the XPath //div[@class='step-icon']
		List<WebElement> stepIconElements = driver.findElements(By.xpath("//li[@class='nav-item']"));

		// Iterate over each 'step-icon' element
		for (WebElement stepIcon : stepIconElements) {
			// Retrieve the text contained within the 'step-icon' element
			String text = stepIcon.getText();

			// Add the text to the list
			allText.add(text);
		}

		// Return the list containing all the text
		return allText;
	}

	public boolean allIndicatorsTextIsDisplayed() {
		// Get all progress indicator texts
		List<String> allText = getAllIndicatorsText();

		// Iterate over each text
		for (String text : allText) {
			// Find the element containing the text
			List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));

			// Check if the element is displayed
			boolean isDisplayed = false;
			for (WebElement element : elements) {
				if (element.isDisplayed()) {
					isDisplayed = true;
					break;
				}
			}

			// If any text is not displayed, return false
			if (!isDisplayed) {
				return false;
			}
		}

		// If all text is displayed, return true
		return true;
	}
	
	
}
