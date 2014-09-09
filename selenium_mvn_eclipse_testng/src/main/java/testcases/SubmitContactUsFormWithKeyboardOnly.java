package testcases;

import org.openqa.selenium.Keys;
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

/**
 * Submit the Contact Us form using keyboard keys only. It should display the
 * 'Thanks....' message on the page after successful submission of the Contact
 * Us form.
 * 
 * @author Nilesh Awasthey
 *
 */
public class SubmitContactUsFormWithKeyboardOnly extends CommonActions {

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
	public void submitFormWithKeyboard(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		String confirmationMsg = "Thanks for contacting us. Your message was received.";
		String issueDetailsText = issueDetailsTestString + (getRandomNum());

		WebElement contactUsButton = waitAndSearchElementUntilClickable(ById,
				ElementIdentifiers.contactUsButtonId);

		asserts.log("Search CONTACT US button and hit Enter to click on it....");

		contactUsButton.sendKeys(Keys.ENTER);

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

		asserts.log("TESTCASE - Verify the user is able to submit the contact us form using the keyboard only. ");

		contactNameTextBox.clear();
		contactNameTextBox.sendKeys(nameTestString);
		asserts.log("Name Textbox value is set to " + nameTestString);

		contactNameTextBox.sendKeys(Keys.TAB);
		contactEmailTextBox.clear();
		contactEmailTextBox.sendKeys(emailTestString);
		asserts.log("Email Textbox value is set to " + emailTestString);

		contactEmailTextBox.sendKeys(Keys.TAB);
		contactIssueDetailsTextArea.clear();
		contactIssueDetailsTextArea.sendKeys(issueDetailsText);
		asserts.log("Issue details Textarea value is set to " + emailTestString);

		contactIssueDetailsTextArea.sendKeys(Keys.TAB);
		contactSubmitbutton.sendKeys(Keys.ENTER);
		asserts.log("Submit button was selected and Enter button was pressed to submit the form");

		// Check the confirmation message. If it exists, the testcase has passed
		WebElement successMsgForContactusForm = waitAndSearchElementUntilVisible(
				ByXpath, ElementIdentifiers.contactusMsgXpath);

		asserts.assertTrue(((successMsgForContactusForm.getText())
				.equalsIgnoreCase(confirmationMsg)), confirmationMsg
				+ "- Not Displayed");
		asserts.log(confirmationMsg + "- Displayed");

	}

}
