package pages;

import org.openqa.selenium.By;

import utilities.WaitUtilities;

public class RegisterPage extends BasePage {

    private final By firstName = By.id("customer.firstName");
    private final By lastName = By.id("customer.lastName");
    private final By address = By.id("customer.address.street");
    private final By city = By.id("customer.address.city");
    private final By state = By.id("customer.address.state");
    private final By zip = By.id("customer.address.zipCode");
    private final By phone = By.id("customer.phoneNumber");
    private final By ssn = By.id("customer.ssn");
    private final By username = By.id("customer.username");
    private final By password = By.id("customer.password");
    private final By confirm = By.id("repeatedPassword");
    private final By registerBtn = By.cssSelector("input[value='Register']");

    private final By successTitle = By.cssSelector("#rightPanel h1");
    private final By usernameError = By.id("customer.username.errors");

    public void register(String fn, String ln, String add, String c, String st, String z,
                         String ph, String s, String user, String pass) {
        type(firstName, fn);
        type(lastName, ln);
        type(address, add);
        type(city, c);
        type(state, st);
        type(zip, z);
        type(phone, ph);
        type(ssn, s);
        type(username, user);
        type(password, pass);
        type(confirm, pass);
        click(registerBtn);
    }

    public boolean isRegistrationSuccessful() {
        return WaitUtilities.pageContainsAny(
                "Your account was created successfully",
                "You are now logged in",
                "Welcome"
        );
    }

    public String getSuccessTitle() {
        try {
            return WaitUtilities.shortText(successTitle);
        } catch (Exception e) {
            return "";
        }
    }

    public String getUsernameError() {
        try {
            return WaitUtilities.shortText(usernameError);
        } catch (Exception e) {
            return "";
		}
	}
}
