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

public class VerifyRequiredFields extends CommonActions {

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
	public void verifyRequiredFields(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

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

		asserts.log("TESTCASE - Confirm that the Email and Text area fields are mandatory and appropriate message is displayed for blank values for these fields");

		contactNameTextBox.clear();
		contactEmailTextBox.clear();
		contactIssueDetailsTextArea.clear();

		// Click on Submit button
		contactSubmitbutton.click();
		asserts.log("Submit button clicked on the Contact us form.");
		WebElement requiredMsgcontactEmailTextBox = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.requiredMsgContactEmailXpath);
		WebElement requiredMsgcontactIssueTextArea = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.requiredMsgContactIssueDetailsXpath);

		asserts.verifyTrue(((requiredMsgcontactEmailTextBox.getText())
				.equalsIgnoreCase("Email is a required field")),
				"Email is a required field - message is displayed",
				"Email is a required field - message is not displayed");

		asserts.verifyTrue(
				((requiredMsgcontactIssueTextArea.getText())
						.equalsIgnoreCase("Problem description is a required field")),
				"Problem description is a required field - message is displayed",
				"Problem description is a required field - message is not displayed");

		asserts.log("Set value for name and issue details field only and check whether required field message appears for email field");
		contactNameTextBox.clear();
		contactNameTextBox.sendKeys(nameTestString);
		asserts.log("Name text box set to - " + nameTestString);
		contactIssueDetailsTextArea.clear();
		contactIssueDetailsTextArea.sendKeys(issueDetailsTestString);
		asserts.log("Issue details text area set to - "
				+ issueDetailsTestString);
		contactSubmitbutton.click();
		asserts.log("Submit button was clicked");
		requiredMsgcontactEmailTextBox = searchElementInParentElement(
				contactForm, ByXpath,
				ElementIdentifiers.requiredMsgContactEmailXpath);

		asserts.verifyTrue(((requiredMsgcontactEmailTextBox.getText())
				.equalsIgnoreCase("Email is a required field")),
				"'Email is a required field' - message is displayed",
				"'Email is a required field' - message is not displayed");
	}

}
