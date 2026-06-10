package base;

import config.ConfigReader;
import factory.DriverFactory;
import org.testng.annotations.*;

public class BaseTest {

	@Parameters("browser")
	@BeforeMethod(alwaysRun = true)
	public void setUp(@Optional("chrome") String browser) {
		String selectedBrowser = (browser == null || browser.trim().isEmpty()) ? ConfigReader.get("browser") : browser;

		DriverFactory.initDriver(selectedBrowser);
		DriverFactory.getDriver().get(ConfigReader.get("baseUrl"));
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		DriverFactory.quitDriver();
	}
}
