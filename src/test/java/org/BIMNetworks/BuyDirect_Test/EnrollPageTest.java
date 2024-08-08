package org.BIMNetworks.BuyDirect_Test;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
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
	    // Database connection and logic to retrieve IsDLRequired and Is_Plastics_Page_Displayed
	    String query = "select IsDLRequired, Is_Plastics_Page_Displayed from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127";
	    List<String> result = DataBaseConnection.testWithDataBase(query);
	    int isDLRequired = Integer.parseInt(result.get(0));
	    int isPlasticsPageDisplayed = Integer.parseInt(result.get(1));

	    // Expected indicators based on IsDLRequired and Is_Plastics_Page_Displayed
	    List<String> expectedText = new ArrayList<>();

	    expectedText.add("Enroll");
	    expectedText.add("Address");
	    if (isDLRequired == 1) {
	        expectedText.add("Identity");
	    }
	    if (isPlasticsPageDisplayed == 1) {
	        expectedText.add("Plastic");
	    }
	    expectedText.add("Banking");

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
		WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
		helperObject.scrollToElement(checkbox);
		
        enrollPageObject.uncheckTermsAndConditions();
        enrollPageObject.clickContinue();
        
        // Initialize WebDriverWait with appropriate timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Verify error message when aria-invalid is true
        checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        String ariaInvalidBeforeCheck = checkbox.getAttribute("aria-invalid");
        
        if ("true".equals(ariaInvalidBeforeCheck)) {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message 'Please select Terms of Services' is not displayed when aria-invalid is true");
        }
        
        enrollPageObject.checkTermsAndConditions();
        
        // Verify error message is not displayed when aria-invalid is false
        String ariaInvalidAfterCheck = checkbox.getAttribute("aria-invalid");
        
        if ("false".equals(ariaInvalidAfterCheck)) {
            // Wait for the error message to disappear
            boolean isErrorMessageInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(isErrorMessageInvisible, "Error message 'Please select Terms of Services' is displayed when aria-invalid is false");
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

	@Test
	public void testCombinedTermsAndConditionsText() throws SQLException, InterruptedException {
	    // Query the database for the expected combined text
	    String query = "select Terms_Checkbox_text from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127";
	    List<String> result = DataBaseConnection.testWithDataBase(query);
	    String expectedText = result.get(0).replace("{", "").replace("}", ""); // Remove curly braces

	    // Concatenate the TermsAndConditions text and the link text
	    String actualText = enrollPageObject.termsAndConditionsbeforetext();

	    // Validate the TermsAndConditions text against the database value
	    Assert.assertEquals(actualText, expectedText, "TermsAndConditions text does not match the expected value from the database");
	}

	@Test
	public void testTermsAndConditionsBeforeTextIsDisplayed() {
		boolean isDisplayed = enrollPageObject.termsAndConditionsbeforetexisDisplayed();
		Assert.assertTrue(isDisplayed, "Terms and Conditions before text is not displayed");
	}

	@Test
	public void testTermsAndConditionsLinkTextIsDisplayed() {
		boolean isDisplayed = enrollPageObject.termsAndConditionsLinkTextisDisplayed();
		Assert.assertTrue(isDisplayed, "Terms and Conditions link text is not displayed");
	}
	
	@Test
    public void testTermsAndConditionsLinkIsNotBroken() throws IOException, URISyntaxException {
        // Get the href attribute of the link
        String url = enrollPageObject.getTermsAndConditionsLinkHref();

        // Validate the URL by checking the HTTP response status code
        int statusCode = Helper.getHTTPResponseStatusCode(url);

        // Verify the status code is 200 (OK)
        Assert.assertEquals(statusCode, 200, "The Terms and Conditions link is broken");
    }

	// Window Handle
	@Test
	public void testTermsAndConditionsLinkOpensNewWindow() throws InterruptedException { 
	    // Initialize WebDriverWait with a timeout of 10 seconds
	    wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // Store the current window handle
	    String originalWindow = driver.getWindowHandle();
	    
	    // Scroll to the "Terms and Conditions" link element
	    WebElement termsandconditions = enrollPageObject.termsAndConditionsLinkText();
	    helperObject.scrollToElement(termsandconditions);

	    // Click the "Terms and Conditions" link
	    enrollPageObject.clickTermsAndConditionsLink();

	    // Wait for the new window or tab to open
	    wait.until(ExpectedConditions.numberOfWindowsToBe(2));

	    // Loop through all window handles to find the new window
	    for (String windowHandle : driver.getWindowHandles()) {
	        if (!originalWindow.contentEquals(windowHandle)) {
	            driver.switchTo().window(windowHandle);
	            break;
	        }
	    }

	    // Verify the URL of the new window
	    String expectedUrl = "https://test.bimnetworks.com/user/terms/partner/127"; // Replace with the expected URL
	    String actualUrl = driver.getCurrentUrl();
	    Assert.assertEquals(actualUrl, expectedUrl, "The URL of the new window does not match the expected URL");

	    // Close the new window and switch back to the original window
	    driver.close();
	    driver.switchTo().window(originalWindow);
	}
	
	// Validate TextNotifications Text	
	@Test
	public void testTextNotificationsTextIsDisplayed() {
	    boolean isDisplayed = enrollPageObject.isTextNotificationsTextLabelDisplayed();
	    Assert.assertTrue(isDisplayed, "TextNotifications text is not displayed");
	}

	@Test
	public void testTextNotificationsLinkTextIsDisplayed() {
	    boolean isDisplayed = enrollPageObject.isTextNotificationsLinkDisplayed();
	    Assert.assertTrue(isDisplayed, "TextNotifications link text is not displayed");
	}

	@Test
	public void testTextNotificationsLinkIsNotBroken() throws IOException, URISyntaxException {
	    // Get the href attribute of the link
	    String url = enrollPageObject.getTextNotificationsLinkHref();

	    // Validate the URL by checking the HTTP response status code
	    int statusCode = Helper.getHTTPResponseStatusCode(url);

	    // Verify the status code is 200 (OK)
	    Assert.assertEquals(statusCode, 200, "The TextNotifications link is broken");
	}

	@Test
	public void testTextNotificationsLinkOpensNewWindow() throws InterruptedException {
	    // Initialize WebDriverWait with a timeout of 10 seconds
	    wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    // Store the current window handle
	    String originalWindow = driver.getWindowHandle();
	    
	    // Scroll to the "TextNotifications" link element
	    WebElement textnotifications = enrollPageObject.termsAndConditionsLinkText();
	    helperObject.scrollToElement(textnotifications);

	    // Click the "Text Notifications" link
	    enrollPageObject.clickTextNotificationsLink();

	    // Wait for the new window or tab to open
	    wait.until(ExpectedConditions.numberOfWindowsToBe(2));

	    // Switch to the new window
	    for (String windowHandle : driver.getWindowHandles()) {
	        if (!originalWindow.equals(windowHandle)) {
	            driver.switchTo().window(windowHandle);

	            // Verify the URL of the new window
	            String expectedUrl = "https://test.bimnetworks.com/user/terms/partner/127#text-notification";
	            String actualUrl = driver.getCurrentUrl();
	            Assert.assertEquals(actualUrl, expectedUrl, "The URL of the new window does not match the expected URL");

	            // Close the new window and switch back to the original window
	            driver.close();
	            driver.switchTo().window(originalWindow);
	            break;
	        }
	    }
	}
	
	@Test
	public void testTextBeforeSignInHereLink() throws InterruptedException {
		String actualText = enrollPageObject.signInHereBeforeText().substring(0, "To continue enrolling,".length()).trim();
		Assert.assertEquals(actualText, "To continue enrolling,");
	}

	@Test
	public void testSignInHereLinkTextisDisplayed() throws InterruptedException {
		boolean signInLinkDisplayed = enrollPageObject.signInHereTextisDisplayed();
		assertTrue(signInLinkDisplayed, "Sign in here link is not displayed on the welcome page");
	}
	
	

}