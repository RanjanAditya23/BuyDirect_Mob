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

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.BuyDirect.listeners.ExtentReportManager;
import com.BuyDirect.pageobjects.EnrollPage;
import com.BuyDirect.pageobjects.SignInPage;
import com.BuyDirect.pageobjects.WelcomePage;
import com.BuyDirect.pageobjects.AddressPage; 

import com.BuyDirect.utils.DataBaseConnection;
import com.BuyDirect.utils.Generics;
import com.BuyDirect.utils.Helper;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

@Listeners(com.BuyDirect.listeners.ExtentTestNGListener.class)
public class SignInPageTest extends BaseTest {

    private EnrollPage enrollPageObject;
    private Generics genericsObject;
    private WelcomePage welcomePageObject;
    private Helper helperObject;
    private SignInPage signInPageObject;
    private AddressPage addressPageObject;  
    private WebDriverWait wait;
    private ExtentTest test;

    @BeforeClass
    public void driverInitialization() {
        // Initialize Extent Reports and create a new test
        ExtentReportManager.createTest("SignInPage Tests");

        // Get the current ExtentTest instance
        test = ExtentReportManager.getTest(); // Assign test to instance variable

        // Ensure ExtentTest is initialized before logging
        if (test == null) {
            throw new IllegalStateException("ExtentTest object not initialized.");
        }

        // Initialize page objects
        welcomePageObject = new WelcomePage(driver);
        enrollPageObject = new EnrollPage(driver);
        genericsObject = new Generics(driver);
        helperObject = new Helper(driver);
        signInPageObject = new SignInPage(driver);
        addressPageObject = new AddressPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Log page object initialization
        test.log(Status.INFO, "Page objects initialized: WelcomePage, EnrollPage, SignInPage, Generics, Helper, and AddressPage.");
    }

    @Test
    public void clickOnWelcomePageContinueButton() throws InterruptedException {
        welcomePageObject.ClickOnContinue();
        test.log(Status.INFO, "Clicked on the Continue button on the Welcome Page.");
    }

    @Test
    public void test_PartnerLogoisDisplayed() {
        boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();
        test.log(Status.INFO, "Partner Logo Displayed: " + partnerLogoDisplayed);
        Assert.assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page.");
    }

    @Test
    public void brokenImageTest() throws URISyntaxException, IOException {
        URL url = new URI(genericsObject.partnerlogo()).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();

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

        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Partner Logo URL: " + expectedURL);
        test.log(Status.INFO, "Actual Partner Logo URL: " + actualURL);

        Assert.assertEquals(actualURL, expectedURL, "Partner Logo URL does not match the expected URL.");
    }

    @Test
    public void testPartnerTenderNameisDisplayed() {
        boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();
        test.log(Status.INFO, "Partner Tender Name Displayed: " + partnerTenderNameDisplayed);
        Assert.assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page.");
    }

    @Test
    public void testPartnerTenderNameHeader() throws SQLException {
        String query = "SELECT Tender_Name_Header FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127 AND Tender_Name_Header = 'BIM Grocery Pay'";
        String expectedHeader = DataBaseConnection.testWithDataBase(query).get(0);
        String actualHeader = genericsObject.partnerTenderNameHeader();

        test.log(Status.INFO, "Database Query: " + query);
        test.log(Status.INFO, "Expected Tender Name Header: " + expectedHeader);
        test.log(Status.INFO, "Actual Tender Name Header: " + actualHeader);

        Assert.assertEquals(actualHeader, expectedHeader, "Tender Name Header does not match the expected value.");
    }

    @Test
    public void testContinueButtonText() throws Exception {
        String expectedText = "Continue";
        String actualText = addressPageObject.ContinueButton();

        test.log(Status.INFO, "Expected Continue Button Text: " + expectedText);
        test.log(Status.INFO, "Actual Continue Button Text: " + actualText);

        Assert.assertEquals(actualText, expectedText, "Continue button text does not match expected.");
    }

    @Test
    public void test_FooterTextisDisplayed() {
        boolean footertextDisplayed = signInPageObject.footerTextisDisplayed();
        test.log(Status.INFO, "Footer Text Displayed: " + footertextDisplayed);
        assertTrue(footertextDisplayed, "Footer text is not displayed on the welcome page.");
    }

    @Test
    public void test_FooterText() throws SQLException {
        String query = "SELECT P1.Product_Name, P2.Partner_Contact_Number FROM [dbo].[Partner_BuyDirect_Settings] AS P1 INNER JOIN [dbo].[Partner_Profile] AS P2 ON P1.Partner_ID = P2.Partner_ID WHERE P1.Partner_ID = 127";
        List<String> data = DataBaseConnection.testWithDataBase(query);

        if (data.size() >= 2) {
            String partnerName = data.get(0);
            String contactNumber = data.get(1);
            String expectedFooterText = "Please contact the " + partnerName + " Support Team at " + contactNumber + " with any questions.";
            String actualFooterText = signInPageObject.footerText();

            test.log(Status.INFO, "Expected Footer Text: " + expectedFooterText);
            test.log(Status.INFO, "Actual Footer Text: " + actualFooterText);

            Assert.assertEquals(actualFooterText, expectedFooterText, "Footer text does not match expected.");
        }
    }
    
    
}
