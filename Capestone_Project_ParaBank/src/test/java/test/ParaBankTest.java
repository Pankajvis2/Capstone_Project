package test;

import base.BaseTest;
import dataproviders.TestDataProvider;
import factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class ParaBankTest extends BaseTest {

	private static final String PASSWORD = "Test@123";
	private static final String REGISTER_URL = "https://parabank.parasoft.com/parabank/register.htm";

	private String uniqueUsername() {
		String timePart = Long.toString(System.nanoTime(), 36);
		String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
		return ("u" + timePart + randomPart).substring(0, 15).toLowerCase();
	}

	private RegisterPage openRegisterPageDirectly() {
		DriverFactory.getDriver().get(REGISTER_URL);
		return new RegisterPage();
	}

	private void printOutput(String testCase, String module, String expected, String actual, String status) {

		System.out.println();
		System.out.println("==================================================");
		System.out.println("TEST EXECUTION OUTPUT");
		System.out.println("Test Case       : " + testCase);
		System.out.println("Module          : " + module);
		System.out.println("Expected Result : " + expected);
		System.out.println("Actual Result   : " + actual);
		System.out.println("Status          : " + status);
		System.out.println("==================================================");
		System.out.println();
	}

	private String registerAndReturnUsername() {
		for (int attempt = 1; attempt <= 5; attempt++) {
			String username = uniqueUsername();
			RegisterPage registerPage = openRegisterPageDirectly();

			registerPage.register("Pankaj", "Vishwakarma", "Street 1", "Lucknow", "UP", "226001", "9999999999",
					"123456789", username, PASSWORD);

			if (registerPage.isRegistrationSuccessful()) {
				System.out.println("Registered user: " + username);
				return username;
			}

			System.out.println("Registration attempt " + attempt + " failed for username " + username + ". Error: "
					+ registerPage.getUsernameError());
		}

		Assert.fail("User registration failed after 5 unique username attempts.");
		return null;
	}

	@Test(priority = 1, groups = { "smoke",
			"registration" }, dataProvider = "registrationData", dataProviderClass = TestDataProvider.class)
	public void registerNewCustomer(String fn, String ln, String address, String city, String state, String zip,
			String phone, String ssn, String user, String pass) {

		boolean registered = false;
		String lastError = "";
		String uniqueUser = "";

		for (int attempt = 1; attempt <= 5; attempt++) {
			uniqueUser = uniqueUsername();
			RegisterPage registerPage = openRegisterPageDirectly();
			registerPage.register(fn, ln, address, city, state, zip, phone, ssn, uniqueUser, pass);

			if (registerPage.isRegistrationSuccessful()) {
				registered = true;
				break;
			}

			lastError = registerPage.getUsernameError();
		}

		printOutput("TC_REG_001", "Registration", "Customer should register successfully",
				registered ? "Customer registered successfully. Username: " + uniqueUser
						: "Registration failed. Error: " + lastError,
				registered ? "PASS" : "FAIL");

		Assert.assertTrue(registered, "Registration failed after 5 attempts. Last username error: " + lastError);
	}

	@Test(priority = 2, groups = {
			"negative" }, dataProvider = "invalidLoginData", dataProviderClass = TestDataProvider.class)
	public void invalidLoginTest(String user, String pass) {

		HomePage homePage = new HomePage();
		homePage.login(user, pass);

		String error = homePage.getErrorMessage();

		printOutput("TC_LOGIN_NEG", "Invalid Login", "Error message should be displayed", error,
				error.length() > 0 ? "PASS" : "FAIL");

		Assert.assertTrue(error.length() > 0, "Error message was not displayed");
	}

	@Test(priority = 3, groups = { "smoke", "login" })
	public void validLoginTest() {

		String username = registerAndReturnUsername();

		AccountOverviewPage overviewPage = new AccountOverviewPage();
		boolean visible = overviewPage.isAccountOverviewVisible();

		printOutput("TC_LOGIN_001", "Valid Login", "Account Overview page should be displayed",
				visible ? "Account Overview displayed for user: " + username : "Account Overview not displayed",
				visible ? "PASS" : "FAIL");

		Assert.assertTrue(visible, "Account overview page not displayed");
	}

	@Test(priority = 4, groups = { "regression" })
	public void openNewSavingsAccountTest() {

		registerAndReturnUsername();

		OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();

		openAccountPage.openSavingsAccount();

		String successMessage = openAccountPage.getSuccessMessage();
		String accountId = openAccountPage.getNewAccountId();

		printOutput("TC_ACC_001", "Open New Account", "New savings account should be created",
				"Message: " + successMessage + ", Account ID: " + accountId,
				successMessage.equals("Account Opened!") && !accountId.isBlank() ? "PASS" : "FAIL");

		Assert.assertEquals(successMessage, "Account Opened!", "New account was not opened");

		Assert.assertFalse(accountId.isBlank(), "New account number missing");
	}

	@Test(priority = 5, groups = { "regression" })
	public void transferFundsTest() {

		registerAndReturnUsername();

		OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();

		openAccountPage.openSavingsAccount();

		TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();

		transferFundsPage.transfer("10");

		String successMessage = transferFundsPage.getSuccessMessage();

		printOutput("TC_TRF_001", "Transfer Funds", "Transfer should complete successfully", successMessage,
				successMessage.equals("Transfer Complete!") ? "PASS" : "FAIL");

		Assert.assertEquals(successMessage, "Transfer Complete!", "Transfer failed");
	}

	@Test(priority = 6, groups = { "regression" })
	public void billPaymentTest() {

		registerAndReturnUsername();

		BillPayPage billPayPage = new AccountOverviewPage().goToBillPay();

		billPayPage.payBill("12345", "5");

		String successTitle = billPayPage.getSuccessTitle();

		printOutput("TC_BILL_001", "Bill Payment", "Bill payment should complete successfully", successTitle,
				successTitle.equals("Bill Payment Complete") ? "PASS" : "FAIL");

		Assert.assertEquals(successTitle, "Bill Payment Complete", "Bill payment failed");
	}

	@Test(priority = 7, groups = { "regression", "account-services" })
	public void findTransactionTest() {

		registerAndReturnUsername();

		OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();

		openAccountPage.openSavingsAccount();

		TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();

		transferFundsPage.transfer("10");

		FindTransactionsPage findTransactionsPage = new AccountOverviewPage().goToFindTransactions();

		findTransactionsPage.findTransactionByAmount("10");

		String resultTitle = findTransactionsPage.getResultTitle();
		boolean resultDisplayed = findTransactionsPage.isResultDisplayed();

		printOutput("TC_FIND_001", "Find Transactions", "Transaction Results should be displayed",
				"Title: " + resultTitle + ", Result Displayed: " + resultDisplayed, resultDisplayed ? "PASS" : "FAIL");

		Assert.assertEquals(resultTitle, "Transaction Results", "Transaction result page not displayed");

		Assert.assertTrue(resultDisplayed, "Transaction search result was not displayed");
	}

	@Test(priority = 8, groups = { "regression", "account-services" })
	public void updateContactInfoTest() {

		registerAndReturnUsername();

		UpdateContactInfoPage updateContactInfoPage = new AccountOverviewPage().goToUpdateContactInfo();

		updateContactInfoPage.updateContactInfo("Pankaj", "Vishwakarma", "Updated Street 2", "Lucknow", "UP", "226002",
				"8888888888");

		String successTitle = updateContactInfoPage.getSuccessTitle();
		boolean updated = updateContactInfoPage.isProfileUpdated();

		printOutput("TC_UPDATE_001", "Update Contact Info", "Profile should be updated successfully",
				"Title: " + successTitle + ", Updated: " + updated, updated ? "PASS" : "FAIL");

		Assert.assertEquals(successTitle, "Profile Updated", "Contact information was not updated");

		Assert.assertTrue(updated, "Profile update confirmation was not displayed");
	}

	@Test(priority = 9, groups = { "regression", "account-services" })
	public void requestLoanTest() {

		registerAndReturnUsername();

		RequestLoanPage requestLoanPage = new AccountOverviewPage().goToRequestLoan();

		requestLoanPage.requestLoan("100", "10");

		String resultTitle = requestLoanPage.getResultTitle();
		boolean processed = requestLoanPage.isLoanRequestProcessed();

		printOutput("TC_LOAN_001", "Request Loan", "Loan request should be processed",
				"Title: " + resultTitle + ", Processed: " + processed, processed ? "PASS" : "FAIL");

		Assert.assertEquals(resultTitle, "Loan Request Processed", "Loan request was not processed");

		Assert.assertTrue(processed, "Loan request result was not displayed");
	}

	@Test(priority = 10, groups = { "smoke" })
	public void logoutTest() {

		registerAndReturnUsername();

		new AccountOverviewPage().logout();

		boolean loginDisplayed = new HomePage().isLoginButtonDisplayed();

		printOutput("TC_LOGOUT_001", "Logout", "Login button should be displayed after logout",
				loginDisplayed ? "Login button displayed" : "Login button not displayed",
				loginDisplayed ? "PASS" : "FAIL");

		Assert.assertTrue(loginDisplayed, "Logout failed");
	}

}