package com.BuyDirect.listeners;

import com.BuyDirect.utils.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentSparkReporter sparkReporter;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static final String REPORTS_DIR_PATH = System.getProperty("user.dir") + "/test-output/Extent Reports";
    private static final int DAYS_TO_KEEP_REPORTS = 7; // Retain reports for 7 days

    // Initialize the ExtentReports and SparkReporter
    public static synchronized void init() {
        if (extent == null) {
            try {
                // Create Extent Reports directory if it doesn't exist
                File reportsDir = new File(REPORTS_DIR_PATH);
                if (!reportsDir.exists()) {
                    if (reportsDir.mkdirs()) {
                        System.out.println("Created directory: " + REPORTS_DIR_PATH);
                    } else {
                        System.err.println("Failed to create directory: " + REPORTS_DIR_PATH);
                    }
                }

                // Generate report file name with date and time
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String reportPath = REPORTS_DIR_PATH + "/ExtentReport_" + timestamp + ".html";
                System.out.println("Report path: " + reportPath);

                // Initialize SparkReporter
                sparkReporter = new ExtentSparkReporter(reportPath);
                sparkReporter.config().setDocumentTitle("Automation Test Report");
                sparkReporter.config().setReportName("Regression Test Execution");
                sparkReporter.config().setTheme(Theme.DARK); // Dark theme for better readability
                sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss"); // Better timestamp format
                sparkReporter.config().setEncoding("UTF-8"); // Correct encoding

                // Initialize ExtentReports
                extent = new ExtentReports();
                extent.attachReporter(sparkReporter);
                extent.setSystemInfo("Host Name", "BimNetworks-BuyDirect_Mobile");
                extent.setSystemInfo("Environment", "UQA");
                extent.setSystemInfo("User Name", "Ranjan");
                extent.setSystemInfo("OS", System.getProperty("os.name"));
                extent.setSystemInfo("Java Version", System.getProperty("java.version"));
                extent.setSystemInfo("Browser", "Chrome 91");
                extent.setSystemInfo("Execution Mode", "Headless");

                System.out.println("ExtentReports initialized successfully.");
                
                // Cleanup old reports
                cleanupOldReports();

            } catch (Exception e) {
                System.err.println("Failed to initialize ExtentReports: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("ExtentReports is already initialized.");
        }
    }

    // Cleanup reports older than the specified number of days
    private static void cleanupOldReports() {
        try (Stream<Path> files = Files.list(Paths.get(REPORTS_DIR_PATH))) {
            files.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".html"))
                 .forEach(path -> {
                     File file = path.toFile();
                     long diffInMillis = new Date().getTime() - file.lastModified();
                     long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
                     if (diffInDays > DAYS_TO_KEEP_REPORTS) {
                         if (file.delete()) {
                             System.out.println("Deleted old report: " + file.getName());
                         } else {
                             System.err.println("Failed to delete old report: " + file.getName());
                         }
                     }
                 });
        } catch (IOException e) {
            System.err.println("Error during old report cleanup: " + e.getMessage());
        }
    }

    // Create a new ExtentTest and set it in the ThreadLocal
    public static void createTest(String testName) {
        if (extent != null) {
            ExtentTest test = extent.createTest(testName);
            extentTest.set(test);
            logStartTime();
            System.out.println("Test created: " + testName);
        } else {
            System.err.println("ExtentReports is not initialized. Call init() first.");
        }
    }

    // Get the current ExtentTest instance from the ThreadLocal
    public static ExtentTest getTest() {
        ExtentTest test = extentTest.get();
        if (test == null) {
            System.err.println("No ExtentTest instance found.");
        }
        return test;
    }

    // Log information to the current ExtentTest instance
    public static void logInfo(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, message);
        }
    }

    // Log step-level details
    public static void logStep(String stepDescription) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.INFO, stepDescription);
        }
    }

    // Log pass status to the current ExtentTest instance
    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }

    // Log fail status to the current ExtentTest instance
    public static void logFail(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }

    // Log retry status
    public static void logRetry(String retryMessage) {
        ExtentTest test = getTest();
        if (test != null) {
            test.log(Status.WARNING, retryMessage);
        }
    }

    // Log screenshot to the report
    public static void logScreenshot(String screenshotPath) throws IOException {
        ExtentTest test = getTest();
        if (test != null) {
            test.addScreenCaptureFromPath(screenshotPath, "Screenshot");
            test.log(Status.INFO, "Screenshot captured: " + screenshotPath);
        }
    }

    // Capture and log screenshot at any point
    public static void captureScreenshot(String stepDescription) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                TakesScreenshot screenshotDriver = (TakesScreenshot) DriverManager.getDriver();
                byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
                String base64String = Base64.getEncoder().encodeToString(screenshotBytes);
                test.info(stepDescription, MediaEntityBuilder.createScreenCaptureFromBase64String(base64String).build());
            } catch (Exception e) {
                test.log(Status.WARNING, "Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    // Dynamically set system info
    public static void setSystemInfo(String key, String value) {
        if (extent != null) {
            extent.setSystemInfo(key, value);
            System.out.println("System info set: " + key + " = " + value);
        }
    }

    // Log start and end times for the test
    public static void logStartTime() {
        ExtentTest test = getTest();
        if (test != null) {
            String timestamp = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date());
            test.log(Status.INFO, "Test started at: " + timestamp);
        }
    }

    public static void logEndTime() {
        ExtentTest test = getTest();
        if (test != null) {
            String timestamp = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss").format(new Date());
            test.log(Status.INFO, "Test ended at: " + timestamp);
        }
    }

    // Add execution summary to the report
    public static void addExecutionSummary(int totalTests, int passedTests, int failedTests, long executionTimeMillis) {
        if (extent != null) {
            extent.setSystemInfo("Total Tests", String.valueOf(totalTests));
            extent.setSystemInfo("Passed", String.valueOf(passedTests));
            extent.setSystemInfo("Failed", String.valueOf(failedTests));
            extent.setSystemInfo("Execution Time", (executionTimeMillis / 1000) + " seconds");
        }
    }

    // Flush the reports at the end of the test execution
    public static void flush() {
        logEndTime();
        if (extent != null) {
            extent.flush();
            System.out.println("ExtentReports flushed to disk.");
        }
    }
}
