package com.BuyDirect.pageobjects;

import java.time.Duration;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class BankingIntroPage {

	@SuppressWarnings("unused")
	private AndroidDriver driver;
	
	
    public BankingIntroPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        new WebDriverWait(driver, Duration.ofSeconds(30));

    }  

	}
	
	

	
	


