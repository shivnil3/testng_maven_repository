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

public class VerifyInValidStringsAreHandledProperly extends CommonActions {

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
		asserts.log("TESTCASE - Verify the Contact us form handles valid/invalid string inputs gracefully and does not throw any exception on the page.. ");

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
	public void verifyValidAndInvalidStringInput(String nameTestString,
			String emailTestString, String issueDetailsTestString) {

		String confirmationMsg = "Thanks for contacting us. Your message was received.";
		String issueDetailsText = issueDetailsTestString;

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

		waitUntilElementNotVisibleOnUI(successMsgForContactusForm);
	}

}
