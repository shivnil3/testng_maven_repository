package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import common.CommonActions;
import common.ElementIdentifiers;
import loggerFunctions.CustomAsserts;

/**
 * Verify all the labels, text boxes, header text, close(x) icon and submit
 * button exists on the Contact us form.
 * 
 * @author Nilesh Awasthey
 *
 */
public class VerifyElements extends CommonActions implements
		SauceOnDemandSessionIdProvider {

	String browserToUse;
	String browserVersion;
	String environmentOS;
	String webApplicationURL;

	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
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
	@Parameters({ "browser", "version", "os", "appURL" })
	public void initializeValues(String browser, String version, String os,
			String appURL) {
		browserToUse = browser;
		browserVersion = version;
		environmentOS = os;
		webApplicationURL = appURL;
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

	@Test
	public void verifyElements() {
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

		asserts.log("TESTCASE - Verify all the elements are displayed on the contact us form... ");

		WebElement contactUsHeaderText = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.contactUsHeaderTextXpath);
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

		WebElement contactUsFormCloseIcon = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.contactUsFormCloseIconXpath);
		WebElement contactNameLabel = searchElementInParentElement(contactForm,
				ByClassName, ElementIdentifiers.contactNameLabelClassName);
		WebElement contactEmailLabel = searchElementInParentElement(
				contactForm, ByClassName,
				ElementIdentifiers.contactEmailLabelClassName);
		WebElement contactIssueDetailsLabel = searchElementInParentElement(
				contactForm, ByClassName,
				ElementIdentifiers.contactIssueDetailsLabelClassName);

		asserts.log("Searching 'CONTACT US' text in the header of the contact form....");
		asserts.verifyTrue(
				(contactUsHeaderText.getText()).equalsIgnoreCase("CONTACT US"),
				"- Found", "- Not Found");

		asserts.log("Searching Close(X) icon in the header of contact form....");
		asserts.verifyTrue((contactUsFormCloseIcon.isDisplayed()), "- Found",
				"- Not Found");

		asserts.log("Searching 'Your Name (optional)' label on the contact form....");
		asserts.verifyTrue(((contactNameLabel.getText())
				.equalsIgnoreCase("Your Name (optional)")), "- Found",
				"- Not Found");

		asserts.log("Searching 'Your Name' textbox on the contact form....");
		asserts.verifyTrue((contactNameTextBox.isDisplayed()), "- Found",
				"- Not Found");

		asserts.log("Searching 'Your email address' label on the contact form....");
		asserts.verifyTrue(((contactEmailLabel.getText())
				.equalsIgnoreCase("Your email address")), "- Found",
				"- Not Found");

		asserts.log("Searching 'Your email address' textbox on the contact form....");
		asserts.verifyTrue(((contactEmailTextBox.isDisplayed())), "- Found",
				"- Not Found");

		asserts.log("Searching 'How can we help you?' label on the contact form....");
		asserts.verifyTrue(((contactIssueDetailsLabel.getText())
				.equalsIgnoreCase("How can we help you?")), "- Found",
				"- Not Found");

		asserts.log("Searching 'How can we help you?' textarea on the contact form....");
		asserts.verifyTrue(((contactIssueDetailsTextArea.isDisplayed())),
				"- Found", "- Not Found");

		asserts.log("Searching Submit button on the contact form....");
		asserts.verifyTrue(((contactSubmitbutton.isDisplayed())), "- Found",
				"- Not Found");

	}

}
