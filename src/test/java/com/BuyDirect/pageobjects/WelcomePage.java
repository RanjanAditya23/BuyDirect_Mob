package com.BuyDirect.pageobjects;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class WelcomePage  {

	@SuppressWarnings("unused")
	private AndroidDriver driver;
	private WebDriverWait wait;


	public WelcomePage(AndroidDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	}
	
	// Welcome Page Title
	@FindBy(xpath = "//div[@class='text-center mb-5']//h5")
	private WebElement title;

	public String pagetitle() throws InterruptedException {
	    wait.until(ExpectedConditions.visibilityOf(title));
		return title.getText();
	}

	
	// Page Title Text is Displayed in Middle
	@FindBy(id = "buydirectMobileLogo")
	private WebElement Element1;

	public WebElement logoElement() {
		wait.until(ExpectedConditions.visibilityOf(Element1));
		Element1.getText();
		return Element1;
	}

	@FindBy(xpath = "//div[@class='text-center mb-5']//h5")
	private WebElement Element2;

	public WebElement titleElement() {
		wait.until(ExpectedConditions.visibilityOf(Element2));
		Element2.getText();
		return Element2;
	}

	// Welcome Message1
	@FindBy(xpath = "//p[@id='dynamic-welcome-message']/child::span[1]")
	private WebElement welcomemessage1;

	public String dynamicWelcomeMessage1() {
		wait.until(ExpectedConditions.visibilityOf(welcomemessage1));
		return welcomemessage1.getText();
	}


	// Welcome Message2
	@FindBy(xpath = "//p[@id='dynamic-welcome-message']/child::span[2]")
	private WebElement welcomemessage2;

	public String dynamicWelcomeMessage2() {
		wait.until(ExpectedConditions.visibilityOf(welcomemessage2));
		return welcomemessage2.getText();
	}
	
	// Continue Button
	@FindBy(id = "btnContinueEnroll")
	private WebElement continueButton;

	public String ContinueButton() {
		wait.until(ExpectedConditions.visibilityOf(continueButton));
		return continueButton.getText();
	}
	
	// SignInHere Before Text
	@FindBy(xpath = "//div[@id='openingWelcome']/div[2]/div[2]//parent::p")
	private WebElement signinbeforetext;

	public String signInHereBeforeText() {
		wait.until(ExpectedConditions.visibilityOf(signinbeforetext));
		return signinbeforetext.getText();
	}

	// SignInHere Link
	@FindBy(xpath = "//div[@id='openingWelcome']/div[2]/div[2]/div/p/a")
	private WebElement signinherelink;

	public void signInHereLink() {
		signinherelink.getAttribute("href");
	}

	public boolean signInHereTextisDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(signinherelink));
		return signinherelink.isDisplayed();
	}

	// Enroll Page WebElement 
	@FindBy(xpath = "//span[@id='zuluEnroll']")
	private WebElement enrollProgressIndicator;

	public WebElement EnrollIndicator() {
		wait.until(ExpectedConditions.visibilityOf(enrollProgressIndicator));
		enrollProgressIndicator.getText();
		return enrollProgressIndicator;
	}

	public void ClickOnContinue() throws InterruptedException {
		continueButton.click();
		Thread.sleep(2000);
	}
	
	// Footer Text on Welcome Page
	@FindBy(xpath = "//div[@id='openingWelcome']/div[2]/div[3]/div/p")
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
