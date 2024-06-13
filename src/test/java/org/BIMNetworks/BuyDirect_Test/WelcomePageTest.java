package org.BIMNetworks.BuyDirect_Test;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import org.BIMNetworks.BuyDirect_PageObject.WelcomePage;
import org.BuyDirect_Android_utils.DataBaseConnection;
import org.BuyDirect_Android_utils.Generics;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(org.BuyDirect_Android_utils.ExtentTestNGListener.class)
public class WelcomePageTest extends BaseTest {

    WelcomePage welcomePageObject;
    Generics genericsObject;
    

	@BeforeClass
	public void driverInitialization() {
		welcomePageObject = new WelcomePage(driver);
		genericsObject = new Generics(driver); 
	}

	@Test
	public void test_WelcomePageTitle() throws Exception {
		String expectedTitle = "Welcome";
		Assert.assertEquals(welcomePageObject.pagetitle(), expectedTitle, "Page title doesn't match expected.");
	}

	@Test
	public void test_CurrentURL() {
	    String expectedUrl = "https://bimhep-qa.bimnetworkstech.com/?partnerId=MTI3";
	    Assert.assertEquals(driver.getCurrentUrl(), expectedUrl, "URL mismatch: ");
	}

	@Test
	public void test_PartnerLogoisDisplayed() {
		boolean partnerLogoDisplayed = genericsObject.partnerLogoisDisplayed();
		assertTrue(partnerLogoDisplayed, "Partner Logo is not displayed on the welcome page");
	}
	
	@Test
	public void BrokenImageTest() throws URISyntaxException, IOException {

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
	public void test_PartnerLogo() throws SQLException {
		String query = "select Partner_Image_URL from [dbo].[Partner_Image] where Partner_ID = 127 and Partner_Image_ID = 1";
		Assert.assertEquals(genericsObject.partnerlogo(), DataBaseConnection.testWithDataBase(query).get(0));
	}

	@Test
	public void test_PartnerTenderNameisDisplayed() {
		boolean partnerTenderNameDisplayed = genericsObject.partnerTenderNameisDisplayed();
		assertTrue(partnerTenderNameDisplayed, "Partner Tender Name is not displayed on the welcome page");
	}

	@Test
	public void test_PartnerTenderNameHeader() throws SQLException {
		String query = "select Tender_Name_Header from [dbo].[Partner_BuyDirect_Settings] where Partner_ID = 127 and Tender_Name_Header = 'BIM Grocery Pay'";
		Assert.assertEquals(genericsObject.partnerTenderNameHeader(), DataBaseConnection.testWithDataBase(query).get(0));
	}

	@Test
	public void test_TitleisDisplayedCenter() {

	    WebElement logoElement = welcomePageObject.logoElement();
	    WebElement titleElement = welcomePageObject.titleElement();

	    // Get the location and size of the logo and title elements
	    int logoElementY = logoElement.getLocation().getY();
	    int logoElementWidth = logoElement.getSize().getWidth();
	    int titleElementX = titleElement.getLocation().getX();
	    int titleElementY = titleElement.getLocation().getY();

	    // Verify that the title text is displayed below the logo
	    Assert.assertTrue(titleElementY > logoElementY + logoElementWidth, "Title text is not displayed below the Tender Name");

	    // Verify that the title text is centered horizontally
	    int titleElementWidth = titleElement.getSize().getWidth();
	    int windowWidth = driver.manage().window().getSize().getWidth();
	    int centerWindowX = windowWidth / 2;
	    int titleElementCenterX = titleElementX + (titleElementWidth / 2);
	    Assert.assertEquals(centerWindowX, titleElementCenterX, "Title text is not centered horizontally");
	}

	@Test
	public void test_welcomeMessage1() throws SQLException {
		String query = "SELECT REPLACE(REPLACE(REPLACE(Welcome_Message,'<PartnerName>',Tender_Name_Body), '<PartnerPhone>', Partner_Contact_Number),'', '') AS WelcomeMessage FROM [dbo].[Partner_BuyDirect_Settings] PBS INNER JOIN [dbo].[Partner_Profile] PP ON PBS.Partner_ID = PP.Partner_ID WHERE PBS.Partner_ID = 127";
		Assert.assertEquals(welcomePageObject.dynamicWelcomeMessage1(), DataBaseConnection.testWithDataBase(query).get(0).split("\\|")[0].trim());
	}

	@Test
	public void test_welcomeMessage2() throws SQLException {
		String query = "SELECT REPLACE(REPLACE(REPLACE(Welcome_Message,'<PartnerName>',Tender_Name_Body), '<PartnerPhone>', Partner_Contact_Number),'', '') AS WelcomeMessage FROM [dbo].[Partner_BuyDirect_Settings] PBS INNER JOIN [dbo].[Partner_Profile] PP ON PBS.Partner_ID = PP.Partner_ID WHERE PBS.Partner_ID = 127";
		Assert.assertEquals(welcomePageObject.dynamicWelcomeMessage2(), DataBaseConnection.testWithDataBase(query).get(0).split("\\|")[1].trim());
	}

	@Test
	public void test_TextBeforeSignInHereLink() {
		String actualText = welcomePageObject.signInHereBeforeText().substring(0, "To continue enrolling,".length()).trim();
		Assert.assertEquals(actualText, "To continue enrolling,");
	}

	@Test
	public void test_SignInHereLinkTextisDisplayed() {
		boolean signInLinkDisplayed = welcomePageObject.signInHereTextisDisplayed();
		assertTrue(signInLinkDisplayed, "Sign in here link is not displayed on the welcome page");
	}

	@Test
	public void test_FooterTextisDisplayed() {
		boolean footertextDisplayed = genericsObject.footerTextisDisplayed();
		assertTrue(footertextDisplayed, "Footer text is not displayed on the welcome page");
	}

	@Test
	public void test_FooterText() throws SQLException {
		String query = "SELECT P1.Product_Name, P2.Partner_Contact_Number FROM [dbo].[Partner_BuyDirect_Settings] AS P1 INNER JOIN [dbo].[Partner_Profile] AS P2 ON P1.Partner_ID = P2.Partner_ID WHERE P1.Partner_ID = 127";

		// Fetching data from the database as a list
		List<String> data = DataBaseConnection.testWithDataBase(query);

		// Check if the list contains at least two elements
		if (data.size() >= 2) {
			String partnerName = data.get(0);
			String contactNumber = data.get(1);
			Assert.assertEquals(genericsObject.footerText(), "Please contact the " + partnerName + " Support Team at " + contactNumber + " with any questions.");
		}
	}

	@Test
	public void test_ContinueButtonText() throws Exception {
		Assert.assertEquals(welcomePageObject.ContinueButton(), "Begin");
	}

	@Test(dependsOnMethods = "test_welcomeMessage2")
	public void test_ContinueButton() throws InterruptedException {

		welcomePageObject.ClickOnContinue();
		assertTrue(welcomePageObject.EnrollIndicator().isDisplayed(), "User is not redirected to the enrollment page");
	}

}
