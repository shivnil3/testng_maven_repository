package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.CommonActions;
import common.ElementIdentifiers;
import loggerFunctions.CustomAsserts;

public class VerifyCloseIconFunctionality extends CommonActions {

	String browserToUse;
	String webApplicationURL;
	String testcaseFile;
	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
	CustomAsserts asserts = new CustomAsserts();

	@BeforeTest
	@Parameters({ "browser", "appURL", "inputDataFile" })
	public void initializeValues(String browser, String appURL,
			String inputDataFile) {
		browserToUse = browser;
		webApplicationURL = appURL;
		testcaseFile = inputDataFile;
	}

	@BeforeTest
	public void launchWebApp() {
		/*
		 * launch browser using the url specified in the testng.xml file
		 */
		browserDriver = launchURLInBrowser(webApplicationURL, browserToUse);
		asserts.assertNotNull(browserDriver,
				"Unable to launch browser.Terminating script....");
	}

	@DataProvider
	public String[][] getData() {
		return getTestcaseFileData(testcaseFile);
	}

	@AfterTest
	public void closeWebApp() {
		// Close the browser
		closeBrowser(browserDriver);

	}

	@Test(dataProvider = "getData")
	public void verifyCloseIconWorks(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		String issueDetailsText = issueDetailsTestString + (getRandomNum());

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
				"Contact us form not not displayed");
		asserts.log("Contact Form is displayed...");

		WebElement contactNameTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactNameTextBoxId);
		WebElement contactEmailTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactEmailTextBoxId);
		WebElement contactIssueDetailsTextArea = searchElementInParentElement(
				contactForm, ById,
				ElementIdentifiers.contactIssueDetailsTextAreaId);

		WebElement contactUsFormCloseIcon = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.contactUsFormCloseIconXpath);

		asserts.log("TESTCASE - Verify the contact us form is closed after clicking the Close(X) icon");

		contactNameTextBox.clear();
		contactEmailTextBox.clear();
		contactIssueDetailsTextArea.clear();

		contactNameTextBox.sendKeys(nameTestString);
		asserts.log("Name Textbox value is set to " + nameTestString);

		contactEmailTextBox.sendKeys(emailTestString);
		asserts.log("Email Textbox value is set to " + emailTestString);

		contactIssueDetailsTextArea.sendKeys(issueDetailsText);
		asserts.log("Issue details Textarea value is set to " + emailTestString);

		asserts.log("Clicking Close(X) icon to close Conact us form...");
		contactUsFormCloseIcon.click();

		contactForm = searchElement(ById, ElementIdentifiers.contactFormId);

		asserts.assertFalse(contactForm.isDisplayed(),
				"Contact us form still displayed");
		asserts.log("Contact us form not displayed anymore...");

	}

}