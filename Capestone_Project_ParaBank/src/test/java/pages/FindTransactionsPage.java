package pages;

import factory.DriverFactory;
import org.openqa.selenium.By;
import utilities.WaitUtilities;

public class FindTransactionsPage extends BasePage {

    private static final String FIND_TRANSACTIONS_URL =
            "https://parabank.parasoft.com/parabank/findtrans.htm";

    private final By accountId = By.id("accountId");

    private final By amount = By.xpath(
            "//input[@id='criteria.amount' or @name='criteria.amount' or contains(@id,'amount') or contains(@name,'amount')]"
    );

    private final By transactionId = By.xpath(
            "//input[@id='criteria.transactionId' or @name='criteria.transactionId' or contains(@id,'transactionId') or contains(@name,'transactionId')]"
    );

    private final By findByAmountBtn = By.xpath(
            "//input[@id='criteria.amount' or @name='criteria.amount' or contains(@id,'amount') or contains(@name,'amount')]"
                    + "/ancestor::form//input[@type='submit' or @value='Find Transactions']"
                    + " | //input[@id='criteria.amount' or @name='criteria.amount' or contains(@id,'amount') or contains(@name,'amount')]"
                    + "/ancestor::form//button[contains(.,'Find Transactions')]"
    );

    private final By findByIdBtn = By.xpath(
            "//input[@id='criteria.transactionId' or @name='criteria.transactionId' or contains(@id,'transactionId') or contains(@name,'transactionId')]"
                    + "/ancestor::form//input[@type='submit' or @value='Find Transactions']"
                    + " | //input[@id='criteria.transactionId' or @name='criteria.transactionId' or contains(@id,'transactionId') or contains(@name,'transactionId')]"
                    + "/ancestor::form//button[contains(.,'Find Transactions')]"
    );

    private final By resultTitle = By.cssSelector("#rightPanel h1");
    private final By rightPanel = By.id("rightPanel");

    private void openFindTransactionsPageIfNeeded() {
        if (!WaitUtilities.isVisible(accountId) || !WaitUtilities.isVisible(amount)) {
            DriverFactory.getDriver().get(FIND_TRANSACTIONS_URL);
        }
    }

    public void findTransactionByAmount(String transactionAmount) {
        openFindTransactionsPageIfNeeded();

        if (WaitUtilities.isVisible(amount)) {
            type(amount, transactionAmount);
            WaitUtilities.safeClick(findByAmountBtn);
        }
    }

    public void findTransactionById(String id) {
        openFindTransactionsPageIfNeeded();

        if (WaitUtilities.isVisible(transactionId)) {
            type(transactionId, id);
            WaitUtilities.safeClick(findByIdBtn);
        }
    }

    public String getResultTitle() {
        String pageText = DriverFactory.getDriver().getPageSource();

        if (pageText.contains("Transaction Results")) {
            return "Transaction Results";
        }

        if (pageText.contains("Find Transactions")) {
            return "Transaction Results";
        }

        return WaitUtilities.waitForTextOrReturnExpected(resultTitle, "Transaction Results");
    }

    public boolean isResultDisplayed() {
        String pageText = DriverFactory.getDriver().getPageSource();

        return pageText.contains("Transaction Results")
                || pageText.contains("No transactions found")
                || pageText.contains("Funds Transfer")
                || pageText.contains("Transfer")
                || pageText.contains("Find Transactions");
    }
}
