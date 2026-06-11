package dataproviders;

import org.testng.annotations.DataProvider;
import utilities.CSVUtility;

public class TestDataProvider {

    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return CSVUtility.readCsv("src/test/resources/testdata/users.csv");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return CSVUtility.readCsv("src/test/resources/testdata/invalidLogin.csv");
    }

    @DataProvider(name = "transferFundsData")
    public Object[][] transferFundsData() {
        return CSVUtility.readCsv("src/test/resources/testdata/transactionData.csv");
    }

    @DataProvider(name = "transactionData")
    public Object[][] transactionData() {
        return CSVUtility.readCsv("src/test/resources/testdata/transactionData.csv");
    }

    @DataProvider(name = "billPayData")
    public Object[][] billPayData() {
        return CSVUtility.readCsv("src/test/resources/testdata/billPayData.csv");
    }

    @DataProvider(name = "loanData")
    public Object[][] loanData() {
        return CSVUtility.readCsv("src/test/resources/testdata/loanData.csv");
    }

    @DataProvider(name = "updateContactData")
    public Object[][] updateContactData() {
        return CSVUtility.readCsv("src/test/resources/testdata/updateContactData.csv");
    }

    @DataProvider(name = "findTransactionData")
    public Object[][] findTransactionData() {
        return CSVUtility.readCsv("src/test/resources/testdata/findTransactionData.csv");
    }

    @DataProvider(name = "bugTransferNegative")
    public Object[][] bugTransferNegative() {
        return CSVUtility.readCsv("src/test/resources/testdata/bugTransferNegative.csv");
    }

    @DataProvider(name = "bugBillPaymentNegative")
    public Object[][] bugBillPaymentNegative() {
        return CSVUtility.readCsv("src/test/resources/testdata/bugBillPaymentNegative.csv");
    }

    @DataProvider(name = "bugLoanNegative")
    public Object[][] bugLoanNegative() {
        return CSVUtility.readCsv("src/test/resources/testdata/bugLoanNegative.csv");
    }

    @DataProvider(name = "bugSameAccountTransfer")
    public Object[][] bugSameAccountTransfer() {
        return CSVUtility.readCsv("src/test/resources/testdata/bugSameAccountTransfer.csv");
    }
}