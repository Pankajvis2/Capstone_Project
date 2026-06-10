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
        return new Object[][]{
                {"wrong_User", "wrong_Pass"},
                {"", "wrong_Pass"},
                {"wrongUser_", ""}
        };
    }
}
