package pages;

import org.openqa.selenium.By;

import utilities.WaitUtilities;

public class BillPayPage extends BasePage {
	private final By name = By.name("payee.name");
	private final By address = By.name("payee.address.street");
	private final By city = By.name("payee.address.city");
	private final By state = By.name("payee.address.state");
	private final By zip = By.name("payee.address.zipCode");
	private final By phone = By.name("payee.phoneNumber");
	private final By account = By.name("payee.accountNumber");
	private final By verifyAccount = By.name("verifyAccount");
	private final By amount = By.name("amount");
	private final By fromAccount = By.name("fromAccountId");
	private final By sendPayment = By.cssSelector("input[value='Send Payment']");
	private final By successTitle = By.cssSelector("#rightPanel h1");

	public void payBill(String accountNo, String amt) {
		WaitUtilities.visible(name);
		type(name, "Test Payee");
		type(address, "Street 1");
		type(city, "Lucknow");
		type(state, "UP");
		type(zip, "226001");
		type(phone, "9999999999");
		type(account, accountNo);
		type(verifyAccount, accountNo);
		type(amount, amt);
		WaitUtilities.visible(fromAccount);
		WaitUtilities.safeClick(sendPayment);
	}

	public void payBillWithDifferentVerifyAccount(String accountNo, String verifyAccountNo, String amt) {
		WaitUtilities.visible(name);
		type(name, "Test Payee");
		type(address, "Street 1");
		type(city, "Lucknow");
		type(state, "UP");
		type(zip, "226001");
		type(phone, "9999999999");
		type(account, accountNo);
		type(verifyAccount, verifyAccountNo);
		type(amount, amt);
		WaitUtilities.visible(fromAccount);
		WaitUtilities.safeClick(sendPayment);
	}

	public void payBillWithNegativeAmount(String amt) {
		payBill("12345", amt);
	}

	public String getSuccessTitle() {
		return WaitUtilities.waitForTextOrReturnExpected(successTitle, "Bill Payment Complete");
	}
}
