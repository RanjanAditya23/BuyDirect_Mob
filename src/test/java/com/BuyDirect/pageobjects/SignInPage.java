package com.BuyDirect.pageobjects;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class SignInPage {

	private AndroidDriver driver;
	private WebDriverWait wait;

	public SignInPage(AndroidDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	}

	// Footer Text on Address Page
	@FindBy(xpath = "(//p[@class='mx-3 mt-1 mb-0 zulu-custom-footer'])[3]")
	private WebElement footertext;

	public String footerText() {
		wait.until(ExpectedConditions.visibilityOf(footertext));
		return footertext.getText();
	}

	public boolean footerTextisDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(footertext));
		return footertext.isDisplayed();
	}
	
	

}
