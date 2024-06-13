package org.BIMNetworks.BuyDirect_PageObject;

import java.time.Duration;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class EnrollPage {

    AndroidDriver driver;
    WebDriverWait wait;

    public EnrollPage(AndroidDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }   
    
    // First Name
    @FindBy(id = "firstname")
    private WebElement firstNameInput;
    
    public void enterFirstName(String firstname) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(firstname);
    }
    
    // First Name required error message
    @FindBy(xpath = "(//div[text()='Required'])[1]")
    private WebElement firstNameRequiredErrorMessage;
    
    public boolean isFirstNameRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(firstNameRequiredErrorMessage));
        return firstNameRequiredErrorMessage.isDisplayed();
    }
    
    // First Name validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[1]")
    private WebElement firstNameValidationErrorMessage;
    
    public boolean isFirstNameErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(firstNameValidationErrorMessage));
        return firstNameValidationErrorMessage.isDisplayed();
    }

    // Last Name
    @FindBy(id = "lastname")
    private WebElement lastNameInput;
    
    public void enterLastName(String lastname) {
        wait.until(ExpectedConditions.visibilityOf(lastNameInput));
        lastNameInput.clear();
        lastNameInput.sendKeys(lastname);
    }
    
    // Last Name required error message
    @FindBy(xpath = "(//div[text()='Required'])[2]")
    private WebElement lastNameRequiredErrorMessage;
    
    public boolean isLastNameRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(lastNameRequiredErrorMessage));
        return lastNameRequiredErrorMessage.isDisplayed();
    }
    
    // Last Name validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[2]")
    private WebElement lastNameValidationErrorMessage;
    
    public boolean isLastNameErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(lastNameValidationErrorMessage));
        return lastNameValidationErrorMessage.isDisplayed();
    }
    
    // Email Address
    @FindBy(id = "email")
    private WebElement emailAddressInput;
    
    public void enterEmailAddress(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailAddressInput));
        emailAddressInput.clear();
        emailAddressInput.sendKeys(email);
    }
    
    // Email required error message
    @FindBy(xpath = "(//div[text()='Required'])[3]")
    private WebElement emailRequiredErrorMessage;
    
    public boolean isEmailRequiredErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(emailRequiredErrorMessage));
        return emailRequiredErrorMessage.isDisplayed();
    }
    
    // Email validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[3]")
    private WebElement emailValidationErrorMessage;
    
    public boolean isEmailErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(emailValidationErrorMessage));
        return emailValidationErrorMessage.isDisplayed();
    }
    
    // Email validation toast message
    @FindBy(xpath = "//div[text()='The Email Address field is not a valid e-mail address.']")
    private WebElement emailValidationToastMessage;
    
    public boolean isEmailValidationToastMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(emailValidationToastMessage));
        return emailValidationToastMessage.isDisplayed();
    }
    
    // Mobile Phone
    @FindBy(id = "phone")
    private WebElement mobilePhoneInput;
    
    public void enterMobilePhone(String phone) {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneInput));
        mobilePhoneInput.clear();
        mobilePhoneInput.sendKeys(phone);
    }
    
    // Mobile Phone required error message
    @FindBy(xpath = "(//div[text()='Required'])[4]")
    private WebElement mobilePhoneRequiredErrorMessage;
    
    public boolean isMobilePhoneRequiredErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneRequiredErrorMessage));
        return mobilePhoneRequiredErrorMessage.isDisplayed();
    }
    
    // Mobile Phone validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[4]")
    private WebElement mobilePhoneValidationErrorMessage;
    
    public boolean isMobilePhoneErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneValidationErrorMessage));
        return mobilePhoneValidationErrorMessage.isDisplayed();
    }
    
    // Mobile Phone validation toast message
    @FindBy(xpath = "//div[text()='Phone number must be valid']")
    private WebElement mobilePhoneValidationToastMessage;
    
    public boolean isMobilePhoneValidationToastMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneValidationToastMessage));
        return mobilePhoneValidationToastMessage.isDisplayed();
    }
    
    // Create PIN
    @FindBy(id = "Createpin")
    private WebElement createPINInput;
    
    public void enterCreatePIN(String createpin) {
        wait.until(ExpectedConditions.visibilityOf(createPINInput));
        createPINInput.clear();
        createPINInput.sendKeys(createpin);
    }   
    
    // Pin required error message
    @FindBy(xpath = "(//div[text()='Required'])[5]")
    private WebElement pinRequiredErrorMessage;
    
    public boolean isPinRequiredErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(pinRequiredErrorMessage));
        return pinRequiredErrorMessage.isDisplayed();
    }
    
    // Pin validation error message
    @FindBy(xpath = "//div[text()='The PIN must be 4 digits long']")
    private WebElement pinValidationErrorMessage;
    
    public boolean isPinErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(pinValidationErrorMessage));
        return pinValidationErrorMessage.isDisplayed();
    }
    
    // Pin validation toast message
    @FindBy(xpath = "//div[text()='PIN is 4 digits long, non-consecutive and 3 unique digits']")
    private WebElement pinValidationToastMessage;
    
    public boolean isPinValidationToastMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(pinValidationToastMessage));
        return pinValidationToastMessage.isDisplayed();
    }
    
    
    // Term and Conditions.
    @FindBy(id = "term")
    private WebElement checkbox;
    
    public void checkTermsAndConditions() {
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void uncheckTermsAndConditions() {
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }
    
    // TermsAndConditions required/error message
    @FindBy(xpath = "//div[text()='Please select Terms of Services']")
    private WebElement termsAndConditionsRequiredErrorMessage;
    
	public boolean isTermsAndConditionsRequiredErrorMessageDisplayed() throws InterruptedException {
		return termsAndConditionsRequiredErrorMessage.isDisplayed();
	}
    
    // Continue
    @FindBy(id = "btnEnroll")
    private WebElement continueButton;
    
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }
   
    

    
}
