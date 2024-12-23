package com.BuyDirect.tests;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.BuyDirect.pageobjects.WelcomePage;
import com.BuyDirect.utils.DataBaseConnection;
import com.BuyDirect.listeners.ExtentReportManager;
import com.BuyDirect.utils.Generics;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

@Listeners(com.BuyDirect.listeners.ExtentTestNGListener.class)
public class WelcomePageTest extends BaseTest {

    private WelcomePage welcomePageObject;
    private Generics genericsObject;
    private ExtentTest test;

    @BeforeClass
    public void driverInitialization() {
        try {
            // Initialize Extent Reports
            ExtentReportManager.init();
            
            // Create a new test in Extent Reports
            ExtentReportManager.createTest("WelcomePage Tests");
            
            // Retrieve the current ExtentTest instance
            test = ExtentReportManager.getTest();

            // Ensure ExtentTest is initialized before logging
            if (test == null) {
                throw new IllegalStateException("ExtentTest object not initialized.");
            }
            
            // Initialize page objects
            welcomePageObject = new WelcomePage(driver);
            genericsObject = new Generics(driver);
            new WebDriverWait(driver, Duration.ofSeconds(30));
            
            // Log information about initialized page objects
            test.log(Status.INFO, "Page objects initialized: WelcomePage and Generics");

        } catch (Exception e) {
            // Handle and log any exceptions during initialization
            if (test != null) {
                test.log(Status.FAIL, "Failed to initialize driver or page objects: " + e.getMessage());
            }
            throw e; // Re-throw the exception after logging
        }
    }

    @Test
    public void testWelcomePageTitle() throws InterruptedException {
        String expectedTitle = "Welcome";        
        test.log(Status.INFO, "Expected Welcome Page Title: " + expectedTitle);       
        String actualTitle = welcomePageObject.pagetitle();        
        test.log(Status.INFO, "Actual Welcome Page Title: " + actualTitle);        
        try {
            Assert.assertEquals(actualTitle, expectedTitle, "Page title doesn't match expected.");        
            test.log(Status.PASS, "Welcome page title matched expected.");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Welcome page title did not match expected. " + e.getMessage());
            throw e; // Re-throw the exception after logging
        }
    }

    @Test
    public void testCurrentURL() {
        String expectedUrl = "https://uqa-va-buydirect.azurewebsites.net/?partnerId=MTI3";
        test.log(Status.INFO, "Expected URL: " + expectedUrl);
        String actualUrl = driver.getCurrentUrl();        
        test.log(Status.INFO, "Actual URL: " + actualUrl);
        try {
            Assert.assertEquals(actualUrl, expectedUrl, "URL mismatch: ");
            test.log(Status.PASS, "Current URL matched expected URL.");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Current URL did not match expected URL. " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void testPartnerLogoisDisplayed() {
        // Check if the partner logo is displayed
        boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();
        test.log(Status.INFO, "Partner logo displayed: " + partnerLogoDisplayed);
        try {
            assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page");
            test.log(Status.PASS, "Partner logo is displayed on the welcome page.");
        } catch (AssertionError e) {
            test.log(Status.FAIL, "Partner logo is not displayed on the welcome page. " + e.getMessage());
            throw e;
        }
    }
	
    @Test
    public void brokenImageTest() throws URISyntaxException, IOException {
        URL url = new URI(genericsObject.partnerlogo()).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();

        // Log the image URL and response code
        test.log(Status.INFO, "Image URL: " + genericsObject.partnerlogo());
        test.log(Status.INFO, "HTTP Response Code: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK) {
            test.log(Status.FAIL, "Broken Image: " + genericsObject.partnerlogo());
        }

        Assert.assertEquals(responseCode, HttpURLConnection.HTTP_OK, "Image is broken.");
    }

    @Test
    public void testPartnerLogo() throws SQLException {
        String query = "SELECT Partner_Image_URL FROM [dbo].[Partner_Image] WHERE Partner_ID = 127 AND Partner_Image_ID = 1";
        String expectedURL = DataBaseConnection.testWithDataBase(query).get(0);

        // Log the database query and expected URL
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Partner Logo URL: " + expectedURL);

        String actualURL = genericsObject.partnerlogo();

        // Log the actual URL
        test.log(Status.INFO, "Actual Partner Logo URL: " + actualURL);

        Assert.assertEquals(actualURL, expectedURL, "Partner Logo URL does not match the expected URL.");
    }

    @Test
    public void testPartnerTenderNameisDisplayed() {
        boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();

        // Log the display status
        test.log(Status.INFO, "Partner Tender Name Displayed: " + partnerTenderNameDisplayed);

        assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page");
    }

    @Test
    public void testPartnerTenderNameHeader() throws SQLException {
        String query = "SELECT Tender_Name_Header FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127 AND Tender_Name_Header = 'BIM Grocery Pay'";
        String expectedHeader = DataBaseConnection.testWithDataBase(query).get(0);

        // Log the database query and expected header
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Tender Name Header: " + expectedHeader);

        String actualHeader = genericsObject.partnerTenderNameHeader();

        // Log the actual header
        test.log(Status.INFO, "Actual Tender Name Header: " + actualHeader);

        Assert.assertEquals(actualHeader, expectedHeader, "Tender Name Header does not match the expected value.");
    }

    @Test
    public void testTitleisDisplayedCenter() {
        WebElement logoElement = welcomePageObject.logoElement();
        WebElement titleElement = welcomePageObject.titleElement();

        int logoElementY = logoElement.getLocation().getY();
        int logoElementWidth = logoElement.getSize().getWidth();
        int titleElementX = titleElement.getLocation().getX();
        int titleElementY = titleElement.getLocation().getY();
        int titleElementWidth = titleElement.getSize().getWidth();
        int windowWidth = driver.manage().window().getSize().getWidth();
        int centerWindowX = windowWidth / 2;
        int titleElementCenterX = titleElementX + (titleElementWidth / 2);

        // Log the relevant positions and sizes
        test.log(Status.INFO, "Logo Y Position: " + logoElementY);
        test.log(Status.INFO, "Title Y Position: " + titleElementY);
        test.log(Status.INFO, "Title Center X Position: " + titleElementCenterX);
        test.log(Status.INFO, "Window Center X Position: " + centerWindowX);

        // Assertions
        Assert.assertTrue(titleElementY > logoElementY + logoElementWidth, "Title text is not displayed below the Tender Name");
        Assert.assertEquals(centerWindowX, titleElementCenterX, "Title text is not centered horizontally");
    }

    @Test
    public void testWelcomeMessage1() throws SQLException {
        String query = "SELECT REPLACE(REPLACE(REPLACE(Welcome_Message,'<PartnerName>',Tender_Name_Body), '<PartnerPhone>', Partner_Contact_Number),'', '') AS WelcomeMessage FROM [dbo].[Partner_BuyDirect_Settings] PBS INNER JOIN [dbo].[Partner_Profile] PP ON PBS.Partner_ID = PP.Partner_ID WHERE PBS.Partner_ID = 127";
        String expectedMessage = DataBaseConnection.testWithDataBase(query).get(0).split("\\|")[0].trim();

        // Log the database query and expected message
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Welcome Message 1: " + expectedMessage);

        String actualMessage = welcomePageObject.dynamicWelcomeMessage1();

        // Log the actual message
        test.log(Status.INFO, "Actual Welcome Message 1: " + actualMessage);

        Assert.assertEquals(actualMessage, expectedMessage, "Welcome Message 1 does not match the expected value.");
    }

    @Test
    public void testWelcomeMessage2() throws SQLException {
        String query = "SELECT REPLACE(REPLACE(REPLACE(Welcome_Message,'<PartnerName>',Tender_Name_Body), '<PartnerPhone>', Partner_Contact_Number),'', '') AS WelcomeMessage FROM [dbo].[Partner_BuyDirect_Settings] PBS INNER JOIN [dbo].[Partner_Profile] PP ON PBS.Partner_ID = PP.Partner_ID WHERE PBS.Partner_ID = 127";
        String expectedMessage = DataBaseConnection.testWithDataBase(query).get(0).split("\\|")[1].trim();

        // Log the database query and expected message
        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Welcome Message 2: " + expectedMessage);

        String actualMessage = welcomePageObject.dynamicWelcomeMessage2();

        // Log the actual message
        test.log(Status.INFO, "Actual Welcome Message 2: " + actualMessage);

        Assert.assertEquals(actualMessage, expectedMessage, "Welcome Message 2 does not match the expected value.");
    }

    @Test
    public void testTextBeforeSignInHereLink() {
        String expectedText = "To continue enrolling,";
        String actualText = welcomePageObject.signInHereBeforeText().substring(0, expectedText.length()).trim();

        // Log the expected and actual texts
        test.log(Status.INFO, "Expected Text Before 'Sign in Here' Link: " + expectedText);
        test.log(Status.INFO, "Actual Text Before 'Sign in Here' Link: " + actualText);

        Assert.assertEquals(actualText, expectedText, "Text before 'Sign in Here' link does not match expected.");
    }

    @Test
    public void testSignInHereLinkTextisDisplayed() {
        boolean signInLinkDisplayed = welcomePageObject.signInHereTextisDisplayed();

        // Log the display status
        test.log(Status.INFO, "'Sign in Here' Link Displayed: " + signInLinkDisplayed);

        assertTrue(signInLinkDisplayed, "Sign in here link is not displayed on the welcome page");
    }

    @Test
    public void testContinueButtonText() {
        String expectedText = "Begin";
        String actualText = welcomePageObject.ContinueButton();

        // Log the expected and actual button text
        test.log(Status.INFO, "Expected Continue Button Text: " + expectedText);
        test.log(Status.INFO, "Actual Continue Button Text: " + actualText);

        Assert.assertEquals(actualText, expectedText, "Continue button text does not match expected.");
    }

    @Test
    public void test_FooterTextisDisplayed() {
        boolean footertextDisplayed = welcomePageObject.footerTextisDisplayed();

        // Log the display status
        test.log(Status.INFO, "Footer Text Displayed: " + footertextDisplayed);

        assertTrue(footertextDisplayed, "Footer text is not displayed on the welcome page");
    }

    @Test
    public void test_FooterText() throws SQLException {
        String query = "SELECT P1.Product_Name, P2.Partner_Contact_Number FROM [dbo].[Partner_BuyDirect_Settings] AS P1 INNER JOIN [dbo].[Partner_Profile] AS P2 ON P1.Partner_ID = P2.Partner_ID WHERE P1.Partner_ID = 127";
        List<String> data = DataBaseConnection.testWithDataBase(query);

        if (data.size() >= 2) {
            String partnerName = data.get(0);
            String contactNumber = data.get(1);
            String expectedFooterText = "Please contact the " + partnerName + " Support Team at " + contactNumber + " with any questions.";
            String actualFooterText = welcomePageObject.footerText();

            // Log the expected and actual footer text
            test.log(Status.INFO, "Expected Footer Text: " + expectedFooterText);
            test.log(Status.INFO, "Actual Footer Text: " + actualFooterText);

            Assert.assertEquals(actualFooterText, expectedFooterText, "Footer text does not match expected.");
        }
    }

    @Test(dependsOnMethods = "testWelcomeMessage2")
    public void testContinueButton() throws InterruptedException {
        welcomePageObject.ClickOnContinue();

        boolean enrollPageDisplayed = welcomePageObject.EnrollIndicator().isDisplayed();

        // Log the display status of the enrollment indicator
        test.log(Status.INFO, "Enrollment Page Displayed: " + enrollPageDisplayed);

        assertTrue(enrollPageDisplayed, "User is not redirected to the enrollment page");
    }
}
