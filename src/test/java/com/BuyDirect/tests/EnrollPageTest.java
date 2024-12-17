package com.BuyDirect.tests;

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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.BuyDirect.pageobjects.EnrollPage;
import com.BuyDirect.pageobjects.WelcomePage;
import com.BuyDirect.utils.DataBaseConnection;
import com.BuyDirect.utils.ExcelUtility;
import com.BuyDirect.listeners.ExtentReportManager;
import com.BuyDirect.utils.Generics;
import com.BuyDirect.utils.Helper;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

@Listeners(com.BuyDirect.listeners.ExtentTestNGListener.class)
public class EnrollPageTest extends BaseTest {

    private EnrollPage enrollPageObject;
    private Generics genericsObject;
    private WelcomePage welcomePageObject;
    private Helper helperObject;
    private WebDriverWait wait;
    private ExtentTest test;

    @BeforeClass
    public void driverInitialization() {
        // Initialize Extent Reports and create a new test
        ExtentReportManager.createTest("EnrollPage Tests");
        test = ExtentReportManager.getTest();
        
        // Ensure ExtentTest is initialized before logging
        if (test == null) {
            throw new IllegalStateException("ExtentTest object not initialized.");
        }
        
        // Initialize page objects
        welcomePageObject = new WelcomePage(driver);
        enrollPageObject = new EnrollPage(driver);
        genericsObject = new Generics(driver);        
        helperObject = new Helper(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Log page object initialization
        test.log(Status.INFO, "Page objects initialized: WelcomePage, EnrollPage, Generics, and Helper");
    }

	// Click on Continue Button On Welcome Page
	@Test
    public void clickOnWelcomePageContinueButton() throws InterruptedException {
        welcomePageObject.ClickOnContinue();
        // Log the action of clicking on the Continue button
        test.log(Status.INFO, "Clicked on the Continue button on the welcome page.");
    }

    @Test
    public void test_PartnerLogoisDisplayed() {
        boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();

        // Log the display status of the Partner Logo
        test.log(Status.INFO, "Partner Logo Displayed: " + partnerLogoDisplayed);

        Assert.assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page");
    }

    @Test
    public void brokenImageTest() throws URISyntaxException, IOException {
        URL url = new URI(genericsObject.partnerlogo()).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();

        // Log the URL being tested and the HTTP response code
        test.log(Status.INFO, "Image URL: " + genericsObject.partnerlogo());
        test.log(Status.INFO, "HTTP Response Code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            test.log(Status.FAIL, "Broken Image detected: " + genericsObject.partnerlogo());
        }

        Assert.assertEquals(responseCode, HttpURLConnection.HTTP_OK, "Image is broken.");
    }

    @Test
    public void testPartnerLogo() throws SQLException {
        String query = "SELECT Partner_Image_URL FROM [dbo].[Partner_Image] WHERE Partner_ID = 127 AND Partner_Image_ID = 1";
        String expectedURL = DataBaseConnection.testWithDataBase(query).get(0);
        String actualURL = genericsObject.partnerlogo();

        // Log the database query, expected and actual URLs
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Partner Logo URL: " + expectedURL);
        test.log(Status.INFO, "Actual Partner Logo URL: " + actualURL);

        Assert.assertEquals(actualURL, expectedURL, "Partner Logo URL does not match the expected URL.");
    }

    @Test
    public void testPartnerTenderNameisDisplayed() {
        boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();

        // Log the display status of the Partner Tender Name
        test.log(Status.INFO, "Partner Tender Name Displayed: " + partnerTenderNameDisplayed);

        Assert.assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page");
    }

    @Test
    public void testPartnerTenderNameHeader() throws SQLException {
        String query = "SELECT Tender_Name_Header FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127 AND Tender_Name_Header = 'BIM Grocery Pay'";
        String expectedHeader = DataBaseConnection.testWithDataBase(query).get(0);
        String actualHeader = genericsObject.partnerTenderNameHeader();

        // Log the database query, expected and actual headers
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Tender Name Header: " + expectedHeader);
        test.log(Status.INFO, "Actual Tender Name Header: " + actualHeader);

        Assert.assertEquals(actualHeader, expectedHeader, "Tender Name Header does not match the expected value.");
    }

    @Test
    public void testProgressIndicators() throws InterruptedException {
        boolean allProgressIndicatorsDisplayed = genericsObject.allProgressIndicatorIsDisplayed();

        // Log the display status of all progress indicators
        test.log(Status.INFO, "All Progress Indicators Displayed: " + allProgressIndicatorsDisplayed);

        Assert.assertTrue(allProgressIndicatorsDisplayed, "Not all progress indicators are displayed.");
    }

    @Test
    public void testProgressIndicatorsOnEnrollPage() throws SQLException, InterruptedException {
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Database connection and logic to retrieve IsDLRequired and Is_Plastics_Page_Displayed
        String query = "SELECT IsDLRequired, Is_Plastics_Page_Displayed FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127";
        List<String> result = DataBaseConnection.testWithDataBase(query);
        int isDLRequired = Integer.parseInt(result.get(0));
        int isPlasticsPageDisplayed = Integer.parseInt(result.get(1));

        // Log the database query and retrieved results
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "IsDLRequired: " + isDLRequired + ", Is_Plastics_Page_Displayed: " + isPlasticsPageDisplayed);

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
        test.log(Status.INFO, "Expected Indicators: " + expectedText);

        // Wait for the indicators to be visible in the UI before retrieving them
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//li[@class='nav-item']")));

        // Get the displayed text from UI
        List<String> displayedTexts = genericsObject.getAllIndicatorsText();

        // Log the displayed indicators
        test.log(Status.INFO, "Displayed Indicators: " + displayedTexts);

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
        test.log(Status.INFO, "Clicked on the Continue button on the Enroll page.");

        // Validate that required field error messages are displayed and log the results
        Thread.sleep(5000);       
        boolean isFirstNameRequired = enrollPageObject.isFirstNameRequiredMessageDisplayed();
        boolean isLastNameRequired = enrollPageObject.isLastNameRequiredMessageDisplayed();
        boolean isEmailRequired = enrollPageObject.isEmailRequiredErrorMessageDisplayed();
        boolean isMobilePhoneRequired = enrollPageObject.isMobilePhoneRequiredErrorMessageDisplayed();
        boolean isPinRequired = enrollPageObject.isPinRequiredErrorMessageDisplayed();

        test.log(Status.INFO, "First Name Required: " + isFirstNameRequired);
        test.log(Status.INFO, "Last Name Required: " + isLastNameRequired);
        test.log(Status.INFO, "Email Required: " + isEmailRequired);
        test.log(Status.INFO, "Mobile Phone Required: " + isMobilePhoneRequired);
        test.log(Status.INFO, "Pin Required: " + isPinRequired);

        Assert.assertTrue(isFirstNameRequired, "Required First Name error message not displayed");
        Assert.assertTrue(isLastNameRequired, "Required Last Name error message not displayed");
        Assert.assertTrue(isEmailRequired, "Required Email Address error message not displayed");
        Assert.assertTrue(isMobilePhoneRequired, "Required Mobile Phone error message not displayed");
        Assert.assertTrue(isPinRequired, "Required Pin error message not displayed");

        // Scroll to the Terms and Conditions checkbox and log the action
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);
        test.log(Status.INFO, "Scrolled to the Terms and Conditions checkbox.");

        // Validate that the Terms and Conditions required field error message is displayed and log the result
        boolean isTermsAndConditionsRequired = enrollPageObject.isTermsAndConditionsRequiredErrorMessageDisplayed();
        test.log(Status.INFO, "Terms and Conditions Required: " + isTermsAndConditionsRequired);

        Assert.assertTrue(isTermsAndConditionsRequired, "Required Terms and Services error message not displayed");
    }
	
    @Test
    public void testValidateTermsAndConditions() throws InterruptedException, TimeoutException {
        // Scroll to the checkbox and log the action
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);
        test.log(Status.INFO, "Scrolled to the Terms and Conditions checkbox.");

        // Uncheck the Terms and Conditions checkbox, log the action, and click continue
        enrollPageObject.uncheckTermsAndConditions();
        test.log(Status.INFO, "Unchecked the Terms and Conditions checkbox.");
        enrollPageObject.clickContinue();
        test.log(Status.INFO, "Clicked on the Continue button without checking Terms and Conditions.");

        // Initialize WebDriverWait with appropriate timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Verify error message when aria-invalid is true
        checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        String ariaInvalidBeforeCheck = checkbox.getAttribute("aria-invalid");
        test.log(Status.INFO, "Aria-invalid value before checking: " + ariaInvalidBeforeCheck);

        if ("true".equals(ariaInvalidBeforeCheck)) {
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(errorMessage.isDisplayed(), "Error message 'Please select Terms of Services' is not displayed when aria-invalid is true");
            test.log(Status.PASS, "Error message 'Please select Terms of Services' is correctly displayed.");
        }

        // Check the Terms and Conditions checkbox, log the action, and click continue
        enrollPageObject.checkTermsAndConditions();
        test.log(Status.INFO, "Checked the Terms and Conditions checkbox.");

        // Verify error message is not displayed when aria-invalid is false
        String ariaInvalidAfterCheck = checkbox.getAttribute("aria-invalid");
        test.log(Status.INFO, "Aria-invalid value after checking: " + ariaInvalidAfterCheck);

        if ("false".equals(ariaInvalidAfterCheck)) {
            // Wait for the error message to disappear
            boolean isErrorMessageInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[text()='Please select Terms of Services']")));
            Assert.assertTrue(isErrorMessageInvisible, "Error message 'Please select Terms of Services' is displayed when aria-invalid is false");
            test.log(Status.PASS, "Error message 'Please select Terms of Services' is correctly hidden.");
        }

        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEnroll")));
        continueButton.click();
        test.log(Status.INFO, "Clicked on the Continue button after checking Terms and Conditions.");
    }
    
    
    
    @Test
    public void testFirstNameLabelIsDisplayedAndCorrect() {
        test.log(Status.INFO, "Starting testFirstNameLabelIsDisplayedAndCorrect");

        // Validate that the First Name label is displayed
        boolean isDisplayed = enrollPageObject.isFirstNameLabelDisplayed();
        test.log(Status.INFO, "First Name label display status: " + isDisplayed);
        Assert.assertTrue(isDisplayed, "First Name label should be displayed on the page.");
        test.log(Status.PASS, "First Name label is displayed on the page.");

        // Validate that the First Name label text is correct
        String actualLabelText = enrollPageObject.getFirstNameLabelText();
        test.log(Status.INFO, "Actual First Name Label text: " + actualLabelText);
        
        String expectedLabelText = "First Name";
        Assert.assertEquals(actualLabelText, expectedLabelText, "The First Name label text does not match the expected value.");
        test.log(Status.PASS, "First Name label text matches the expected value.");
    }
    
    @Test
    public void testLastNameLabelIsDisplayedAndCorrect() {
        test.log(Status.INFO, "Starting testLastNameLabelIsDisplayedAndCorrect");

        // Validate that the Last Name label is displayed
        boolean isDisplayed = enrollPageObject.isLastNameLabelDisplayed();
        test.log(Status.INFO, "Last Name label display status: " + isDisplayed);
        Assert.assertTrue(isDisplayed, "Last Name label should be displayed on the page.");
        test.log(Status.PASS, "Last Name label is displayed on the page.");

        // Validate that the Last Name label text is correct
        String actualLabelText = enrollPageObject.getLastNameLabelText();
        test.log(Status.INFO, "Actual Last Name Label text: " + actualLabelText);
        
        String expectedLabelText = "Last Name";
        Assert.assertEquals(actualLabelText, expectedLabelText, "The Last Name label text does not match the expected value.");
        test.log(Status.PASS, "Last Name label text matches the expected value.");
    }

    @Test
    public void testEmailLabelIsDisplayedAndCorrect() {
        test.log(Status.INFO, "Starting testEmailLabelIsDisplayedAndCorrect");

        // Validate that the Email label is displayed
        boolean isDisplayed = enrollPageObject.isEmailAddressLabelDisplayed();
        test.log(Status.INFO, "Email label display status: " + isDisplayed);
        Assert.assertTrue(isDisplayed, "Email label should be displayed on the page.");
        test.log(Status.PASS, "Email label is displayed on the page.");

        // Validate that the Email label text is correct
        String actualLabelText = enrollPageObject.getEmailAddressLabelText();
        test.log(Status.INFO, "Actual Email Label text: " + actualLabelText);
        
        String expectedLabelText = "Email Address";
        Assert.assertEquals(actualLabelText, expectedLabelText, "The Email label text does not match the expected value.");
        test.log(Status.PASS, "Email label text matches the expected value.");
    }

    @Test
    public void testMobilePhoneLabelIsDisplayedAndCorrect() {
        test.log(Status.INFO, "Starting testMobilePhoneLabelIsDisplayedAndCorrect");

        // Validate that the Mobile Phone label is displayed
        boolean isDisplayed = enrollPageObject.isMobilePhoneLabelDisplayed();
        test.log(Status.INFO, "Mobile Phone label display status: " + isDisplayed);
        Assert.assertTrue(isDisplayed, "Mobile Phone label should be displayed on the page.");
        test.log(Status.PASS, "Mobile Phone label is displayed on the page.");

        // Validate that the Mobile Phone label text is correct
        String actualLabelText = enrollPageObject.getMobilePhoneLabelText();
        test.log(Status.INFO, "Actual Mobile Phone Label text: " + actualLabelText);
        
        String expectedLabelText = "Mobile Phone";
        Assert.assertEquals(actualLabelText, expectedLabelText, "The Mobile Phone label text does not match the expected value.");
        test.log(Status.PASS, "Mobile Phone label text matches the expected value.");
    }

    @Test
    public void testCreatePINLabelIsDisplayedAndCorrect() {
        test.log(Status.INFO, "Starting testCreatePINLabelIsDisplayedAndCorrect");

        // Validate that the Create PIN label is displayed
        boolean isDisplayed = enrollPageObject.isCreatePINLabelDisplayed();
        test.log(Status.INFO, "Create PIN label display status: " + isDisplayed);
        Assert.assertTrue(isDisplayed, "Create PIN label should be displayed on the page.");
        test.log(Status.PASS, "Create PIN label is displayed on the page.");

        // Validate that the Create PIN label text is correct
        String actualLabelText = enrollPageObject.getCreatePINLabelText();
        test.log(Status.INFO, "Actual Create PIN Label text: " + actualLabelText);
        
        String expectedLabelText = "Create PIN";
        Assert.assertEquals(actualLabelText.trim(), expectedLabelText, "The Create PIN label text does not match the expected value.");
        test.log(Status.PASS, "Create PIN label text matches the expected value.");
    }

    @Test
    public void testPinFieldWithToggle() throws InterruptedException {
        // Define the file path, sheet name
        String filePath = ".\\datafiles\\testdata.xlsx";
        String sheetName = "Enroll";

        // Fetch PIN data from Excel
        int rowNum = 1;
        String pin = ExcelUtility.getCellData(filePath, sheetName, "pin", rowNum);

        // Log the input data
        test.log(Status.INFO, "Enrollment data: PIN: " + pin);

        // Enter the PIN into the form
        enrollPageObject.enterCreatePIN(pin);
        test.log(Status.INFO, "Entered PIN: " + pin);

        // Validate the PIN is initially hidden
        boolean isPinHidden = enrollPageObject.isPinFieldHidden();
        test.log(Status.INFO, "PIN field is initially hidden: " + isPinHidden);
        Assert.assertTrue(isPinHidden, "PIN field should be hidden by default.");

        // Toggle the visibility to make PIN visible
        enrollPageObject.togglePinVisibility();
        boolean isPinVisible = enrollPageObject.isPinFieldVisible();
        test.log(Status.INFO, "PIN field visibility status after toggle: " + isPinVisible);
        Assert.assertTrue(isPinVisible, "PIN field should be visible after toggling.");

        // Toggle the visibility back to hidden
        enrollPageObject.togglePinVisibility();
        isPinHidden = enrollPageObject.isPinFieldHidden();
        test.log(Status.INFO, "PIN field visibility status after second toggle: " + isPinHidden);
        Assert.assertTrue(isPinHidden, "PIN field should be hidden after toggling back.");
    }
    
    @Test
    public void testCreatePinToolTipDisplayAndHide() throws TimeoutException, InterruptedException {
        test.log(Status.INFO, "Starting testCreatePinToolTipDisplayAndHide");

        // Scroll to Create PIN tooltip Icon
        WebElement createPinToolTipIcon = enrollPageObject.getCreatePinToolTip();
        helperObject.scrollToElement(createPinToolTipIcon);

        // Click on the tooltip icon to display the tooltip
        enrollPageObject.clickCreatePinToolTip();
        test.log(Status.INFO, "Clicked on Create PIN tooltip icon");

        // Wait for the tooltip to be displayed
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement tooltipElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='tooltip-inner'][contains(text(), 'PIN is 4 digits long')]")));
        Assert.assertNotNull(tooltipElement, "Tooltip should be displayed.");
        
        // Validate the tooltip text content
        String actualToolTipText = tooltipElement.getText();
        test.log(Status.INFO, "Actual Create PIN Tooltip text: " + actualToolTipText);

        String expectedToolTipText = "PIN is 4 digits long, non-consecutive and 3 unique digits";
        Assert.assertEquals(actualToolTipText, expectedToolTipText, "The Create PIN tooltip text does not match the expected value.");
        test.log(Status.PASS, "Create PIN tooltip text matches the expected value.");

        // Click outside the tooltip to hide it
        enrollPageObject.clickOutsideTooltip();
        test.log(Status.INFO, "Clicked outside the tooltip to hide it.");

        // Validate that the tooltip is no longer displayed
        boolean isToolTipHidden = enrollPageObject.isCreatePinToolTipHidden();
        test.log(Status.INFO, "Create PIN tooltip hidden status: " + isToolTipHidden);
        Assert.assertTrue(isToolTipHidden, "Create PIN tooltip should be hidden after clicking outside the tooltip.");
        test.log(Status.PASS, "Create PIN tooltip is hidden after clicking outside the tooltip.");
    }
    
	@Test
    public void testCombinedTermsAndConditionsText() throws SQLException, InterruptedException {
        test.log(Status.INFO, "Starting testCombinedTermsAndConditionsText");
        
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Query the database for the expected combined text
        String query = "select Terms_Checkbox_text from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127";
        List<String> result = DataBaseConnection.testWithDataBase(query);
        String expectedText = result.get(0).replace("{", "").replace("}", ""); // Remove curly braces
        test.log(Status.INFO, "Expected Terms and Conditions text: " + expectedText);

        // Wait for the Terms and Conditions text element to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[text()='I certify that I am at least 18 years old and that I agree to the ']")));
        
        // Get the actual text
        String actualText = enrollPageObject.termsAndConditionsbeforetext();
        test.log(Status.INFO, "Actual Terms and Conditions text: " + actualText);

        // Validate the Terms and Conditions text against the database value
        Assert.assertEquals(actualText, expectedText, "Terms and Conditions text does not match the expected value from the database");
        test.log(Status.PASS, "Terms and Conditions text matches the expected value.");
    }

    @Test
    public void testTermsAndConditionsBeforeTextIsDisplayed() {
        test.log(Status.INFO, "Starting testTermsAndConditionsBeforeTextIsDisplayed");
        
        boolean isDisplayed = enrollPageObject.termsAndConditionsbeforetexisDisplayed();
        Assert.assertTrue(isDisplayed, "Terms and Conditions before text is not displayed");
        test.log(Status.PASS, "Terms and Conditions before text is displayed.");
    }

    @Test
    public void testTermsAndConditionsLinkTextIsDisplayed() throws TimeoutException {
        test.log(Status.INFO, "Starting testTermsAndConditionsLinkTextIsDisplayed");
        
        // Initialize WebDriverWait with a timeout of 15 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Term and Conditions']"))).isDisplayed();
        Assert.assertTrue(isDisplayed, "Terms and Conditions link text is not displayed");
        test.log(Status.PASS, "Terms and Conditions link text is displayed.");
    }

    @Test
    public void testTermsAndConditionsLinkIsNotBroken() throws IOException, URISyntaxException {
        test.log(Status.INFO, "Starting testTermsAndConditionsLinkIsNotBroken");
        
        // Get the href attribute of the link
        String url = enrollPageObject.getTermsAndConditionsLinkHref();
        test.log(Status.INFO, "Terms and Conditions link URL: " + url);

        // Validate the URL by checking the HTTP response status code
        int statusCode = Helper.getHTTPResponseStatusCode(url);
        Assert.assertEquals(statusCode, 200, "The Terms and Conditions link is broken");
        test.log(Status.PASS, "Terms and Conditions link is valid with status code 200.");
    }

    @Test
    public void testTermsAndConditionsLinkOpensNewWindow() throws InterruptedException {
        test.log(Status.INFO, "Starting testTermsAndConditionsLinkOpensNewWindow");
        
        // Initialize WebDriverWait with a timeout of 30 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Store the current window handle
        String originalWindow = driver.getWindowHandle();

        // Scroll to the "Terms and Conditions" link element
        WebElement termsAndConditions = enrollPageObject.termsAndConditionsLinkText();
        helperObject.scrollToElement(termsAndConditions);
        test.log(Status.INFO, "Scrolled to the Terms and Conditions link.");

        // Ensure the element is visible and interactable
        wait.until(ExpectedConditions.elementToBeClickable(termsAndConditions));

        // Click the "Terms and Conditions" link
        enrollPageObject.clickTermsAndConditionsLink();
        test.log(Status.INFO, "Clicked on the Terms and Conditions link.");

        // Wait for the new window or tab to open
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        // Variable to store the new window handle
        String newWindowHandle = null;

        // Loop through all window handles to find the new window
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                newWindowHandle = windowHandle;
                driver.switchTo().window(newWindowHandle);
                break;
            }
        }

        // Ensure that a new window was found
        Assert.assertNotNull(newWindowHandle, "No new window was opened after clicking the Terms and Conditions link.");

        // Verify the URL of the new window
        String expectedUrl = "https://test.bimnetworks.com/user/terms/partner/127"; // Replace with the expected URL
        String actualUrl = driver.getCurrentUrl();
        Assert.assertEquals(actualUrl, expectedUrl, "The URL of the new window does not match the expected URL. Actual: " + actualUrl + ", Expected: " + expectedUrl);
        test.log(Status.PASS, "New window opened with the expected URL.");

        // Close the new window and switch back to the original window
        driver.close();
        driver.switchTo().window(originalWindow);
        test.log(Status.INFO, "Closed the new window and switched back to the original window.");
    }

    @Test
    public void testTextNotificationsTextIsDisplayed() throws TimeoutException, InterruptedException {
        test.log(Status.INFO, "Starting testTextNotificationsTextIsDisplayed");
        
        // Wait for the element to be visible
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[@class='form-check-label label--text-notification']"))).isDisplayed();
        
        // Perform the assertion
        Assert.assertTrue(isDisplayed, "Text Notifications text is not displayed");
        test.log(Status.PASS, "Text Notifications text is displayed.");
    }

    @Test
    public void testTextNotificationsLinkTextIsDisplayed() {
        test.log(Status.INFO, "Starting testTextNotificationsLinkTextIsDisplayed");
        
        boolean isDisplayed = enrollPageObject.isTextNotificationsLinkDisplayed();
        Assert.assertTrue(isDisplayed, "Text Notifications link text is not displayed");
        test.log(Status.PASS, "Text Notifications link text is displayed.");
    }

    @Test
    public void testTextNotificationsLinkIsNotBroken() throws IOException, URISyntaxException {
        test.log(Status.INFO, "Starting testTextNotificationsLinkIsNotBroken");
        
        // Get the href attribute of the link
        String url = enrollPageObject.getTextNotificationsLinkHref();
        test.log(Status.INFO, "Text Notifications link URL: " + url);

        // Validate the URL by checking the HTTP response status code
        int statusCode = Helper.getHTTPResponseStatusCode(url);
        Assert.assertEquals(statusCode, 200, "The Text Notifications link is broken");
        test.log(Status.PASS, "Text Notifications link is valid with status code 200.");
    }

    @Test
    public void testTextNotificationsLinkOpensNewWindow() throws InterruptedException {
        test.log(Status.INFO, "Starting testTextNotificationsLinkOpensNewWindow");
        
        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Store the current window handle
        String originalWindow = driver.getWindowHandle();

        // Scroll to the "Text Notifications" link element
        WebElement textNotifications = enrollPageObject.termsAndConditionsLinkText(); // Update this line to target the correct element for Text Notifications link
        helperObject.scrollToElement(textNotifications);

        // Click the "Text Notifications" link
        enrollPageObject.clickTextNotificationsLink();
        test.log(Status.INFO, "Clicked on the Text Notifications link.");

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
                test.log(Status.PASS, "New window opened with the expected URL.");

                // Close the new window and switch back to the original window
                driver.close();
                driver.switchTo().window(originalWindow);
                test.log(Status.INFO, "Closed the new window and switched back to the original window.");
                break;
            }
        }
    }

    @Test
    public void testTextBeforeSignInHereLink() throws InterruptedException {
        test.log(Status.INFO, "Starting testTextBeforeSignInHereLink");
        
        // Initialize WebDriverWait with a timeout of 30 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Wait for the element to be visible
        WebElement signInHereTextElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='text-muted mb-0 SingInHereText'])[1]")));

        // Extract the text and perform the assertion
        String actualText = signInHereTextElement.getText().substring(0, "To continue enrolling,".length()).trim();
        Assert.assertEquals(actualText, "To continue enrolling,");
        test.log(Status.PASS, "Sign in here text before link matches the expected text.");
    }

    @Test
    public void testSignInHereLinkTextisDisplayed() throws InterruptedException, TimeoutException {
        test.log(Status.INFO, "Starting testSignInHereLinkTextisDisplayed");
        
        // Initialize WebDriverWait with a longer timeout if necessary
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));

        // Ensure the element is visible before interacting
        WebElement signInLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//p[@class='text-muted mb-0 SingInHereText'])[1]")));
        boolean signInLinkDisplayed = signInLink.isDisplayed();

        assertTrue(signInLinkDisplayed, "Sign in here link is not displayed on the welcome page");
        test.log(Status.PASS, "Sign in here link is displayed.");
    }

    @Test
    public void validateFirstNamePlaceholder() {
        test.log(Status.INFO, "Starting validateFirstNamePlaceholder");
        
        String actualPlaceholder = enrollPageObject.firstNameValidatePlaceholder();
        String expectedPlaceholder = "First Name";
        
        test.log(Status.INFO, "Actual First Name Placeholder: " + actualPlaceholder);
        test.log(Status.INFO, "Expected First Name Placeholder: " + expectedPlaceholder);
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "First Name Placeholder text does not match!");
        test.log(Status.PASS, "First Name Placeholder text matches the expected value.");
    }

    @Test
    public void validateLastNamePlaceholder() {
        test.log(Status.INFO, "Starting validateLastNamePlaceholder");
        
        String actualPlaceholder = enrollPageObject.lastNameValidatePlaceholder();
        String expectedPlaceholder = "Last Name";
        
        test.log(Status.INFO, "Actual Last Name Placeholder: " + actualPlaceholder);
        test.log(Status.INFO, "Expected Last Name Placeholder: " + expectedPlaceholder);
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Placeholder text does not match!");
        test.log(Status.PASS, "Last Name Placeholder text matches the expected value.");
    }

    @Test
    public void validateEmailAddressPlaceholder() {
        test.log(Status.INFO, "Starting validateEmailAddressPlaceholder");
        
        String actualPlaceholder = enrollPageObject.emailValidatePlaceholder();
        String expectedPlaceholder = "Email Address";
        
        test.log(Status.INFO, "Actual Email Address Placeholder: " + actualPlaceholder);
        test.log(Status.INFO, "Expected Email Address Placeholder: " + expectedPlaceholder);
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Email Address Placeholder text does not match!");
        test.log(Status.PASS, "Email Address Placeholder text matches the expected value.");
    }

    @Test
    public void validateMobilePhonePlaceholder() {
        test.log(Status.INFO, "Starting validateMobilePhonePlaceholder");
        
        String actualPlaceholder = enrollPageObject.mobilePhoneValidatePlaceholder();
        String expectedPlaceholder = "Mobile Phone";
        
        test.log(Status.INFO, "Actual Mobile Phone Placeholder: " + actualPlaceholder);
        test.log(Status.INFO, "Expected Mobile Phone Placeholder: " + expectedPlaceholder);
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Mobile Phone Placeholder text does not match!");
        test.log(Status.PASS, "Mobile Phone Placeholder text matches the expected value.");
    }

    @Test
    public void validateCreatePINPlaceholder() {
        test.log(Status.INFO, "Starting validateCreatePINPlaceholder");
        
        String actualPlaceholder = enrollPageObject.pinValidatePlaceholder();
        String expectedPlaceholder = "Create PIN";
        
        test.log(Status.INFO, "Actual Create PIN Placeholder: " + actualPlaceholder);
        test.log(Status.INFO, "Expected Create PIN Placeholder: " + expectedPlaceholder);
        
        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Create PIN Placeholder text does not match!");
        test.log(Status.PASS, "Create PIN Placeholder text matches the expected value.");
    }
        
   @Test
    public void testContinueButtonText() throws Exception {
        String expectedText = "Continue";
        String actualText = enrollPageObject.ContinueButton();

        // Log the expected and actual button text
        test.log(Status.INFO, "Expected Continue Button Text: " + expectedText);
        test.log(Status.INFO, "Actual Continue Button Text: " + actualText);

        Assert.assertEquals(actualText, expectedText, "Continue button text does not match expected.");
    }
    
    @Test
    public void test_FooterTextisDisplayed() {
        boolean footertextDisplayed = enrollPageObject.footerTextisDisplayed();

        // Log the display status
        test.log(Status.INFO, "Footer Text Displayed: " + footertextDisplayed);

        assertTrue(footertextDisplayed, "Footer text is not displayed on the welcome page");
    }

    @Test
    public void test_FooterText() throws SQLException, InterruptedException {
        String query = "SELECT P1.Product_Name, P2.Partner_Contact_Number FROM [dbo].[Partner_BuyDirect_Settings] AS P1 INNER JOIN [dbo].[Partner_Profile] AS P2 ON P1.Partner_ID = P2.Partner_ID WHERE P1.Partner_ID = 127";
        List<String> data = DataBaseConnection.testWithDataBase(query);

        if (data.size() >= 2) {
            String partnerName = data.get(0);
            String contactNumber = data.get(1);
            String expectedFooterText = "Please contact the " + partnerName + " Support Team at " + contactNumber + " with any questions.";
            String actualFooterText = enrollPageObject.footerText();

            // Log the expected and actual footer text
            test.log(Status.INFO, "Expected Footer Text: " + expectedFooterText);
            test.log(Status.INFO, "Actual Footer Text: " + actualFooterText);

            Assert.assertEquals(actualFooterText, expectedFooterText, "Footer text does not match expected.");
        }
    }
    
    @Test(dependsOnMethods = {"validateMobilePhonePlaceholder"})
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
        test.log(Status.INFO, "Enrollment data: First Name: " + firstname + ", Last Name: " + lastname + ", Email: " + email + ", Phone: " + phone + ", PIN: " + pin);

        // Fill out the form
        enrollPageObject.enterFirstName(firstname);
        enrollPageObject.enterLastName(lastname);
        enrollPageObject.enterEmailAddress(email);
        enrollPageObject.enterMobilePhone(phone);
        enrollPageObject.enterCreatePIN(pin);
        test.log(Status.INFO, "Entered all valid enrollment input fields.");

        // Scroll and accept terms and conditions
        helperObject.scroll();
        enrollPageObject.checkTermsAndConditions();
        WebElement checkbox = enrollPageObject.getTermsAndConditionsCheckbox();
        helperObject.scrollToElement(checkbox);

        // Click the Continue button
        enrollPageObject.clickContinue();
        test.log(Status.INFO, "Clicked on the Continue button after entering valid data.");

        // Validate if the next page or section is loaded
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        
        // Replace 'uniqueElementOnNextPage' with an actual unique element that appears on the next page
        WebElement uniqueElementOnNextPage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("address")));

        // Log the presence of the unique element on the next page
        test.log(Status.INFO, "Unique element on the next page is displayed.");

        // Assert that the unique element on the next page is displayed
        Assert.assertTrue(uniqueElementOnNextPage.isDisplayed(), "Navigation to the next page failed. Unique element is not displayed.");
    }
    
    
}