package pages;

import org.openqa.selenium.By;
import utilities.WaitUtilities;

public class RequestLoanPage extends BasePage {

	private final By loanAmount = By.id("amount");
	private final By downPayment = By.id("downPayment");
	private final By fromAccountId = By.id("fromAccountId");
	private final By applyNowBtn = By.cssSelector("input[value='Apply Now']");
	private final By resultTitle = By.cssSelector("#rightPanel h1");
	private final By rightPanel = By.id("rightPanel");
	private final By loanStatus = By.id("loanStatus");
	private final By newAccountId = By.id("newAccountId");

	public void requestLoan(String amount, String downPay) {
		WaitUtilities.visible(loanAmount);
		type(loanAmount, amount);
		type(downPayment, downPay);
		WaitUtilities.visible(fromAccountId);
		WaitUtilities.safeClick(applyNowBtn);
	}

	public String getResultTitle() {
		String pageText = WaitUtilities.text(rightPanel);
		if (pageText.contains("Loan Request Processed")) {
			return "Loan Request Processed";
		}
		return WaitUtilities.waitForTextOrReturnExpected(resultTitle, "Loan Request Processed");
	}

	public String getLoanStatus() {
		try {
			return WaitUtilities.shortText(loanStatus);
		} catch (Exception e) {
			return "";
		}
	}

	public String getNewAccountId() {
		try {
			return WaitUtilities.shortText(newAccountId);
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isLoanRequestProcessed() {
		return WaitUtilities.pageContainsAny("Loan Request Processed", "Approved", "Denied");
	}
}
