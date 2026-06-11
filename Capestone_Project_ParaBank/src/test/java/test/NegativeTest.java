package test;

import base.BaseTest;
import config.ConfigReader;
import dataproviders.TestDataProvider;
import factory.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import java.util.UUID;

public class NegativeTest extends BaseTest {

    private String uniqueUsername() {
        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10);

        return ("bug" + randomPart).toLowerCase();
    }

    private RegisterPage openRegisterPageDirectly() {
        DriverFactory.getDriver().get(
                ConfigReader.get("registerUrl")
        );
        return new RegisterPage();
    }

    private void registerUser(String firstName, String lastName, String address, String city,
                              String state, String zip, String phone, String ssn,
                              String password) {

        RegisterPage registerPage = openRegisterPageDirectly();

        String generatedUsername = uniqueUsername();

        registerPage.register(
                firstName,
                lastName,
                address,
                city,
                state,
                zip,
                phone,
                ssn,
                generatedUsername,
                password
        );

        Assert.assertTrue(
                registerPage.isRegistrationSuccessful(),
                "Precondition failed: user registration was not completed."
        );

        System.out.println("Registered negative test user: " + generatedUsername);
    }

    private void createSecondAccount() {

        OpenAccountPage openAccountPage =
                new AccountOverviewPage().goToOpenNewAccount();

        openAccountPage.openSavingsAccount();

        String successMessage = openAccountPage.getSuccessMessage();
        String newAccountId = openAccountPage.getNewAccountId();

        System.out.println("Second account creation message: " + successMessage);
        System.out.println("Second account id: " + newAccountId);

        Assert.assertEquals(
                successMessage,
                "Account Opened!",
                "Precondition failed: second account was not created."
        );
    }

    private void printBugOutput(String bugId, String module, String inputData,
                                String expectedResult, String actualResult,
                                String status) {

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

    @Test(priority = 1, groups = {"negative", "bug", "transfer"},
            dataProvider = "bugTransferNegative",
            dataProviderClass = TestDataProvider.class)
    public void bug001_transferFundsAllowsNegativeAmount(
            String firstName,
            String lastName,
            String address,
            String city,
            String state,
            String zip,
            String phone,
            String ssn,
            String username,
            String password,
            String amount) {

        registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

        createSecondAccount();

        TransferFundsPage transferFundsPage =
                new AccountOverviewPage().goToTransferFunds();

        transferFundsPage.transfer(amount);

        String actualResult = transferFundsPage.getSuccessMessage();

        printBugOutput(
                "PB-001",
                "Transfer Funds",
                "Amount = " + amount,
                "System should reject negative transfer amount.",
                actualResult,
                actualResult.equals("Transfer Complete!")
                        ? "BUG REPRODUCED"
                        : "BUG NOT REPRODUCED"
        );

        Assert.assertEquals(
                actualResult,
                "Transfer Complete!",
                "BUG NOT REPRODUCED: Negative transfer amount was rejected."
        );
    }

    @Test(priority = 2, groups = {"negative", "bug", "billpay"},
            dataProvider = "bugBillPaymentNegative",
            dataProviderClass = TestDataProvider.class)
    public void bug002_billPaymentAllowsNegativeAmount(
            String firstName,
            String lastName,
            String address,
            String city,
            String state,
            String zip,
            String phone,
            String ssn,
            String username,
            String password,
            String accountNumber,
            String amount) {

        registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

        BillPayPage billPayPage =
                new AccountOverviewPage().goToBillPay();

        billPayPage.payBillWithNegativeAmount(amount);

        String actualResult = billPayPage.getSuccessTitle();

        printBugOutput(
                "PB-002",
                "Bill Payment",
                "Account Number = " + accountNumber + ", Amount = " + amount,
                "System should reject negative bill payment amount.",
                actualResult,
                actualResult.equals("Bill Payment Complete")
                        ? "BUG REPRODUCED"
                        : "BUG NOT REPRODUCED"
        );

        Assert.assertEquals(
                actualResult,
                "Bill Payment Complete",
                "BUG NOT REPRODUCED: Negative bill payment amount was rejected."
        );
    }

    @Test(priority = 3, groups = {"negative", "bug", "transfer"},
            dataProvider = "bugSameAccountTransfer",
            dataProviderClass = TestDataProvider.class)
    public void bug003_transferFundsAllowsSameSourceAndDestinationAccount(
            String firstName,
            String lastName,
            String address,
            String city,
            String state,
            String zip,
            String phone,
            String ssn,
            String username,
            String password,
            String amount) {

        registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

        TransferFundsPage transferFundsPage =
                new AccountOverviewPage().goToTransferFunds();

        transferFundsPage.transferSameAccount(amount);

        String actualResult = transferFundsPage.getSuccessMessage();

        printBugOutput(
                "PB-003",
                "Transfer Funds",
                "From Account = Same Account, To Account = Same Account, Amount = " + amount,
                "System should block same source and destination account transfer.",
                actualResult,
                actualResult.equals("Transfer Complete!")
                        ? "BUG REPRODUCED"
                        : "BUG NOT REPRODUCED"
        );

        Assert.assertEquals(
                actualResult,
                "Transfer Complete!",
                "BUG NOT REPRODUCED: Same account transfer was blocked."
        );
    }

    @Test(priority = 4, groups = {"negative", "bug", "loan"},
            dataProvider = "bugLoanNegative",
            dataProviderClass = TestDataProvider.class)
    public void bug004_requestLoanAllowsNegativeDownPayment(
            String firstName,
            String lastName,
            String address,
            String city,
            String state,
            String zip,
            String phone,
            String ssn,
            String username,
            String password,
            String loanAmount,
            String downPayment) {

        registerUser(firstName, lastName, address, city, state, zip, phone, ssn, password);

        RequestLoanPage requestLoanPage =
                new AccountOverviewPage().goToRequestLoan();

        requestLoanPage.requestLoan(loanAmount, downPayment);

        String actualResult = requestLoanPage.getResultTitle();

        printBugOutput(
                "PB-004",
                "Request Loan",
                "Loan Amount = " + loanAmount + ", Down Payment = " + downPayment,
                "System should reject negative down payment value.",
                actualResult,
                actualResult.equals("Loan Request Processed")
                        ? "BUG REPRODUCED"
                        : "BUG NOT REPRODUCED"
        );

        Assert.assertEquals(
                actualResult,
                "Loan Request Processed",
                "BUG NOT REPRODUCED: Negative down payment was rejected."
        );
    }
}