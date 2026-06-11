package base;

import config.ConfigReader;
import factory.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("") String browser) {

        String selectedBrowser;

        if (browser == null || browser.trim().isEmpty()) {
            selectedBrowser = ConfigReader.get("browser");
        } else {
            selectedBrowser = browser;
        }

        DriverFactory.initDriver(selectedBrowser);

        DriverFactory.getDriver().manage().window().maximize();

        DriverFactory.getDriver().get(
                ConfigReader.get("baseUrl")
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (DriverFactory.getDriver() != null) {
            DriverFactory.quitDriver();
        }
    }
}