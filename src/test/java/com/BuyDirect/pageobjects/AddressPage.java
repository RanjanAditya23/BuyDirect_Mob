package com.BuyDirect.pageobjects;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class AddressPage {

	private AndroidDriver driver;
	private WebDriverWait wait;

	public AddressPage(AndroidDriver driver) {
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	}
	
	// Street Address
    @FindBy(id = "address")
    private WebElement addressInput;
    
    // Street Address Label
    @FindBy(xpath="//label[text()='Street Address']")
    private WebElement streetAddressLabel;

    public boolean isStreetAddressLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(streetAddressLabel));
        return streetAddressLabel.isDisplayed();
    }

    public String getStreetAddressLabelText() {
        return streetAddressLabel.getText();
    }
    
    public void enterStreetAddress(String address) {
        wait.until(ExpectedConditions.visibilityOf(addressInput));
        addressInput.clear();
        addressInput.sendKeys(address);
    }
    
    // Street Address required error message
    @FindBy(id = "address-error")
    private WebElement streetAddressRequiredErrorMessage;
    
    public boolean isStreetAddressRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(streetAddressRequiredErrorMessage));
        return streetAddressRequiredErrorMessage.isDisplayed();
    }
    
    // Street Address validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[1]")
    private WebElement streetAddressValidationErrorMessage;
    
    public boolean isFirstNameErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(streetAddressValidationErrorMessage));
        return streetAddressValidationErrorMessage.isDisplayed();
    }
    
	// validate Street Address Placeholder
	public String streetAddressValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(addressInput));
		return addressInput.getAttribute("placeholder");
	}
	
	// Suite / Apartment
	@FindBy(id = "suite")
    private WebElement suiteInput;
    
    public void enterSuite(String suite) {
        wait.until(ExpectedConditions.visibilityOf(suiteInput));
        suiteInput.clear();
        suiteInput.sendKeys(suite);
    }
    
    // Suite / Apartment Label
    @FindBy(xpath="//label[text()='Suite / Apartment']")
    private WebElement suiteApartmentLabel;

    public boolean isSuiteApartmentLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(suiteApartmentLabel));
        return suiteApartmentLabel.isDisplayed();
    }

    public String getSuiteApartmentLabelText() {
        return suiteApartmentLabel.getText();
    }
       
	// Validate Suit Placeholder
	public String suitValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(suiteInput));
		return suiteInput.getAttribute("placeholder");
	}

    // City  
    @FindBy(id = "city")
    private WebElement cityInput;
    
    public void enterCity(String city) {
        wait.until(ExpectedConditions.visibilityOf(cityInput));
        addressInput.clear();
        addressInput.sendKeys(city);
    }
    
    // City Label
    @FindBy(xpath="//label[text()='City']")
    private WebElement cityLabel;

    public boolean isCityLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(cityLabel));
        return cityLabel.isDisplayed();
    }

    public String getCityLabelText() {
        return cityLabel.getText();
    }
    
    // City required error message
    @FindBy(id = "city-error")
    private WebElement cityRequiredErrorMessage;
    
    public boolean isCityRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(cityRequiredErrorMessage));
        return cityRequiredErrorMessage.isDisplayed();
    }
    
    // City validation error message
    @FindBy(xpath = "(//div[text()='Invalid'])[2]")
    private WebElement cityValidationErrorMessage;
    
    public boolean isCityErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(cityValidationErrorMessage));
        return cityValidationErrorMessage.isDisplayed();
    }
    
	// Validate City Placeholder
	public String cityValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(cityInput));
		return cityInput.getAttribute("placeholder");
	}    
	    
	// State
	@FindBy(id = "state")
	private WebElement stateDropdown;

	// Method to get the state dropdown WebElement
	public WebElement getStateDropdown() {
		return stateDropdown;
	}

	// Method to get all options in the state dropdown
	public List<String> getStateDropdownOptions() {
		Select dropdown = new Select(stateDropdown);
		List<WebElement> options = dropdown.getOptions();
		return options.stream().map(WebElement::getText).collect(Collectors.toList());
	}

	public void selectState(String state) {
		wait.until(ExpectedConditions.visibilityOf(stateDropdown));
		stateDropdown.clear();
		Select select = new Select(stateDropdown);
		select.selectByVisibleText(state);
	}

	public String getSelectedDropdownValue() {
		// Use Select class to interact with the dropdown
		Select stateSelect = new Select(stateDropdown);
		// Get the first selected option
		return stateSelect.getFirstSelectedOption().getText();
	}

	// Method to reset the form
	public void resetForm() {
		Select dropdown = new Select(stateDropdown);	    
	    // Select by visible text 'Select State'
	    dropdown.selectByVisibleText("Select State");
	}
	
	// State Label
    @FindBy(xpath="//label[text()='State']")
    private WebElement stateLabel;

    public boolean isStateLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(stateLabel));
        return stateLabel.isDisplayed();
    }

    public String getStateLabelText() {
        return stateLabel.getText();
    }
    
    // State required error message
    @FindBy(id = "city-error")
    private WebElement stateRequiredErrorMessage;
    
    public boolean isStateRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(stateRequiredErrorMessage));
        return stateRequiredErrorMessage.isDisplayed();
    }
	
	
	// dateofbirth
    @FindBy(xpath = "//i[@class='mdi mdi-calendar zulu-pass-icon']")  
    private WebElement dobIcon;

    public boolean isDobIconDisplayed() throws TimeoutException {
        // Wait for the DOB icon to be visible
		wait.until(ExpectedConditions.visibilityOf(dobIcon));
		return dobIcon.isDisplayed();
    }
    
	// Validate date of birth Placeholder
	public String dateofbirthValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(dateofbirth));
		return dateofbirth.getAttribute("placeholder");
	}
    
    @FindBy(id = "dldateofbirth")
    private WebElement dateofbirth;
	
    public void enterDateOfBirth(String dob) {
        wait.until(ExpectedConditions.elementToBeClickable(dateofbirth));
        dateofbirth.clear();
        dateofbirth.sendKeys(dob);  // Format should match the date picker format (e.g., "MM/dd/yyyy")
    }
    
    // Date of Birth Label
    @FindBy(xpath="//label[text()='Date of Birth']")
    private WebElement dateOfBirthLabel;

    public boolean isDateofBirthLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(dateOfBirthLabel));
        return dateOfBirthLabel.isDisplayed();
    }

    public String getDateofBirthLabelText() {
        return dateOfBirthLabel.getText();
    }
	
    // DateofBirth required error message
    @FindBy(id = "city-error")
    private WebElement dateofBirthRequiredErrorMessage;
    
    public boolean isDateofBirthRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(dateofBirthRequiredErrorMessage));
        return dateofBirthRequiredErrorMessage.isDisplayed();
    }


    @FindBy(xpath = "//div[@class='picker__day picker__day--infocus']") // General XPath for a date element in the picker
    private List<WebElement> availableDatesInPicker;

    @FindBy(xpath = "//div[contains(@class, 'picker__day--selected')]") // XPath for the selected date
    private WebElement selectedDateInPicker;

    // Method to open the date picker by clicking the DOB field
    public void openDatePicker() {
        wait.until(ExpectedConditions.elementToBeClickable(dateofbirth));
        dateofbirth.click();
    }

    // Method to select a date from the date picker using UI interaction
    public void selectDateFromPicker(LocalDate date) {
        // Adjust the logic based on how the date picker works in your application.
        String day = String.valueOf(date.getDayOfMonth());
        
        // Ensure you navigate to the correct month/year (if required) using your specific picker controls.
        // Once the correct month/year is displayed, find the day element and click it.
        WebElement dayElement = driver.findElement(By.xpath("//div[contains(@class, 'picker__day') and text()='" + day + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(dayElement));
        dayElement.click(); // Click the day to select it
    }

    // Method to get the selected date from the input field after it has been selected via the date picker
    public String getSelectedDateFromInputField() {
        wait.until(ExpectedConditions.visibilityOf(dateofbirth));
        return dateofbirth.getAttribute("value"); // Get the selected date from the input field
    }

    // Method to check if a specific date is selectable in the picker
    public boolean isDateSelectable(String day) {
        try {
            WebElement dayElement = driver.findElement(By.xpath("//div[contains(@class, 'picker__day') and text()='" + day + "']"));
            return dayElement.isEnabled(); // Check if the day is enabled
        } catch (NoSuchElementException e) {
            return false; // Date is not selectable if the element is not found or is disabled
        }
    }
    
	// Zip Code  
    @FindBy(id = "zip")
    private WebElement zipInput;
    
    public void enterZip(String zip) {
        wait.until(ExpectedConditions.visibilityOf(zipInput));
        zipInput.clear();
        zipInput.sendKeys(zip);
    }
    
    // Zip Code Label
    @FindBy(xpath="//label[text()='Zip Code']")
    private WebElement zipCodeLabel;

    public boolean isZipCodeLabelDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(zipCodeLabel));
        return zipCodeLabel.isDisplayed();
    }

    public String getZipCodeLabelText() {
        return zipCodeLabel.getText();
    }
    
    // Zip Code required error message
    @FindBy(id = "zip-error")
    private WebElement zipRequiredErrorMessage;
    
    public boolean isZipRequiredMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(zipRequiredErrorMessage));
        return zipRequiredErrorMessage.isDisplayed();
    }
    
    // Zip Code validation error message
    @FindBy(id = "zip-error")
    private WebElement zipValidationErrorMessage;
    
    public boolean isZipErrorMessageDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(zipValidationErrorMessage));
        return zipValidationErrorMessage.isDisplayed();
    }
    
	// Validate Zip Code Placeholder
	public String zipValidatePlaceholder() {
		wait.until(ExpectedConditions.visibilityOf(zipInput));
		return zipInput.getAttribute("placeholder");
	}
    
	// Continue btn on Address
    @FindBy(id = "btnAddress")
    private WebElement continueButton;
    
    public void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButton.click();
    }
    
	public String ContinueButton() {
		wait.until(ExpectedConditions.visibilityOf(continueButton));
		return continueButton.getText();
	}
	
	// Footer Text on Address Page
		@FindBy(xpath = "(//p[@class='m-3 mb-0 zulu-custom-footer'])[9]")
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
