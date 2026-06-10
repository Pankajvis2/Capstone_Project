package pages;

import org.openqa.selenium.By;

public class AccountOverviewPage extends BasePage {

    private final By accountOverviewTitle = By.cssSelector("#rightPanel h1");
    private final By openNewAccount = By.linkText("Open New Account");
    private final By transferFunds = By.linkText("Transfer Funds");
    private final By billPay = By.linkText("Bill Pay");
    private final By findTransactions = By.linkText("Find Transactions");
    private final By updateContactInfo = By.linkText("Update Contact Info");
    private final By requestLoan = By.linkText("Request Loan");
    private final By logout = By.linkText("Log Out");

    public boolean isAccountOverviewVisible() {
        return isDisplayed(accountOverviewTitle);
    }

    public OpenAccountPage goToOpenNewAccount() {
        click(openNewAccount);
        return new OpenAccountPage();
    }

    public TransferFundsPage goToTransferFunds() {
        click(transferFunds);
        return new TransferFundsPage();
    }

    public BillPayPage goToBillPay() {
        click(billPay);
        return new BillPayPage();
    }

    public FindTransactionsPage goToFindTransactions() {
        click(findTransactions);
        return new FindTransactionsPage();
    }

    public UpdateContactInfoPage goToUpdateContactInfo() {
        click(updateContactInfo);
        return new UpdateContactInfoPage();
    }

    public RequestLoanPage goToRequestLoan() {
        click(requestLoan);
        return new RequestLoanPage();
    }

    public void logout() {
        click(logout);
    }
}
