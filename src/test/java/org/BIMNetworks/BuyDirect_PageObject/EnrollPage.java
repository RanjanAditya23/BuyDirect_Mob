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
    
    // Method to return the checkbox element
    public WebElement getTermsAndConditionsCheckbox() {
        return checkbox;
    }
    
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
	
	// TermsAndConditions before text
	@FindBy(xpath = "//label[text()='I certify that I am at least 18 years old and that I agree to the ']")
	private WebElement TermsAndConditionsbeforetext;

	public String termsAndConditionsbeforetext() {
		wait.until(ExpectedConditions.visibilityOf(TermsAndConditionsbeforetext));
		return TermsAndConditionsbeforetext.getText();
	}
	
	public boolean termsAndConditionsbeforetexisDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(TermsAndConditionsbeforetext));
		return TermsAndConditionsbeforetext.isDisplayed();
	}

	// TermsAndConditions Link text
	@FindBy(xpath = "//a[text()='Term and Conditions']")
	private WebElement gettermsAndConditionslinkText;
	
    // Method to return the Terms And Conditions LinkText element
    public WebElement termsAndConditionsLinkText() {
        return gettermsAndConditionslinkText;
    }

	public String getTermsAndConditionslinkText() {
		return gettermsAndConditionslinkText.getText();
	}
	
	public String getTermsAndConditionsLinkHref() {
		return gettermsAndConditionslinkText.getAttribute("href");
	}

	public void clickTermsAndConditionsLink() {
	    gettermsAndConditionslinkText.click();
	}
	
	public boolean termsAndConditionsLinkTextisDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(gettermsAndConditionslinkText));
		return gettermsAndConditionslinkText.isDisplayed();
	}
	
	
	// Text Notifications
    @FindBy(id = "notifications")
    private WebElement notificationscheckbox;
    
    // Method to return the checkbox element
    public WebElement getTextNotificationsCheckbox() {
        return notificationscheckbox;
    }
    
    public void checkTextNotifications() {
        if (!notificationscheckbox.isSelected()) {
        	notificationscheckbox.click();
        }
    }

    public void uncheckTextNotifications() {
        if (notificationscheckbox.isSelected()) {
        	notificationscheckbox.click();
        }
    }

    @FindBy(xpath = "//label[@class='form-check-label label--text-notification']")
    private WebElement textNotificationsTextLabel;

    @FindBy(xpath = "//a[text()='Text Notifications']")
    private WebElement textNotificationsLink;

    public String getTextNotificationsTextLabel() {
        wait.until(ExpectedConditions.visibilityOf(textNotificationsTextLabel));
        return textNotificationsTextLabel.getText();
    }

    public boolean isTextNotificationsTextLabelDisplayed() {
        return textNotificationsTextLabel.isDisplayed();
    }

    public String getTextNotificationsLinkText() {
        return textNotificationsLink.getText();
    }

    public String getTextNotificationsLinkHref() {
        return textNotificationsLink.getAttribute("href");
    }

    public void clickTextNotificationsLink() {
        textNotificationsLink.click();
    }

    public boolean isTextNotificationsLinkDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(textNotificationsLink));
        return textNotificationsLink.isDisplayed();
    }
    
    // Continue
    @FindBy(id = "btnEnroll")
    private WebElement continueButton;
    
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }
    
	// SignInHere Before Text
	@FindBy(xpath = "(//p[@class='text-muted mb-0 SingInHereText'])[1]")
	private WebElement signinbeforetext;

	public String signInHereBeforeText() {
		wait.until(ExpectedConditions.visibilityOf(signinbeforetext));
		return signinbeforetext.getText();
	}

	// SignInHere Link
	@FindBy(xpath = "(//p[@class='text-muted mb-0 SingInHereText'])[1]")
	private WebElement signinherelink;

	public void signInHereLink() {
		signinherelink.getAttribute("href");
	}

	public boolean signInHereTextisDisplayed() {
		wait.until(ExpectedConditions.visibilityOf(signinherelink));
		return signinherelink.isDisplayed();
	}
    
}
