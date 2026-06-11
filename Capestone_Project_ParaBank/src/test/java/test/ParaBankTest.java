package test;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.TestDataProvider;
import factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class ParaBankTest extends BaseTest {

    private String uniqueUsername() {
        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10);

        return ("user" + randomPart).toLowerCase();
    }

    private RegisterPage openRegisterPageDirectly() {
        DriverFactory.getDriver().get(
                ConfigReader.get("registerUrl")
        );
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

    private String registerAndReturnUsername(String fn, String ln, String address, String city,
                                             String state, String zip, String phone, String ssn,
                                             String pass) {

        for (int attempt = 1; attempt <= 5; attempt++) {

            String username = uniqueUsername();
            RegisterPage registerPage = openRegisterPageDirectly();

            registerPage.register(fn, ln, address, city, state, zip, phone, ssn, username, pass);

            if (registerPage.isRegistrationSuccessful()) {
                System.out.println("Registered user: " + username);
                return username;
            }

            System.out.println(
                    "Registration attempt " + attempt +
                            " failed for username " + username +
                            ". Error: " + registerPage.getUsernameError()
            );
        }

        Assert.fail("User registration failed after 5 unique username attempts.");
        return null;
    }

    @Test(priority = 1, groups = {"smoke", "registration"},
            dataProvider = "registrationData",
            dataProviderClass = TestDataProvider.class)
    public void registerNewCustomer(String fn, String ln, String address, String city,
                                    String state, String zip, String phone, String ssn,
                                    String user, String pass) {

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

        printOutput(
                "TC_REG_001",
                "Registration",
                "Customer should register successfully",
                registered
                        ? "Customer registered successfully. Username: " + uniqueUser
                        : "Registration failed. Error: " + lastError,
                registered ? "PASS" : "FAIL"
        );

        Assert.assertTrue(
                registered,
                "Registration failed after 5 attempts. Last username error: " + lastError
        );
    }

    @Test(priority = 2, groups = {"negative"},
            dataProvider = "invalidLoginData",
            dataProviderClass = TestDataProvider.class)
    public void invalidLoginTest(String user, String pass) {

        HomePage homePage = new HomePage();
        homePage.login(user, pass);

        String error = homePage.getErrorMessage();

        printOutput(
                "TC_LOGIN_NEG",
                "Invalid Login",
                "Error message should be displayed",
                error,
                error.length() > 0 ? "PASS" : "FAIL"
        );

        Assert.assertTrue(error.length() > 0, "Error message was not displayed");
    }

    @Test(priority = 3, groups = {"smoke", "login"},
            dataProvider = "registrationData",
            dataProviderClass = TestDataProvider.class)
    public void validLoginTest(String fn, String ln, String address, String city,
                               String state, String zip, String phone, String ssn,
                               String user, String pass) {

        String username = registerAndReturnUsername(
                fn, ln, address, city, state, zip, phone, ssn, pass
        );

        AccountOverviewPage overviewPage = new AccountOverviewPage();
        boolean visible = overviewPage.isAccountOverviewVisible();

        printOutput(
                "TC_LOGIN_001",
                "Valid Login",
                "Account Overview page should be displayed",
                visible
                        ? "Account Overview displayed for user: " + username
                        : "Account Overview not displayed",
                visible ? "PASS" : "FAIL"
        );

        Assert.assertTrue(visible, "Account overview page not displayed");
    }

    @Test(priority = 4, groups = {"regression"},
            dataProvider = "registrationData",
            dataProviderClass = TestDataProvider.class)
    public void openNewSavingsAccountTest(String fn, String ln, String address, String city,
                                          String state, String zip, String phone, String ssn,
                                          String user, String pass) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();
        openAccountPage.openSavingsAccount();

        String successMessage = openAccountPage.getSuccessMessage();
        String accountId = openAccountPage.getNewAccountId();

        printOutput(
                "TC_ACC_001",
                "Open New Account",
                "New savings account should be created",
                "Message: " + successMessage + ", Account ID: " + accountId,
                successMessage.equals("Account Opened!") && !accountId.isBlank() ? "PASS" : "FAIL"
        );

        Assert.assertEquals(successMessage, "Account Opened!", "New account was not opened");
        Assert.assertFalse(accountId.isBlank(), "New account number missing");
    }

    @Test(priority = 5, groups = {"regression"},
            dataProvider = "transferFundsData",
            dataProviderClass = TestDataProvider.class)
    public void transferFundsTest(String fn, String ln, String address, String city,
                                  String state, String zip, String phone, String ssn,
                                  String user, String pass, String amount) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();
        openAccountPage.openSavingsAccount();

        TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();
        transferFundsPage.transfer(amount);

        String successMessage = transferFundsPage.getSuccessMessage();

        printOutput(
                "TC_TRF_001",
                "Transfer Funds",
                "Transfer should complete successfully",
                successMessage,
                successMessage.equals("Transfer Complete!") ? "PASS" : "FAIL"
        );

        Assert.assertEquals(successMessage, "Transfer Complete!", "Transfer failed");
    }

    @Test(priority = 6, groups = {"regression"},
            dataProvider = "billPayData",
            dataProviderClass = TestDataProvider.class)
    public void billPaymentTest(String fn, String ln, String address, String city,
                                String state, String zip, String phone, String ssn,
                                String user, String pass, String accountNumber, String amount) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        BillPayPage billPayPage = new AccountOverviewPage().goToBillPay();
        billPayPage.payBill(accountNumber, amount);

        String successTitle = billPayPage.getSuccessTitle();

        printOutput(
                "TC_BILL_001",
                "Bill Payment",
                "Bill payment should complete successfully",
                successTitle,
                successTitle.equals("Bill Payment Complete") ? "PASS" : "FAIL"
        );

        Assert.assertEquals(successTitle, "Bill Payment Complete", "Bill payment failed");
    }

    @Test(priority = 7, groups = {"regression", "account-services"},
            dataProvider = "findTransactionData",
            dataProviderClass = TestDataProvider.class)
    public void findTransactionTest(String fn, String ln, String address, String city,
                                    String state, String zip, String phone, String ssn,
                                    String user, String pass, String amount) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        OpenAccountPage openAccountPage = new AccountOverviewPage().goToOpenNewAccount();
        openAccountPage.openSavingsAccount();

        TransferFundsPage transferFundsPage = new AccountOverviewPage().goToTransferFunds();
        transferFundsPage.transfer(amount);

        FindTransactionsPage findTransactionsPage = new AccountOverviewPage().goToFindTransactions();
        findTransactionsPage.findTransactionByAmount(amount);

        String resultTitle = findTransactionsPage.getResultTitle();
        boolean resultDisplayed = findTransactionsPage.isResultDisplayed();

        printOutput(
                "TC_FIND_001",
                "Find Transactions",
                "Transaction Results should be displayed",
                "Title: " + resultTitle + ", Result Displayed: " + resultDisplayed,
                resultDisplayed ? "PASS" : "FAIL"
        );

        Assert.assertEquals(resultTitle, "Transaction Results", "Transaction result page not displayed");
        Assert.assertTrue(resultDisplayed, "Transaction search result was not displayed");
    }

    @Test(priority = 8, groups = {"regression", "account-services"},
            dataProvider = "updateContactData",
            dataProviderClass = TestDataProvider.class)
    public void updateContactInfoTest(String fn, String ln, String address, String city,
                                      String state, String zip, String phone, String ssn,
                                      String user, String pass,
                                      String updatedFirstName, String updatedLastName,
                                      String updatedAddress, String updatedCity,
                                      String updatedState, String updatedZip,
                                      String updatedPhone) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        UpdateContactInfoPage updateContactInfoPage =
                new AccountOverviewPage().goToUpdateContactInfo();

        updateContactInfoPage.updateContactInfo(
                updatedFirstName,
                updatedLastName,
                updatedAddress,
                updatedCity,
                updatedState,
                updatedZip,
                updatedPhone
        );

        String successTitle = updateContactInfoPage.getSuccessTitle();
        boolean updated = updateContactInfoPage.isProfileUpdated();

        printOutput(
                "TC_UPDATE_001",
                "Update Contact Info",
                "Profile should be updated successfully",
                "Title: " + successTitle + ", Updated: " + updated,
                updated ? "PASS" : "FAIL"
        );

        Assert.assertEquals(successTitle, "Profile Updated", "Contact information was not updated");
        Assert.assertTrue(updated, "Profile update confirmation was not displayed");
    }

    @Test(priority = 9, groups = {"regression", "account-services"},
            dataProvider = "loanData",
            dataProviderClass = TestDataProvider.class)
    public void requestLoanTest(String fn, String ln, String address, String city,
                                String state, String zip, String phone, String ssn,
                                String user, String pass,
                                String loanAmount, String downPayment) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        RequestLoanPage requestLoanPage = new AccountOverviewPage().goToRequestLoan();
        requestLoanPage.requestLoan(loanAmount, downPayment);

        String resultTitle = requestLoanPage.getResultTitle();
        boolean processed = requestLoanPage.isLoanRequestProcessed();

        printOutput(
                "TC_LOAN_001",
                "Request Loan",
                "Loan request should be processed",
                "Title: " + resultTitle + ", Processed: " + processed,
                processed ? "PASS" : "FAIL"
        );

        Assert.assertEquals(resultTitle, "Loan Request Processed", "Loan request was not processed");
        Assert.assertTrue(processed, "Loan request result was not displayed");
    }

    @Test(priority = 10, groups = {"smoke"},
            dataProvider = "registrationData",
            dataProviderClass = TestDataProvider.class)
    public void logoutTest(String fn, String ln, String address, String city,
                           String state, String zip, String phone, String ssn,
                           String user, String pass) {

        registerAndReturnUsername(fn, ln, address, city, state, zip, phone, ssn, pass);

        new AccountOverviewPage().logout();

        boolean loginDisplayed = new HomePage().isLoginButtonDisplayed();

        printOutput(
                "TC_LOGOUT_001",
                "Logout",
                "Login button should be displayed after logout",
                loginDisplayed ? "Login button displayed" : "Login button not displayed",
                loginDisplayed ? "PASS" : "FAIL"
        );

        Assert.assertTrue(loginDisplayed, "Logout failed");
    }
}