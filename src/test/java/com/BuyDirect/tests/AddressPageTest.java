package com.BuyDirect.tests;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.BuyDirect.pageobjects.AddressPage;
import com.BuyDirect.pageobjects.EnrollPage;
import com.BuyDirect.pageobjects.WelcomePage;
import com.BuyDirect.utils.DataBaseConnection;
import com.BuyDirect.listeners.ExtentReportManager;
import com.BuyDirect.utils.Generics;
import com.BuyDirect.utils.Helper;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


@Listeners(com.BuyDirect.listeners.ExtentTestNGListener.class)
public class AddressPageTest extends BaseTest {

	private AddressPage addressPageObject;
    private Generics genericsObject;
    private WelcomePage welcomePageObject;
    private WebDriverWait wait;
    private ExtentTest test;

    @BeforeClass
    public void driverInitialization() {
        // Initialize page objects
        welcomePageObject = new WelcomePage(driver);
        addressPageObject = new AddressPage(driver);
        new EnrollPage(driver);
        genericsObject = new Generics(driver);
        new Helper(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        // Initialize Extent Reports
        ExtentReportManager.init();

        // Create a new test instance
        ExtentReportManager.createTest("AddressPage Test");

        // Retrieve the current ExtentTest instance
        test = ExtentReportManager.getTest();

        // Ensure ExtentTest is initialized before logging
        if (test == null) {
            throw new IllegalStateException("ExtentTest object not initialized.");
        }
        
        test.log(Status.INFO, "Page objects initialized: WelcomePage, EnrollPage, AddressPage, Generics, and Helper");
    }
	
	@Test
	public void clickOnWelcomePageContinueButton() throws InterruptedException {
	    welcomePageObject.ClickOnContinue();
	    // Log the action of clicking on the Continue button
	    test.log(Status.INFO, "Clicked on the Continue button on the Welcome Page.");
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

	    @Test(dependsOnMethods = {"clickOnWelcomePageContinueButton"})
		public void testProgressIndicatorsOnAddressPage() throws SQLException, InterruptedException {
			// Initialize WebDriverWait with a timeout of 30 seconds
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			// Database connection and logic to retrieve IsDLRequired and
			// Is_Plastics_Page_Displayed
			String query = "SELECT IsDLRequired, Is_Plastics_Page_Displayed FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127";
			List<String> result = DataBaseConnection.testWithDataBase(query);
			int isDLRequired = Integer.parseInt(result.get(0));
			int isPlasticsPageDisplayed = Integer.parseInt(result.get(1));

			// Log the database query and retrieved results
			test.log(Status.INFO, "Database Query: " + query);
			test.log(Status.INFO,
					"IsDLRequired: " + isDLRequired + ", Is_Plastics_Page_Displayed: " + isPlasticsPageDisplayed);

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
		public void testAddressPageRequiredFieldErrorMessage() throws InterruptedException {
			wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddress")));
			addressPageObject.clickContinue();

			test.log(Status.INFO, "Clicked on the Continue button on the Address page.");

			// Validate that required field error messages are displayed and log the results
			boolean isStreetAddressRequired = addressPageObject.isStreetAddressRequiredMessageDisplayed();
			boolean isCityRequired = addressPageObject.isCityRequiredMessageDisplayed();
			boolean isStateRequired = addressPageObject.isStateRequiredMessageDisplayed();
			boolean isZipRequired = addressPageObject.isZipRequiredMessageDisplayed();
			boolean isDateofBirthRequired = addressPageObject.isDateofBirthRequiredMessageDisplayed();

			// Log the results for each required field
			test.log(Status.INFO, "Street Address Required: " + isStreetAddressRequired);
			test.log(Status.INFO, "City Required: " + isCityRequired);
			test.log(Status.INFO, "State Required: " + isStateRequired);
			test.log(Status.INFO, "Zip Code Required: " + isZipRequired);
			test.log(Status.INFO, "Date of Birth Required: " + isDateofBirthRequired);

			// Assert that all required field error messages are displayed
			Assert.assertTrue(isStreetAddressRequired, "Required Street Address error message not displayed");
			Assert.assertTrue(isCityRequired, "Required City error message not displayed");
			Assert.assertTrue(isStateRequired, "Required State error message not displayed");
			Assert.assertTrue(isZipRequired, "Required Zip Code error message not displayed");
			Assert.assertTrue(isDateofBirthRequired, "Required Date of Birth error message not displayed");
		}		

	    @Test
	    public void testContinueButtonText() throws Exception {
	        String expectedText = "Continue";
	        String actualText = addressPageObject.ContinueButton();

	        // Log the expected and actual button text
	        test.log(Status.INFO, "Expected Continue Button Text: " + expectedText);
	        test.log(Status.INFO, "Actual Continue Button Text: " + actualText);

	        Assert.assertEquals(actualText, expectedText, "Continue button text does not match expected.");
	    }

	    @Test
	    public void test_FooterTextisDisplayed() {
	        boolean footertextDisplayed = addressPageObject.footerTextisDisplayed();

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
	            String actualFooterText = addressPageObject.footerText();

	            // Log the expected and actual footer text
	            test.log(Status.INFO, "Expected Footer Text: " + expectedFooterText);
	            test.log(Status.INFO, "Actual Footer Text: " + actualFooterText);

	            Assert.assertEquals(actualFooterText, expectedFooterText, "Footer text does not match expected.");
	        }
	    }

	    @Test
	    public void testDobIconVisibility() throws InterruptedException, TimeoutException {
	        boolean isDobIconVisible = addressPageObject.isDobIconDisplayed();
	        test.log(Status.INFO, "Date of Birth icon visibility: " + isDobIconVisible);
	        Assert.assertTrue(isDobIconVisible, "Date of Birth icon should be visible on the input field.");
	        }	  

		@Test
		public void testStateDropdownOptions() {
			Select stateDropdown = new Select(addressPageObject.getStateDropdown());
			List<WebElement> options = stateDropdown.getOptions();
			List<String> actualStates = options.stream().map(WebElement::getText).collect(Collectors.toList());

			List<String> expectedStates = Arrays.asList("AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL",
					"GA", "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MI", "MN", "MO", "MS",
					"MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH", "OK", "OR", "PA", "RI", "SC", "SD",
					"TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY");

			test.log(Status.INFO, "Expected states: " + expectedStates);
			test.log(Status.INFO, "Actual states in dropdown: " + actualStates);

			Assert.assertTrue(actualStates.containsAll(expectedStates), "Some expected states are missing!");
			test.log(Status.PASS, "All expected states are present in the dropdown.");
		}

	    @Test
	    public void testCorrectStateSelected() {
	        String state = "NY";
	        addressPageObject.selectState(state);

	        String selectedState = addressPageObject.getSelectedDropdownValue();
	        test.log(Status.INFO, "Selected state: " + selectedState);

	        Assert.assertEquals(selectedState, state, "The selected state is incorrect!");
	        test.log(Status.PASS, "Correct state was selected and validated.");
	    }

	    @Test
	    public void testDropdownAfterReset() {
	    	addressPageObject.selectState("TX");
	        test.log(Status.INFO, "Selected state before reset: TX");

	        addressPageObject.resetForm();

	        String defaultState = addressPageObject.getSelectedDropdownValue();
	        test.log(Status.INFO, "State after form reset: " + defaultState);

	        Assert.assertEquals(defaultState, "Select a state", "Dropdown was not reset to default!");
	        test.log(Status.PASS, "Dropdown reset to default value after form reset.");
	    }

	    @Test
	    public void testDropdownKeyboardInput() {
	    	addressPageObject.getStateDropdown().sendKeys("C");

	        String selectedState = addressPageObject.getSelectedDropdownValue();
	        test.log(Status.INFO, "State selected after typing 'C': " + selectedState);

	        Assert.assertTrue(selectedState.startsWith("C"), "Dropdown did not jump to the correct state when typing!");
	        test.log(Status.PASS, "Dropdown jumped to the correct state when typing.");
	    }
	    
	    @Test
	    public void validateStreetAddressPlaceholder() {
	        test.log(Status.INFO, "Starting validateStreetAddressPlaceholder");
	        
	        String actualPlaceholder = addressPageObject.streetAddressValidatePlaceholder();
	        String expectedPlaceholder = "Street Address";
	        
	        test.log(Status.INFO, "Actual Street Address Placeholder: " + actualPlaceholder);
	        test.log(Status.INFO, "Expected Street Address Placeholder: " + expectedPlaceholder);
	        
	        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Street Address Placeholder text does not match!");
	        test.log(Status.PASS, "Street Address Placeholder text matches the expected value.");
	    }
	    
	    @Test
	    public void validateSuitePlaceholder() {
	        test.log(Status.INFO, "Starting validateSuitePlaceholder");
	        
	        String actualPlaceholder = addressPageObject.suitValidatePlaceholder();
	        String expectedPlaceholder = "Suite / Apartment";
	        
	        test.log(Status.INFO, "Actual Suite Placeholder: " + actualPlaceholder);
	        test.log(Status.INFO, "Expected Suite Placeholder: " + expectedPlaceholder);
	        
	        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Suite Placeholder text does not match!");
	        test.log(Status.PASS, "Suite Placeholder text matches the expected value.");
	    }

	    @Test
	    public void validateCityPlaceholder() {
	        test.log(Status.INFO, "Starting validateCityPlaceholder");
	        
	        String actualPlaceholder = addressPageObject.cityValidatePlaceholder();
	        String expectedPlaceholder = "City";
	        
	        test.log(Status.INFO, "Actual City Placeholder: " + actualPlaceholder);
	        test.log(Status.INFO, "Expected City Placeholder: " + expectedPlaceholder);
	        
	        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "City Placeholder text does not match!");
	        test.log(Status.PASS, "City Placeholder text matches the expected value.");
	    }

	    @Test
	    public void validateDateOfBirthPlaceholder() {
	        test.log(Status.INFO, "Starting validateDateOfBirthPlaceholder");
	        
	        String actualPlaceholder = addressPageObject.dateofbirthValidatePlaceholder();
	        String expectedPlaceholder = "Date of Birth";
	        
	        test.log(Status.INFO, "Actual Date of Birth Placeholder: " + actualPlaceholder);
	        test.log(Status.INFO, "Expected Date of Birth Placeholder: " + expectedPlaceholder);
	        
	        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Date of Birth Placeholder text does not match!");
	        test.log(Status.PASS, "Date of Birth Placeholder text matches the expected value.");
	    }

	    @Test
	    public void validateZipCodePlaceholder() {
	        test.log(Status.INFO, "Starting validateZipCodePlaceholder");
	        
	        String actualPlaceholder = addressPageObject.zipValidatePlaceholder();
	        String expectedPlaceholder = "Zip Code";
	        
	        test.log(Status.INFO, "Actual Zip Code Placeholder: " + actualPlaceholder);
	        test.log(Status.INFO, "Expected Zip Code Placeholder: " + expectedPlaceholder);
	        
	        Assert.assertEquals(actualPlaceholder, expectedPlaceholder, "Zip Code Placeholder text does not match!");
	        test.log(Status.PASS, "Zip Code Placeholder text matches the expected value.");
	    }
	    
	    @Test
	    public void testStateDropdownDefaultValue() {
	        String selectedValue = addressPageObject.getSelectedDropdownValue();
	        String expectedDefaultValue = "Select State";
	        test.log(Status.INFO, "State dropdown default value: " + selectedValue);
	        Assert.assertEquals(selectedValue, expectedDefaultValue, "State dropdown default value does not match.");
	        test.log(Status.PASS, "State dropdown default value is correctly set.");
	    }
	    
	    @Test
	    public void testDateOfBirthPickerRestrictionUIBased() throws InterruptedException {
	    	test.log(Status.INFO, "Starting testDateOfBirthPickerRestrictionUIBased");

	        // Open the date picker
	        addressPageObject.openDatePicker();
	        test.log(Status.INFO, "Opened Date of Birth picker.");

	        // Calculate a valid date (18 years ago)
	        LocalDate today = LocalDate.now();
	        LocalDate validDate = today.minusYears(18);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	        String validDateFormatted = validDate.format(formatter);

	        // Log the valid date to be selected
	        test.log(Status.INFO, "Valid date to be selected: " + validDateFormatted);

	        // Use UI-based selection to choose the valid date
	        addressPageObject.selectDateFromPicker(validDate);
	        test.log(Status.INFO, "Selected valid date (18 years ago) via date picker.");

	        // Validate that the selected date is correctly applied
	        String selectedDate = addressPageObject.getSelectedDateFromInputField();
	        test.log(Status.INFO, "Selected date in the input field: " + selectedDate);

	        // Assert that the selected date matches the valid date
	        Assert.assertEquals(selectedDate, validDateFormatted, "The selected date should match the valid date.");
	        test.log(Status.PASS, "Valid date (18 years ago) successfully selected and validated.");
	    }



	    
	
	
}
