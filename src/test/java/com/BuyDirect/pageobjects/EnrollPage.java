package com.BuyDirect.pageobjects;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class EnrollPage {

    @SuppressWarnings("unused")
	private AndroidDriver driver;
    private WebDriverWait wait;
    
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
    
    // First Name Label
    @FindBy(xpath="//label[text()='First Name']")
    private WebElement firstNameLabel;
    
    public boolean isFirstNameLabelDisplayed() {
    	wait.until(ExpectedConditions.visibilityOf(firstNameLabel));
        return firstNameLabel.isDisplayed();
    }
    
    public String getFirstNameLabelText() {
        wait.until(ExpectedConditions.visibilityOf(firstNameLabel));
        return firstNameLabel.getText();
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
    
	// validate First Name Placeholder
	public String firstNameValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(firstNameInput));
		return firstNameInput.getAttribute("placeholder");
	}

    // Last Name
    @FindBy(id = "lastname")
    private WebElement lastNameInput;
    
    public void enterLastName(String lastname) {
        wait.until(ExpectedConditions.visibilityOf(lastNameInput));
        lastNameInput.clear();
        lastNameInput.sendKeys(lastname);
    }
    
    // Last Name Label
    @FindBy(xpath="//label[text()='Last Name']")
    private WebElement lastNameLabel;
    
    public boolean isLastNameLabelDisplayed() {
    	wait.until(ExpectedConditions.visibilityOf(lastNameLabel));
        return lastNameLabel.isDisplayed();
    }
    
    public String getLastNameLabelText() {
        wait.until(ExpectedConditions.visibilityOf(lastNameLabel));
        return lastNameLabel.getText();
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
    
    // validate Last Name Placeholder
	public String lastNameValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(lastNameInput));
		return lastNameInput.getAttribute("placeholder");
	}
    
    // Email Address
    @FindBy(id = "email")
    private WebElement emailAddressInput;
    
    public void enterEmailAddress(String email) {
        wait.until(ExpectedConditions.visibilityOf(emailAddressInput));
        emailAddressInput.clear();
        emailAddressInput.sendKeys(email);
    }
    
    // Email Address Label
    @FindBy(xpath="//label[text()='Email Address']")
    private WebElement emailAddressLabel;
    
    public boolean isEmailAddressLabelDisplayed() {
    	wait.until(ExpectedConditions.visibilityOf(emailAddressLabel));
        return emailAddressLabel.isDisplayed();
    }
    
    public String getEmailAddressLabelText() {
        wait.until(ExpectedConditions.visibilityOf(emailAddressLabel));
        return emailAddressLabel.getText();
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
    
    // validate Email Placeholder
	public String emailValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(emailAddressInput));
		return emailAddressInput.getAttribute("placeholder");
	}
    
    // Mobile Phone
    @FindBy(id = "phone")
    private WebElement mobilePhoneInput;
    
    public void enterMobilePhone(String phone) {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneInput));
        mobilePhoneInput.clear();
        mobilePhoneInput.sendKeys(phone);
    }
    
    // Mobile Phone Label
    @FindBy(xpath="//label[text()='Mobile Phone']")
    private WebElement mobilePhoneLabel;
    
    public boolean isMobilePhoneLabelDisplayed() {
    	wait.until(ExpectedConditions.visibilityOf(mobilePhoneLabel));
        return mobilePhoneLabel.isDisplayed();
    }
    
    public String getMobilePhoneLabelText() {
        wait.until(ExpectedConditions.visibilityOf(mobilePhoneLabel));
        return mobilePhoneLabel.getText();
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
    
    // validate Mobile Phone Placeholder
	public String mobilePhoneValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(mobilePhoneInput));
		return mobilePhoneInput.getAttribute("placeholder");
	}
    
    // Create PIN
    @FindBy(id = "Createpin")
    private WebElement createPINInput;
    
    public void enterCreatePIN(String createpin) {
        wait.until(ExpectedConditions.visibilityOf(createPINInput));
        createPINInput.clear();
        createPINInput.sendKeys(createpin);
    }   
    
    // Create PIN Label
    @FindBy(xpath="//label[text()='Create PIN ']")
    private WebElement createPINLabel;
    
    public boolean isCreatePINLabelDisplayed() {
    	wait.until(ExpectedConditions.visibilityOf(createPINLabel));
        return createPINLabel.isDisplayed();
    }
    
    public String getCreatePINLabelText() {
        wait.until(ExpectedConditions.visibilityOf(createPINLabel));
        return createPINLabel.getText();
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
    
    // validate Pin Placeholder
	public String pinValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(createPINInput));
		return createPINInput.getAttribute("placeholder");
	}	
	
	// Create Pin Tooltip Icon
	@FindBy(xpath="//i[@aria-label='PIN is 4 digits long, non-consecutive and 3 unique digits']")
	private WebElement createPinToolTip;

	// Tooltip Text (update locator as per your HTML structure)
	@FindBy(xpath="//div[@class='tooltip-inner'][text()='PIN is 4 digits long, non-consecutive and 3 unique digits'")
	private WebElement createPinToolTipText;

	// Some element on the page to click on to hide the tooltip (e.g., header or a blank area)
	@FindBy(xpath="//div[@class='card-body zulu-card']")
	private WebElement blankAreaElement;

	// Method to click on the tooltip icon
	public void clickCreatePinToolTip() {
	    wait.until(ExpectedConditions.elementToBeClickable(createPinToolTip));
	    createPinToolTip.click();
	}
	
    // Method to return the Create Pin Tooltip Icon element
    public WebElement getCreatePinToolTip() {
        return createPinToolTip;
    }

	// Method to check if the tooltip text is displayed
	public boolean isCreatePinToolTipDisplayed() {
	    wait.until(ExpectedConditions.visibilityOf(createPinToolTipText));
	    return createPinToolTipText.isDisplayed();
	}

	// Method to get the tooltip text
	public String getCreatePinToolTipText() {
	    wait.until(ExpectedConditions.visibilityOf(createPinToolTipText));
	    return createPinToolTipText.getText();
	}

	// Method to click somewhere else on the page to hide the tooltip
	public void clickOutsideTooltip() {
	    wait.until(ExpectedConditions.elementToBeClickable(blankAreaElement));
	    blankAreaElement.click();
	}

	// Method to check if the tooltip text has disappeared
	public boolean isCreatePinToolTipHidden() throws TimeoutException {
	    wait.until(ExpectedConditions.invisibilityOf(createPinToolTipText));
		return true;
	}
	
	// Toggle button for PIN visibility
	@FindBy(id = "password-addon")
	private WebElement togglePinVisibilityButton;

	public void togglePinVisibility() {
	    wait.until(ExpectedConditions.elementToBeClickable(togglePinVisibilityButton));
	    togglePinVisibilityButton.click();
	}

	public boolean isPinFieldVisible() {
	    // Check if the PIN field is visible (type="text")
	    String pinFieldType = createPINInput.getAttribute("type");
	    return pinFieldType.equals("text");
	}

	public boolean isPinFieldHidden() {
	    // Check if the PIN field is hidden (type="password")
	    String pinFieldType = createPINInput.getAttribute("type");
	    return pinFieldType.equals("password");
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
    
 // Continue btn on Enroll
    @FindBy(id = "btnEnroll")
    private WebElement continueButton;
    
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }
    
	public String ContinueButton() {
		wait.until(ExpectedConditions.visibilityOf(continueButton));
		return continueButton.getText();
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
	
	// Footer Text on Enroll Page
	@FindBy(xpath = "(//p[@class='mx-3 mt-1 mb-0 zulu-custom-footer'])[1]")
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
