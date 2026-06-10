package factory;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverFactory {
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static void initDriver(String browser) {
		Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

		boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));
		String browserName = browser == null ? "chrome" : browser.toLowerCase().trim();

		switch (browserName) {
		case "safari":
			if (headless) {
				System.out.println("Safari does not support headless mode. Launching Safari in normal mode.");
			}
			driver.set(new SafariDriver());
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			if (headless) {
				firefoxOptions.addArguments("--headless");
			}
			driver.set(new FirefoxDriver(firefoxOptions));
			break;

		case "chrome":
		default:
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
			if (headless) {
				chromeOptions.addArguments("--headless=new");
			}
			chromeOptions.addArguments("--start-maximized");
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.addArguments("--remote-allow-origins=*");
			driver.set(new ChromeDriver(chromeOptions));
			break;
		}

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getInt("implicitWait")));
		getDriver().manage().window().maximize();
	}

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void quitDriver() {
		if (driver.get() != null) {
			driver.get().quit();
			driver.remove();
		}
	}
}
