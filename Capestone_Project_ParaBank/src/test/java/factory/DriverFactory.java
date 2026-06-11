package factory;

import config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Dimension;
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

        String browserName;

        if (browser == null || browser.trim().isEmpty()) {
            browserName = ConfigReader.get("browser");
        } else {
            browserName = browser;
        }

        browserName = browserName.toLowerCase().trim();

        switch (browserName) {

            case "safari":

                if (headless) {
                    System.out.println(
                            "Safari does not support headless mode. Launching Safari normally.");
                }

                driver.set(new SafariDriver());
                break;

            case "firefox":

                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (headless || isRunningInsideDocker()) {
                    firefoxOptions.addArguments("--headless");
                }

                driver.set(new FirefoxDriver(firefoxOptions));
                break;

            case "chrome":
            default:

                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();

                if (headless || isRunningInsideDocker()) {
                    chromeOptions.addArguments("--headless=new");
                }

                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");

                driver.set(new ChromeDriver(chromeOptions));
                break;
        }

        getDriver().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicitWait"))
        );

        if (!isRunningInsideDocker()) {
            getDriver().manage().window().maximize();
        } else {
            getDriver().manage().window().setSize(new Dimension(1920, 1080));
        }
    }

    private static boolean isRunningInsideDocker() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
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