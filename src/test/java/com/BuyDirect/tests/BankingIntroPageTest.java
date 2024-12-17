package com.BuyDirect.tests;

import java.sql.SQLException;
import java.time.Duration;
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

import com.BuyDirect.pageobjects.AddressPage;
import com.BuyDirect.pageobjects.BankingIntroPage;
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
public class BankingIntroPageTest extends BaseTest {

	private WelcomePage welcomePageObject;
    private EnrollPage enrollPageObject;
    private AddressPage addressPageObject;
    private Helper helperObject;
    private WebDriverWait wait;
    private ExtentTest test;
    
    @BeforeClass
    public void driverInitialization() {
        try {
            // Initialize page objects
            welcomePageObject = new WelcomePage(driver);
            addressPageObject = new AddressPage(driver);
            enrollPageObject = new EnrollPage(driver);
            new BankingIntroPage(driver);
            new Generics(driver);
            helperObject = new Helper(driver);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Initialize Extent Reports
            ExtentReportManager.init();

            // Create a new test instance
            ExtentReportManager.createTest("BankingIntroPage Test");

            // Retrieve the current ExtentTest instance
            test = ExtentReportManager.getTest();

            // Ensure ExtentTest is initialized before logging
            if (test == null) {
                throw new IllegalStateException("ExtentTest object not initialized.");
            }

            // Log information about initialized page objects
            test.log(Status.INFO, "Page objects initialized: WelcomePage, EnrollPage, AddressPage, BankingIntroPage, Generics, and Helper");

        } catch (Exception e) {
            // Handle and log any exceptions during initialization
            if (test != null) {
                test.log(Status.FAIL, "Failed to initialize driver or page objects: " + e.getMessage());
            }
            throw e; // Re-throw the exception after logging
        }
    }



	@Test
	public void clickOnWelcomePageContinueButton() throws InterruptedException {
	    welcomePageObject.ClickOnContinue();
	    // Log the action of clicking on the Continue button
	    test.log(Status.INFO, "Clicked on the Continue button on the Welcome Page.");
	}

	   @Test(dependsOnMethods = "clickOnWelcomePageContinueButton")
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
	
	@Test
	public void testValidAddressInputFields() throws InterruptedException, SQLException, TimeoutException {
		// Initialize WebDriverWait with a timeout of 30 seconds
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		// Database connection and logic to retrieve IsDLRequired
		String query = "SELECT IsDLRequired FROM [dbo].[Partner_BuyDirect_Settings] WHERE Partner_ID = 127";
		List<String> result = DataBaseConnection.testWithDataBase(query);
		int isDLRequired = Integer.parseInt(result.get(0));

		// Log the database query and retrieved result
		test.log(Status.INFO, "Database Query: " + query);
		test.log(Status.INFO, "IsDLRequired: " + isDLRequired);

		// Define the file path and sheet name for the Excel file
		String filePath = ".\\datafiles\\testdata.xlsx";
		String sheetName = "Address";

		// Fetch data for the specified row
		int rowNum = 1;
		String streetAddress = ExcelUtility.getCellData(filePath, sheetName, "streetaddress", rowNum);
		String suite = ExcelUtility.getCellData(filePath, sheetName, "suite", rowNum);
		String city = ExcelUtility.getCellData(filePath, sheetName, "city", rowNum);
		String state = ExcelUtility.getCellData(filePath, sheetName, "state", rowNum);
		String zipCode = ExcelUtility.getCellData(filePath, sheetName, "zipcode", rowNum);
		String dateOfBirth = ExcelUtility.getCellData(filePath, sheetName, "dateofbirth", rowNum);

		// Log the input data
		test.log(Status.INFO, "Enrollment data: Street Address: " + streetAddress + ", Suite: " + suite + ", City: "
				+ city + ", State: " + state + ", Zip Code: " + zipCode + ", Date of Birth: " + dateOfBirth);

		// Enter data into the address form
		addressPageObject.enterStreetAddress(streetAddress);
		addressPageObject.enterSuite(suite);
		addressPageObject.enterCity(city);
		addressPageObject.selectState(state);
		addressPageObject.enterZip(zipCode);

		// Conditionally enter date of birth if required
		if (isDLRequired == 1) {
			addressPageObject.enterDateOfBirth(dateOfBirth);
			test.log(Status.INFO, "Entered Date of Birth: " + dateOfBirth);
		} else {
			test.log(Status.INFO, "Date of Birth not required for this scenario.");
		}

		// Click the Continue button
		addressPageObject.clickContinue();
		test.log(Status.INFO, "Clicked on the Continue button after entering valid data.");

		WebElement uniqueElementOnNextPage = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[@class='text-center mb-0']")));
		Assert.assertTrue(uniqueElementOnNextPage.isDisplayed(), "Failed to navigate to the next page.");
		test.log(Status.INFO, "Successfully navigated to the next page.");
	}
	
	

}
