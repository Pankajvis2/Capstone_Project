package listener;

import com.aventstack.extentreports.ExtentTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.ExtentManager;
import utilities.ScreenshotUtility;

public class TestListener implements ITestListener {

    private static final Logger log =
            LogManager.getLogger(TestListener.class);

    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        log.info("Test execution started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.getExtent().flush();
        log.info("Test execution finished: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest =
                ExtentManager.getExtent()
                        .createTest(result.getMethod().getMethodName());

        test.set(extentTest);

        test.get().info("Test Started");

        log.info("Test started: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");

        log.info("Test passed: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String testName = result.getMethod().getMethodName();

        test.get().fail(result.getThrowable());

        String screenshotPath = ScreenshotUtility.capture(testName);

        if (screenshotPath != null) {
            try {
                test.get().addScreenCaptureFromPath(screenshotPath);
                log.info("Screenshot attached: {}", screenshotPath);
            } catch (Exception e) {
                log.error("Screenshot attach failed: {}", e.getMessage());
            }
        }

        log.error("Test failed: {}", testName, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");

        if (result.getThrowable() != null) {
            test.get().skip(result.getThrowable());
        }

        log.warn("Test skipped: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Test failed but within success percentage: {}",
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        test.get().fail("Test Failed Due To Timeout");

        if (result.getThrowable() != null) {
            test.get().fail(result.getThrowable());
        }

        String screenshotPath = ScreenshotUtility.capture(testName);

        if (screenshotPath != null) {
            try {
                test.get().addScreenCaptureFromPath(screenshotPath);
                log.info("Screenshot attached for timeout test: {}", screenshotPath);
            } catch (Exception e) {
                log.error("Unable to attach screenshot: {}", testName, e);
            }
        }

        log.error("Test failed due to timeout: {}", testName, result.getThrowable());
    }
}