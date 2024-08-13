package org.BuyDirect_Android_utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {
	private static ExtentReports extent;
	private static ExtentSparkReporter sparkReporter;
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public static void init() {
		sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/ExtentReport.html");
		sparkReporter.config().setDocumentTitle("Automation Report");
		sparkReporter.config().setReportName("Functional Testing");
		sparkReporter.config().setTheme(Theme.STANDARD);

		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Host Name", "BimNetworks-BuyDirect_Mobile");
		extent.setSystemInfo("Environment", "UQA");
		extent.setSystemInfo("User Name", "Ranjan");
	}

	public static void logInfo(String message) {
		extentTest.get().log(Status.INFO, message);
	}

	public static void logPass(String message) {
		extentTest.get().log(Status.PASS, message);
	}

	public static void logFail(String message) {
		extentTest.get().log(Status.FAIL, message);
	}

	public static void createTest(String testName) {
		ExtentTest test = extent.createTest(testName);
		extentTest.set(test);
	}

	public static ExtentTest getTest() {
		return extentTest.get();
	}

	public static void flush() {
		extent.flush();
	}
}
