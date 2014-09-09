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

public class SubmitContactFormAndVerifyEmail extends CommonActions {

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
	public void submitFormAndVerifyMail(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		String confirmationMsg = "Thanks for contacting us. Your message was received.";
		String emailPwd = "auto@021";
		String issueDetailsText = issueDetailsTestString + (getRandomNum());

		asserts.log("Searching and clicking on the CONTACT US button....");
		WebElement contactUsButton = waitAndSearchElementUntilClickable(ById,
				ElementIdentifiers.contactUsButtonId);
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

		asserts.log("TESTCASE - Verify the user is able to submit the contact us form with valid input and recieves the notification mail. ");

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

		// Check the confirmation message.
		asserts.log("Checking the confirmation message....");
		WebElement successMsgForContactusForm = waitAndSearchElementUntilVisible(
				ByXpath, ElementIdentifiers.contactusMsgXpath);
		asserts.assertTrue(((successMsgForContactusForm.getText())
				.equalsIgnoreCase(confirmationMsg)), confirmationMsg
				+ "- Not Displayed");
		asserts.log(confirmationMsg + "- Displayed");

		// Check for notification message
		asserts.log("Checking the notification mail on test gmail account....");
		Boolean mailFoundStatus=confirmNotificationMail(emailTestString, emailPwd,
				issueDetailsText);
		asserts.assertTrue(mailFoundStatus, "Notification mail not recieved");
		asserts.log("Notification mail was recieved");

	}
}
