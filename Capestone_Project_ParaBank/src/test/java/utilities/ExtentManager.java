package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getExtent() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter("reports/ParaBankExtentReport.html");
            reporter.config().setReportName("ParaBank Automation Report");
            reporter.config().setDocumentTitle("Selenium TestNG Report");
            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Project", "ParaBank Selenium TestNG Framework");
            extent.setSystemInfo("Tester", "Pankaj");
        }
        return extent;
    }
}
