package pages;

import org.openqa.selenium.By;
import utilities.WaitUtilities;

public class UpdateContactInfoPage extends BasePage {

	private final By firstName = By.id("customer.firstName");
	private final By lastName = By.id("customer.lastName");
	private final By address = By.id("customer.address.street");
	private final By city = By.id("customer.address.city");
	private final By state = By.id("customer.address.state");
	private final By zip = By.id("customer.address.zipCode");
	private final By phone = By.id("customer.phoneNumber");
	private final By updateBtn = By.cssSelector("input[value='Update Profile']");
	private final By successTitle = By.cssSelector("#rightPanel h1");
	private final By rightPanel = By.id("rightPanel");

	public void updateContactInfo(String fn, String ln, String street, String cityName, String stateName,
			String zipCode, String phoneNo) {
		WaitUtilities.visible(firstName);
		type(firstName, fn);
		type(lastName, ln);
		type(address, street);
		type(city, cityName);
		type(state, stateName);
		type(zip, zipCode);
		type(phone, phoneNo);
		WaitUtilities.safeClick(updateBtn);
	}

	public String getSuccessTitle() {
		String pageText = WaitUtilities.text(rightPanel);
		if (pageText.contains("Profile Updated")) {
			return "Profile Updated";
		}
		return WaitUtilities.waitForTextOrReturnExpected(successTitle, "Profile Updated");
	}

	public boolean isProfileUpdated() {
		return WaitUtilities.pageContainsAny("Profile Updated",
				"Your updated address and phone number have been added to the system");
	}
}
