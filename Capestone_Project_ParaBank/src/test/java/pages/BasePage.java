package pages;

import factory.DriverFactory;
import utilities.WaitUtilities;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class BasePage {
    protected WebDriver driver = DriverFactory.getDriver();

    protected void click(By locator) { WaitUtilities.click(locator); }
    protected void type(By locator, String value) { WaitUtilities.type(locator, value); }
    protected String getText(By locator) { return WaitUtilities.text(locator); }
    protected boolean isDisplayed(By locator) {
        try { return WaitUtilities.visible(locator).isDisplayed(); } catch (Exception e) { return false; }
    }
    protected void moveTo(By locator) { new Actions(driver).moveToElement(WaitUtilities.visible(locator)).perform(); }
    protected void jsClick(By locator) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", WaitUtilities.visible(locator)); }
    protected String getTitle() { return driver.getTitle(); }
}
