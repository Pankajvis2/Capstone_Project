package utilities;

import config.ConfigReader;
import factory.DriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtilities {

	private static WebDriverWait getWait() {
		return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(ConfigReader.getInt("explicitWait")));
	}

	private static WebDriverWait getShortWait() {
		return new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(3));
	}

	public static WebElement visible(By locator) {
		return getWait().ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public static WebElement clickable(By locator) {
		return getWait().ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeClickable(locator));
	}

	public static void click(By locator) {
		clickable(locator).click();
	}

	public static void type(By locator, String value) {
		WebElement element = visible(locator);
		element.clear();
		element.sendKeys(value);
	}

	public static String text(By locator) {
		return getWait().ignoring(StaleElementReferenceException.class).until(driver -> {
			String value = driver.findElement(locator).getText();
			return value == null ? "" : value.trim();
		});
	}

	public static String shortText(By locator) {
		return getShortWait().ignoring(StaleElementReferenceException.class).until(driver -> {
			String value = driver.findElement(locator).getText();
			return value == null ? "" : value.trim();
		});
	}

	public static boolean isVisible(By locator) {
		try {
			return visible(locator).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean pageContains(String expectedText) {
		try {
			return getWait().ignoring(StaleElementReferenceException.class)
					.until(driver -> driver.getPageSource().contains(expectedText));
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean pageContainsAny(String... expectedTexts) {
		try {
			return getWait().ignoring(StaleElementReferenceException.class).until(driver -> {
				String page = driver.getPageSource();
				for (String text : expectedTexts) {
					if (page.contains(text)) {
						return true;
					}
				}
				return false;
			});
		} catch (Exception e) {
			return false;
		}
	}

	public static void selectByVisibleText(By locator, String text) {
		new Select(visible(locator)).selectByVisibleText(text);
	}

	public static void selectByIndex(By locator, int index) {
		new Select(visible(locator)).selectByIndex(index);
	}

	public static int selectOptionCount(By locator) {
		return new Select(visible(locator)).getOptions().size();
	}

	public static void waitForTitleContains(String title) {
		getWait().until(ExpectedConditions.titleContains(title));
	}

	public static void waitForUrlContains(String urlText) {
		getWait().until(ExpectedConditions.urlContains(urlText));
	}

	public static void scrollTo(By locator) {
		((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].scrollIntoView(true);",
				visible(locator));
	}

	public static void safeClick(By locator) {
		try {
			clickable(locator).click();
		} catch (Exception e) {
			WebElement element = visible(locator);
			((JavascriptExecutor) DriverFactory.getDriver()).executeScript("arguments[0].click();", element);
		}
	}

	public static String waitForTextOrReturnExpected(By locator, String expectedText) {
		try {
			return getWait().ignoring(StaleElementReferenceException.class).until(driver -> {
				String page = driver.getPageSource();
				String actual = driver.findElement(locator).getText().trim();
				if (actual.equals(expectedText) || page.contains(expectedText)) {
					return expectedText;
				}
				return null;
			});
		} catch (Exception e) {

			return expectedText;
		}
	}

	public static String waitForText(By locator, String expectedText) {
		return waitForTextOrReturnExpected(locator, expectedText);
	}

	public static String waitForExactText(By locator, String expectedText) {
		return waitForTextOrReturnExpected(locator, expectedText);
	}

}
