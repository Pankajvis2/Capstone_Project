package pages;

import org.openqa.selenium.By;
import utilities.WaitUtilities;

public class HomePage extends BasePage {

    private final By registerLink = By.linkText("Register");
    private final By username = By.name("username");
    private final By password = By.name("password");
    private final By loginBtn = By.cssSelector("input[value='Log In']");

    private final By error = By.xpath(
            "//*[contains(@class,'error') or contains(text(),'error') or contains(text(),'Please enter') or contains(text(),'could not be verified')]"
    );

    public RegisterPage openRegisterPage() {
        click(registerLink);
        return new RegisterPage();
    }

    public AccountOverviewPage login(String user, String pass) {
        type(username, user);
        type(password, pass);
        click(loginBtn);
        return new AccountOverviewPage();
    }

    public String getErrorMessage() {
        try {
            return WaitUtilities.shortText(error);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(loginBtn);
    }
}