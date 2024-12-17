package com.BuyDirect.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

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
        ExtentReportManager.logPass("Test passed: " + result.getMethod().getMethodName());
        ExtentReportManager.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.logFail("Test failed: " + result.getMethod().getMethodName());
        try {
            ExtentReportManager.captureScreenshot("Failure Screenshot");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExtentReportManager.flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.logFail("Test skipped: " + result.getMethod().getMethodName());
        ExtentReportManager.flush();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ExtentReportManager.logFail("Test failed but within success percentage: " + result.getMethod().getMethodName());
        ExtentReportManager.flush();
    }

    @Override
    public void onFinish(ITestContext context) {
        // Summary logic can be added here, based on context
        ExtentReportManager.flush();
    }
}
