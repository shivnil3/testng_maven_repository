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
 * Measure the total time involved to do the following actions - - Clicking on
 * Contact us button - Filling all the field on the contact us form - Submitting
 * the form - Verifying the 'Thanks....' message on the page.
 * 
 * Compare this total time with an expected time and set the status of test case
 * based on that.
 * 
 * @author Nilesh Awasthey
 *
 */
public class VerifyBasicPerformanceOfContactUsForm extends CommonActions
		implements SauceOnDemandSessionIdProvider {

	String browserToUse;
	String browserVersion;
	String environmentOS;
	String webApplicationURL;
	String testcaseFile;
	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
	long expectedTotalTimeInMilliSeconds = 4500;
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
	@Parameters({ "browser", "version", "os", "appURL", "inputDataFile" })
	public void initializeValues(String browser, String version, String os,
			String appURL, String inputDataFile) {
		browserToUse = browser;
		browserVersion = version;
		environmentOS = os;
		webApplicationURL = appURL;
		testcaseFile = inputDataFile;
	}

	@BeforeTest
	public void launchWebApp() {
		setTestCaseName(getClass().getSimpleName());
		browserDriver = createDriver(webDriver, sessionId, browserToUse,
				browserVersion, environmentOS);
		asserts.assertNotNull(browserDriver,
				"Unable to launch browser.Terminating script....");
		launchUrlInBrowser(browserDriver, webApplicationURL);
	}

	@AfterTest
	public void closeWebApp() {
		closeBrowser(browserDriver);
	}

	@DataProvider
	public String[][] getData() {
		return getTestcaseFileData(testcaseFile);
	}

	@Test(dataProvider = "getData")
	public void verifyBasicPerformance(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		String confirmationMsg = "Thanks for contacting us. Your message was received.";
		String issueDetailsText = issueDetailsTestString + (getRandomNum());
		long startTime;
		long endTime;
		long actualTotalTimeInMilliSeconds;
		startTime = System.currentTimeMillis();

		WebElement contactUsButton = waitAndSearchElementUntilClickable(ById,
				ElementIdentifiers.contactUsButtonId);

		asserts.log("Searching and clicking on the CONTACT US button....");
		contactUsButton.click();
		asserts.log("CONTACT US button was clicked");

		WebElement contactForm = waitAndSearchElementUntilVisible(ById,
				ElementIdentifiers.contactFormId);
		asserts.assertNotNull(contactForm,
				"Contact Form was not launched properly...");
		asserts.assertTrue(contactForm.isDisplayed(),
				"Contact us form is not displayed");
		asserts.log("Contact Form is displayed...");

		WebElement contactNameTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactNameTextBoxId);
		WebElement contactEmailTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactEmailTextBoxId);
		WebElement contactIssueDetailsTextArea = searchElementInParentElement(
				contactForm, ById,
				ElementIdentifiers.contactIssueDetailsTextAreaId);
		WebElement contactSubmitbutton = searchElementInParentElement(
				contactForm, ByClassName,
				ElementIdentifiers.contactSubmitButtonClassName);

		asserts.log("TESTCASE - Verify the execution time for submission of the Contact us form is in specified range.... ");

		contactNameTextBox.clear();
		contactEmailTextBox.clear();
		contactIssueDetailsTextArea.clear();

		contactNameTextBox.sendKeys(nameTestString);
		asserts.log("Name Textbox value is set to " + nameTestString);

		// Set Email field value

		contactEmailTextBox.sendKeys(emailTestString);
		asserts.log("Email Textbox value is set to " + emailTestString);

		// Set Issue details field value

		contactIssueDetailsTextArea.sendKeys(issueDetailsText);
		asserts.log("Issue details Textarea value is set to " + emailTestString);

		// Click on Submit button
		contactSubmitbutton.click();
		asserts.log("Submit button clicked.");

		// Check the confirmation message. If it exists, the testcase has passed
		WebElement successMsgForContactusForm = waitAndSearchElementUntilVisible(
				ByXpath, ElementIdentifiers.contactusMsgXpath);

		asserts.verifyTrue(((successMsgForContactusForm.getText())
				.equalsIgnoreCase(confirmationMsg)), confirmationMsg
				+ "- Displayed", confirmationMsg + "- Not Displayed");

		endTime = System.currentTimeMillis();

		actualTotalTimeInMilliSeconds = endTime - startTime;
		asserts.assertTrue(
				actualTotalTimeInMilliSeconds < expectedTotalTimeInMilliSeconds,
				"The actual time for submission of the contact us form"
						+ actualTotalTimeInMilliSeconds
						+ "is greater than expected time"
						+ expectedTotalTimeInMilliSeconds);
		asserts.log("The actual time for submission of the contact us form"
				+ actualTotalTimeInMilliSeconds + "is less than expected time"
				+ expectedTotalTimeInMilliSeconds);

	}

}
