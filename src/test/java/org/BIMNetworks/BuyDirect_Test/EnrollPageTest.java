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
import java.util.concurrent.TimeoutException;

import org.BIMNetworks.BuyDirect_PageObject.EnrollPage;
import org.BIMNetworks.BuyDirect_PageObject.WelcomePage;
import org.BuyDirect_Android_utils.DataBaseConnection;
import org.BuyDirect_Android_utils.ExcelUtility;
import org.BuyDirect_Android_utils.ExtentReportManager;
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

import com.aventstack.extentreports.Status;

@Listeners(org.BuyDirect_Android_utils.ExtentTestNGListener.class)
public class EnrollPageTest extends BaseTest {

	EnrollPage enrollPageObject;
	Generics genericsObject;
	WelcomePage welcomePageObject;
	Helper helperObject;
	WebDriverWait wait;


	@BeforeClass
	public void driverInitialization() {
		welcomePageObject = new WelcomePage(driver);
		enrollPageObject = new EnrollPage(driver);
		genericsObject = new Generics(driver);		
		helperObject = new Helper(driver);
		
		// Log the initialization in the Extent Report
        ExtentReportManager.getTest().log(Status.INFO, "Page objects initialized: WelcomePage, EnrollPage, Generics and Helper");

	}

	// Click on Continue Button On Welcome Page
	@Test
    public void clickOnWelcomePageContinueButton() throws InterruptedException {
        welcomePageObject.ClickOnContinue();
        // Log the action of clicking on the Continue button
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Continue button on the welcome page.");
    }

    @Test
    public void test_PartnerLogoisDisplayed() {
        boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();

        // Log the display status of the Partner Logo
        ExtentReportManager.getTest().log(Status.INFO, "Partner Logo Displayed: " + partnerLogoDisplayed);

        Assert.assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page");
    }

    @Test
    public void brokenImageTest() throws URISyntaxException, IOException {
        URL url = new URI(genericsObject.partnerlogo()).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();

        // Log the URL being tested and the HTTP response code
        ExtentReportManager.getTest().log(Status.INFO, "Image URL: " + genericsObject.partnerlogo());
        ExtentReportManager.getTest().log(Status.INFO, "HTTP Response Code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            ExtentReportManager.getTest().log(Status.FAIL, "Broken Image detected: " + genericsObject.partnerlogo());
        }

        Assert.assertEquals(responseCode, HttpURLConnection.HTTP_OK, "Image is broken.");
    }

    @Test
    public void testPartnerLogo() throws SQLException {
        String query = "SELECT Partner_Image_URL FROM [dbo].[Partner_Image] WHERE Partner_ID = 127 AND Partner_Image_ID = 1";
        String expectedURL = DataBaseConnection.testWithDataBase(query).get(0);
        String actualURL = genericsObject.partnerlogo();

        // Log the database query, expected and actual URLs
        ExtentReportManager.getTest().log(Status.INFO, "Database Query: " + query);
        ExtentReportManager.getTest().log(Status.INFO, "Expected Partner Logo URL: " + expectedURL);
        ExtentReportManager.getTest().log(Status.INFO, "Actual Partner Logo URL: " + actualURL);

        Assert.assertEquals(actualURL, expectedURL, "Partner Logo URL does not match the expected URL.");
    }

    @Test
    public void testPartnerTenderNameisDisplayed() {
        boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();

        // Log the display status of the Partner Tender Name
        ExtentReportManager.getTest().log(Status.INFO, "Partner Tender Name Displayed: " + partnerTenderNameDisplayed);

        Assert.assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page");
    }

    @Test
    public void testPartnerTenderNameHeader() throws SQLException {
        String query = "SELECT Tender_Name_Header FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127 AND Tender_Name_Header = 'BIM Grocery Pay'";
        String expectedHeader = DataBaseConnection.testWithDataBase(query).get(0);
        String actualHeader = genericsObject.partnerTenderNameHeader();

        // Log the database query, expected and actual headers
        ExtentReportManager.getTest().log(Status.INFO, "Database Query: " + query);
        ExtentReportManager.getTest().log(Status.INFO, "Expected Tender Name Header: " + expectedHeader);
        ExtentReportManager.getTest().log(Status.INFO, "Actual Tender Name Header: " + actualHeader);

        Assert.assertEquals(actualHeader, expectedHeader, "Tender Name Header does not match the expected value.");
    }

    @Test
    public void testProgressIndicators() throws InterruptedException {
        boolean allProgressIndicatorsDisplayed = genericsObject.allProgressIndicatorIsDisplayed();

        // Log the display status of all progress indicators
        ExtentReportManager.getTest().log(Status.INFO, "All Progress Indicators Displayed: " + allProgressIndicatorsDisplayed);

        Assert.assertTrue(allProgressIndicatorsDisplayed, "Not all progress indicators are displayed.");
    }

    @Test
    public void testIsDLRequired() throws SQLException, InterruptedException {
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Database connection and logic to retrieve IsDLRequired and Is_Plastics_Page_Displayed
        String query = "SELECT IsDLRequired, Is_Plastics_Page_Displayed FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127";
        List<String> result = DataBaseConnection.testWithDataBase(query);
        int isDLRequired = Integer.parseInt(result.get(0));
        int isPlasticsPageDisplayed = Integer.parseInt(result.get(1));

        // Log the database query and retrieved results
        ExtentReportManager.getTest().log(Status.INFO, "Database Query: " + query);
        ExtentReportManager.getTest().log(Status.INFO, "IsDLRequired: " + isDLRequired + ", Is_Plastics_Page_Displayed: " + isPlasticsPageDisplayed);

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

        // Log the expected indicators
        ExtentReportManager.getTest().log(Status.INFO, "Expected Indicators: " + expectedText);

        // Wait for the indicators to be visible in the UI before retrieving them
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@class='nav-item']")));

        // Get the displayed text from UI
        List<String> displayedTexts = genericsObject.getAllIndicatorsText();

        // Log the displayed indicators
        ExtentReportManager.getTest().log(Status.INFO, "Displayed Indicators: " + displayedTexts);

        // Validate displayed texts
        Assert.assertEquals(displayedTexts, expectedText, "Displayed texts do not match the expected texts");
    }

    @Test
    public void testEnrollPageRequiredFieldErrorMessage() throws InterruptedException {
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for the Continue button to be clickable before interacting with it
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEnroll")));
        enrollPageObject.clickContinue();

        // Log the action of clicking the Continue button
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Continue button on the Enroll page.");

        // Validate that required field error messages are displayed and log the results
        boolean isFirstNameRequired = enrollPageObject.isFirstNameRequiredMessageDisplayed();
        boolean isLastNameRequired = enrollPageObject.isLastNameRequiredMessageDisplayed();
        boolean isEmailRequired = enrollPageObject.isEmailRequiredErrorMessageDisplayed();
        boolean isMobilePhoneRequired = enrollPageObject.isMobilePhoneRequiredErrorMessageDisplayed();
        boolean isPinRequired = enrollPageObject.isPinRequiredErrorMessageDisplayed();

        ExtentReportManager.getTest().log(Status.INFO, "First Name Required: " + isFirstNameRequired);
        ExtentReportManager.getTest().log(Status.INFO, "Last Name Required: " + isLastNameRequired);
        ExtentReportManager.getTest().log(Status.INFO, "Email Required: " + isEmailRequired);
        ExtentReportManager.getTest().log(Status.INFO, "Mobile Phone Required: " + isMobilePhoneRequired);
        ExtentReportManager.getTest().log(Status.INFO, "Pin Required: " + isPinRequired);

        Assert.assertTrue(isFirstNameRequired, "Required First Name error message not displayed");
        Assert.assertTrue(isLastNameRequired, "Required Last Name error message not displayed");
        Assert.assertTrue(isEmailRequired, "Required Email Address error message not displayed");
        Assert.assertTrue(isMobilePhoneRequired, "Required Mobile Phone error message not displayed");
        Assert.assertTrue(isPinRequired, "Required Pin error message not displayed");

        // Scroll to the Terms and Conditions checkbox and log the action
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);
        ExtentReportManager.getTest().log(Status.INFO, "Scrolled to the Terms and Conditions checkbox.");

        // Validate that the Terms and Conditions required field error message is displayed and log the result
        boolean isTermsAndConditionsRequired = enrollPageObject.isTermsAndConditionsRequiredErrorMessageDisplayed();
        ExtentReportManager.getTest().log(Status.INFO, "Terms and Conditions Required: " + isTermsAndConditionsRequired);

        Assert.assertTrue(isTermsAndConditionsRequired, "Required Terms and Services error message not displayed");
    }
	
    @Test
    public void testValidateTermsAndConditions() throws InterruptedException, TimeoutException {
        // Scroll to the checkbox and log the action
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);
        ExtentReportManager.getTest().log(Status.INFO, "Scrolled to the Terms and Conditions checkbox.");

        // Uncheck the Terms and Conditions checkbox, log the action, and click continue
        enrollPageObject.uncheckTermsAndConditions();
        ExtentReportManager.getTest().log(Status.INFO, "Unchecked the Terms and Conditions checkbox.");
        enrollPageObject.clickContinue();
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Continue button without checking Terms and Conditions.");

        // Initialize WebDriverWait with appropriate timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Verify error message when aria-invalid is true
        checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        String ariaInvalidBeforeCheck = checkbox.getAttribute("aria-invalid");
        ExtentReportManager.getTest().log(Status.INFO, "Aria-invalid value before checking: " + ariaInvalidBeforeCheck);

        if ("true".equals(ariaInvalidBeforeCheck)) {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message 'Please select Terms of Services' is not displayed when aria-invalid is true");
            ExtentReportManager.getTest().log(Status.PASS, "Error message 'Please select Terms of Services' is correctly displayed.");
        }

        // Check the Terms and Conditions checkbox, log the action, and click continue
        enrollPageObject.checkTermsAndConditions();
        ExtentReportManager.getTest().log(Status.INFO, "Checked the Terms and Conditions checkbox.");

        // Verify error message is not displayed when aria-invalid is false
        String ariaInvalidAfterCheck = checkbox.getAttribute("aria-invalid");
        ExtentReportManager.getTest().log(Status.INFO, "Aria-invalid value after checking: " + ariaInvalidAfterCheck);

        if ("false".equals(ariaInvalidAfterCheck)) {
            // Wait for the error message to disappear
            boolean isErrorMessageInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(isErrorMessageInvisible, "Error message 'Please select Terms of Services' is displayed when aria-invalid is false");
            ExtentReportManager.getTest().log(Status.PASS, "Error message 'Please select Terms of Services' is correctly hidden.");
        }

        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEnroll")));
        continueButton.click();
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Continue button after checking Terms and Conditions.");
    }

    @Test(dependsOnMethods = "testValidateTermsAndConditions")
    public void testValidEnrollmentInputFields() throws InterruptedException {
        // Define the file path, sheet name
        String filePath = ".\\datafiles\\testdata.xlsx";
        String sheetName = "Enroll";

        // Fetch data for the specified row only
        int rowNum = 1;
        String firstname = ExcelUtility.getCellData(filePath, sheetName, "firstname", rowNum);
        String lastname = ExcelUtility.getCellData(filePath, sheetName, "lastname", rowNum);
        String originalEmail = ExcelUtility.getCellData(filePath, sheetName, "email", rowNum);
        String email = Helper.generateRandomEmail(originalEmail); // Using Helper to generate random email
        String phone = ExcelUtility.getCellData(filePath, sheetName, "phone", rowNum);
        String pin = ExcelUtility.getCellData(filePath, sheetName, "pin", rowNum);

        // Log the input data
        ExtentReportManager.getTest().log(Status.INFO, "Enrollment data: First Name: " + firstname + ", Last Name: " + lastname + ", Email: " + email + ", Phone: " + phone + ", PIN: " + pin);

        enrollPageObject.enterFirstName(firstname);
        enrollPageObject.enterLastName(lastname);
        enrollPageObject.enterEmailAddress(email);
        enrollPageObject.enterMobilePhone(phone);
        enrollPageObject.enterCreatePIN(pin);
        ExtentReportManager.getTest().log(Status.INFO, "Entered all valid enrollment input fields.");

        helperObject.scroll();
        enrollPageObject.checkTermsAndConditions();
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);
        enrollPageObject.clickContinue();
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Continue button after entering valid data.");
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
        ExtentReportManager.getTest().log(Status.INFO, "Starting testCombinedTermsAndConditionsText");
        
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Query the database for the expected combined text
        String query = "select Terms_Checkbox_text from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127";
        List<String> result = DataBaseConnection.testWithDataBase(query);
        String expectedText = result.get(0).replace("{", "").replace("}", ""); // Remove curly braces
        ExtentReportManager.getTest().log(Status.INFO, "Expected Terms and Conditions text: " + expectedText);

        // Wait for the Terms and Conditions text element to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='I certify that I am at least 18 years old and that I agree to the ']")));
        
        // Get the actual text
        String actualText = enrollPageObject.termsAndConditionsbeforetext();
        ExtentReportManager.getTest().log(Status.INFO, "Actual Terms and Conditions text: " + actualText);

        // Validate the Terms and Conditions text against the database value
        Assert.assertEquals(actualText, expectedText, "Terms and Conditions text does not match the expected value from the database");
        ExtentReportManager.getTest().log(Status.PASS, "Terms and Conditions text matches the expected value.");
    }

    @Test
    public void testTermsAndConditionsBeforeTextIsDisplayed() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTermsAndConditionsBeforeTextIsDisplayed");
        
        boolean isDisplayed = enrollPageObject.termsAndConditionsbeforetexisDisplayed();
        Assert.assertTrue(isDisplayed, "Terms and Conditions before text is not displayed");
        ExtentReportManager.getTest().log(Status.PASS, "Terms and Conditions before text is displayed.");
    }

    @Test
    public void testTermsAndConditionsLinkTextIsDisplayed() throws TimeoutException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTermsAndConditionsLinkTextIsDisplayed");
        
        // Initialize WebDriverWait with a timeout of 15 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Term and Conditions']"))).isDisplayed();
        Assert.assertTrue(isDisplayed, "Terms and Conditions link text is not displayed");
        ExtentReportManager.getTest().log(Status.PASS, "Terms and Conditions link text is displayed.");
    }

    @Test
    public void testTermsAndConditionsLinkIsNotBroken() throws IOException, URISyntaxException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTermsAndConditionsLinkIsNotBroken");
        
        // Get the href attribute of the link
        String url = enrollPageObject.getTermsAndConditionsLinkHref();
        ExtentReportManager.getTest().log(Status.INFO, "Terms and Conditions link URL: " + url);

        // Validate the URL by checking the HTTP response status code
        int statusCode = Helper.getHTTPResponseStatusCode(url);
        Assert.assertEquals(statusCode, 200, "The Terms and Conditions link is broken");
        ExtentReportManager.getTest().log(Status.PASS, "Terms and Conditions link is valid with status code 200.");
    }

    @Test
    public void testTermsAndConditionsLinkOpensNewWindow() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTermsAndConditionsLinkOpensNewWindow");
        
        // Initialize WebDriverWait with a timeout of 15 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Store the current window handle
        String originalWindow = driver.getWindowHandle();

        // Scroll to the "Terms and Conditions" link element
        WebElement termsAndConditions = enrollPageObject.termsAndConditionsLinkText();
        helperObject.scrollToElement(termsAndConditions);
        ExtentReportManager.getTest().log(Status.INFO, "Scrolled to the Terms and Conditions link.");

        // Ensure the element is visible and interactable
        wait.until(ExpectedConditions.elementToBeClickable(termsAndConditions));

        // Click the "Terms and Conditions" link
        enrollPageObject.clickTermsAndConditionsLink();
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Terms and Conditions link.");

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
        ExtentReportManager.getTest().log(Status.PASS, "New window opened with the expected URL.");

        // Close the new window and switch back to the original window
        driver.close();
        driver.switchTo().window(originalWindow);
        ExtentReportManager.getTest().log(Status.INFO, "Closed the new window and switched back to the original window.");
    }

    @Test
    public void testTextNotificationsTextIsDisplayed() throws TimeoutException, InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTextNotificationsTextIsDisplayed");
        
        // Wait for the element to be visible
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='form-check-label label--text-notification']"))).isDisplayed();
        
        // Perform the assertion
        Assert.assertTrue(isDisplayed, "Text Notifications text is not displayed");
        ExtentReportManager.getTest().log(Status.PASS, "Text Notifications text is displayed.");
    }

    @Test
    public void testTextNotificationsLinkTextIsDisplayed() {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTextNotificationsLinkTextIsDisplayed");
        
        boolean isDisplayed = enrollPageObject.isTextNotificationsLinkDisplayed();
        Assert.assertTrue(isDisplayed, "Text Notifications link text is not displayed");
        ExtentReportManager.getTest().log(Status.PASS, "Text Notifications link text is displayed.");
    }

    @Test
    public void testTextNotificationsLinkIsNotBroken() throws IOException, URISyntaxException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTextNotificationsLinkIsNotBroken");
        
        // Get the href attribute of the link
        String url = enrollPageObject.getTextNotificationsLinkHref();
        ExtentReportManager.getTest().log(Status.INFO, "Text Notifications link URL: " + url);

        // Validate the URL by checking the HTTP response status code
        int statusCode = Helper.getHTTPResponseStatusCode(url);
        Assert.assertEquals(statusCode, 200, "The Text Notifications link is broken");
        ExtentReportManager.getTest().log(Status.PASS, "Text Notifications link is valid with status code 200.");
    }

    @Test
    public void testTextNotificationsLinkOpensNewWindow() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTextNotificationsLinkOpensNewWindow");
        
        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Store the current window handle
        String originalWindow = driver.getWindowHandle();

        // Scroll to the "Text Notifications" link element
        WebElement textNotifications = enrollPageObject.termsAndConditionsLinkText(); // Update this line to target the correct element for Text Notifications link
        helperObject.scrollToElement(textNotifications);

        // Click the "Text Notifications" link
        enrollPageObject.clickTextNotificationsLink();
        ExtentReportManager.getTest().log(Status.INFO, "Clicked on the Text Notifications link.");

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
                ExtentReportManager.getTest().log(Status.PASS, "New window opened with the expected URL.");

                // Close the new window and switch back to the original window
                driver.close();
                driver.switchTo().window(originalWindow);
                ExtentReportManager.getTest().log(Status.INFO, "Closed the new window and switched back to the original window.");
                break;
            }
        }
    }

    @Test
    public void testTextBeforeSignInHereLink() throws InterruptedException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testTextBeforeSignInHereLink");
        
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for the element to be visible
        WebElement signInHereTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='text-muted mb-0 SingInHereText'])[1]")));

        // Extract the text and perform the assertion
        String actualText = signInHereTextElement.getText().substring(0, "To continue enrolling,".length()).trim();
        Assert.assertEquals(actualText, "To continue enrolling,");
        ExtentReportManager.getTest().log(Status.PASS, "Sign in here text before link matches the expected text.");
    }

    @Test
    public void testSignInHereLinkTextisDisplayed() throws InterruptedException, TimeoutException {
        ExtentReportManager.getTest().log(Status.INFO, "Starting testSignInHereLinkTextisDisplayed");
        
        // Initialize WebDriverWait with a longer timeout if necessary
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Ensure the element is visible before interacting
        WebElement signInLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='text-muted mb-0 SingInHereText'])[1]")));
        boolean signInLinkDisplayed = signInLink.isDisplayed();

        assertTrue(signInLinkDisplayed, "Sign in here link is not displayed on the welcome page");
        ExtentReportManager.getTest().log(Status.PASS, "Sign in here link is displayed.");
    }

	
	

}