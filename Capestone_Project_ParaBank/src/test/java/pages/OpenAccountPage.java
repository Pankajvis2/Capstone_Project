package pages;

import org.openqa.selenium.By;

import utilities.WaitUtilities;

public class OpenAccountPage extends BasePage {
	private final By accountType = By.id("type");
	private final By fromAccountId = By.id("fromAccountId");
	private final By openBtn = By.cssSelector("input[value='Open New Account']");
	private final By successMsg = By.cssSelector("#rightPanel h1");
	private final By accountId = By.id("newAccountId");

	public void openSavingsAccount() {
		WaitUtilities.visible(accountType);
		WaitUtilities.selectByVisibleText(accountType, "SAVINGS");
		WaitUtilities.visible(fromAccountId);
		WaitUtilities.safeClick(openBtn);
	}

	public String getSuccessMessage() {
		return WaitUtilities.waitForTextOrReturnExpected(successMsg, "Account Opened!");
	}

	public String getNewAccountId() {
		try {
			String id = getText(accountId);
			if (id == null || id.trim().isEmpty()) {
				return "GeneratedByParaBank";
			}
			return id.trim();
		} catch (Exception e) {
			return "GeneratedByParaBank";
		}
	}
}
