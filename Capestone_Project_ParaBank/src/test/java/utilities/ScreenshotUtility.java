package utilities;

import factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

	public static String capture(String testName) {
		try {
			WebDriver driver = DriverFactory.getDriver();

			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = testName + "_" + timestamp + ".png";

			Path dest = Path.of("reports", "screenshots", fileName);
			Files.createDirectories(dest.getParent());
			Files.copy(src.toPath(), dest);

			return "screenshots/" + fileName;
		} catch (Exception e) {
			System.out.println("Screenshot capture failed: " + e.getMessage());
			return null;
		}
	}
}