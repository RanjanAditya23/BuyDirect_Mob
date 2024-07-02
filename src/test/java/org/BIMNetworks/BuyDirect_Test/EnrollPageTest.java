package org.BIMNetworks.BuyDirect_Test;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import org.BIMNetworks.BuyDirect_PageObject.EnrollPage;
import org.BIMNetworks.BuyDirect_PageObject.WelcomePage;
import org.BuyDirect_Android_utils.DataBaseConnection;
import org.BuyDirect_Android_utils.ExcelUtility;
import org.BuyDirect_Android_utils.Generics;
import org.BuyDirect_Android_utils.Helper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(org.BuyDirect_Android_utils.ExtentTestNGListener.class)
public class EnrollPageTest extends BaseTest {

	EnrollPage enrollPageObject;
	Generics genericsObject;
	WelcomePage welcomePageObject;
	Helper helperObject;
	WebDriverWait wait;


	@BeforeClass
	public void driverInitialization() {
		enrollPageObject = new EnrollPage(driver);
		genericsObject = new Generics(driver);
		welcomePageObject = new WelcomePage(driver);
		helperObject = new Helper(driver);

	}

	// Click on Continue Button On Welcome Page
	@Test
	public void clickOnWelcomePageContinueButton() throws InterruptedException {
		welcomePageObject.ClickOnContinue();
	}

	// PartnerLogo
	@Test
	public void test_PartnerLogoisDisplayed() {
		boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();
		assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page");
	}

	// BrokenImageTest
	@Test
	public void brokenImageTest() throws URISyntaxException, IOException {

		// Construct URL object from the image URL string
		URL url = new URI(genericsObject.partnerlogo()).toURL();

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("HEAD");
		int responseCode = connection.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.out.println("Broken Image: " + genericsObject.partnerlogo());
		}

		Assert.assertEquals(responseCode, HttpURLConnection.HTTP_OK, "Image is broken.");
	}

	@Test
	public void testPartnerLogo() throws SQLException {
		String query = "select Partner_Image_URL from [dbo].[Partner_Image] where Partner_ID = 127 and Partner_Image_ID = 1";
		Assert.assertEquals(genericsObject.partnerlogo(), DataBaseConnection.testWithDataBase(query).get(0));
	}

	// TenderName
	@Test
	public void testPartnerTenderNameisDisplayed() {
		boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();
		assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page");
	}

	@Test
	public void testPartnerTenderNameHeader() throws SQLException {
		String query = "select Tender_Name_Header from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127 and Tender_Name_Header = 'BIM Grocery Pay'";
		Assert.assertEquals(genericsObject.partnerTenderNameHeader(), DataBaseConnection.testWithDataBase(query).get(0));
	}

	@Test
	public void testProgressIndicators() throws InterruptedException {
		genericsObject.allProgressIndicatorIsDisplayed();
	}

	@Test
	public void testIsDLRequired() throws SQLException, InterruptedException {
		// Database connection and logic to retrieve IsDLRequired
		String query = "select IsDLRequired from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127";
		int isDLRequired = Integer.parseInt(DataBaseConnection.testWithDataBase(query).get(0));

		// Expected indicators based on IsDLRequired
		List<String> expectedText;
		switch (isDLRequired) {
		case 0:
			expectedText = Helper.createList("Enroll", "Address", "Plastic", "Banking");
			break;
		case 1:
			expectedText = Helper.createList("Enroll", "Address", "Identity", "Plastic", "Banking");
			break;
		default:
			Assert.fail("Invalid value retrieved from the database: " + isDLRequired);
			return;
		}

		// Get the displayed text from UI
		List<String> displayedTexts = genericsObject.getAllIndicatorsText();

		// Validate displayed texts
		Assert.assertEquals(displayedTexts, expectedText);
	}

	@Test
	public void testEnrollPageRequiredFieldErrorMessage() throws InterruptedException {
		// Validate enroll page input field required error message 
		enrollPageObject.clickContinue();

		Assert.assertTrue(enrollPageObject.isFirstNameRequiredMessageDisplayed(),"Required First Name error message not displayed");
		Assert.assertTrue(enrollPageObject.isLastNameRequiredMessageDisplayed(),"Required Last Name error message not displayed");
		Assert.assertTrue(enrollPageObject.isEmailRequiredErrorMessageDisplayed(),"Required Email Address error message not displayed");
		Assert.assertTrue(enrollPageObject.isMobilePhoneRequiredErrorMessageDisplayed(),"Required Mobile Phone error message not displayed");
		Assert.assertTrue(enrollPageObject.isPinRequiredErrorMessageDisplayed(),"Required Pin error message not displayed");
		WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
		helperObject.scrollToElement(checkbox);
		Assert.assertTrue(enrollPageObject.isTermsAndConditionsRequiredErrorMessageDisplayed(),"Required Terms and Services error message not displayed");
	}	
	
	@Test
	public void testValidateTermsAndConditions() throws InterruptedException {
		// Validate TermsAndConditions checkbox
	    enrollPageObject.uncheckTermsAndConditions();
	    enrollPageObject.clickContinue();

	    // Initialize WebDriverWait
	     wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // Verify error message when aria-invalid is true
	    WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
	    String ariaInvalidBeforeCheck = checkbox.getAttribute("aria-invalid");

	    if ("true".equals(ariaInvalidBeforeCheck)) {
	        WebElement errorMessage = wait.until(ExpectedConditions
	                .visibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
	        Assert.assertTrue(errorMessage.isDisplayed(),
	                "Error message 'Please select Terms of Services' is not displayed when aria-invalid is true");
	    }

	    helperObject.scrollToElement(checkbox);
	    enrollPageObject.checkTermsAndConditions();

	    // Verify error message is not displayed when aria-invalid is false
	    String ariaInvalidAfterCheck = checkbox.getAttribute("aria-invalid");
	    if ("false".equals(ariaInvalidAfterCheck)) {
	        // Wait for the error message to disappear
	        boolean isErrorMessageInvisible = wait.until(ExpectedConditions
	                .invisibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
	        Assert.assertTrue(isErrorMessageInvisible,
	                "Error message 'Please select Terms of Services' is displayed when aria-invalid is false");
	    }
	}
	
	@Test(dependsOnMethods = "testValidateTermsAndConditions")
	public void testValidEnrollmentInputFields() throws InterruptedException {
		
	    // Define the file path, sheet name
	    String filePath = ".\\datafiles\\testdata.xlsx";
	    String sheetName = "Enroll";

	    // Fetch data for the specified row only.
	    int rowNum = 1;
	    String firstname = ExcelUtility.getCellData(filePath, sheetName, "firstname", rowNum);
	    String lastname = ExcelUtility.getCellData(filePath, sheetName, "lastname", rowNum);
	    String originalEmail = ExcelUtility.getCellData(filePath, sheetName, "email", rowNum);
	    String email = Helper.generateRandomEmail(originalEmail); // Using Helper to generate random email
	    String phone = ExcelUtility.getCellData(filePath, sheetName, "phone", rowNum);
	    String pin = ExcelUtility.getCellData(filePath, sheetName, "pin", rowNum);

	    enrollPageObject.enterFirstName(firstname);
	    enrollPageObject.enterLastName(lastname);
	    enrollPageObject.enterEmailAddress(email);
	    enrollPageObject.enterMobilePhone(phone);
	    enrollPageObject.enterCreatePIN(pin);
	    helperObject.scroll();
	    enrollPageObject.checkTermsAndConditions();
	    WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
		helperObject.scrollToElement(checkbox);
	    enrollPageObject.clickContinue();
	}

	public void testValidateFirstName(String firstName) {
	    String regexValidator = "^[a-zA-Z]+(?:[\\s-_.’']+[a-zA-Z]+)*$";

	    if (firstName.length() < 1 || firstName.length() > 28 || !firstName.matches(regexValidator)) {
	        Assert.assertTrue(enrollPageObject.isFirstNameErrorMessageDisplayed(), "Invalid First Name error message not displayed");
	    } else {
	        Assert.assertFalse(enrollPageObject.isFirstNameErrorMessageDisplayed(), "Invalid First Name error message displayed");
	    }
	}

	public void testValidateLastName(String lastName) {
	    String regexValidator = "^[a-zA-Z]+(?:[\\s-_.’']+[a-zA-Z]+)*$";

	    if (lastName.length() < 1 || lastName.length() > 28 || !lastName.matches(regexValidator)) {
	        Assert.assertTrue(enrollPageObject.isLastNameErrorMessageDisplayed(), "Invalid Last Name error message not displayed");
	    } else {
	        Assert.assertFalse(enrollPageObject.isLastNameErrorMessageDisplayed(), "Invalid Last Name error message displayed");
	    }
	}

	public void testValidateEmail(String email) {
		String regexValidator = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

	    if (!email.matches(regexValidator)) {
	        Assert.assertTrue(
	            enrollPageObject.isEmailErrorMessageDisplayed() || enrollPageObject.isEmailValidationToastMessageDisplayed(),
	            "Invalid Email error message or toast message not displayed for incorrect format");
	    } else {
	        Assert.assertFalse(
	            enrollPageObject.isEmailErrorMessageDisplayed() || enrollPageObject.isEmailValidationToastMessageDisplayed(),
	            "Invalid Email error message or toast message displayed");
	    }
	}

	public void testValidateMobilePhone(String phone) {
	    String regexValidator = "^(1-?)?(\\([2-9]\\d{2}\\)|[2-9]\\d{2})-?[2-9]\\d{2}-?\\d{4}$";

	    if (!phone.matches(regexValidator)) {
	        Assert.assertTrue(
	            enrollPageObject.isMobilePhoneErrorMessageDisplayed() || enrollPageObject.isMobilePhoneValidationToastMessageDisplayed(),
	            "Invalid Mobile Phone error message or toast message not displayed for incorrect format");
	    } else {
	        Assert.assertFalse(
	            enrollPageObject.isMobilePhoneErrorMessageDisplayed() || enrollPageObject.isMobilePhoneValidationToastMessageDisplayed(),
	            "Invalid Mobile Phone error message or toast message displayed");
	    }
	}

	// Validate PIN
	public void testValidatePIN(String pin) {
	    // Check if the PIN is exactly 4 digits
	    if (!pin.matches("^\\d{4}$")) {
	        Assert.assertTrue(
	            enrollPageObject.isPinErrorMessageDisplayed() || enrollPageObject.isPinValidationToastMessageDisplayed(),
	            "Invalid PIN error message or toast message not displayed for incorrect format");
	    } else if (pin.chars().distinct().count() < 3) {
	        // Check if there are fewer than 3 unique digits
	        Assert.assertTrue(enrollPageObject.isPinErrorMessageDisplayed(), "Invalid PIN error message not displayed for lack of unique digits");
	    } else {
	        // Check for consecutive repeating digits
	        boolean isValid = true;
	        for (int i = 0; i < pin.length() - 1; i++) {
	            if (pin.charAt(i) == pin.charAt(i + 1)) {
	                Assert.assertTrue(enrollPageObject.isPinValidationToastMessageDisplayed(), "PIN toast message not displayed for consecutive digits");
	                isValid = false;
	                break;
	            }
	        }
	        Assert.assertTrue(isValid, "PIN is valid");
	    }
	}

	


	
}