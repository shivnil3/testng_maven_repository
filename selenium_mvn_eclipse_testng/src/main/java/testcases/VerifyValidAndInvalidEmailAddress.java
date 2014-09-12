package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import common.CommonActions;
import common.ElementIdentifiers;
import loggerFunctions.CustomAsserts;

/**
 * It covers two scenarios -
 * 
 * - Provide a set of valid email addresses through data
 * file(ValidEmailAddresses_DataFile.csv) and verify the Email text box does not
 * display any validation message
 * 
 * - Provide a set of invalid email addresses through data
 * file(InValidEmailAddresses_DataFile.csv) and verify the Email text box
 * displays a 'Invalid email address' message for each of them
 * 
 * 
 * @author Nilesh Awasthey
 *
 */
public class VerifyValidAndInvalidEmailAddress extends CommonActions implements
		SauceOnDemandSessionIdProvider {

	String browserToUse;
	String browserVersion;
	String environmentOS;
	String webApplicationURL;
	String validEmailAddressesDataFile;
	String invalidEmailAddressesDataFile;
	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
	WebElement contactUsButton;
	WebElement contactForm;
	WebElement contactNameTextBox;
	WebElement contactEmailTextBox;
	WebElement contactIssueDetailsTextArea;
	WebElement contactSubmitbutton;
	WebElement validationMsgContactEmail;
	CustomAsserts asserts = new CustomAsserts();
	ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	ThreadLocal<String> sessionId = new ThreadLocal<String>();

	@Override
	public String getSessionId() {
		return sessionId.get();
	}

	@BeforeSuite
	@Parameters({ "SauceOnDemandUsername", "SauceOnDemandAccessKey" })
	public void initializeSauceCredentails(String sauceOnDemandUsername,
			String sauceOnDemandAccessKey) {
		setSauceOnDemandUsername(sauceOnDemandUsername);
		setSauceOnDemandAccessKey(sauceOnDemandAccessKey);

	}

	@BeforeTest
	@Parameters({ "browser", "version", "os", "appURL", "validEmailDataFile",
			"invalidEmailDataFile" })
	public void initializeValues(String browser, String version, String os,
			String appURL, String validEmailDataFile,
			String invalidEmailDataFile) {
		browserToUse = browser;
		browserVersion = version;
		environmentOS = os;
		webApplicationURL = appURL;
		validEmailAddressesDataFile = validEmailDataFile;
		invalidEmailAddressesDataFile = invalidEmailDataFile;

	}

	@BeforeTest
	public void launchContactUsForm() {
		setTestCaseName(getClass().getSimpleName());
		browserDriver = createDriver(webDriver, sessionId, browserToUse,
				browserVersion, environmentOS);
		asserts.assertNotNull(browserDriver,
				"Unable to launch browser.Terminating script....");
		launchUrlInBrowser(browserDriver, webApplicationURL);

		contactUsButton = waitAndSearchElementUntilClickable(ById,
				ElementIdentifiers.contactUsButtonId);

		asserts.log("Searching and clicking on the CONTACT US button....");
		contactUsButton.click();
		asserts.log("CONTACT US button was clicked");

		contactForm = waitAndSearchElementUntilVisible(ById,
				ElementIdentifiers.contactFormId);
		asserts.assertNotNull(contactForm,
				"Contact Form was not launched properly...");
		asserts.assertTrue(contactForm.isDisplayed(),
				"Contact us form is not displayed");
		asserts.log("Contact Form is displayed...");

		contactNameTextBox = searchElementInParentElement(contactForm, ById,
				ElementIdentifiers.contactNameTextBoxId);
		contactEmailTextBox = searchElementInParentElement(contactForm, ById,
				ElementIdentifiers.contactEmailTextBoxId);
		contactIssueDetailsTextArea = searchElementInParentElement(contactForm,
				ById, ElementIdentifiers.contactIssueDetailsTextAreaId);
		contactSubmitbutton = searchElementInParentElement(contactForm,
				ByClassName, ElementIdentifiers.contactSubmitButtonClassName);

		contactNameTextBox.clear();
		contactEmailTextBox.clear();
		contactIssueDetailsTextArea.clear();

	}

	@AfterTest
	public void closeWebApp() {
		closeBrowser(browserDriver);
	}

	@DataProvider
	public String[][] getValidEmailAddressData() {
		return getTestcaseFileData(validEmailAddressesDataFile);
	}

	@DataProvider
	public String[][] getInValidEmailAddressData() {
		return getTestcaseFileData(invalidEmailAddressesDataFile);
	}

	@Test(dataProvider = "getValidEmailAddressData", priority = 1)
	public void checkForValidEmailAddresses(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		asserts.log("TESTCASE - Verify the 'Your Email Address' Field accepts valid email addresses... ");

		contactNameTextBox.clear();
		contactNameTextBox.sendKeys(nameTestString);

		asserts.log("Setting email address as ... - " + emailTestString);

		contactEmailTextBox.clear();
		contactEmailTextBox.sendKeys(emailTestString);
		pauseExecution(2);
		contactSubmitbutton.click();

		validationMsgContactEmail = searchElementInParentElement(contactForm,
				ByXpath, ElementIdentifiers.validationMsgContactEmailXpath);

		asserts.log("The validation message is not expected as it is a valid email address.. (WebElement will return null) ");

		asserts.verifyNull(
				validationMsgContactEmail,
				emailTestString
						+ " - is valid and accepted by Email field on the Contact Us form.",
				emailTestString
						+ " - is valid but not accepted by the Email field on Contact us form.");

	}

	@Test(dataProvider = "getInValidEmailAddressData", priority = 2)
	public void checkForInValidEmailAddresses(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		asserts.log("TESTCASE - Verify the 'Your Email Address' Field does not accept invalid email addresses and gives validation message... ");

		contactNameTextBox.clear();
		contactNameTextBox.sendKeys(nameTestString);

		asserts.log("Setting email address as ... - " + emailTestString);

		contactEmailTextBox.clear();
		contactEmailTextBox.sendKeys(emailTestString);
		pauseExecution(2);
		contactSubmitbutton.click();

		validationMsgContactEmail = searchElementInParentElement(contactForm,
				ByXpath, ElementIdentifiers.validationMsgContactEmailXpath);

		asserts.log("The validation message is expected as it is an invalid email address.. ");
		asserts.verifyNotNull(
				validationMsgContactEmail,
				emailTestString
						+ " - is invalid and not accepted by Email field on the Contact Us form.",
				emailTestString
						+ " - is invalid but still accepted by the Email field on Contact us form.");

	}

}