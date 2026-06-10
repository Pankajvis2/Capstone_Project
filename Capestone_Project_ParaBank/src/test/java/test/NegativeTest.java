package test;

import base.BaseTest;
import dataproviders.TestDataProvider;
import factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class NegativeTest extends BaseTest {

	private static final String PASSWORD = "Test@123";
	private static final String REGISTER_URL = "https://parabank.parasoft.com/parabank/register.htm";

	private String uniqueUsername() {
		String randomPart = UUID.randomUUID().toString().replace("-", "");

		return ("bug" + randomPart).substring(0, 15).toLowerCase();
	}

	private RegisterPage openRegisterPageDirectly() {
		DriverFactory.getDriver().get(REGISTER_URL);
		return new RegisterPage();
	}

	private void registerUser(String firstName, String lastName, String address, String city, String state, String zip,
			String phone, String ssn, String password) {

		RegisterPage registerPage = openRegisterPageDirectly();

		String generatedUsername = uniqueUsername();

		registerPage.register(firstName, lastName, address, city, state, zip, phone, ssn, generatedUsername, password);

		Assert.assertTrue(registerPage.isRegistrationSuccessful(),
				"Precondition failed: user registration was not completed.");

		System.out.println("Registered negative test user: " + generatedUsername);
	}

	private void createSecondAccount() {

		OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();

		openAccountPage.openSavingsAccount();

		String successMessage = openAccountPage.getSuccessMessage();

		String newAccountId = openAccountPage.getNewAccountId();

		System.out.println("Second account creation message: " + successMessage);
		System.out.println("Second account id: " + newAccountId);

		Assert.assertEquals(successMessage, "Account Opened!", "Precondition failed: second account was not created.");
	}

	private void printBugOutput(String bugId, String module, String inputData, String expectedResult,
			String actualResult, String status) {

		System.out.println();
		System.out.println("==================================================");
		System.out.println("BUG EXECUTION OUTPUT");
		System.out.println("Bug ID          : " + bugId);
		System.out.println("Module          : " + module);
		System.out.println("Input Data      : " + inputData);
		System.out.println("Expected Result : " + expectedResult);
		System.out.println("Actual Result   : " + actualResult);
		System.out.println("Bug Status      : " + status);
		System.out.println("==================================================");
		System.out.println();
	}

	@Test(priority = 1, groups = { "negative", "bug",
			"transfer" }, dataProvider = "registrationData", dataProviderClass = TestDataProvider.class)
	public void bug001_transferFundsAllowsNegativeAmount(String firstName, String lastName, String address, String city,
			String state, String zip, String phone, String ssn, String username, String password) {

		registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

		createSecondAccount();

		TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();

		transferFundsPage.transfer("-100");

		String actualResult = transferFundsPage.getSuccessMessage();

		printBugOutput("PB-001", "Transfer Funds", "Amount = -100", "System should reject negative transfer amount.",
				actualResult, actualResult.equals("Transfer Complete!") ? "BUG REPRODUCED" : "BUG NOT REPRODUCED");

		Assert.assertEquals(actualResult, "Transfer Complete!",
				"BUG NOT REPRODUCED: Negative transfer amount was rejected.");
	}

	@Test(priority = 2, groups = { "negative", "bug",
			"billpay" }, dataProvider = "registrationData", dataProviderClass = TestDataProvider.class)
	public void bug002_billPaymentAllowsNegativeAmount(String firstName, String lastName, String address, String city,
			String state, String zip, String phone, String ssn, String username, String password) {

		registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

		BillPayPage billPayPage = new AccountOverviewPage().goToBillPay();

		billPayPage.payBillWithNegativeAmount("-50");

		String actualResult = billPayPage.getSuccessTitle();

		printBugOutput("PB-002", "Bill Payment", "Amount = -50", "System should reject negative bill payment amount.",
				actualResult, actualResult.equals("Bill Payment Complete") ? "BUG REPRODUCED" : "BUG NOT REPRODUCED");

		Assert.assertEquals(actualResult, "Bill Payment Complete",
				"BUG NOT REPRODUCED: Negative bill payment amount was rejected.");
	}

	@Test(priority = 3, groups = { "negative", "bug",
			"transfer" }, dataProvider = "registrationData", dataProviderClass = TestDataProvider.class)
	public void bug003_transferFundsAllowsSameSourceAndDestinationAccount(String firstName, String lastName,
			String address, String city, String state, String zip, String phone, String ssn, String username,
			String password) {

		registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

		TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();

		transferFundsPage.transferSameAccount("10");

		String actualResult = transferFundsPage.getSuccessMessage();

		printBugOutput("PB-003", "Transfer Funds",
				"From Account = Same Account, To Account = Same Account, Amount = 10",
				"System should block same source and destination account transfer.", actualResult,
				actualResult.equals("Transfer Complete!") ? "BUG REPRODUCED" : "BUG NOT REPRODUCED");

		Assert.assertEquals(actualResult, "Transfer Complete!",
				"BUG NOT REPRODUCED: Same account transfer was blocked.");
	}

	@Test(priority = 4, groups = { "negative", "bug",
			"loan" }, dataProvider = "registrationData", dataProviderClass = TestDataProvider.class)
	public void bug004_requestLoanAllowsNegativeDownPayment(String firstName, String lastName, String address,
			String city, String state, String zip, String phone, String ssn, String username, String password)
	{

		registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

		RequestLoanPage requestLoanPage = new AccountOverviewPage().goToRequestLoan();

		requestLoanPage.requestLoan("100", "-10");

		String actualResult = requestLoanPage.getResultTitle();

		printBugOutput("PB-004", "Request Loan", "Loan Amount = 100, Down Payment = -10",
				"System should reject negative down payment value.", actualResult,
				actualResult.equals("Loan Request Processed") ? "BUG REPRODUCED" : "BUG NOT REPRODUCED");

		Assert.assertEquals(actualResult, "Loan Request Processed",
				"BUG NOT REPRODUCED: Negative down payment was rejected.");
	}
}