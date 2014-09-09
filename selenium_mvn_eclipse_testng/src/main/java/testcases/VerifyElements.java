package testcases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.CommonActions;
import common.ElementIdentifiers;
import loggerFunctions.CustomAsserts;

public class VerifyElements extends CommonActions {

	String browserToUse;
	String webApplicationURL;
	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
	CustomAsserts asserts = new CustomAsserts();

	@BeforeTest
	@Parameters({ "browser", "appURL" })
	public void initializeValues(String browser, String appURL) {
		browserToUse = browser;
		webApplicationURL = appURL;
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

	@AfterTest
	public void closeWebApp() {
		// Close the browser
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
