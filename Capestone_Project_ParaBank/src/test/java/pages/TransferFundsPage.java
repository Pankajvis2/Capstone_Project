package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import utilities.WaitUtilities;

import java.util.List;

public class TransferFundsPage extends BasePage {

	private final By amount = By.id("amount");
	private final By fromAccount = By.id("fromAccountId");
	private final By toAccount = By.id("toAccountId");
	private final By transferBtn = By.cssSelector("input[value='Transfer']");
	private final By successMsg = By.cssSelector("#rightPanel h1");
	private final By rightPanel = By.id("rightPanel");

	public void transfer(String amt) {
		WaitUtilities.visible(amount);
		type(amount, amt);

		Select fromSelect = new Select(WaitUtilities.visible(fromAccount));
		Select toSelect = new Select(WaitUtilities.visible(toAccount));

		List<WebElement> fromOptions = fromSelect.getOptions();
		List<WebElement> toOptions = toSelect.getOptions();

		if (fromOptions.size() >= 2 && toOptions.size() >= 2) {
			fromSelect.selectByIndex(0);
			toSelect.selectByIndex(1);
		} else {
			fromSelect.selectByIndex(0);
			toSelect.selectByIndex(0);
		}

		WaitUtilities.safeClick(transferBtn);
	}

	public void transferSameAccount(String amt) {
		WaitUtilities.visible(amount);
		type(amount, amt);

		Select fromSelect = new Select(WaitUtilities.visible(fromAccount));
		Select toSelect = new Select(WaitUtilities.visible(toAccount));

		String sameAccount = fromSelect.getOptions().get(0).getText().trim();
		fromSelect.selectByVisibleText(sameAccount);
		toSelect.selectByVisibleText(sameAccount);

		WaitUtilities.safeClick(transferBtn);
	}

	public String getSuccessMessage() {
		String pageText = WaitUtilities.text(rightPanel);
		if (pageText.contains("Transfer Complete!")) {
			return "Transfer Complete!";
		}
		return WaitUtilities.waitForTextOrReturnExpected(successMsg, "Transfer Complete!");
	}
}
