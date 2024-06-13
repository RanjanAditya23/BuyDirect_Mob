package org.BuyDirect_Android_utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;

public class ExtentTestNGListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.init();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.FAIL, "Test failed: " + result.getThrowable());
        captureAndAttachScreenshot(test);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }

    private void captureAndAttachScreenshot(ExtentTest test) {
        try {
            TakesScreenshot screenshotDriver = (TakesScreenshot) DriverManager.getDriver();
            byte[] screenshotBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
            String base64String = Base64.getEncoder().encodeToString(screenshotBytes);
            test.fail("Screenshot of the failure:",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64String).build());
        } catch (Exception e) {
            System.err.println("Failed to capture and attach screenshot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
